package org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.batches.compare;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.pikater.shared.database.jpa.JPAAttributeMetaData;
import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.views.tableview.AbstractTableRowDBView;
import org.pikater.shared.database.views.tableview.datasets.DataSetTableDBRow;
import org.pikater.shared.util.Tuple;
import org.pikater.web.vaadin.gui.server.components.dbviews.base.TableRowPicker;
import org.pikater.web.vaadin.gui.server.components.popups.MyNotifications;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.DialogCommons.IDialogComponent;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog.IProgressDialogResultHandler;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog.IProgressDialogTaskResult;
import org.pikater.web.vaadin.gui.server.components.wizards.WizardForDialog;
import org.pikater.web.vaadin.gui.server.components.wizards.steps.ParentAwareWizardStep;
import org.pikater.web.vaadin.gui.server.components.wizards.steps.RefreshableWizardStep;
import org.pikater.web.vaadin.gui.server.ui_visualization.VisualizationUI.DSVisTwoUIArgs;
import org.pikater.web.visualisation.DatasetVisualizationEntryPoint;
import org.pikater.web.visualisation.DatasetVisualizationValidation;
import org.pikater.web.visualisation.definition.AttrComparisons;
import org.pikater.web.visualisation.definition.AttrMapping;
import org.pikater.web.visualisation.definition.result.DSVisTwoResult;

