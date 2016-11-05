package seedu.task.model;

import javafx.collections.transformation.FilteredList;
import seedu.task.commons.core.ComponentManager;
import seedu.task.commons.core.LogsCenter;
import seedu.task.commons.core.UnmodifiableObservableList;
import seedu.task.commons.events.model.SuperbTodoChangedEvent;
import seedu.task.commons.util.StringUtil;
import seedu.task.logic.commands.ListCommand;
import seedu.task.model.task.ReadOnlyTask;
import seedu.task.model.task.Task;
import seedu.task.model.task.UniqueTaskList;
import seedu.task.model.task.UniqueTaskList.TaskNotFoundException;

import java.util.Date;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Represents the in-memory model of the address book data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final SuperbTodo superbtodo;
    private final FilteredList<Task> filteredTasks;
    private Expression viewCondition;
    
    /**
     * Initializes a ModelManager with the given Superbtodo
     * Superbtodo and its variables should not be null
     */
    public ModelManager(SuperbTodo src, UserPrefs userPrefs) {
        super();
        assert src != null;
        assert userPrefs != null;

        logger.fine("Initializing with address book: " + src + " and user prefs " + userPrefs);
        
        superbtodo = new SuperbTodo(src);
        filteredTasks = new FilteredList<>(superbtodo.getTasks());       
    }

    public ModelManager() {
        this(new SuperbTodo(), new UserPrefs());
    }

    public ModelManager(ReadOnlySuperbTodo initialData, UserPrefs userPrefs) {
        superbtodo = new SuperbTodo(initialData);
        filteredTasks = new FilteredList<>(superbtodo.getTasks());
    }

    @Override
    public void resetData(ReadOnlySuperbTodo newData) {
    	superbtodo.resetData(newData);
    	superbtodo.resetData();
        indicateSuperbTodoChanged();
    }

    @Override
    public ReadOnlySuperbTodo getSuperbTodo() {
        return superbtodo;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateSuperbTodoChanged() {
        raise(new SuperbTodoChangedEvent(superbtodo));
    }

    @Override
    public synchronized void deleteTask(ReadOnlyTask target) throws TaskNotFoundException {
        superbtodo.removeTask(target);
        refreshFilteredTaskList();
        indicateSuperbTodoChanged();
    }
    
    @Override
    public synchronized void editTask(ReadOnlyTask target, Task task) throws TaskNotFoundException {
        superbtodo.editTask(target, task);
        refreshFilteredTaskList();
        indicateSuperbTodoChanged();
    }

    
    @Override
    public synchronized void doneTask(ReadOnlyTask target) throws TaskNotFoundException {
        superbtodo.doneTask(target);
        refreshFilteredTaskList();
        indicateSuperbTodoChanged();
    }
    
    @Override
    public synchronized void undoneTask(ReadOnlyTask target) throws TaskNotFoundException {
        superbtodo.undoneTask(target);
        refreshFilteredTaskList();
        indicateSuperbTodoChanged();
    }
    
    @Override
    public synchronized void changePath(String newpath) {
        superbtodo.changePath(newpath);
        indicateSuperbTodoChanged();
    }
    @Override
    public synchronized void addTask(Task task, int position) throws UniqueTaskList.DuplicateTaskException {
    	if (position == -1) {
    		superbtodo.addTask(task);
    	} else {
    		superbtodo.addTask(task, position);
    	}
        indicateSuperbTodoChanged();
    }


    //=========== Filtered Task List Accessors ===============================================================

    @Override
    public UnmodifiableObservableList<ReadOnlyTask> getFilteredTaskList() {
        return new UnmodifiableObservableList<>(filteredTasks);
    }

    @Override
    public void updateFilteredListToShowAll() {
        filteredTasks.setPredicate(null);
    }
    
    //@@author A0135763B-reused
    @Override
    public void updateFilteredListToShowByType(String arg) {
    	updateFilteredTaskList(new PredicateExpression(new TypeQualifier(arg)));
    }
    
    @Override
    public void updateFilteredListToShowByTime(Date start, Date end, int type) {
    	updateFilteredTaskList(new PredicateExpression(new TimeQualifier(start, end, type)));
    }

    @Override
    public void updateFilteredTaskList(Set<String> keywords){
        updateFilteredTaskList(new PredicateExpression(new NameQualifier(keywords)));
    }

    private void updateFilteredTaskList(Expression expression) {
    	viewCondition = expression;
        filteredTasks.setPredicate(expression::satisfies);
    }
    
    private void refreshFilteredTaskList() {
    	updateFilteredTaskList(viewCondition);
    }

    //========== Inner classes/interfaces used for filtering ==================================================

    interface Expression {
        boolean satisfies(ReadOnlyTask person);
        String toString();
    }

    private class PredicateExpression implements Expression {

        private final Qualifier qualifier;

        PredicateExpression(Qualifier qualifier) {
            this.qualifier = qualifier;
        }

        @Override
        public boolean satisfies(ReadOnlyTask task) {
            return qualifier.run(task);
        }

        @Override
        public String toString() {
            return qualifier.toString();
        }
    }

    interface Qualifier {
        boolean run(ReadOnlyTask task);
        String toString();
    }

    private class NameQualifier implements Qualifier {
        private Set<String> nameKeyWords;

        NameQualifier(Set<String> nameKeyWords) {
            this.nameKeyWords = nameKeyWords;
        }

        @Override
        public boolean run(ReadOnlyTask task) {
            return nameKeyWords.stream()
                    .filter(keyword -> StringUtil.containsIgnoreCase(task.getName().fullName, keyword))
                    .findAny()
                    .isPresent();
        }

        @Override
        public String toString() {
            return "name=" + String.join(", ", nameKeyWords);
        }
    }

    private class TypeQualifier implements Qualifier {
        private String type;

        TypeQualifier(String arg) {
            this.type = arg.toUpperCase();
        }

        @Override
        public boolean run(ReadOnlyTask task) {
            if (type.equals(ListCommand.LIST_TYPE_UNTIMED)) {
            	return task.getDateTime().value == null && task.getDueTime().value == null;
            } else if (type.equals(ListCommand.LIST_TYPE_TIMED)) {
            	return task.getDateTime().value == null && task.getDueTime().value != null;
            } else if (type.equals(ListCommand.LIST_TYPE_EVENT)){
            	return task.getDateTime().value != null && task.getDueTime().value != null;
            } else if (type.equals(ListCommand.LIST_TYPE_DONE)){
            	return task.isDoneTask();
            } else if (type.equals(ListCommand.LIST_TYPE_UNDONE)){
            	return !task.isDoneTask();
            }
            return false;
        }

        @Override
        public String toString() {
            return "type=" + String.join(", ", this.type);
        }
    }
    
    private class TimeQualifier implements Qualifier {
        private Date startDate, endDate;
        private int searchType;

        TimeQualifier(Date start, Date end, int type) {
            this.startDate = start;
            this.endDate = end;
            this.searchType = type;
        }

        @Override
        public boolean run(ReadOnlyTask task) {
        	Date compareDate = task.getDueTime().value;
        	if (compareDate != null) {
	            if (searchType == ListCommand.MAP_LIST_TYPE_TODAY || 
	            		searchType == ListCommand.MAP_LIST_TYPE_TOMORROW || 
	            		searchType == ListCommand.MAP_LIST_TYPE_DATE) {
	            	return startDate.before(compareDate) && endDate.after(compareDate);
	            } else if (searchType == ListCommand.MAP_LIST_TYPE_OVERDUE) {
	            	return endDate.after(compareDate) && !task.isDoneTask();
	            }
        	}
            return false;
        }

        @Override
        public String toString() {
            return "start time=" + String.join(", ", startDate.toString()) + "end time=" + String.join(", ", endDate.toString());
        }
    }

}
