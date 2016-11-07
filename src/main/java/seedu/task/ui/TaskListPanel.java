package seedu.task.ui;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seedu.task.commons.core.LogsCenter;
import seedu.task.commons.events.ui.TaskPanelSelectionChangedEvent;
import seedu.task.model.task.ReadOnlyTask;

import java.util.Calendar;
import java.util.logging.Logger;
//@@author A0113992B-reused
/**
 * Panel containing the list of persons.
 */
public class TaskListPanel extends UiPart {
    private final Logger logger = LogsCenter.getLogger(TaskListPanel.class);
    private static final String FXML = "TaskListPanel.fxml";
    private VBox panel;
    private AnchorPane placeHolderPane;

    @FXML
    private ListView<ReadOnlyTask> taskListView;

    public TaskListPanel() {
        super();
    }

    @Override
    public void setNode(Node node) {
        panel = (VBox) node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }

    @Override
    public void setPlaceholder(AnchorPane pane) {
        this.placeHolderPane = pane;
    }

    public static TaskListPanel load(Stage primaryStage, AnchorPane taskListPlaceholder,
                                       ObservableList<ReadOnlyTask> taskList) {
        TaskListPanel taskListPanel =
                UiPartLoader.loadUiPart(primaryStage, taskListPlaceholder, new TaskListPanel());
        taskListPanel.configure(taskList);
        return taskListPanel;
    }

   
    private void configure(ObservableList<ReadOnlyTask> taskList) {
        setConnections(taskList);
        addToPlaceholder();
    }

    private void setConnections(ObservableList<ReadOnlyTask> taskList) {
        taskListView.setItems(taskList);
        taskListView.setCellFactory(listView -> new TaskListViewCell());
        setEventHandlerForSelectionChangeEvent();
    }

    private void addToPlaceholder() {
        SplitPane.setResizableWithParent(placeHolderPane, false);
        placeHolderPane.getChildren().add(panel);
    }

    private void setEventHandlerForSelectionChangeEvent() {
        taskListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                logger.fine("Selection in person list panel changed to : '" + newValue + "'");
                raise(new TaskPanelSelectionChangedEvent(newValue));
            }
        });
    }

    public void scrollTo(int index) {
        Platform.runLater(() -> {
            taskListView.scrollTo(index);
            taskListView.getSelectionModel().clearAndSelect(index);
        });
    }
    
    //@@author A0113992B
    public static final String[] months = { "Jan", "Feb", "Mar", "Apr",
            "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
    
    public static final String[] dateformat  = {"00", "01", "02", "03", "04", "05", "06",
            "07", "08", "09"};
    
    //@@author A0113992B-reused
    class TaskListViewCell extends ListCell<ReadOnlyTask> {

        public TaskListViewCell() {
        }

        @Override
        protected void updateItem(ReadOnlyTask task, boolean empty) {
            super.updateItem(task, empty);
            
            boolean test = true;
            if (empty || task == null) {
                setDisable(true);
                setGraphic(null);
                setText(null);
                
            } else {
                //@@author A0113992B
                setDisable(false);
                setGraphic(TaskCard.load(task, getIndex() + 1).getLayout());

                // sets task color to yellow if it is marked as done
                if (task.isDoneTask() == test) {
                    setStyle("-fx-control-inner-background: #d6c400");
                } 
                else {
                    if ((getIndex() + 1)%2 == 0 ) {
                        setStyle("-fx-control-inner-background: white");
                    } else {
                       setStyle("-fx-control-inner-background: #5e64b1"); 
                    }
                    
                }
                
                
                // sets task color to red if it is overdue
                Calendar c = Calendar.getInstance();      
                String hour = Integer.toString(c.get(Calendar.HOUR_OF_DAY));
                String minute = "";                
                if (c.get(Calendar.MINUTE) < 10) {
                    minute = "0" + c.get(Calendar.MINUTE);
                } else {
                    minute = Integer.toString(c.get(Calendar.MINUTE));
                }

                String year_now = String.format("%1$d", c.get(Calendar.YEAR)) ;
                String date_now = dateformat[c.get(Calendar.DATE)] + " " + months[c.get(Calendar.MONTH)];
                String time_now = hour + ":" + minute + " Hrs";
                
                
                if (task.getDueTime().date_value.compareTo("") >0 && task.getDueTime().time_value.compareTo("") >0) {
                  
                  
                  String task_year = task.getDueTime().date_value.substring(7, 11);
                  String task_date = task.getDueTime().date_value.substring(0, 6);
                  String task_time = task.getDueTime().time_value;
  
                  // compare task param with current time param to determine if task is overdue
                  // if so change task color
                  if (task_year.compareTo(year_now) < 0) {
                      System.out.println("year");
                      setStyle("-fx-control-inner-background: #ee2d07");
                  } else{
                      if (task_date.compareTo(date_now) < 0 ) {                        
                          setStyle("-fx-control-inner-background: #ee2d07");
                      } else {
                          if (task_date.equals(date_now) && task_time.compareTo(time_now) < 0 ) {
                              setStyle("-fx-control-inner-background: #ee2d07");
                          }
                      }
                  } 
              
                }

            }
            
        }
    }

}
