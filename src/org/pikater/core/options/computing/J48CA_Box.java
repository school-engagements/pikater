package org.pikater.core.options.computing;

import org.pikater.core.agents.experiment.computing.Agent_WekaJ48;
import org.pikater.core.ontology.subtrees.agentinfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchdescription.ComputingAgent;
import org.pikater.core.ontology.subtrees.newoption.base.NewOption;
import org.pikater.core.ontology.subtrees.newoption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newoption.values.BooleanValue;
import org.pikater.core.ontology.subtrees.newoption.values.FloatValue;
import org.pikater.core.ontology.subtrees.newoption.values.IntegerValue;
import org.pikater.core.options.OptionsHelper;
import org.pikater.core.options.SlotsHelper;

public class J48CA_Box {
	
	public static AgentInfo get() {
		
		NewOption optionU = new NewOption("U", new BooleanValue(false));
		optionU.setDescription("Use unpruned tree");
		
		
		NewOption optionC = new NewOption("C", new FloatValue(0.25f), new RangeRestriction(
				new FloatValue(0.0f),
				new FloatValue(1.0f))
		);
		optionC.setDescription("Set confidence threshold for pruning. (Default: 0.25) (smaller values incur more pruning).");
		
		
		NewOption optionM = new NewOption("M", new IntegerValue(2), new RangeRestriction(
				new IntegerValue(1),
				new IntegerValue(10))
		);
		optionM.setDescription("Set minimum number of instances per leaf");
		
				
		NewOption optionR = new NewOption("R", new BooleanValue(false));
		optionR.setDescription("Use reduced error pruning. No subtree raising is performed");

		
		NewOption optionN = new NewOption("N", new IntegerValue(3), new RangeRestriction(
				new IntegerValue(1),
				new IntegerValue(10))
		);
		optionN.setDescription("Set minimum number of instances per leaf");

		
		NewOption optionB = new NewOption("B", new BooleanValue(false)); 
		optionB.setDescription("Use binary splits for nominal attributes");
		

		NewOption optionS = new NewOption("S", new BooleanValue(false));
		optionS.setDescription("Don't perform subtree raising");
		
		
		NewOption optionA = new NewOption("A", new BooleanValue(false));
		optionA.setDescription("If set, Laplace smoothing is used for predicted probabilites");


		NewOption optionQ = new NewOption("Q", new IntegerValue(3), new RangeRestriction(
				new IntegerValue(1),
				new IntegerValue(Integer.MAX_VALUE))
		);
		optionQ.setDescription("The seed for reduced-error pruning");
		

		AgentInfo agentInfo = new AgentInfo();
		agentInfo.importAgentClass(Agent_WekaJ48.class);
		agentInfo.importOntologyClass(ComputingAgent.class);
	
		agentInfo.setName("J48");
		agentInfo.setDescription("J48 method description");

		agentInfo.addOption(optionU);
		agentInfo.addOption(optionC);
		agentInfo.addOption(optionM);
		agentInfo.addOption(optionR);
		agentInfo.addOption(optionN);
		agentInfo.addOption(optionB);
		agentInfo.addOption(optionS);
		agentInfo.addOption(optionA);
		agentInfo.addOption(optionQ);
		agentInfo.addOptions(OptionsHelper.getCAOptions());

		//Slot Definition
		agentInfo.setInputSlots(SlotsHelper.getInputSlots_CA());
		agentInfo.setOutputSlots(SlotsHelper.getOutputSlots_CA());

		return agentInfo;
	}

}
