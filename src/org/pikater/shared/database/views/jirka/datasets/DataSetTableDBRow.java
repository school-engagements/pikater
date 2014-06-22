package org.pikater.shared.database.views.jirka.datasets;

import org.pikater.shared.AppHelper;
import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.jpa.JPAGlobalMetaData;
import org.pikater.shared.database.views.jirka.abstractview.AbstractTableRowDBView;
import org.pikater.shared.database.views.jirka.abstractview.IColumn;
import org.pikater.shared.database.views.jirka.abstractview.values.AbstractDBViewValue;
import org.pikater.shared.database.views.jirka.abstractview.values.StringDBViewValue;

public class DataSetTableDBRow extends AbstractTableRowDBView {

	private JPADataSetLO dataset=null;

	public DataSetTableDBRow(JPADataSetLO dataset)
	{
		this.dataset=dataset;
	}

	@Override
	public AbstractDBViewValue<? extends Object> initValueWrapper(final IColumn column)
	{
		DataSetTableDBView.Column specificColumn = (DataSetTableDBView.Column) column;
		switch(specificColumn)
		{
		/*
		 * First the read-only properties.
		 */
		case DESCRIPTION:
			return new StringDBViewValue(dataset.getDescription(), true)
			{
				@Override
				protected void updateEntities(String newValue)
				{
				}
				
				@Override
				protected void commitEntities()
				{
				}
			};
		case NUMBER_OF_INSTANCES:
			return new StringDBViewValue(dataset.getGlobalMetaData()!=null?""+dataset.getGlobalMetaData().getNumberofInstances():"N/A", true)
			{
				@Override
				protected void updateEntities(String newValue)
				{
				}
				
				@Override
				protected void commitEntities()
				{
				}
			};
		case DEFAULT_TASK_TYPE:
			JPAGlobalMetaData gmd=dataset.getGlobalMetaData();
			if(gmd!=null){
				return new StringDBViewValue(gmd.getDefaultTaskType()!=null? gmd.getDefaultTaskType().getName() : "N/A", true)
				{
					@Override
					protected void updateEntities(String newValue)
					{
					}
					
					@Override
					protected void commitEntities()
					{
					}
				};
			}else{
				//the global metadata for this dataset is not available
				return new StringDBViewValue("N/A", true)
				{
					@Override
					protected void updateEntities(String newValue)
					{
					}
					
					@Override
					protected void commitEntities()
					{
					}
				};
			}
		case SIZE:
			return new StringDBViewValue(AppHelper.formatFileSize(dataset.getSize()), true)
			{
				@Override
				protected void updateEntities(String newValue)
				{
				}
				
				@Override
				protected void commitEntities()
				{
				}
			};
		case CREATED:
			return new StringDBViewValue(""+dataset.getCreated(),true)
			{
				@Override
				protected void updateEntities(String newValue)
				{
				}
				
				@Override
				protected void commitEntities()
				{
				}
			};
		case OWNER:
			return new StringDBViewValue(dataset.getOwner().getLogin(),true) 
			{
				@Override
				protected void updateEntities(String newValue)
				{
				}
				
				@Override
				protected void commitEntities()
				{
				}
			};


		default:
			throw new IllegalStateException("Unknown column: " + specificColumn.name());
		}
	}

	@Override
	public void commitRow()
	{
	}
}
