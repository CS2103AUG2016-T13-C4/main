package seedu.address.ui;

import java.util.Calendar;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import seedu.address.commons.core.Config;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.events.ui.ExitAppRequestEvent;
import seedu.address.commons.util.FxViewUtil;
import seedu.address.logic.Logic;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.ReadOnlyTask;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart {

    private static final String ICON = "/images/address_book_32.png";
    private static final String FXML = "MainWindow.fxml";
    public static final int MIN_HEIGHT = 600;
    public static final int MIN_WIDTH = 800;

    private Logic logic;

    // Independent Ui parts residing in this Ui container
    private TaskListPanel taskListPanel;
    private ResultDisplay resultDisplay;
    private CommandBox commandBox;
    private Config config;
    private UserPrefs userPrefs;
    private TaskScope taskScopeBox;

    // Handles to elements of clock building   
    @FXML
    private static final String[] months = { "Jan", "Feb", "Mar", "Apr",
            "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

    @FXML
    private Calendar c;
    @FXML
    private final Label date_string = new Label();
    @FXML
    private final Label currentHour = new Label();
    @FXML
    private final Label currentMin = new Label();
    @FXML
    private final Label currentSec = new Label();
    @FXML
    private final Label colon1 = new Label(":");
    @FXML
    private final Label colon2 = new Label(":");
    
    // Handles to elements of task scope display
    @FXML
    private Label taskScope; 
    
    CommandBox commandInput;
    
    // Handles to elements of this Ui container 
    private VBox rootLayout;
    private Scene scene;

    private String addressBookName;

    @FXML
    private AnchorPane currentTimePanelPlaceholder;
    
    @FXML
    private AnchorPane taskScopePlaceholder;

    @FXML
    private AnchorPane commandBoxPlaceholder;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private AnchorPane taskListPanelPlaceholder;

    @FXML
    private AnchorPane resultDisplayPlaceholder;


    public MainWindow() {
        super();
    }

    @Override
    public void setNode(Node node) {
        rootLayout = (VBox) node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }

    public static MainWindow load(Stage primaryStage, Config config, UserPrefs prefs, Logic logic) {

        MainWindow mainWindow = UiPartLoader.loadUiPart(primaryStage, new MainWindow());
        mainWindow.configure(config.getAppTitle(), config.getAddressBookName(), config, prefs, logic);
        return mainWindow;
    }

    private void configure(String appTitle, String addressBookName, Config config, UserPrefs prefs,
                           Logic logic) {

        //Set dependencies
        this.logic = logic;
        this.addressBookName = addressBookName;
        this.config = config;
        this.userPrefs = prefs;

        //Configure the UI
        setTitle(appTitle);
        setIcon(ICON);
        setWindowMinSize();
        setWindowDefaultSize(prefs);
        scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        
        
        setAccelerators();
    }
   
    private void setAccelerators() {
        helpMenuItem.setAccelerator(KeyCombination.valueOf("F1"));
    }
    
    /** This method calls relevant methods to build clock and 
     *  display current time to the sec inside time holder pane
     */
    void clockFunction() {        
        //Load the clock function
        currentTimePanelPlaceholder.getChildren().addAll(buildClock());
        displayTime();
        timelineCall();
    }
    
    /**
     * This method builds the structure of the current time and date display
     *
     * @return currentTimePane HBox frame for displayTime()
     */
    private HBox buildClock() {
        HBox clock = new HBox();
        //sets clock position
        clock.setLayoutX(20.0);
        clock.setSnapToPixel(true);
        clock.setSpacing(5.0);
        //formats the labels
        colon1.setTextFill(Color.web("#f7cacf"));
        colon1.setFont(Font.font("Century Gothic", 16.0));
        colon2.setTextFill(Color.web("#f7cacf"));
        colon2.setFont(Font.font("Century Gothic", 16.0));
        date_string.setTextFill(Color.web("#a8dbec"));
        date_string.setFont(Font.font("Century Gothic", 16.0));
        //builds the clock
        clock.getChildren().addAll(date_string,currentHour, colon1, currentMin, colon2,
              currentSec );
        return clock;
    }
    
    /**
     * This method fills the content of the HBox clock using current time
     * it will be called once every second on the timeline
     *
     */
    private void displayTime() {
        c = Calendar.getInstance();

        currentHour.setText("      Now is " + Integer.toString(c.get(Calendar.HOUR_OF_DAY)));
        currentHour.setTextFill(Color.web("#f7cacf"));
        currentHour.setFont(Font.font("Century Gothic", 16.0));

        String minute = "";
        if (c.get(Calendar.MINUTE) < 10) {
            minute = "0" + c.get(Calendar.MINUTE);
        } else {
            minute = Integer.toString(c.get(Calendar.MINUTE));
        }
        currentMin.setText(minute);
        currentMin.setTextFill(Color.web("#f7cacf"));
        currentMin.setFont(Font.font("Century Gothic", 16.0));

        String sec = "";
        if (c.get(Calendar.SECOND) < 10) {
            sec = "0" + c.get(Calendar.SECOND);
        } else {
            sec = Integer.toString(c.get(Calendar.SECOND));
        }
        currentSec.setText(sec);
        currentSec.setTextFill(Color.web("#f7cacf"));
        currentSec.setFont(Font.font("Century Gothic", 16.0));

        date_string.setText("Today is " + c.get(Calendar.DATE) + " "
                + months[c.get(Calendar.MONTH)] + ", " + c.get(Calendar.YEAR));

    }
    
    /**
     * This method creates the timeline call when loading the pane
     *
     */
    
    private void timelineCall() {
        final Timeline time = new Timeline();
        time.setCycleCount(Timeline.INDEFINITE);
        time.getKeyFrames().add(
                new KeyFrame(Duration.millis(1000),
                        new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent evt) {
                        displayTime();
                    }
                }));
        time.play();
    }
    
    /** This method displays task scope as entered by the user
     * 
     */
    
