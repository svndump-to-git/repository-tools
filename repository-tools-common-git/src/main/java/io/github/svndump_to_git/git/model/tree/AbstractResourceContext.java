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

import org.eclipse.jgit.lib.ObjectId;

public abstract class AbstractResourceContext implements ResourceContext {

	protected ObjectId objectId;
	private String type;

	/**
	 * 
	 */
	public AbstractResourceContext(String type, ObjectId objectId) {
		super();
		this.type = type;
		this.objectId = objectId;
	}

	/* (non-Javadoc)
	 * @see GitTreeData.ResourceContext#getErrorMessage()
	 */
	@Override
	public String getErrorMessage() {
		return new StringBuilder(" ").append(type).append("Id = ").append(objectId).toString();
	}
	
	
	
}