import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class ResultCompareWizard extends WizardForDialog<ResultCompareCommons> implements IDialogComponent
{
	private static final long serialVersionUID = -2782484084003504941L;
	
	public ResultCompareWizard(JPADataSetLO originalDataset)
	{
		super(new ResultCompareCommons(originalDataset));
		setRefreshActivatedSteps(true);
		
		addStep(new Step1(this));
		addStep(new Step2(this));
		addStep(new Step3(this));
		
		getFinishButton().addClickListener(new Button.ClickListener()
		{
			private static final long serialVersionUID = -1069956626807311766L;

			@Override
			public void buttonClick(ClickEvent event)
			{
				// determine attribute sets to (potentially) compare
				Set<AttrMapping> attrComps_original = getCompatibleAttributes(
						getOutput().getDatasetOriginal(),
						getOutput().getFormOriginal().getSelectedAttributes(),
						getOutput().getFormOriginal().getSelectedTargetAttribute()
				);
				Set<AttrMapping> attrComps_compared = getCompatibleAttributes(
						getOutput().getDatasetCompareTo(),
						getOutput().getFormCompareTo().getSelectedAttributes(),
						getOutput().getFormCompareTo().getSelectedTargetAttribute()
				);
				
				// determine attribute pairs to compare
				final AttrComparisons attrComparisons = new AttrComparisons();
				for(AttrMapping mapping1 : attrComps_original)
				{
					for(AttrMapping mapping2 : attrComps_compared)
					{
						if(DatasetVisualizationValidation.areCompatible(mapping1, mapping2))
						{
							attrComparisons.add(new Tuple<AttrMapping, AttrMapping>(mapping1, mapping2));
						}
					}
				}
				if(!attrComparisons.isEmpty())
				{
					// show progress dialog
					ProgressDialog.show("Vizualization progress...", new ProgressDialog.IProgressDialogTaskHandler()
					{
						private DatasetVisualizationEntryPoint underlyingTask;

						@Override
						public void startTask(IProgressDialogResultHandler contextForTask) throws Throwable
						{
							// start the task and bind it with the progress dialog
							underlyingTask = new DatasetVisualizationEntryPoint(contextForTask);
							underlyingTask.visualizeDatasetComparison(getOutput().getDatasetOriginal(), getOutput().getDatasetCompareTo(), attrComparisons);
						}

						@Override
						public void abortTask()
						{
							underlyingTask.abort();
						}

						@Override
						public void onTaskFinish(IProgressDialogTaskResult result)
						{
							// and when the task finishes, construct the UI
							DSVisTwoUIArgs uiArgs = new DSVisTwoUIArgs(getOutput().getDatasetOriginal(), getOutput().getDatasetCompareTo(), (DSVisTwoResult) result); 
							Page.getCurrent().setLocation(uiArgs.toRedirectURL());
						}
					});
				}
				else
				{
					MyNotifications.showWarning("Nothing to compare", "No compatible mappings found.");
				}
			}
			
			private Set<AttrMapping> getCompatibleAttributes(JPADataSetLO dataset, JPAAttributeMetaData[] selectedAttrs, JPAAttributeMetaData attrTarget)
			{
				Set<AttrMapping> attrComps = new HashSet<AttrMapping>();
				for(JPAAttributeMetaData attrX : selectedAttrs)
				{
					for(JPAAttributeMetaData attrY : selectedAttrs)
					{
						AttrMapping attributes = new AttrMapping(attrX, attrY, attrTarget);
						if(DatasetVisualizationValidation.isCompatible(attributes))
						{
							attrComps.add(attributes);
						}
					}
				}
				return attrComps;
			}
		});
	}
	
	@Override
	public void addArgs(List<Object> arguments)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isResultReadyToBeHandled()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean handleResult(Object[] args)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	//--------------------------------------------------------------
	// INDIVIDUAL STEPS
	
	private class Step1 extends ParentAwareWizardStep<ResultCompareCommons, ResultCompareWizard>
	{
		private final VerticalLayout vLayout;
		
		public Step1(ResultCompareWizard parentWizard)
		{
			super(parentWizard);
			
			this.vLayout = new VerticalLayout();
			this.vLayout.setSizeFull();
			this.vLayout.setSpacing(true);
			
			Label label = new Label(String.format("First select attributes to be compared for '%s':", 
					getOutput().getDatasetOriginal().getFileName()));
			label.setSizeUndefined();
			label.setStyleName("v-label-undefWidth-wordWrap");
			
			this.vLayout.addComponent(label);
			this.vLayout.addComponent(getOutput().getFormOriginal());
			this.vLayout.setExpandRatio(getOutput().getFormOriginal(), 1);
		}

		@Override
		public String getCaption()
		{
			return "Select attributes...";
		}

		@Override
		public Component getContent()
		{
			return vLayout;
		}

		@Override
		public boolean onAdvance()
		{
			return true;
		}

		@Override
		public boolean onBack()
		{
			return false;
		}
	}
	
	private class Step2 extends ParentAwareWizardStep<ResultCompareCommons, ResultCompareWizard>
	{
		private final TableRowPicker innerLayout;
		
		public Step2(ResultCompareWizard parentWizard)
		{
			super(parentWizard);
			
			this.innerLayout = new TableRowPicker("Select a row and click 'Next':");
			this.innerLayout.setSizeFull();
		}

		@Override
		public String getCaption()
		{
			return "Compare to...";
		}

		@Override
		public Component getContent()
		{
			return innerLayout;
		}

		@Override
		public boolean onAdvance()
		{
			AbstractTableRowDBView[] selectedViews = innerLayout.getTable().getViewsOfSelectedRows();
			if(selectedViews.length > 0)
			{
				// this assumes single select mode
				DataSetTableDBRow selectedView = (DataSetTableDBRow) selectedViews[0];
				getOutput().setCompareToDataset(selectedView.getDataset());
				return true;
			}
			else
			{
				MyNotifications.showError(null, "No table row (dataset) is selected.");
				return false;
			}
		}

		@Override
		public boolean onBack()
		{
			return true;
		}
	}
	
	private class Step3 extends RefreshableWizardStep<ResultCompareCommons, ResultCompareWizard>
	{
		private final VerticalLayout vLayout;
		
		public Step3(ResultCompareWizard parentWizard)
		{
			super(parentWizard);
			
			this.vLayout = new VerticalLayout();
			this.vLayout.setSizeFull();
			this.vLayout.setSpacing(true);
		}

		@Override
		public String getCaption()
		{
			return "Select attributes...";
		}

		@Override
		public Component getContent()
		{
			return vLayout;
		}

		@Override
		public boolean onAdvance()
		{
			return false;
		}

		@Override
		public boolean onBack()
		{
			return true;
		}

		@Override
		public void refresh()
		{
			this.vLayout.removeAllComponents();
			
			Label label = new Label(String.format("And finally, select attributes to be compared for '%s':", 
					getOutput().getDatasetCompareTo().getFileName()));
			label.setSizeUndefined();
			label.setStyleName("v-label-undefWidth-wordWrap");
			
			this.vLayout.addComponent(label);
			this.vLayout.addComponent(getOutput().getFormCompareTo());
			this.vLayout.setExpandRatio(getOutput().getFormCompareTo(), 1);
		}
	}
}