//    void setDefaultTaskScope() {
//        taskScope.setText("Viewing all tasks for now");
//    }
//    
//    @FXML
//    private void taskScopeChanger() {       
//        if (commandInput.getCommandText().equals("list") 
////            || commandInput.getCommandText().equals("list tday")
////            || commandInput.getCommandText().equals("ls today")
////            || commandInput.getCommandText().equals("ls tdy")
//                ) {
//            taskScope.setText("Viewing today's tasks");}
//        } else if (commandInput.getCommandText().equals("list tomorrow") 
//            || commandInput.getCommandText().equals("list tmr")
//            || commandInput.getCommandText().equals("list tmw")
//            || commandInput.getCommandText().equals("ls tomorrow")
//            || commandInput.getCommandText().equals("ls tmr")
//            || commandInput.getCommandText().equals("ls tmw")) {
//            taskScope.setText("Viewing tomorrow's tasks");
//        } else if (commandInput.getCommandText().equals("list this week")
//            || commandInput.getCommandText().equals("ls this week")) {
//            taskScope.setText("Viewing this week's tasks");
//        } else if (commandInput.getCommandText().equals("list next week")
//                || commandInput.getCommandText().equals("ls next week")) {
//                taskScope.setText("Viewing next week's tasks");
//        } else if (commandInput.getCommandText().equals("list next week")
//                || commandInput.getCommandText().equals("ls next week")) {
//                taskScope.setText("Viewing next week's tasks");
//        } else if (commandInput.getCommandText().equals("list undone")
//                || commandInput.getCommandText().equals("ls undone")) {
//                taskScope.setText("Viewing undone tasks");
//        } else if (commandInput.getCommandText().equals("list done")
//                || commandInput.getCommandText().equals("ls done")) {
//                taskScope.setText("Viewing done tasks");
//        } else if (commandInput.getCommandText().equals("list all")
//                || commandInput.getCommandText().equals("ls all")) {
//                taskScope.setText("Viewing all tasks");
//        } 
//    }
    

    /**
     * This method loads various components into SuperbTodo
     */
    void fillInnerParts() {
        taskListPanel = TaskListPanel.load(primaryStage, getTaskListPlaceholder(), logic.getFilteredPersonList());
        resultDisplay = ResultDisplay.load(primaryStage, getResultDisplayPlaceholder());
        commandBox = CommandBox.load(primaryStage, getCommandBoxPlaceholder(), resultDisplay, logic);
    }

    private AnchorPane getCommandBoxPlaceholder() {
        return commandBoxPlaceholder;
    }

    private AnchorPane getResultDisplayPlaceholder() {
        return resultDisplayPlaceholder;
    }

    public AnchorPane getTaskListPlaceholder() {
        return taskListPanelPlaceholder;
    }

    public void hide() {
        primaryStage.hide();
    }

    private void setTitle(String appTitle) {
        primaryStage.setTitle(appTitle);
    }

    /**
     * Sets the default size based on user preferences.
     */
    protected void setWindowDefaultSize(UserPrefs prefs) {
        primaryStage.setHeight(prefs.getGuiSettings().getWindowHeight());
        primaryStage.setWidth(prefs.getGuiSettings().getWindowWidth());
        if (prefs.getGuiSettings().getWindowCoordinates() != null) {
            primaryStage.setX(prefs.getGuiSettings().getWindowCoordinates().getX());
            primaryStage.setY(prefs.getGuiSettings().getWindowCoordinates().getY());
        }
    }

    private void setWindowMinSize() {
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.setMinWidth(MIN_WIDTH);
    }

    /**
     * Returns the current size and the position of the main Window.
     */
    public GuiSettings getCurrentGuiSetting() {
        return new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
    }

    @FXML
    public void handleHelp() {
        HelpWindow helpWindow = HelpWindow.load(primaryStage);
        helpWindow.show();
    }

    public void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        raise(new ExitAppRequestEvent());
    }

    public TaskListPanel getTaskListPanel() {
        return this.taskListPanel;
    }
    

//    void highlightCurrentSelection(ReadOnlyTask person) {
//        browserPanel.loadPersonPage(person);
//    }

}
