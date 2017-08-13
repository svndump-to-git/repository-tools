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
package io.github.svndump_to_git.git.model.tree;

import io.github.svndump_to_git.git.model.tree.utils.GitTreeProcessor;
import org.eclipse.jgit.lib.ObjectId;
import io.github.svndump_to_git.git.model.tree.exceptions.GitTreeNodeInitializationException;

/**
 * @author ocleirig
 *
 */
public class GitTreeNodeInitializerImpl implements GitTreeNodeInitializer {

	private GitTreeProcessor treeProcessor;
	
	/**
	 * 
	 */
	public GitTreeNodeInitializerImpl(GitTreeProcessor treeProcessor) {
		this.treeProcessor = treeProcessor;
	}

	/* (non-Javadoc)
	 * @see GitTreeNodeInitializer#initialize(GitTreeData.GitTreeNodeData)
	 */
	@Override
	public void initialize(GitTreeNodeData node) throws GitTreeNodeInitializationException {
		
		try {
			ObjectId originalTreeId = node.getOriginalTreeObjectId();
			
			if (originalTreeId != null) {
				
				GitTreeNodeData loadedNode = treeProcessor.extractExistingTreeData(originalTreeId, node.getName());
			
				node.replaceWith (loadedNode);
				
			}
			
			node.setInitialized(true);
		} catch (Exception e) {
			throw new GitTreeNodeInitializationException("initialize(node name="+node!=null?node.getName():"null"+") failed unexpectantly: ", e);
		}
		
	}
	
	

}
