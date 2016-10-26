package seedu.address.storage;


import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.util.FileUtil;
import seedu.address.model.SuperbTodo;
<<<<<<< HEAD
import seedu.address.model.task.Task;
import seedu.address.model.ReadOnlySuperbTodo;
=======
import seedu.address.model.ReadOnlySuperbTodo;
import seedu.address.model.person.Task;
>>>>>>> 4273f02f1c54ed839f49a1bec0bbf5281b70c2c4
import seedu.address.testutil.TypicalTestTasks;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class XmlAddressBookStorageTest {
    private static String TEST_DATA_FOLDER = FileUtil.getPath("./src/test/data/XmlAddressBookStorageTest/");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void readAddressBook_nullFilePath_assertionFailure() throws Exception {
        thrown.expect(AssertionError.class);
        readAddressBook(null);
    }

    private java.util.Optional<ReadOnlySuperbTodo> readAddressBook(String filePath) throws Exception {
<<<<<<< HEAD
        return new XmlSuperbTodoStorage(filePath).readAddressBook(addToTestDataPathIfNotNull(filePath));
=======
        return new XmlSuperbTodoStorage(filePath).readSuperbTodo(addToTestDataPathIfNotNull(filePath));
>>>>>>> 4273f02f1c54ed839f49a1bec0bbf5281b70c2c4
    }

    private String addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER + prefsFileInTestDataFolder
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readAddressBook("NonExistentFile.xml").isPresent());
    }

    @Test
    public void read_notXmlFormat_exceptionThrown() throws Exception {

        thrown.expect(DataConversionException.class);
        readAddressBook("NotXmlFormatAddressBook.xml");

        /* IMPORTANT: Any code below an exception-throwing line (like the one above) will be ignored.
         * That means you should not have more than one exception test in one method
         */
    }

    @Test
    public void readAndSaveAddressBook_allInOrder_success() throws Exception {
        String filePath = testFolder.getRoot().getPath() + "TempAddressBook.xml";
        TypicalTestTasks td = new TypicalTestTasks();
        SuperbTodo original = td.getTypicalSuperbToDo();
<<<<<<< HEAD
        XmlSuperbTodoStorage xmlSuperbTodoStorage = new XmlSuperbTodoStorage(filePath);

        //Save in new file and read back
        xmlSuperbTodoStorage.saveAddressBook(original, filePath);
        ReadOnlySuperbTodo readBack = xmlSuperbTodoStorage.readAddressBook(filePath).get();
=======
        XmlSuperbTodoStorage xmlAddressBookStorage = new XmlSuperbTodoStorage(filePath);

        //Save in new file and read back
        xmlAddressBookStorage.saveSuperbTodo(original, filePath);
        ReadOnlySuperbTodo readBack = xmlAddressBookStorage.readSuperbTodo(filePath).get();
>>>>>>> 4273f02f1c54ed839f49a1bec0bbf5281b70c2c4
        assertEquals(original, new SuperbTodo(readBack));
/*
        //Modify data, overwrite exiting file, and read back
        original.addPerson(new Task(TypicalTestTasks.hoon));
        original.removePerson(new Task(TypicalTestTasks.alice));
        xmlAddressBookStorage.saveAddressBook(original, filePath);
        readBack = xmlAddressBookStorage.readAddressBook(filePath).get();
        assertEquals(original, new AddressBook(readBack));

        //Save and read without specifying file path
        original.addPerson(new Task(TypicalTestTasks.ida));
        xmlAddressBookStorage.saveAddressBook(original); //file path not specified
        readBack = xmlAddressBookStorage.readAddressBook().get(); //file path not specified
        assertEquals(original, new AddressBook(readBack));
*/
    }

    @Test
    public void saveAddressBook_nullAddressBook_assertionFailure() throws IOException {
        thrown.expect(AssertionError.class);
        saveAddressBook(null, "SomeFile.xml");
    }

    private void saveAddressBook(ReadOnlySuperbTodo addressBook, String filePath) throws IOException {
<<<<<<< HEAD
        new XmlSuperbTodoStorage(filePath).saveAddressBook(addressBook, addToTestDataPathIfNotNull(filePath));
=======
        new XmlSuperbTodoStorage(filePath).saveSuperbTodo(addressBook, addToTestDataPathIfNotNull(filePath));
>>>>>>> 4273f02f1c54ed839f49a1bec0bbf5281b70c2c4
    }

    @Test
    public void saveAddressBook_nullFilePath_assertionFailure() throws IOException {
        thrown.expect(AssertionError.class);
        saveAddressBook(new SuperbTodo(), null);
    }


}
