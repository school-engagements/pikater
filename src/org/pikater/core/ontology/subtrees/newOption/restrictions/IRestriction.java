package org.pikater.core.ontology.subtrees.newOption.restrictions;

import org.pikater.core.ontology.subtrees.newOption.base.IValidated;
import org.pikater.shared.util.ICloneable;

import jade.content.Concept;

public interface IRestriction extends Concept, ICloneable, IValidated {
	@Override
	IRestriction clone();
}