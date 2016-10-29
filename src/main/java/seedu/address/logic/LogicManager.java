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
import seedu.address.ui.MainWindow;

import java.util.logging.Logger;

/**
 * The main LogicManager of the app.
 */
public class LogicManager extends ComponentManager implements Logic {
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Parser parser;
    private final UndoManagerStorage undoManager;
    private int taskIndex;
    private String deleteTaskInfo = "", editTaskInfo = "";

    public LogicManager(Model model, SuperbTodoIO storage) {
        this.model = model;
        this.parser = new Parser();
        this.undoManager = new UndoManagerStorage ();
    }
    //undo storage above
    //undo command below
    @Override
    public CommandResult execute(String commandText) {
        logger.info("----------------[USER COMMAND][" + commandText + "]");
        Command command = parser.parseCommand(commandText);
        command.setData(model);
        
        // This calls the method to store every user input 
        taskIndex = parser.getTaskIndex();
        deleteTaskInfo = parser.getTaskToDeleteInfo();
        editTaskInfo = parser.getEditTaskInfo();
        undoManager.recorder(taskIndex, editTaskInfo, deleteTaskInfo, command, commandText);
        
        return command.execute();
    }
    
    /**
     * Returns to be deleted task info for undo and redo purposes
     * @return
     */
    public String getTaskInfo() {
        return deleteTaskInfo;
    }

    @Override
    public ObservableList<ReadOnlyTask> getFilteredPersonList() {
        return model.getFilteredTaskList();
    }
}
