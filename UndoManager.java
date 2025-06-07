package project;

import java.util.Stack;

/**
 * Manages undo operations by keeping track of system states.
 */
public class UndoManager {
	private Scheduler scheduler;
	private Stack<SchedulerMemento> history;

	/**
	 * Constructs a new UndoManager for a scheduler.
	 * 
	 * @param scheduler the scheduler to manage undo for
	 */
	public UndoManager(Scheduler scheduler) {
		this.scheduler = scheduler;
		this.history = new Stack<>();
	}

	/**
	 * Saves the current state of the scheduler.
	 */
	public void saveState() {
		history.push(scheduler.createMemento());
	}

	/**
	 * Restores the previous state of the scheduler.
	 * 
	 * @return true if undo was successful, false if no history
	 */
	public boolean undo() {
		if (history.isEmpty()) {
			return false;
		}

		SchedulerMemento memento = history.pop();
		scheduler.restoreFromMemento(memento);
		return true;
	}
}