package seedu.task.model.task;

import com.joestelmach.natty.*;

import seedu.task.commons.exceptions.IllegalValueException;

import java.util.Date;
import java.util.List;

//@@author A0135763B
/**
 * Represents a Task's due date and time in SuperbTodo. Guarantees: immutable;
 * is valid as declared in {@link #isValidDate(String)}
 */
public class DueDateTime {

	public static final String MESSAGE_DATE_CONSTRAINTS = "Task's DueTime should be a valid number representing date and time";
	public static final String DEFAULT_TIME_SUFFIX = " Hrs";
	public static final String DEFAULT_DAY_END_TIME = "23:59" + DEFAULT_TIME_SUFFIX;
	public static final int DEFAULT_PARSER_GET_INDEX = 0;

	public final Date value;
	public final String date_value;
	public final String time_value;

	/**
	 * Validates given date time.
	 *
	 * @throws IllegalValueException
	 *             if given date string is invalid.
	 */
	public DueDateTime(String date) throws IllegalValueException {
		int chooseDateIdx = 0;
		assert date != null;

		date = date.trim();
		Parser parser = new Parser();
		List<DateGroup> dateParser = parser.parse(date);
		if (!isValidDate(dateParser)) {
			throw new IllegalValueException(MESSAGE_DATE_CONSTRAINTS);
		}

		if (dateParser.get(DEFAULT_PARSER_GET_INDEX).getDates().size() > 1) {
			chooseDateIdx = 1;
		}

		this.value = dateParser.get(DEFAULT_PARSER_GET_INDEX).getDates().get(chooseDateIdx);
		this.date_value = getDate();
		if (date.toLowerCase().equals("today")) {
			this.time_value = DEFAULT_DAY_END_TIME;
		} else {
			this.time_value = getTime();
		}
	}

	/**
	 * Empty constructor
	 */
	public DueDateTime() {
		this.value = null;
		this.date_value = "";
		this.time_value = "";
	}

	/**
	 * function which formats a natty parser date component into SuperbTodo
	 * format
	 */
	private String getDate() {
		String[] dateComponent = this.value.toString().split(" ");
		return dateComponent[2] + " " + dateComponent[1] + " " + dateComponent[5];
	}

	/**
	 * function which formats a natty parser time component into SuperbTodo
	 * format
	 */
	private String getTime() {
		String[] dateComponent = this.value.toString().split(" ");
		return dateComponent[3].substring(0, dateComponent[3].length() - 3) + DEFAULT_TIME_SUFFIX;
	}

	/**
	 * Returns true if a given string is a valid date.
	 */
	public static boolean isValidDate(List<DateGroup> test) {
		return !test.isEmpty();
	}

	@Override
	public String toString() {
		return date_value + " (" + time_value + ")";
	}

	@Override
	public boolean equals(Object other) {
		return other == this // short circuit if same object
				|| (other instanceof DueDateTime // instanceof handles nulls
						&& this.value.equals(((DueDateTime) other).value)); // state
																			// check
	}

	@Override
	public int hashCode() {
		return this.value.hashCode();
	}

}
