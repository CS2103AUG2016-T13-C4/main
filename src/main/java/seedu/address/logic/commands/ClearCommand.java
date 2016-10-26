package seedu.address.logic.commands;

import seedu.address.model.SuperbTodo;

/**
 * Clears the address book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "All tasks have been cleared!";

    public ClearCommand() {}


    @Override
    public CommandResult execute() {
        assert model != null;
        model.resetData(SuperbTodo.getEmptyAddressBook());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
