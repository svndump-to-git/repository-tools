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
package io.github.svndump_to_git.git.model.branch;

import java.util.ArrayList;
import java.util.List;

import io.github.svndump_to_git.branch.model.BranchData;
import io.github.svndump_to_git.git.model.branch.exceptions.VetoBranchException;

/**
 * @author Kuali Student Team
 *
 */
public class BranchDetectorSequencer implements BranchDetector {

	private List<BranchDetector>detectors = new ArrayList<>();
	
	/**
	 * 
	 */
	public BranchDetectorSequencer() {
		// TODO Auto-generated constructor stub
	}
	
	

	/**
	 * @param detectors the detectors to set
	 */
	public void setDetectors(List<BranchDetector> detectors) {
		this.detectors = detectors;
	}



	/* (non-Javadoc)
	 * @see BranchDetector#parseBranch(java.lang.Long, java.lang.String)
	 */
	@Override
	public BranchData parseBranch(Long revision, String path)
			throws VetoBranchException {
		
		for (BranchDetector detector : detectors) {
			
			BranchData data = detector.parseBranch(revision, path);
			
			if (data != null)
				return data;
			
		}
		
		// at this point none of the detectors found or vetoed the branch
		return null;
	}
	
	
	
	

}
