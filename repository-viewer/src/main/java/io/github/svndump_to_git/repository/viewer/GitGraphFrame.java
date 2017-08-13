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
package io.github.svndump_to_git.repository.viewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Paint;
import java.io.File;
import java.util.Collection;
import java.util.Map;

import javax.swing.JFrame;

import org.apache.commons.collections15.Transformer;
import org.eclipse.jgit.revwalk.RevCommit;
import io.github.svndump_to_git.repository.viewer.listener.GitGraphVertexPickedListener;
import io.github.svndump_to_git.repository.viewer.listener.GitModalGraphMousePlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

/**
 * @author ocleirig
 *
 */
public class GitGraphFrame extends JFrame {

	private static final Logger log = LoggerFactory.getLogger(GitGraphFrame.class);
	
	/**
	 * @param simplify 
	 * @throws HeadlessException
	 */
	public GitGraphFrame(File repository, final Graph<RevCommit, String> graph, final Map<RevCommit, String> branchHeadCommitToBranchNameMap, boolean simplify)
			throws HeadlessException {
		super("Git Graph Viewer, repository: " + repository.getAbsolutePath());

		Layout<RevCommit, String> layout = new StaticLayout<RevCommit, String>(graph, new GitGraphTransformer(branchHeadCommitToBranchNameMap, simplify));

		Dimension d;
		layout.setSize(d = new Dimension(800, 600));

		VisualizationViewer<RevCommit, String> visualizer = new VisualizationViewer<RevCommit, String>(
				layout);

		
//		visualizer.getRenderer().setVertexRenderer(new Renderer.Vertex<RevCommit, String>() {
//			
//			@Override
//			public void paintVertex(RenderContext<RevCommit, String> rc,
//					Layout<RevCommit, String> layout, RevCommit v) {
//				
//			}
//		});
		visualizer.setPreferredSize(d);

		visualizer.getRenderContext().setVertexLabelTransformer(
				new Transformer<RevCommit, String>() {

					@Override
					public String transform(RevCommit input) {
						Collection<String> inEdges = graph.getInEdges(input);
						
						return String.valueOf(inEdges.size());
					}
				});
		
		visualizer.getRenderer().getVertexLabelRenderer().setPosition(Position.E);
		
		visualizer.getRenderContext().setVertexFillPaintTransformer(new Transformer<RevCommit, Paint>() {

			/* (non-Javadoc)
			 * @see org.apache.commons.collections15.Transformer#transform(java.lang.Object)
			 */
			@Override
			public Paint transform(RevCommit input) {
				
				Collection<String> inEdges = graph.getInEdges(input);
				
				if (branchHeadCommitToBranchNameMap.containsKey(input)) {
					
					if (inEdges.size() > 10)
						// light yellow
						return new Color(255, 255, 188);
					else
						return Color.YELLOW;
				}
				else {
					
					if (inEdges.size() > 10)
						// pink
						return new Color(253, 188, 188);
					else
						return Color.RED;
				}
			}
			
		});

		GitModalGraphMousePlugin gm = new GitModalGraphMousePlugin();
		
		visualizer.setGraphMouse(gm);

		GitGraphDetailsPanel detailsPanel = new GitGraphDetailsPanel(gm.getModeComboBox(), branchHeadCommitToBranchNameMap, simplify);
		
		new GitGraphVertexPickedListener(visualizer, detailsPanel);
		
		visualizer.getRenderer().getVertexLabelRenderer()
				.setPosition(Position.CNTR);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(visualizer, BorderLayout.CENTER);

		getContentPane().add(detailsPanel, BorderLayout.SOUTH);
		
		pack();
		setVisible(true);

	}

}
