package seedu.task.logic.commands;

import seedu.task.model.SuperbTodo;

//@@author A0135763B-reused
/**
 * Clears SuperbTodo.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "All tasks have been cleared!";

    public ClearCommand() {}


    @Override
    public CommandResult execute() {
        assert model != null;
        model.resetData(SuperbTodo.getEmptySuperbTodo());
        return new CommandResult(MESSAGE_SUCCESS);
    }


	@Override
	public CommandResult execute(String feedbackSuccess, String feedbackUnsucess) {
		return execute();
	}
}
