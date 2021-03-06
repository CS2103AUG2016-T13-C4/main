package seedu.task.model.task;

import seedu.task.commons.exceptions.IllegalValueException;

//@@author A0135763B-reused
/**
 * Represents a Task's name in the address book. Guarantees: immutable; is valid
 * as declared in {@link #isValidName(String)}
 */
public class TaskName {

	public static final String MESSAGE_NAME_CONSTRAINTS = "Task names should be spaces or alphanumeric characters";
	public static final String NAME_VALIDATION_REGEX = "[\\p{Alnum} ]+";

	public final String fullName;

	/**
	 * Validates given name.
	 *
	 * @throws IllegalValueException
	 *             if given name string is invalid.
	 */
	public TaskName(String name) throws IllegalValueException {
		assert name != null;
		name = name.trim();
		if (!isValidName(name)) {
			throw new IllegalValueException(MESSAGE_NAME_CONSTRAINTS);
		}
		this.fullName = name;
	}

	/**
	 * Returns true if a given string is a valid task name.
	 */
	public static boolean isValidName(String test) {
		return test.matches(NAME_VALIDATION_REGEX);
	}

	@Override
	public String toString() {
		return fullName;
	}

	@Override
	public boolean equals(Object other) {
		return other == this // short circuit if same object
				|| (other instanceof TaskName // instanceof handles nulls
						&& this.fullName.equals(((TaskName) other).fullName)); // state
																				// check
	}

	@Override
	public int hashCode() {
		return fullName.hashCode();
	}

}
