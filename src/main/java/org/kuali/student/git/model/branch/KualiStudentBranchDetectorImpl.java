/*
 * Copyright 2014 The Kuali Foundation
 * 
 * Licensed under the Educational Community License, Version 1.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.kuali.student.git.model.branch;

import org.apache.commons.lang3.StringUtils;
import org.kuali.student.git.model.exceptions.VetoBranchException;
import org.kuali.student.svn.tools.merge.model.BranchData;

/**
 * @author Kuali Student Team
 *
 */
public class KualiStudentBranchDetectorImpl implements BranchDetector {

	private static final String MERGES = "merges";

	private static final String TOOLS = "tools";
	
	private static final String DEPLOYMENTLAB_TEST_BRANCHES_PROPOSALHISTORY = "deploymentlab/test/branches/proposalhistory";

	private static final String DEPLOYMENTLAB_BRANCHES_PROPOSALHISTORY = "deploymentlab/branches/proposalhistory";

	private static final String DEPLOYMENTLAB_STUDENT_TRUNK_KS_TOOLS_MAVEN_COMPONENT_SANDBOX_TRUNK = "deploymentlab/student/trunk/ks-tools/maven-component-sandbox/trunk";

	private static final String DEPLOYMENTLAB_UI_KS_CUKE_TESTING_TRUNK = "deploymentlab/UI/ks-cuke-testing/trunk";
	
	private static final String BRANCHES_INACTIVE = "branches/inactive";
	private static final String BUILD_DASH = "build-";
	private static final String TAGS_BUILDS = "tags/builds";
	private static final String KS_CFG_DBS = "ks-cfg-dbs";
	private static final String DICTIONARY = "dictionary";
	private static final String SANDBOX = "sandbox";
	private static final String ENUMERATION = "enumeration";
	private static final String TRUNK = "trunk";
	private static final String BRANCHES = "branches";
	private static final String TAGS = "tags";
	private static final String OLD_TAGS = "old-tags";
	private static final String OLD_BUILD_TAGS = "old-build-tags";
	private static final String KS_OLD_DIRECTORY_STRUCTURE = "old-directory-structure";
	private static final String DEPLOYMENTLAB = "deploymentlab";
	
	private static final String TOOLS_MAVEN_DICTIONARY_GENERATOR = "tools/maven-dictionary-generator";
	private static final String DEPLOYMENTLAB_KS_CUKE_TESTING = "deploymentlab/ks-cuke-testing";
	private static final String DEPLOYMENTLAB_UI_KS_CUKE_TESTING = "deploymentlab/UI/ks-cuke-testing";
	private static final String SANDBOX_TEAM2_KS_RICE_STANDALONE_BRANCHES_KS_RICE_STANDALONE_UBERWAR = "sandbox/team2/ks-rice-standalone/branches/ks-rice-standalone-uberwar";
	private static final String KS_WEB_BRANCHES_KS_WEB_DEV = "ks-web/branches/ks-web-dev";

	/**
	 * 
	 */
	public KualiStudentBranchDetectorImpl() {
		// TODO Auto-generated constructor stub
	}


