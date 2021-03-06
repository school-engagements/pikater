package org.pikater.shared.experiment;

import org.pikater.core.ontology.subtrees.batchdescription.export.Slot;

/**
 * Connects experiment {@link UniversalElement elements}. Represents
 * an oriented edge between them.
 * 
 * @author stepan
 */
public class UniversalElementConnector {
	/**
	 * The edge represented by this instance goes FROM this element.
	 */
	private UniversalElement fromElement;

	/**
	 * Name/identifier of the output {@link Slot endpoint} of the edge
	 * represented by this instance. The endpoint must belong in
	 * {@link #fromElement}.
	 */
	private String outputDataIdentifier;

	/**
	 * Name/identifier of the input {@link Slot endpoint} of the edge
	 * represented by this instance. The endpoint must NOT belong in
	 * {@link #fromElement}.
	 */
	private String inputDataIdentifier;

	/**
	 * Gets the input {@link #inputDataIdentifier endpoint}.
	 */
	public String getInputDataIdentifier() {
		return inputDataIdentifier;
	}

	/**
	 * Sets the input {@link #inputDataIdentifier endpoint}.
	 */
	public void setInputDataIdentifier(String inputDataIdentifier) {
		this.inputDataIdentifier = inputDataIdentifier;
	}

	/**
	 * Gets the output {@link #outputDataIdentifier endpoint}.
	 */
	public String getOutputDataIdentifier() {
		return outputDataIdentifier;
	}

	/**
	 * Sets the output {@link #outputDataIdentifier endpoint}.
	 */
	public void setOutputDataIdentifier(String outputDataIdentifier) {
		this.outputDataIdentifier = outputDataIdentifier;
	}

	/**
	 * Gets {@link #fromElement output element}.
	 */
	public UniversalElement getFromElement() {
		return fromElement;
	}

	/**
	 * Sets {@link #fromElement output element}.
	 */
	public void setFromElement(UniversalElement fromElement) {
		this.fromElement = fromElement;
	}

	/**
	 * Are both endpoints known?
	 */
	public boolean isFullySpecified() {
		return (inputDataIdentifier != null) && (outputDataIdentifier != null);
	}

	/**
	 * Is this connection valid? If not, throws an exception.
	 */
	public void validate() {
		if (fromElement == null) {
			throw new IllegalStateException("From element is not defined.");
		} else if (((inputDataIdentifier != null) && (outputDataIdentifier == null)) || ((inputDataIdentifier == null) && (outputDataIdentifier != null))) {
			throw new IllegalStateException("One of the data types is not defined.");
		}
	}
}
