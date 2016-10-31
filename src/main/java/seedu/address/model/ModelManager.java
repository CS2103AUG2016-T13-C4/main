package seedu.address.model;

import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.util.StringUtil;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.Task;
import seedu.address.model.task.UniqueTaskList;
import seedu.address.model.task.UniqueTaskList.TaskNotFoundException;
import seedu.address.commons.events.model.SuperbTodoChangedEvent;
import seedu.address.commons.core.ComponentManager;

import java.util.Set;
import java.util.logging.Logger;

/**
 * Represents the in-memory model of the address book data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final SuperbTodo superbtodo;
    private final FilteredList<Task> filteredPersons;

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
        filteredPersons = new FilteredList<>(superbtodo.getPersons());
    }

    public ModelManager() {
        this(new SuperbTodo(), new UserPrefs());
    }

    public ModelManager(ReadOnlySuperbTodo initialData, UserPrefs userPrefs) {
        superbtodo = new SuperbTodo(initialData);
        filteredPersons = new FilteredList<>(superbtodo.getPersons());
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
        updateFilteredListToShowAll();
        indicateSuperbTodoChanged();
    }

    @Override
    public synchronized void addTask(Task task) throws UniqueTaskList.DuplicateTaskException {
        superbtodo.addTask(task);
        updateFilteredListToShowAll();
        indicateSuperbTodoChanged();
    }

    //=========== Filtered Person List Accessors ===============================================================

    @Override
    public UnmodifiableObservableList<ReadOnlyTask> getFilteredTaskList() {
        return new UnmodifiableObservableList<>(filteredPersons);
    }

    @Override
    public void updateFilteredListToShowAll() {
        filteredPersons.setPredicate(null);
    }

    @Override
    public void updateFilteredTaskList(Set<String> keywords){
        updateFilteredPersonList(new PredicateExpression(new NameQualifier(keywords)));
    }

    private void updateFilteredPersonList(Expression expression) {
        filteredPersons.setPredicate(expression::satisfies);
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
        public boolean satisfies(ReadOnlyTask person) {
            return qualifier.run(person);
        }

        @Override
        public String toString() {
            return qualifier.toString();
        }
    }

    interface Qualifier {
        boolean run(ReadOnlyTask person);
        String toString();
    }

    private class NameQualifier implements Qualifier {
        private Set<String> nameKeyWords;

        NameQualifier(Set<String> nameKeyWords) {
            this.nameKeyWords = nameKeyWords;
        }

        @Override
        public boolean run(ReadOnlyTask person) {
            return nameKeyWords.stream()
                    .filter(keyword -> StringUtil.containsIgnoreCase(person.getName().fullName, keyword))
                    .findAny()
                    .isPresent();
        }

        @Override
        public String toString() {
            return "name=" + String.join(", ", nameKeyWords);
        }
    }

}
