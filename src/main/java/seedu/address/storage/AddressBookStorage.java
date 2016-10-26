package seedu.address.storage;

import seedu.address.commons.exceptions.DataConversionException;
<<<<<<< HEAD
import seedu.address.model.ReadOnlySuperbTodo;
=======
import seedu.address.model.ReadOnlyAddressBook;
>>>>>>> parent of 9af9e38... Delete unused files

import java.io.IOException;
import java.util.Optional;

/**
<<<<<<< HEAD
 * Represents a storage for {@link seedu.address.model.SuperbTodo}.
=======
 * Represents a storage for {@link seedu.address.model.AddressBook}.
>>>>>>> parent of 9af9e38... Delete unused files
 */
public interface AddressBookStorage {

    /**
     * Returns the file path of the data file.
     */
    String getAddressBookFilePath();

    /**
<<<<<<< HEAD
     * Returns AddressBook data as a {@link ReadOnlySuperbTodo}.
=======
     * Returns AddressBook data as a {@link ReadOnlyAddressBook}.
>>>>>>> parent of 9af9e38... Delete unused files
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
<<<<<<< HEAD
    Optional<ReadOnlySuperbTodo> readAddressBook() throws DataConversionException, IOException;
=======
    Optional<ReadOnlyAddressBook> readAddressBook() throws DataConversionException, IOException;
>>>>>>> parent of 9af9e38... Delete unused files

    /**
     * @see #getAddressBookFilePath()
     */
<<<<<<< HEAD
    Optional<ReadOnlySuperbTodo> readAddressBook(String filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlySuperbTodo} to the storage.
     * @param addressBook cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveAddressBook(ReadOnlySuperbTodo addressBook) throws IOException;

    /**
     * @see #saveAddressBook(ReadOnlySuperbTodo)
     */
    void saveAddressBook(ReadOnlySuperbTodo addressBook, String filePath) throws IOException;
=======
    Optional<ReadOnlyAddressBook> readAddressBook(String filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlyAddressBook} to the storage.
     * @param addressBook cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveAddressBook(ReadOnlyAddressBook addressBook) throws IOException;

    /**
     * @see #saveAddressBook(ReadOnlyAddressBook)
     */
    void saveAddressBook(ReadOnlyAddressBook addressBook, String filePath) throws IOException;
>>>>>>> parent of 9af9e38... Delete unused files

}