	@Override
	public BranchData parseBranch(Long revision, String path) throws VetoBranchException {

		String[] parts = path.split("\\/");
		
		if (parts.length == 1 && !path.equals(TRUNK)) {

			throw new VetoBranchException(path
					+ " vetoed because length is only one part");
		}
		
		String lastPart = parts[parts.length-1];
		
		if (BRANCHES.equals(lastPart) || TAGS.equals(lastPart) || OLD_TAGS.equals(lastPart) || OLD_BUILD_TAGS.equals(lastPart))
			throw new VetoBranchException(path + " vetoed because it is incomplete.");
		
		
		if (TAGS.equals(parts[0]) && KS_OLD_DIRECTORY_STRUCTURE.equals(lastPart))
			throw new VetoBranchException (TAGS + "/" + KS_OLD_DIRECTORY_STRUCTURE + " is not a valid branch by itself, its children are valid individually.");
		

		if (!(isPathValidBranchTagOrTrunk(path))) {

			/*
			 * If it starts with enumeration allow it to be treated as a
			 * branch.
			 * 
			 * sandbox is also a special case.
			 */
			if (!(path.startsWith(ENUMERATION)
					|| path.startsWith(SANDBOX)
					|| path.startsWith(DICTIONARY) || path
					.startsWith(KS_CFG_DBS) || path.startsWith(DEPLOYMENTLAB) || path.startsWith(TOOLS) || path.startsWith(MERGES)))
				throw new VetoBranchException(
						path
								+ "vetoed because it does not contain tags, branches or trunk");
		}

		/*
		 * Custom whitelist for tricky paths
		 */
		
		if (path.startsWith(KS_WEB_BRANCHES_KS_WEB_DEV)) {
			
			return buildBranchData(revision, path, KS_WEB_BRANCHES_KS_WEB_DEV);
		}
		else if (path.startsWith(DEPLOYMENTLAB)) {
			
			if (path.contains(TRUNK))
				return null; // let the default logic pick this up.
			
			if (path.startsWith(DEPLOYMENTLAB_BRANCHES_PROPOSALHISTORY)) {
				return buildBranchData(revision, path, DEPLOYMENTLAB_BRANCHES_PROPOSALHISTORY);
			}
			else if (path.startsWith(DEPLOYMENTLAB_TEST_BRANCHES_PROPOSALHISTORY)) {
				return buildBranchData (revision, path, DEPLOYMENTLAB_TEST_BRANCHES_PROPOSALHISTORY);
			}
			else if (path.startsWith(DEPLOYMENTLAB_UI_KS_CUKE_TESTING) && !isPathValidBranchTagOrTrunk(path)) {
				return buildBranchData(revision, path, DEPLOYMENTLAB_UI_KS_CUKE_TESTING);
			}
			else if (path.startsWith(DEPLOYMENTLAB_UI_KS_CUKE_TESTING_TRUNK)) {
				
				return buildBranchData(revision, path, DEPLOYMENTLAB_UI_KS_CUKE_TESTING_TRUNK);
			}
			else if (path.startsWith(DEPLOYMENTLAB_KS_CUKE_TESTING)) {
				return buildBranchData(revision, path, DEPLOYMENTLAB_KS_CUKE_TESTING);
			}
			else if (path.startsWith(DEPLOYMENTLAB_STUDENT_TRUNK_KS_TOOLS_MAVEN_COMPONENT_SANDBOX_TRUNK)) {
				return buildBranchData(revision, path, DEPLOYMENTLAB_STUDENT_TRUNK_KS_TOOLS_MAVEN_COMPONENT_SANDBOX_TRUNK);
			}
		}
		
		else if (path.startsWith(SANDBOX_TEAM2_KS_RICE_STANDALONE_BRANCHES_KS_RICE_STANDALONE_UBERWAR)) {
		
			return buildBranchData(revision, path, SANDBOX_TEAM2_KS_RICE_STANDALONE_BRANCHES_KS_RICE_STANDALONE_UBERWAR);
		}
		
		else if (path.startsWith(TOOLS_MAVEN_DICTIONARY_GENERATOR) && !isPathValidBranchTagOrTrunk(path)) {
			/*
			 * BranchUtils.parse can find the trunk if it exists.
			 * 
			 * only make the branch if there is no trunk in the path.
			 */
			return buildBranchData(revision, path, TOOLS_MAVEN_DICTIONARY_GENERATOR);
		}
		else if (path.contains(TAGS_BUILDS)) {
			
			int partContainingBuildNameIndex = -1;
			
			for (int i = parts.length-1; i >= 0; i--) {
				
				String canidatePart = parts[i];
				
				if (canidatePart.contains(BUILD_DASH)) {
					partContainingBuildNameIndex = i;
					break;
				}
			}
			
			if (partContainingBuildNameIndex != -1 && partContainingBuildNameIndex < parts.length) {
				return buildBranchData(revision, parts, partContainingBuildNameIndex);
			}
			
			// else fall through to return null below
			
		}
		else if (path.contains(BRANCHES_INACTIVE)) {
			
			int branchesPartIndex = -1;
			
			for (int i = 0; i < parts.length; i++) {
				String canidatePart = parts[i];
				
				if (canidatePart.equals(BRANCHES)) {
					branchesPartIndex = i;
					break;
				}
			}
			
			if (branchesPartIndex != -1) {
				int inactvePartIndex = branchesPartIndex+1;
				int branchNamePartIndex = inactvePartIndex+1;
				
				if (branchNamePartIndex < parts.length) {
					return buildBranchData(revision, parts, branchNamePartIndex);
				}
				// else fall through
			}
			
			// else fall through to return null below
			
		}
		else if (path.startsWith(MERGES)) { 
			int branchNameIndex = 1;
			
			if (branchNameIndex < parts.length) {
			
				return buildBranchData(revision, path, branchNameIndex+1);
			}
			// else fall through and return null
			
		}
		else if ((path.startsWith(ENUMERATION) || path.startsWith(DICTIONARY) || path.startsWith(KS_CFG_DBS) || path.startsWith(DEPLOYMENTLAB) ) && !isPathValidBranchTagOrTrunk(path)) {
			return buildBranchData(revision, path, 1);
		}
		
		return null;
	}

	private BranchData buildBranchData(Long revision, String path, int filePathStartIndex) {

		String[] parts = path.split("\\/");
		
		String branchPath = StringUtils.join(parts, '/', 0, filePathStartIndex);
		String filePath = StringUtils.join(parts, '/', filePathStartIndex, parts.length);
		
		return new BranchData(revision, branchPath, filePath);
	}

	private boolean isPathValidBranchTagOrTrunk(String path) {
		if (path.contains(TAGS) || path.contains(BRANCHES) || path
				.contains(TRUNK))
				return true;
		else
			return false;
				
	}

	private BranchData buildBranchData(Long revision, String path, String branchPath) {
		
		StringBuilder filePath = new StringBuilder(path.substring(branchPath.length()));
		
		if (filePath.length() > 0 &&filePath.charAt(0) == '/') 
			filePath.delete(0, 1);
		
		return new BranchData(revision, branchPath, filePath.toString());
		
	}

	private BranchData buildBranchData(Long revision, String[] parts,
			int lastPartOfBranchPathIndex) {
		
		String branchPath = StringUtils.join(parts, '/', 0, lastPartOfBranchPathIndex+1);
		
		String filePath = StringUtils.join(parts, '/', lastPartOfBranchPathIndex+1, parts.length);
		
		return new BranchData(revision, branchPath, filePath);
		
	}
}