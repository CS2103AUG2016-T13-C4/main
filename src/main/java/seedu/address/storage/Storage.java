package seedu.address.storage;

<<<<<<< HEAD
import seedu.address.commons.events.model.SuperbTodoChangedEvent;
import seedu.address.commons.events.storage.DataSavingExceptionEvent;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlySuperbTodo;
=======
import seedu.address.commons.events.model.AddressBookChangedEvent;
import seedu.address.commons.events.storage.DataSavingExceptionEvent;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyAddressBook;
>>>>>>> parent of 9af9e38... Delete unused files
import seedu.address.model.UserPrefs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

/**
 * API of the Storage component
 */
public interface Storage extends AddressBookStorage, UserPrefsStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException;

    @Override
    void saveUserPrefs(UserPrefs userPrefs) throws IOException;

    @Override
    String getAddressBookFilePath();

    @Override
<<<<<<< HEAD
    Optional<ReadOnlySuperbTodo> readAddressBook() throws DataConversionException, IOException;

    @Override
    void saveAddressBook(ReadOnlySuperbTodo addressBook) throws IOException;
=======
    Optional<ReadOnlyAddressBook> readAddressBook() throws DataConversionException, IOException;

    @Override
    void saveAddressBook(ReadOnlyAddressBook addressBook) throws IOException;
>>>>>>> parent of 9af9e38... Delete unused files

    /**
     * Saves the current version of the Address Book to the hard disk.
     *   Creates the data file if it is missing.
     * Raises {@link DataSavingExceptionEvent} if there was an error during saving.
     */
<<<<<<< HEAD
    void handleAddressBookChangedEvent(SuperbTodoChangedEvent abce);
=======
    void handleAddressBookChangedEvent(AddressBookChangedEvent abce);
>>>>>>> parent of 9af9e38... Delete unused files
}
