/*
 *  Copyright 2013 The Kuali Foundation Licensed under the
 *	Educational Community License, Version 2.0 (the "License"); you may
 *	not use this file except in compliance with the License. You may
 *	obtain a copy of the License at
 *
 *	http://www.osedu.org/licenses/ECL-2.0
 *
 *	Unless required by applicable law or agreed to in writing,
 *	software distributed under the License is distributed on an "AS IS"
 *	BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 *	or implied. See the License for the specific language governing
 *	permissions and limitations under the License.
 */
package io.github.svndump_to_git.common.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

import io.github.svndump_to_git.common.exceptions.InvalidKeyLineException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *         This class started as a copy from
 *         org.tmatesoft.svn.core.internal.wc.SVNUtil version 1.7.8 and was
 *         originally written by: TMate Software Ltd., Peter Skoog
 * 
 *         it is subject to the <a
 *         href="http://svnkit.com/license.html">svnkit.com license</a>.
 *         
 *         The key part of the copied code related to using the decoder to extract each line (see readLine method).
 * 
 */
public final class IOUtils {

	private static final Logger log = LoggerFactory.getLogger(IOUtils.class);

	/**
	 * 
	 */
	private IOUtils() {
	}

	private static CharsetDecoder decoder = Charset.forName("UTF-8")
			.newDecoder();

	public static String readLine(InputStream in, String charsetName)
			throws IOException {

		ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

		int r = -1;

		while ((r = in.read()) != '\n') {

			if (r == -1) {
				
				String out = decode(decoder, byteBuffer.toByteArray());

				if (out == null || out.isEmpty())
					return null;
				else
					return out;

			}

			byteBuffer.write(r);

		}

		String out = decode(decoder, byteBuffer.toByteArray());

		return out;

	}

	/**
	 * @return true where 'PROPS-END' hasn't been encountered yet
	 * @throws IOException
	 * @throws InvalidKeyLineException
	 */
	public static boolean readKeyAndValuePair(InputStream inputStream,
			Map<String, String> nodeProperties) throws InvalidKeyLineException,
			IOException {

		String key = readKey(inputStream);

		if (key == null) {
			// end of props section reached
			return false;
		}

		String value = readValue(inputStream);

		nodeProperties.put(key, value);

		return true;
	}

	private static String readValue(InputStream inputStream)
			throws InvalidKeyLineException, IOException {

		String value = readLinePair("V", inputStream);

		if (value == null)
			throw new InvalidKeyLineException("no value");
		else
			return value;

	}

	private static String readKey(InputStream inputStream)
			throws InvalidKeyLineException, IOException {
		return readLinePair("K", inputStream);
	}

	private static String readLinePair(String startsWithCharacter,
			InputStream inputStream) throws InvalidKeyLineException,
			IOException {

		while (true) {
		String lengthLine = readLine(inputStream, "UTF-8");

		if (lengthLine != null && lengthLine.length() == 0)
			continue;
		
		
		
		if (lengthLine != null && lengthLine.equals("PROPS-END")) {
			// if it looks like there is a line gap between the last property
			// and the end it it really because
			// a V 0 is realized as a \n. e.g. zero length text plus line
			// ending.
			// reading the V 0 value takes care of the line ending and we just
			// read the PROPS-END.
			return null;
		} else if (lengthLine == null || !lengthLine.startsWith(startsWithCharacter)) {
			throw new InvalidKeyLineException(lengthLine + " is invalid");
		}
		

		String parts[] = lengthLine.trim().split(" ");

		if (parts.length != 2)
			throw new InvalidKeyLineException(lengthLine
					+ " does not contain two parts");

		String lengthString = parts[1];

		int length = Integer.parseInt(lengthString);

		// we also need the null byte

		byte[] valueBuffer = new byte[length + 1];

		org.apache.commons.io.IOUtils.readFully(inputStream, valueBuffer);

		String value = new String(valueBuffer).trim();

		return value;
		
		}
	}

	private static String decode(CharsetDecoder decoder, byte[] in) {

		ByteBuffer inBuf = ByteBuffer.wrap(in);

		CharBuffer outBuf = CharBuffer.allocate(inBuf.capacity()
				* Math.round(decoder.maxCharsPerByte() + 0.5f));

		decoder.decode(inBuf, outBuf, true);

		decoder.flush(outBuf);

		decoder.reset();

		return outBuf.flip().toString();

	}

	/**
	 * Consume as many empty lines as required until a content line is read.
	 * 
	 * the number of empty lines read is tracked so that if this is a rewrite of
	 * the data the same number can appear in the output.
	 * 
	 * @param fileInputStream
	 * @param encoding
	 * @return the content line with info on the number of blank/empty lines
	 *         read before it was reached.
	 * @throws IOException
	 */
	public static ReadLineData readTilNonEmptyLine(
			InputStream fileInputStream, String encoding)
			throws IOException {
		String line = IOUtils.readLine(
				fileInputStream, encoding);

		int skippedLines = 0;
		while (line != null) {

			if (line.trim().length() == 0) {
				skippedLines++;
				line = IOUtils.readLine(
						fileInputStream, encoding);
				continue; // skip over emtpy lines
			} else
				return new ReadLineData(line, skippedLines);
		}

		if (skippedLines == 0)
			return null;
		else
			return new ReadLineData(null, skippedLines);
	}

	public static Map<String, String> extractRevisionProperties(
			InputStream inputStream, long propContentLength, long contentLength)
			throws IOException, InvalidKeyLineException {
		
		return extractRevisionProperties(inputStream, (int)propContentLength, (int)contentLength);
	}
	public static Map<String, String> extractRevisionProperties(
			InputStream inputStream, int propContentLength, int contentLength)
			throws IOException, InvalidKeyLineException {

		Map<String, String> revisionProperties = new LinkedHashMap<String, String>();

		while (IOUtils.readKeyAndValuePair(inputStream, revisionProperties));
		
		return revisionProperties;

	}
}
