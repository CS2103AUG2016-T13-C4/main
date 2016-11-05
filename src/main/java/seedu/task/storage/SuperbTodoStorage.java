//@@author A0133945B

package seedu.task.storage;

import seedu.task.commons.exceptions.DataConversionException;
import seedu.task.model.ReadOnlySuperbTodo;

import java.io.IOException;
import java.util.Optional;

/**
 * Represents a storage for {@link seedu.task.model.SuperbTodo}.
 */
public interface SuperbTodoStorage {

    /**
     * Returns the file path of the data file.
     */
    String getSuperbTodoBookFilePath();

    /**
     * Returns superbtodo data as a {@link ReadOnlySuperbTodo}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlySuperbTodo> readSuperbTodo() throws DataConversionException, IOException;

    /**
     * @see #getSuperbTodoBookFilePath()
     */
    Optional<ReadOnlySuperbTodo> readSuperbTodo(String filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlySuperbTodo} to the storage.
     * @param superbtodo cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveSuperbTodo(ReadOnlySuperbTodo superbtodo) throws IOException;

    /**
     * @see #saveSuperbTodo(ReadOnlySuperbTodo)
     */
    void saveSuperbTodo(ReadOnlySuperbTodo superbtodo, String filePath) throws IOException;

}
