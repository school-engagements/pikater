package org.pikater.web.vaadin.gui.server.components.dbviews.pickers;

import org.pikater.shared.database.views.tableview.datasets.DatasetTableDBViewMin;
import org.pikater.web.vaadin.gui.server.components.dbviews.DatasetDBViewRoot;
import org.pikater.web.vaadin.gui.server.components.dbviews.base.AbstractTablePicker;

public class DatasetTablePicker extends AbstractTablePicker
{
	private static final long serialVersionUID = -417558814086758389L;

	public DatasetTablePicker(String caption)
	{
		super(caption);
		setView(new DatasetDBViewRoot<DatasetTableDBViewMin>(new DatasetTableDBViewMin()));
	}
}