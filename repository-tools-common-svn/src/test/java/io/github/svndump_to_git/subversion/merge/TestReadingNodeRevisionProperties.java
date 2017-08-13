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
package io.github.svndump_to_git.subversion.merge;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.github.svndump_to_git.common.exceptions.InvalidKeyLineException;
import io.github.svndump_to_git.common.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import io.github.svndump_to_git.common.io.ReadLineData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kuali Student Team
 * 
 *         Test how to read in the revision properties from the svn dump stream.
 * 
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TestReadingNodeRevisionProperties {

	private static final Logger log = LoggerFactory.getLogger(TestReadingNodeRevisionProperties.class);
	/**
	 * 
	 */
	public TestReadingNodeRevisionProperties() {
		// TODO Auto-generated constructor stub
	}

	@Test
	public void testReadingRevisionProperties() throws InvalidKeyLineException,
			IOException {

		Map<String, String> nodeProperties = new HashMap<String, String>();

		FileInputStream inputStream = new FileInputStream(
				"src/test/resources/revision-properties.dat");

		while (IOUtils.readKeyAndValuePair(
				inputStream, nodeProperties))
			;

		Assert.assertTrue(nodeProperties.containsKey("svn:author"));
		Assert.assertTrue(nodeProperties.containsKey("svn:date"));
		Assert.assertTrue(nodeProperties.containsKey("svn:log"));

		Assert.assertEquals("hjohnson", nodeProperties.get("svn:author"));
		Assert.assertEquals("2009-02-26T22:30:46.373926Z",
				nodeProperties.get("svn:date"));
		Assert.assertEquals("Fix firefox popup frame size issue",
				nodeProperties.get("svn:log"));

	}

	@Test
	public void testReadingComplexRevisionProperties() throws IOException,
			InvalidKeyLineException {

		Map<String, String> nodeProperties = new HashMap<String, String>();

		FileInputStream inputStream = new FileInputStream(
				"src/test/resources/revision-properties-no-log.dat");

		String contentLengthLine = IOUtils
				.readLine(inputStream, "UTF8");

		Assert.assertEquals("Content-length: 102", contentLengthLine.replace("\r", ""));

		String skippedLine = IOUtils.readLine(
				inputStream, "UTF8");

		while (IOUtils.readKeyAndValuePair(
				inputStream, nodeProperties))
			;

		Assert.assertTrue(nodeProperties.containsKey("svn:author"));
		Assert.assertTrue(nodeProperties.containsKey("svn:date"));
		Assert.assertTrue(nodeProperties.containsKey("svn:log"));

		Assert.assertEquals("hjohnson", nodeProperties.get("svn:author"));
		Assert.assertEquals("2009-02-26T22:30:46.373926Z",
				nodeProperties.get("svn:date"));
		Assert.assertEquals("", nodeProperties.get("svn:log"));

	}

	private int extractLength(String length) {
		String[] parts = length.split(":");

		return Integer.parseInt(parts[1].trim());
	}

	@Test
	public void testReadingMultipleNodes() throws IOException,
			InvalidKeyLineException {

		// just in the sequential order they were found
		Map<Integer, Map<String, String>> orderedRevisionProperties = new HashMap<Integer, Map<String, String>>();

		int revPropCounter = 0;
		
		FileInputStream inputStream = new FileInputStream(
				"src/test/resources/sample.dump");

		int propContentLength = Integer.MAX_VALUE;
		int contentLength = Integer.MAX_VALUE;

		while (true) {

			ReadLineData lineData = IOUtils
					.readTilNonEmptyLine(inputStream, "UTF8");

			if (lineData == null) {
				break;
			} else if (lineData.getLine() == null) {

				break;
			}

//			log.info(lineData.getLine());
			
			if (!lineData.startsWith("Content-length:")) {

				if (lineData.startsWith("Prop-content-length:")) {
					propContentLength = extractLength(lineData.getLine());
				}
				continue;
			}

			String emptyLine = IOUtils.readLine(inputStream, "UTF8");
			
			Assert.assertEquals(true, emptyLine.isEmpty());
			
			contentLength = extractLength(lineData.getLine());
			
			Map<String, String> revisionProperties = IOUtils.extractRevisionProperties(inputStream, propContentLength, contentLength);
			
			orderedRevisionProperties.put(revPropCounter, revisionProperties);
			
			revPropCounter++;
			
			propContentLength = -1;
			contentLength = -1;
			
			
		}
		
		// first
		Map<String, String> revProps = orderedRevisionProperties.get(0);

		Assert.assertEquals(3, revProps.size());
		
		Assert.assertTrue(revProps.containsKey("svn:author"));
		Assert.assertTrue(revProps.containsKey("svn:date"));
		Assert.assertTrue(revProps.containsKey("svn:log"));
	
	}
	
	@Test
	public void testRice15812SvnAdmin() throws InvalidKeyLineException, IOException {
		
		FileInputStream inputStream = new FileInputStream("src/test/resources/rice-r15812-svnadmin.dump");
		
		test15812(inputStream);
		
		
	}
	
	@Test
	public void testRice15812SvnKit() throws InvalidKeyLineException, IOException {
		
		
		FileInputStream inputStream = new FileInputStream("src/test/resources/rice-r15812-svnkit.dump");
		
		test15812(inputStream);
		
		
	}

	private void test15812(FileInputStream inputStream) throws InvalidKeyLineException, IOException {

		Map<String, String> nodeProperties = new HashMap<>();
		
		while (IOUtils.readKeyAndValuePair(inputStream, nodeProperties));
		
		String author = nodeProperties.get("svn:author");
		
		Assert.assertNotNull(author);
		
		Assert.assertEquals("jkneal", author);
		
		String date = nodeProperties.get("svn:date");
		
		Assert.assertNotNull(date);
		
		Assert.assertEquals("2011-02-21T14:55:36.976704Z", date);
		
		String commitMessage = nodeProperties.get("svn:log");
		
		Assert.assertNotNull(commitMessage);
		
		Assert.assertEquals(637, commitMessage.getBytes().length);
		
		log.info("");
	}
	
	@Test
	public void testStripStarFromRevision() {
		
		String recordOnlyChange = "123456*";
		
		Long revision = Long.parseLong(recordOnlyChange.replaceAll("\\*", ""));
		
		Assert.assertNotNull(revision);
		
		long expected = 123456L;
		
		Assert.assertEquals(expected, revision.longValue());
	}
}
