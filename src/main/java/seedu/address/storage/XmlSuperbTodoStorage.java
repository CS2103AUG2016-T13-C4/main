package seedu.address.storage;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.util.FileUtil;
import seedu.address.model.ReadOnlySuperbTodo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * A class to access SuperbTodo data stored as an xml file on the hard disk.
 */
public class XmlSuperbTodoStorage implements SuperbTodoStorage {

    private static final Logger logger = LogsCenter.getLogger(XmlSuperbTodoStorage.class);

    private String filePath;

    public XmlSuperbTodoStorage(String filePath){
        this.filePath = filePath;
    }

    public String getSuperbTodoBookFilePath(){
        return filePath;
    }

    /**
     * Similar to {@link #readSuperbTodo()}
     * @param filePath location of the data. Cannot be null
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlySuperbTodo> readSuperbTodo(String filePath) throws DataConversionException, FileNotFoundException {
        assert filePath != null;

        File superbTodoFile = new File(filePath);

        if (!superbTodoFile.exists()) {
            logger.info("SuperbTodo file "  + superbTodoFile + " not found");
            return Optional.empty();
        }

        ReadOnlySuperbTodo superbTodoOptional = XmlFileStorage.loadDataFromSaveFile(new File(filePath));

        return Optional.of(superbTodoOptional);
    }

    /**
     * Similar to {@link #saveSuperbTodo(ReadOnlySuperbTodo)}
     * @param filePath location of the data. Cannot be null
     */
    public void saveSuperbTodo(ReadOnlySuperbTodo superbTodo, String filePath) throws IOException {
        assert superbTodo != null;
        assert filePath != null;

        File file = new File(filePath);
        FileUtil.createIfMissing(file);
        XmlFileStorage.saveDataToFile(file, new XmlSerializableAddressBook(superbTodo));
    }

    @Override
    public Optional<ReadOnlySuperbTodo> readSuperbTodo() throws DataConversionException, IOException {
        return readSuperbTodo(filePath);
    }

    @Override
    public void saveSuperbTodo(ReadOnlySuperbTodo superbTodo) throws IOException {
        saveSuperbTodo(superbTodo, filePath);
    }
}
