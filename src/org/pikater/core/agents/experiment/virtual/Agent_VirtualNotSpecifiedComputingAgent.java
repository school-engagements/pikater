package org.pikater.core.agents.experiment.virtual;

import org.pikater.core.ontology.subtrees.agentinfo.AgentInfo;
import org.pikater.core.options.virtual.NotSpecifiedComputingAgent_Box;

public class Agent_VirtualNotSpecifiedComputingAgent extends Agent_VirtualBoxProvider {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7726357156984180804L;

	@Override
	protected AgentInfo getAgentInfo() {

		return NotSpecifiedComputingAgent_Box.get();
	}
}
