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

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.runner.RunWith;
import io.github.svndump_to_git.branch.model.BranchData;
import io.github.svndump_to_git.git.model.branch.exceptions.VetoBranchException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Kuali Student Team
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:git-branch-detection-beans.xml"})
public abstract class AbstractBranchDetectorTest {
	
	@Resource (name="branchDetector")
	protected BranchDetector branchDetector;

	/**
	 * 
	 */
	public AbstractBranchDetectorTest() {
		super();
	}
	
	protected void assertPath (String filePath, String expectedBranchPath, String expectedFilePath) {
		assertPath(filePath, expectedBranchPath, expectedFilePath, false);
	}
	
	protected void assertPath (String filePath, String expectedBranchPath, String expectedFilePath, boolean expectVeto) {
		
		try {
			BranchData data = branchDetector.parseBranch(0L, filePath);
			
			Assert.assertNotNull(data);
			Assert.assertEquals(expectedBranchPath, data.getBranchPath());
			Assert.assertEquals(expectedFilePath, data.getPath());
		} catch (VetoBranchException e) {
			Assert.assertTrue(filePath + "vetoed unexpectantly.", expectVeto);
		}
		
	}
	
	
}
