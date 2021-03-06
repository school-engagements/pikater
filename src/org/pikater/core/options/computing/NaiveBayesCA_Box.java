package org.pikater.core.options.computing;

import org.pikater.core.agents.experiment.computing.Agent_WekaNaiveBayesCA;
import org.pikater.core.ontology.subtrees.agentinfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchdescription.ComputingAgent;
import org.pikater.core.ontology.subtrees.newoption.base.NewOption;
import org.pikater.core.ontology.subtrees.newoption.values.BooleanValue;
import org.pikater.core.options.OptionsHelper;
import org.pikater.core.options.SlotsHelper;

public class NaiveBayesCA_Box {

	public static AgentInfo get() {

		/**
		#Use kernel estimation for modelling numeric attributes rather than a single normal distribution.
		$ K boolean
		**/
		NewOption optionK = new NewOption("K", new BooleanValue(false));
		optionK.setDescription("Use kernel estimation for modelling numeric attributes rather than a single normal distribution");
		
		
		/**
		# Use supervised discretization to process numeric attributes.
		$ D boolean
		**/
		NewOption optionD = new NewOption("D", new BooleanValue(false));
		optionD.setDescription("Use supervised discretization to process numeric attributes");


		AgentInfo agentInfo = new AgentInfo();
		agentInfo.importAgentClass(Agent_WekaNaiveBayesCA.class);
		agentInfo.importOntologyClass(ComputingAgent.class);
	
		agentInfo.setName("NaiveBayes");
		agentInfo.setDescription("Naive Bayes Method");

		agentInfo.addOption(optionK);
		agentInfo.addOption(optionD);
		agentInfo.addOptions(OptionsHelper.getCAOptions());
		
		// Slots Definition
		agentInfo.setInputSlots(SlotsHelper.getInputSlots_CA());
		agentInfo.setOutputSlots(SlotsHelper.getOutputSlots_CA());

		return agentInfo;
	}

}
