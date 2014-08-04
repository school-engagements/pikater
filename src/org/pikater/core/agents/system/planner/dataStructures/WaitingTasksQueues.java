package org.pikater.core.agents.system.planner.dataStructures;

import org.pikater.core.ontology.subtrees.batchDescription.durarion.IExpectedDuration;
import org.pikater.core.ontology.subtrees.batchDescription.durarion.LongTermDuration;
import org.pikater.core.ontology.subtrees.batchDescription.durarion.ShortTimeDuration;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class WaitingTasksQueues {
	
	private Queue<TaskToSolve> shortTimeDurationQueue;
	private Queue<TaskToSolve> longTermDurationQueue;
	
	public WaitingTasksQueues() {
		
		Comparator<TaskToSolve> comparator = new PlannerComparator();
		
		shortTimeDurationQueue = new PriorityQueue<TaskToSolve>(10, comparator);
		longTermDurationQueue = new PriorityQueue<TaskToSolve>(10, comparator);
	}
	
	Queue<TaskToSolve> queue = new PriorityQueue<TaskToSolve>();
	
	public void addTask(TaskToSolve taskToSolve) {
		
		IExpectedDuration expectedDuration =
				taskToSolve.getTask().getExpectedDuration();
		if (expectedDuration instanceof ShortTimeDuration) {
		
			this.shortTimeDurationQueue.add(taskToSolve);
		} else if (expectedDuration instanceof LongTermDuration) {
			
			this.longTermDurationQueue.add(taskToSolve);
		} else {
			throw new IllegalArgumentException("Illegal field expectedDuration");
		}
	}
	
	public TaskToSolve removeTaskWithHighestPriority() {
		
		if (this.shortTimeDurationQueue.size() != 0) {
			return this.shortTimeDurationQueue.remove();
			
		} else if (this.longTermDurationQueue.size() != 0) {
			return this.longTermDurationQueue.remove();
		}
		
		return null;
	}

}