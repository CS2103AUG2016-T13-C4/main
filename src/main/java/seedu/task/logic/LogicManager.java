package seedu.task.logic;

import javafx.collections.ObservableList;
import seedu.task.commons.core.ComponentManager;
import seedu.task.commons.core.LogsCenter;
import seedu.task.logic.commands.Command;
import seedu.task.logic.commands.CommandResult;
import seedu.task.logic.parser.Parser;
import seedu.task.model.Model;
import seedu.task.model.task.ReadOnlyTask;
import seedu.task.storage.SuperbTodoIO;
import seedu.task.storage.UndoManagerStorage;

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
   
    public LogicManager(Model model, SuperbTodoIO storage) {
        this.model = model;
        this.parser = new Parser();
        LogicManager.actionRecorder = new UndoManagerStorage(new Vector<UndoManagerStorage>(), new Vector<UndoManagerStorage>());
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
