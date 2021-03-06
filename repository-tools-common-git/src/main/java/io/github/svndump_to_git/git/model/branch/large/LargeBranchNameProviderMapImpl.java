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
package io.github.svndump_to_git.git.model.branch.large;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jgit.lib.ObjectId;
import io.github.svndump_to_git.git.model.branch.utils.GitBranchUtils;
import io.github.svndump_to_git.git.model.branch.utils.GitBranchUtils.ILargeBranchNameProvider;

public class LargeBranchNameProviderMapImpl implements ILargeBranchNameProvider {

	private Map<String, String>largeBranchNameMap = new HashMap<String, String>();
	
	@Override
	public String getBranchName(String longBranchId, long revision) {
		return largeBranchNameMap.get(longBranchId);
	}

	@Override
	public String storeLargeBranchName(String branchName, long revision) {
	
		ObjectId largeBranchNameId = GitBranchUtils.getBranchNameObjectId(branchName);
		
		this.largeBranchNameMap.put(largeBranchNameId.name(), branchName);
		
		return largeBranchNameId.name();
		
	}

	/**
	 * 
	 * @see java.util.Map#clear()
	 */
	public void clear() {
		largeBranchNameMap.clear();
	}

	
}
