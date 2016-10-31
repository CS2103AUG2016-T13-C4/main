package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.Task;
import seedu.address.model.task.UniqueTaskList;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .equals comparison)
 */
public class SuperbTodo implements ReadOnlySuperbTodo {

    private final UniqueTaskList task;
    private final UniqueTagList tags;

    {
        task = new UniqueTaskList();
        tags = new UniqueTagList();
    }

    public SuperbTodo() {}

    /**
     * Task and Tags are copied into SuperbToDo
     */
    public SuperbTodo(ReadOnlySuperbTodo toBeCopied) {
        this(toBeCopied.getUniquePersonList(), toBeCopied.getUniqueTagList());
    }

    /**
     * Task and Tags are copied into SuperbToDo
     */
    public SuperbTodo(UniqueTaskList persons, UniqueTagList tags) {
        resetData(persons.getInternalList(), tags.getInternalList());
    }
    
    public SuperbTodo(UniqueTaskList persons) {
        resetData(persons.getInternalList(), tags.getInternalList());
    }

    public static ReadOnlySuperbTodo getEmptySuperbTodo() {
        return new SuperbTodo();
    }

//// list overwrite operations

    public ObservableList<Task> getPersons() {
        return task.getInternalList();
    }

    public void setTasks(List<Task> tasks) {
        this.task.getInternalList().setAll(tasks);
    }

    public void setTags(Collection<Tag> tags) {
        this.tags.internalList.clear();
        this.tags.internalList.addAll(tags);
//        this.tags.getInternalList().setAll(tags);
    }

    public void resetData(Collection<? extends ReadOnlyTask> newPersons, Collection<Tag> newTags) {
        setTasks(newPersons.stream().map(Task::new).collect(Collectors.toList()));
        setTags(newTags);
    }

    public void resetData(ReadOnlySuperbTodo newData) {
        resetData(newData.getPersonList(), newData.getTagList());
    }

    // hard reset by default
    public void resetData() {
    	this.task.getInternalList().clear();
    	this.tags.internalList.clear();
    }
//// person-level operations

    /**
     * Adds a person to the address book.
     * Also checks the new person's tags and updates {@link #tags} with any new tags found,
     * and updates the Tag objects in the person to point to those in {@link #tags}.
     *
     * @throws UniqueTaskList.DuplicateTaskException if an equivalent person already exists.
     */
    public void addTask(Task p) throws UniqueTaskList.DuplicateTaskException {
        syncTagsWithMasterList(p);
        task.add(p);
    }

    /**
     * Ensures that every tag in this person:
     *  - exists in the master list {@link #tags}
     *  - points to a Tag object in the master list
     */
    private void syncTagsWithMasterList(Task person) {
        final UniqueTagList personTags = person.getTags();
        tags.mergeFrom(personTags);

        // Create map with values = tag object references in the master list
        final Map<Tag, Tag> masterTagObjects = new HashMap<>();
        for (Tag tag : tags) {
            masterTagObjects.put(tag, tag);
        }

        // Rebuild the list of person tags using references from the master list
        final Set<Tag> commonTagReferences = new HashSet<>();
        for (Tag tag : personTags) {
            commonTagReferences.add(masterTagObjects.get(tag));
        }
        person.setTags(new UniqueTagList(commonTagReferences));
    }

    public boolean removeTask(ReadOnlyTask key) throws UniqueTaskList.TaskNotFoundException {
        if (task.remove(key)) {
            return true;
        } else {
            throw new UniqueTaskList.TaskNotFoundException();
        }
    }
    
    public boolean editTask(ReadOnlyTask key, Task toReplace) throws UniqueTaskList.TaskNotFoundException {
    	syncTagsWithMasterList(toReplace);
        if (task.edit(key, toReplace)) {
            return true;
        } else {
            throw new UniqueTaskList.TaskNotFoundException();
        }
    }

//// tag-level operations

    public void addTag(Tag t) throws UniqueTagList.DuplicateTagException {
        tags.add(t);
    }

//// util methods

    @Override
    public String toString() {
        return task.getInternalList().size() + " persons, " + tags.getInternalList().size() +  " tags";
        // TODO: refine later
    }

    @Override
    public List<ReadOnlyTask> getPersonList() {
    	return Collections.unmodifiableList(task.getInternalList());
    }
    

    @Override
    public List<Tag> getTagList() {
        return Collections.unmodifiableList(tags.getInternalList());
    }

    @Override
    public UniqueTaskList getUniquePersonList() {
        return this.task;
    }

    @Override
    public UniqueTagList getUniqueTagList() {
        return this.tags;
    }


    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SuperbTodo // instanceof handles nulls
                && this.task.equals(((SuperbTodo) other).task)
                && this.tags.equals(((SuperbTodo) other).tags));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(task, tags);
    }
}
