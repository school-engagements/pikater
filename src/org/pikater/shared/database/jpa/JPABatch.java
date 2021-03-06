package org.pikater.shared.database.jpa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.pikater.shared.database.jpa.status.JPABatchStatus;

/**
 * Class {@link JPABatch} represents record about a computation batch.
 * <p>
 * Each of this class contains a list of experiments that form the actual batch. 
 */
@Entity
@Table(name = "Batch", indexes = { @Index(columnList = "owner_id"), @Index(columnList = "name"), @Index(columnList = "note"), @Index(columnList = "created"), @Index(columnList = "finished"),
		@Index(columnList = "status"), @Index(columnList = "totalPriority"), @Index(columnList = "owner_id,status") })
@NamedQueries({
		@NamedQuery(name = "Batch.getAll", query = "select b from JPABatch b"),
		@NamedQuery(name = "Batch.getAll.count", query = "select count(b) from JPABatch b"),
		@NamedQuery(name = "Batch.getByStatus", query = "select b from JPABatch b where b.status=:status"),
		@NamedQuery(name = "Batch.getByOwner", query = "select b from JPABatch b where b.owner=:owner"),
		@NamedQuery(name = "Batch.getByOwner.count", query = "select count(b) from JPABatch b where b.owner=:owner"),
		@NamedQuery(name = "Batch.getByIDwithResult", query = "select b,e,r from JPABatch b, JPAExperiment e, JPAResult r where b.id=:batchID and e member of b.experiments and r member of e.results"),
		@NamedQuery(name = "Batch.getByIDonlyResults", query = "select r from JPABatch b, JPAExperiment e, JPAResult r where b.id=:batchID and e member of b.experiments and r member of e.results"),
		@NamedQuery(name = "Batch.getByIDonlyResults.count", query = "select count(r) from JPABatch b, JPAExperiment e, JPAResult r where b.id=:batchID and e member of b.experiments and r member of e.results") })
public class JPABatch extends JPAAbstractEntity {

	private String name;
	private String note;
	@Lob
	private String XML;
	@ManyToOne
	private JPAUser owner;
	private int userAssignedPriority;
	private int totalPriority;
	private boolean sendEmailAfterFinish;
	@Enumerated(EnumType.STRING)
	private JPABatchStatus status;

	@OneToMany(cascade = CascadeType.PERSIST)
	private List<JPAExperiment> experiments = new ArrayList<JPAExperiment>();

	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	@Temporal(TemporalType.TIMESTAMP)
	private Date started;
	@Temporal(TemporalType.TIMESTAMP)
	private Date finished;

	/**
	 * Constructor for JPA compatibility
	 */
	public JPABatch() {
	}

	/**
	 * Creates an experiment to be saved (not queued).
	 */
	public JPABatch(String name, String note, String xml, JPAUser owner) {
		this.name = name;
		this.note = note;
		this.XML = xml;
		this.owner = owner;
		this.userAssignedPriority = 0;
		this.totalPriority = 0;
		this.created = new Date();
		this.status = JPABatchStatus.CREATED;
	}

	/**
	 * Creates an experiment to be queued.
	 */
	public JPABatch(String name, String note, String xml, JPAUser owner, int userAssignedPriority, boolean sendEmailAfterFinish) {
		this.name = name;
		this.note = note;
		this.XML = xml;
		this.owner = owner;
		this.sendEmailAfterFinish = sendEmailAfterFinish;
		this.userAssignedPriority = userAssignedPriority; // this needs to presede updating total priority:
		updateTotalPriority();
		this.created = new Date();
		this.status = JPABatchStatus.WAITING;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getXML() {
		return XML;
	}

	public void setXML(String xML) {
		XML = xML;
	}

	public JPAUser getOwner() {
		return owner;
	}

	public void setOwner(JPAUser owner) {
		this.owner = owner;
	}

	public void setUserAssignedPriority(int priority) {
		/*
		 * Currently it is only set once when an experiment is queued and after that it is readonly
		 * so we don't need to update total priority.
		 */

		if ((priority >= 0) && priority < 10) {
			this.userAssignedPriority = priority;
		} else {
			throw new IllegalArgumentException("Only values from 0 to 9 are allowed. Received: " + priority);
		}
	}

	public int getUserAssignedPriority() {
		return this.userAssignedPriority;
	}

	/**
	 * Use this method with caution as it may break synchronization between user priority and 
	 * user assigned priority.</br>
	 * <ul>
	 * <li> To override priority by an administrator, use this method.
	 * <li> To initialize total priority of a new batch, use {@link #updateTotalPriority()}.
	 * </ul>
	 */
	@Deprecated
	public void setTotalPriority(int totalPriority) {
		this.totalPriority = totalPriority;
	}

	/**
	 * Only THIS value is used in core system as priority for the whole batch.
	 */
	public int getTotalPriority() {
		return this.totalPriority;
	}

	public void updateTotalPriority() {
		setTotalPriority(owner.getPriorityMax() * 10 + userAssignedPriority);
	}

	public boolean isSendEmailAfterFinish() {
		return sendEmailAfterFinish;
	}

	public void setSendEmailAfterFinish(boolean sendEmailAfterFinish) {
		this.sendEmailAfterFinish = sendEmailAfterFinish;
	}

	public List<JPAExperiment> getExperiments() {
		return experiments;
	}

	public void setExperiments(List<JPAExperiment> experiments) {
		this.experiments = experiments;
	}

	public void addExperiment(JPAExperiment experiment) {
		this.experiments.add(experiment);
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getStarted() {
		return started;
	}

	public void setStarted(Date started) {
		this.started = started;
	}

	public Date getFinished() {
		return finished;
	}

	public void setFinished(Date finished) {
		this.finished = finished;
	}

	public JPABatchStatus getStatus() {
		return status;
	}

	public void setStatus(JPABatchStatus status) {
		this.status = status;
	}

	/**
	 * Sets the status of the batch to the desired value
	 * @param status String representation of JPABatchStatus object
	 */
	public void setStatus(String status) {
		this.setStatus(JPABatchStatus.valueOf(status));
	}

	public boolean isDesignatedForExecution() {
		return getStatus() != JPABatchStatus.CREATED;
	}

	public boolean isBeingExecuted() {
		return (getStatus().ordinal() >= JPABatchStatus.STARTED.ordinal()) && (getStatus().ordinal() <= JPABatchStatus.COMPUTING.ordinal());
	}

	public boolean isFinishedOrFailed() {
		return (getStatus() == JPABatchStatus.FINISHED) || (getStatus() == JPABatchStatus.FAILED);
	}

	@Transient
	public static final String EntityName = "Batch";

	@Override
	public void updateValues(JPAAbstractEntity newValues) throws Exception {
		JPABatch updateValues = (JPABatch) newValues;
		this.created = updateValues.getCreated();
		this.experiments = updateValues.getExperiments();
		this.started = updateValues.getStarted();
		this.finished = updateValues.getFinished();
		this.name = updateValues.getName();
		this.note = updateValues.getNote();
		this.owner = updateValues.getOwner();
		this.userAssignedPriority = updateValues.getUserAssignedPriority();
		this.status = updateValues.getStatus();
		this.totalPriority = updateValues.getTotalPriority();
		this.XML = updateValues.getXML();
	}
}
