package org.pikater.core.dataStructures.options.types;

import org.pikater.core.ontology.messages.Option;




public class OptionValue extends AbstractOption {

	private Object object = null;

	public OptionValue() {}

	public OptionValue(Object object) {
		this.object = object;
	}

	@Override
	public Class<? extends Object> getOptionClass() {
		return this.object.getClass();
	}

	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}

	@Override
	public Option toOption() {
		// TODO Not complete

		Option option = new Option();
		
		if (object instanceof Integer) {
			
			Integer thisInteger = (Integer) object;
			option.setData_type("INT");
			option.setValue(thisInteger.toString());
			
		} else if (object instanceof Float) {
			
			Float thisFloat = (Float) object;
			option.setData_type("FLOAT");
			option.setValue(thisFloat.toString());
		}
		
		return option;
	}
}