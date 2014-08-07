package org.pikater.shared.database.views.tableview.base;

import org.pikater.shared.database.views.base.values.DBViewValueType;

public interface ITableColumn
{
	String getDisplayName();
	DBViewValueType getColumnType();
}