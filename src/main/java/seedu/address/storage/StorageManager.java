package seedu.address.storage;

import com.google.common.eventbus.Subscribe;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.SuperbTodoChangedEvent;
import seedu.address.commons.events.storage.DataSavingExceptionEvent;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlySuperbTodo;
import seedu.address.model.UserPrefs;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Manages storage of AddressBook data in local storage.
 */
public class StorageManager extends ComponentManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private SuperbTodoStorage addressBookStorage;
    private UserPrefsStorage userPrefsStorage;


    public StorageManager(SuperbTodoStorage addressBookStorage, UserPrefsStorage userPrefsStorage) {
        super();
        this.addressBookStorage = addressBookStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    public StorageManager(String addressBookFilePath, String userPrefsFilePath) {
        this(new XmlSuperbTodoStorage(addressBookFilePath), new JsonUserPrefsStorage(userPrefsFilePath));
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(UserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ AddressBook methods ==============================

    @Override
    public String getSuperbTodoBookFilePath() {
        return addressBookStorage.getSuperbTodoBookFilePath();
    }

    @Override
    public Optional<ReadOnlySuperbTodo> readSuperbTodo() throws DataConversionException, IOException {
        return readSuperbTodo(addressBookStorage.getSuperbTodoBookFilePath());
    }

    @Override
    public Optional<ReadOnlySuperbTodo> readSuperbTodo(String filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return addressBookStorage.readSuperbTodo(filePath);
    }

    @Override
    public void saveSuperbTodo(ReadOnlySuperbTodo addressBook) throws IOException {
        saveSuperbTodo(addressBook, addressBookStorage.getSuperbTodoBookFilePath());
    }

    @Override
    public void saveSuperbTodo(ReadOnlySuperbTodo addressBook, String filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        addressBookStorage.saveSuperbTodo(addressBook, filePath);
    }


    @Override
    @Subscribe
    public void handleAddressBookChangedEvent(SuperbTodoChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
        	SuperbTodoIO.saveTasksIntoFile(event.data);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }

}
