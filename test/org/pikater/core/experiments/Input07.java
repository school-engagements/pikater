package org.pikater.core.experiments;

import org.pikater.core.agents.experiment.computing.Agent_WekaRBFNetworkCA;
import org.pikater.core.ontology.subtrees.batchdescription.ComputationDescription;
import org.pikater.core.ontology.subtrees.batchdescription.ComputingAgent;
import org.pikater.core.ontology.subtrees.batchdescription.DataSourceDescription;
import org.pikater.core.ontology.subtrees.batchdescription.EvaluationMethod;
import org.pikater.core.ontology.subtrees.batchdescription.FileDataProvider;
import org.pikater.core.ontology.subtrees.batchdescription.FileDataSaver;
import org.pikater.core.ontology.subtrees.batchdescription.evaluationmethod.CrossValidation;
import org.pikater.core.ontology.subtrees.newoption.base.NewOption;

/**
 * Single datasource -> single computing agent -> single save.
 * 
 * @author Klara
 */
public final class Input07 implements ITestExperiment {

	public ComputationDescription createDescription() {

		FileDataProvider fileDataProvider = new FileDataProvider();
		fileDataProvider.setFileURI("weather.arff");

		// Specify a datasource
		DataSourceDescription fileDataSource = new DataSourceDescription();
		fileDataSource.setDataProvider(fileDataProvider);

		// Create validation method for a computing agent
		EvaluationMethod evaluationMethod = new EvaluationMethod(
				CrossValidation.class.getName());

		// Create cross validation option
		NewOption optionF = new NewOption("F", 8);

		evaluationMethod.addOption(optionF);

		// Create two options for single computing agent
		NewOption optionS = new NewOption("S", 1);

		NewOption optionM = new NewOption("M", 2);

		// Create new computing agent, add options and datasource that we have
		// created above
		ComputingAgent comAgent1 = new ComputingAgent();
		comAgent1.setAgentType(Agent_WekaRBFNetworkCA.class.getName());
		comAgent1.addOption(optionS);
		comAgent1.addOption(optionM);
		comAgent1.setTrainingData(fileDataSource);
		comAgent1.setTestingData(fileDataSource);
		comAgent1.setValidationData(fileDataSource);
		comAgent1.setEvaluationMethod(new EvaluationMethod(
				CrossValidation.class.getName()));
		comAgent1.setEvaluationMethod(evaluationMethod);
		comAgent1.setModel(null);

		// Labeled data labeled by our CA are the new datasource
		DataSourceDescription computingDataSource1 = new DataSourceDescription();
		computingDataSource1.setOutputType("Data");
		computingDataSource1.setDataProvider(comAgent1);

		ComputingAgent comAgent2 = new ComputingAgent();
		comAgent2.setAgentType(Agent_WekaRBFNetworkCA.class.getName());
		comAgent2.addOption(optionS);
		comAgent2.addOption(optionM);
		comAgent2.setTrainingData(computingDataSource1);
		comAgent2.setTestingData(computingDataSource1);
		comAgent2.setValidationData(fileDataSource);
		comAgent2.setEvaluationMethod(new EvaluationMethod(
				CrossValidation.class.getName()));
		comAgent2.setEvaluationMethod(evaluationMethod);
		comAgent2.setModel(null);

		// Labeled data labeled by our CA are the new datasource
		DataSourceDescription computingDataSource2 = new DataSourceDescription();
		computingDataSource2.setOutputType("Data");
		computingDataSource2.setDataProvider(comAgent2);

		// Save labeled data
		FileDataSaver saver = new FileDataSaver();
		saver.setDataSource(computingDataSource2);

		ComputationDescription comDescription = new ComputationDescription();
		comDescription.addRootElement(saver);

		return comDescription;
	}
}
