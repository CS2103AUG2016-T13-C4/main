package guitests.guihandles;

import java.util.Set;

import guitests.GuiRobot;
import javafx.scene.Node;
import javafx.stage.Stage;
import seedu.address.model.task.ReadOnlyTask;

/**
 * Provides a handle to a person card in the person list panel.
 */
public class TaskCardHandle extends GuiHandle {
    private static final String TASK_NAME_FIELD_ID = "#taskname";
    private static final String DATE_TIME_DATE_FIELD_ID = "#dateTimeDate";
    private static final String DATE_TIME_TIME_FIELD_ID = "#dateTimeTime";
    private static final String DUE_DATE_TIME_DATE_FIELD_ID = "#dueDateTimeDate";
    private static final String DUE_DATE_TIME_TIME_FIELD_ID = "#dueDateTimeTime";
    private static final String TAGS_FIELD_ID = "#tags";

    private Node node;

    public TaskCardHandle(GuiRobot guiRobot, Stage primaryStage, Node node){
        super(guiRobot, primaryStage, null);
        this.node = node;
    }

    protected String getTextFromLabel(String fieldId) {
        return getTextFromLabel(fieldId, node);
    }


    public String getTaskName() {
        return getTextFromLabel(TASK_NAME_FIELD_ID);
    }

    public String getDateTimeDate() {
        return getTextFromLabel(DATE_TIME_DATE_FIELD_ID);
    }

    
    public String getDateTimeTime() {
        return getTextFromLabel(DATE_TIME_TIME_FIELD_ID);
    }
    
    public String getDueDateTimeDate() {
        return getTextFromLabel(DUE_DATE_TIME_DATE_FIELD_ID);
    }
    
    public String getDueDateTimeTime() {
        return getTextFromLabel(DUE_DATE_TIME_TIME_FIELD_ID);
    }
    
    public String getTags() {
        return getTextFromLabel(TAGS_FIELD_ID);
    }

    public boolean isSamePerson(ReadOnlyTask task){
        return getTaskName().equals(task.getName().fullName) && getDateTimeDate().equals(task.getDateTime().date_value)
                && getDateTimeTime().equals(task.getDateTime().time_value) && getDueDateTimeDate().equals(task.getDueTime().date_value)
                && getDueDateTimeTime().equals(task.getDueTime().time_value) && getTags().equals(task.getTags().toString());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof TaskCardHandle) {
            TaskCardHandle handle = (TaskCardHandle) obj;
            return getTaskName().equals(handle.getTaskName()) && getDateTimeDate().equals(handle.getDateTimeDate())
                    && getDateTimeTime().equals(handle.getDateTimeTime()) && getDueDateTimeDate().equals(handle.getDueDateTimeDate())
                    && getDueDateTimeTime().equals(handle.getDueDateTimeTime()) && getTags().equals(handle.getTags());
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return getTaskName() + " " + getDateTimeDate() + " " 
               + getDateTimeTime() + " " + getDueDateTimeDate()  + " " 
               + getDueDateTimeTime() + " " + getTags();
    }
}
