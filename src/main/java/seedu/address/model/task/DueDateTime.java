package seedu.address.model.task;

import seedu.address.commons.exceptions.IllegalValueException;
import com.joestelmach.natty.*;
import java.util.List;

//@@author A0135763B
/**
 * Represents a Task's due date and time in SuperbTodo.
 * Guarantees: immutable; is valid as declared in {@link #isValidDate(String)}
 */
public class DueDateTime {

    public static final String MESSAGE_DATE_CONSTRAINTS = "Task's DueTime should be a valid number representing date and time";
	public static final String DEFAULT_DAY_END_TIME = "23:59 Hrs";
	
    public final String date_value;
    public final String time_value;

    /**
     * Validates given date time.
     *
     * @throws IllegalValueException if given date string is invalid.
     */
    public DueDateTime(String date) throws IllegalValueException {
    	int chooseDate = 0;
        assert date != null;
        
        date = date.trim();
        Parser parser = new Parser();
    	List<DateGroup> dateParser = parser.parse(date);
        if (!isValidDate(dateParser)) {
            throw new IllegalValueException(MESSAGE_DATE_CONSTRAINTS);
        }
        
        if (dateParser.get(0).getDates().size() > 1) {
        	chooseDate = 1;
        }
        
        this.date_value = formatDate(dateParser.get(0).getDates().get(chooseDate).toString());
        if (date.toLowerCase().equals("today")) {
        	this.time_value = DEFAULT_DAY_END_TIME;
        } else {
        	this.time_value = formatTime(dateParser.get(0).getDates().get(chooseDate).toString());
        }
    }
    
    /**
     * Empty constructor
     */
    public DueDateTime() {
        this.date_value = "";
        this.time_value = "";
    }

    /**
     * function which formats a natty parser date component into SuperbTodo format
     */
	private String formatDate(String dateString) {
		String[] dateComponent = dateString.substring(1, dateString.length()).split(" ");
        return dateComponent[2] + " " + dateComponent[1] + " " + dateComponent[5];
	}
	
	/**
     * function which formats a natty parser time component into SuperbTodo format
     */
	private String formatTime(String dateString) {
		String[] dateComponent = dateString.substring(1, dateString.length() - 1).split(" ");
        return dateComponent[3].substring(0, dateComponent[3].length() - 3) + " Hrs";
	}

    /**
     * Returns true if a given string is a valid date.
     */
    public static boolean isValidDate(List<DateGroup> test) {
    	if (!test.isEmpty()) {
    		return true;
    	} else {
    		return false;
    	}
    }

    @Override
    public String toString() {
    	return date_value + " (" + time_value + ")";
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DueDateTime // instanceof handles nulls
                && this.date_value.equals(((DueDateTime) other).date_value)); // state check
    }

    @Override
    public int hashCode() {
        return date_value.hashCode();
    }

}

