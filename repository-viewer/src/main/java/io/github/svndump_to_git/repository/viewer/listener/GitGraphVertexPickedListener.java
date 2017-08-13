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
package io.github.svndump_to_git.repository.viewer.listener;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collection;

import io.github.svndump_to_git.repository.viewer.GitGraphDetailsPanel;
import org.eclipse.jgit.revwalk.RevCommit;

import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.picking.PickedState;

/**
 * @author ocleirig
 *
 */
public class GitGraphVertexPickedListener implements ItemListener {

	private PickedState<RevCommit> pickedState;
	private GitGraphDetailsPanel detailsPanel;
	private VisualizationViewer<RevCommit, String> visualizer;

	/**
	 * @param visualizer
	 * 
	 */
	public GitGraphVertexPickedListener(VisualizationViewer<RevCommit, String> visualizer, GitGraphDetailsPanel detailsPanel) {
		super();
		this.visualizer = visualizer;
		this.pickedState = visualizer.getPickedVertexState();
		this.detailsPanel = detailsPanel;
		
		pickedState.addItemListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	@Override
	public void itemStateChanged(ItemEvent e) {

		if (e.getStateChange() != ItemEvent.SELECTED)
			return; // only consume selected events
		
		Object subject = e.getItem();
		
		if (subject instanceof RevCommit) {

			RevCommit commit = (RevCommit) subject;
			
			if (pickedState.isPicked(commit)) {
				// selected
				
				Collection<String> inEdges = visualizer.getGraphLayout().getGraph().getInEdges(commit);
				
				detailsPanel.setSelectedCommit(commit, inEdges.size());
				
			}
			else {
				// not selected
			}
			
		}
	}

}
