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
<<<<<<< HEAD
        model.resetData(SuperbTodo.getEmptyAddressBook());
=======
        model.resetData(SuperbTodo.getEmptySuperbTodo());
>>>>>>> 4273f02f1c54ed839f49a1bec0bbf5281b70c2c4
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
