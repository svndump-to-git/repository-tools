/*
 *  Copyright 2014 The Kuali Foundation Licensed under the
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
package io.github.svndump_to_git.git.model.tree.utils;

import java.io.IOException;

import io.github.svndump_to_git.git.model.tree.GitTreeData;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectId;

/**
 * @author ocleirig
 *
 */
public interface TreeProcessor {

	/**
	 * Extract in GitTreeData (mutable) form the tree data pointed at by the Git Tree Id provided.
	 * 
	 * @param treeId
	 * @param shallow
	 * @return
	 * @throws MissingObjectException
	 * @throws IncorrectObjectTypeException
	 * @throws CorruptObjectException
	 * @throws IOException
	 */
	GitTreeData extractExistingTreeData(ObjectId treeId, boolean shallow)
			throws MissingObjectException, IncorrectObjectTypeException,
			CorruptObjectException, IOException;

}
