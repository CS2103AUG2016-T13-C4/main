package seedu.address.logic;

import javafx.collections.ObservableList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.parser.Parser;
import seedu.address.model.Model;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.storage.SuperbTodoIO;
import seedu.address.storage.UndoManagerStorage;

import java.util.Vector;
import java.util.logging.Logger;

/**
 * The main LogicManager of the app.
 */
public class LogicManager extends ComponentManager implements Logic {
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Parser parser;
    public static UndoManagerStorage actionRecorder;
    private static Vector<UndoManagerStorage> storedTasksUndone = new Vector<UndoManagerStorage>();
    private static Vector<UndoManagerStorage> storedTasksDone = new Vector<UndoManagerStorage>();
    // initialize the storage for undo command
    public static UndoManagerStorage theOne = new UndoManagerStorage(storedTasksUndone,storedTasksDone);
   
    public LogicManager(Model model, SuperbTodoIO storage) {
        this.model = model;
        this.parser = new Parser();
        this.actionRecorder = new UndoManagerStorage(new Vector<UndoManagerStorage>(), new Vector<UndoManagerStorage>());
    }
    
    public static UndoManagerStorage getUndoManager() {
    	return actionRecorder;
    }
    
    @Override
    public CommandResult execute(String commandText) {
        logger.info("----------------[USER COMMAND][" + commandText + "]");
        Command command = parser.parseCommand(commandText);
        command.setData(model);
        return command.execute();
    }

    @Override
    public ObservableList<ReadOnlyTask> getFilteredPersonList() {
        return model.getFilteredTaskList();
    }
    
}
