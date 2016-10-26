package seedu.address.storage;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;
<<<<<<< HEAD
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.UniqueTaskList;
import seedu.address.model.ReadOnlySuperbTodo;
=======
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.ReadOnlyTask;
import seedu.address.model.person.UniqueTaskList;
>>>>>>> parent of 9af9e38... Delete unused files

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An Immutable AddressBook that is serializable to XML format
 */
@XmlRootElement(name = "addressbook")
<<<<<<< HEAD
public class XmlSerializableAddressBook implements ReadOnlySuperbTodo {

    @XmlElement
    private List<XmlAdaptedTask> persons;
=======
public class XmlSerializableAddressBook implements ReadOnlyAddressBook {

    @XmlElement
    private List<XmlAdaptedPerson> persons;
>>>>>>> parent of 9af9e38... Delete unused files
    @XmlElement
    private List<Tag> tags;

    {
        persons = new ArrayList<>();
        tags = new ArrayList<>();
    }

    /**
     * Empty constructor required for marshalling
     */
    public XmlSerializableAddressBook() {}

    /**
     * Conversion
     */
<<<<<<< HEAD
    public XmlSerializableAddressBook(ReadOnlySuperbTodo src) {
        persons.addAll(src.getPersonList().stream().map(XmlAdaptedTask::new).collect(Collectors.toList()));
=======
    public XmlSerializableAddressBook(ReadOnlyAddressBook src) {
        persons.addAll(src.getPersonList().stream().map(XmlAdaptedPerson::new).collect(Collectors.toList()));
>>>>>>> parent of 9af9e38... Delete unused files
        tags = src.getTagList();
    }

    @Override
    public UniqueTagList getUniqueTagList() {
        try {
            return new UniqueTagList(tags);
        } catch (UniqueTagList.DuplicateTagException e) {
            //TODO: better error handling
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public UniqueTaskList getUniquePersonList() {
        UniqueTaskList lists = new UniqueTaskList();
<<<<<<< HEAD
        for (XmlAdaptedTask p : persons) {
=======
        for (XmlAdaptedPerson p : persons) {
>>>>>>> parent of 9af9e38... Delete unused files
            try {
                lists.add(p.toModelType());
            } catch (IllegalValueException e) {
                //TODO: better error handling
            }
        }
        return lists;
    }

    @Override
    public List<ReadOnlyTask> getPersonList() {
        return persons.stream().map(p -> {
            try {
                return p.toModelType();
            } catch (IllegalValueException e) {
                e.printStackTrace();
                //TODO: better error handling
                return null;
            }
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<Tag> getTagList() {
        return Collections.unmodifiableList(tags);
    }

}
