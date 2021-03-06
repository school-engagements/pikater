package org.pikater.shared.database.views.tableview.batches;

import java.util.LinkedHashSet;
import java.util.Set;

import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.views.base.ITableColumn;
import org.pikater.shared.database.views.base.values.AbstractDBViewValue;
import org.pikater.shared.database.views.base.values.NamedActionDBViewValue;
import org.pikater.shared.database.views.base.values.RepresentativeDBViewValue;
import org.pikater.shared.database.views.base.values.RepresentativeReadonlyDBViewValue;
import org.pikater.shared.database.views.base.values.StringReadOnlyDBViewValue;
import org.pikater.shared.database.views.tableview.AbstractTableRowDBView;
import org.pikater.shared.util.DateUtils;

public class BatchTableDBRow extends AbstractTableRowDBView {
	private static final Set<String> allowedTotalPriorities = new LinkedHashSet<String>();
	private static final Set<String> allowedUserPriorities = new LinkedHashSet<String>();
	static {
		for (int i = 0; i < 100; i++) {
			allowedTotalPriorities.add(String.valueOf(i));
		}
		for (int i = 0; i < 10; i++) {
			allowedUserPriorities.add(String.valueOf(i));
		}
	}

	private final JPABatch batch;
	private final boolean adminMode;

	public BatchTableDBRow(JPABatch batch, boolean adminMode) {
		this.batch = batch;
		this.adminMode = adminMode;
	}

	public JPABatch getBatch() {
		return batch;
	}

	@Override
	public AbstractDBViewValue<? extends Object> initValueWrapper(final ITableColumn column) {
		BatchTableDBView.Column specificColumn = (BatchTableDBView.Column) column;
		switch (specificColumn) {
		/*
		 * First the read-only properties.
		 */
		case NAME:
			return new StringReadOnlyDBViewValue(batch.getName());
		case CREATED:
			return new StringReadOnlyDBViewValue(DateUtils.toCzechDate(batch.getCreated()));
		case FINISHED:
			return new StringReadOnlyDBViewValue(DateUtils.toCzechDate(batch.getFinished()));
		case OWNER:
			return new StringReadOnlyDBViewValue(batch.getOwner().getLogin());
		case STATUS:
			return new StringReadOnlyDBViewValue(batch.getStatus().name());
		case NOTE:
			return new StringReadOnlyDBViewValue(batch.getNote());

			/*
			 * Then priority properties. 
			 */
		case TOTAL_PRIORITY:
			if (adminMode) {
				return new RepresentativeDBViewValue(allowedTotalPriorities, String.valueOf(batch.getTotalPriority())) {
					@Override
					public boolean isReadOnly() {
						return !batch.isBeingExecuted();
					}

					@SuppressWarnings("deprecation")
					// we know what we're doing here
					@Override
					protected void updateEntities(String newValue) {
						batch.setTotalPriority(Integer.parseInt(newValue));
					}

					@Override
					protected void commitEntities() {
						commitRow();
					}
				};
			} else {
				return new RepresentativeReadonlyDBViewValue(allowedTotalPriorities, String.valueOf(batch.getTotalPriority()));
			}
		case USER_PRIORITY:
			return new RepresentativeReadonlyDBViewValue(allowedUserPriorities, String.valueOf(batch.getUserAssignedPriority()));

			/*
			 * And then actions. 
			 */
		case ABORT:
			return new NamedActionDBViewValue("Abort") // no DB changes needed - this is completely GUI managed
			{
				@Override
				public boolean isEnabled() {
					return batch.isBeingExecuted();
				}

				@Override
				public void updateEntities() {
				}

				@Override
				protected void commitEntities() {
				}
			};
		case RESULTS:
			return new NamedActionDBViewValue("Download") // no DB changes needed - this is completely GUI managed
			{
				@Override
				public boolean isEnabled() {
					return batch.isFinishedOrFailed();
				}

				@Override
				protected void commitEntities() {
				}

				@Override
				public void updateEntities() {
				}
			};

		default:
			throw new IllegalStateException("Unknown column: " + specificColumn.name());
		}
	}

	@Override
	public void commitRow() {
		DAOs.batchDAO.updateEntity(batch);
	}
}
