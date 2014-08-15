package org.pikater.web.vaadin.gui.server.components.dbviews;

import org.pikater.shared.database.views.tableview.base.AbstractTableDBView;
import org.pikater.shared.database.views.tableview.base.AbstractTableRowDBView;
import org.pikater.shared.database.views.tableview.base.ITableColumn;

import com.vaadin.ui.AbstractComponent;

public interface IDBViewRoot<T extends AbstractTableDBView>
{
	T getUnderlyingDBView();
	int getColumnSize(ITableColumn column);
	ITableColumn getExpandColumn();
	void onCellCreate(ITableColumn column, AbstractComponent component);
	void approveAction(ITableColumn column, AbstractTableRowDBView row, Runnable action);
}