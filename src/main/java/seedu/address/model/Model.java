package seedu.address.model;

import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.Task;
import seedu.address.model.task.UniqueTaskList;

import java.util.Date;
import java.util.Set;

/**
 * The API of the Model component.
 */
public interface Model {
    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlySuperbTodo newData);

    /** Returns the AddressBook */
    ReadOnlySuperbTodo getSuperbTodo();


    /** Deletes the given task. */
    void deleteTask(ReadOnlyTask target) throws UniqueTaskList.TaskNotFoundException;
    
    /** Edit/Replace the given task. */
    void editTask(ReadOnlyTask target, Task task) throws UniqueTaskList.TaskNotFoundException;
    
    /** Mark done the given task. */
    void doneTask(ReadOnlyTask target) throws UniqueTaskList.TaskNotFoundException;
    
    /** Mark undone the given task. */
    void undoneTask(ReadOnlyTask target) throws UniqueTaskList.TaskNotFoundException;
    
    /** Adds the given task */
    void addTask(Task task, int position) throws UniqueTaskList.DuplicateTaskException;

    /** Returns the filtered task list as an {@code UnmodifiableObservableList<ReadOnlyTask>} */
    UnmodifiableObservableList<ReadOnlyTask> getFilteredTaskList();

    /** Updates the filter of the filtered task list to show all persons */
    void updateFilteredListToShowAll();

    /** Updates the filter of the filtered task list to filter by the given keywords*/
    void updateFilteredTaskList(Set<String> keywords);
    
    /** Updates the filter of the filtered task list to filter by the type*/
	void updateFilteredListToShowByType(String arg);

	/** Updates the filter of the filtered task list to filter by the period*/
	void updateFilteredListToShowByTime(Date start, Date end, int type);

}
