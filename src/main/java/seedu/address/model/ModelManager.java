package seedu.address.model;

import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.commands.ListCommand;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.Task;
import seedu.address.model.task.UniqueTaskList;
import seedu.address.model.task.UniqueTaskList.TaskNotFoundException;
import seedu.address.commons.events.model.SuperbTodoChangedEvent;
import seedu.address.commons.core.ComponentManager;

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
        indicateSuperbTodoChanged();
    }
    
    @Override
    public synchronized void editTask(ReadOnlyTask target, Task task) throws TaskNotFoundException {
        superbtodo.editTask(target, task);
        //updateFilteredListToShowAll();
        indicateSuperbTodoChanged();
    }

    
    @Override
    public synchronized void doneTask(ReadOnlyTask target) throws TaskNotFoundException {
        superbtodo.doneTask(target);
        //updateFilteredListToShowAll();
        indicateSuperbTodoChanged();
    }
    
    @Override
    public synchronized void undoneTask(ReadOnlyTask target) throws TaskNotFoundException {
        superbtodo.undoneTask(target);
        //updateFilteredListToShowAll();
        indicateSuperbTodoChanged();
    }
    @Override
    public synchronized void addTask(Task task) throws UniqueTaskList.DuplicateTaskException {
        superbtodo.addTask(task);
        //updateFilteredListToShowAll();
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
        filteredTasks.setPredicate(expression::satisfies);
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
            	if (task.getDateTime().value == null && task.getDueTime().value == null) {
            		return true;
            	}
            } else if (type.equals(ListCommand.LIST_TYPE_TIMED)) {
            	if (task.getDateTime().value == null && task.getDueTime().value != null) {
            		return true;
            	}
            } else if (type.equals(ListCommand.LIST_TYPE_EVENT)){
            	if (task.getDateTime().value != null && task.getDueTime().value != null) {
            		return true;
            	}
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
	            if (searchType == ListCommand.MAP_LIST_TYPE_TODAY || searchType == ListCommand.MAP_LIST_TYPE_TOMORROW) {
	            	return startDate.before(compareDate) && endDate.after(compareDate);
	            } else if (searchType == ListCommand.MAP_LIST_TYPE_DATE) {
	            	return startDate.after(compareDate);
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
