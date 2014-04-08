package org.pikater.core.options.xmlGenerators;

import org.pikater.core.options.LogicalUnit;
import org.pikater.shared.experiment.BoxType;
import org.pikater.shared.experiment.parameters.AbstractParameter;
import org.pikater.shared.experiment.parameters.RangedValueParameter;
import org.pikater.shared.util.Interval;

public class SimulatedAnnealingBox extends LogicalUnit
{

	protected SimulatedAnnealingBox() {

		this.setIsBox(true);
		this.setDisplayName("SimulatedAnnealing-Searcher");
		this.setAgentName("pikater/Agent_SimulatedAnnealing");
		this.setType(BoxType.SEARCHER);
		this.setOntology(null);
		this.setPicture("picture2.jpg");
		this.setDescription("Searches defined parameters and provides them in output slots. Simulated annealing is used for searching.");
		
		AbstractParameter paramE = new RangedValueParameter<Double>(
				0.1,
				new Interval<Double>(0.0, 1.0),
				false);
		
		AbstractParameter paramM = new RangedValueParameter<Integer>(
				50,
				new Interval<Integer>(1, 1000),
				false);
		
		AbstractParameter paramT = new RangedValueParameter<Double>(
				1.0,
				new Interval<Double>(0.0, 100.0),
				false);
		
		AbstractParameter paramS = new RangedValueParameter<Double>(
				0.5,
				new Interval<Double>(0.0, 1.0),
				false);

		this.addParameter(paramE);
		this.addParameter(paramM);
		this.addParameter(paramT);
		this.addParameter(paramS);
		
	}
}