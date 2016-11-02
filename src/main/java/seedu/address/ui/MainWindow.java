package seedu.address.ui;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
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
import seedu.address.model.task.ReadOnlyTask;
//@@author A0113992B-reused
/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart {

    // @@author A0113992B
    private static final String ICON = "/images/SuperbTodo.png";
    // @@author A0113992B-reused
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

    // @@author A0113992B
    // Handles to elements of clock building   
    @FXML
    private static final String[] months = { "Jan", "Feb", "Mar", "Apr",
            "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

    @FXML
    private Calendar c;
    @FXML
    private final Label dateToday = new Label();
    @FXML
    private final Label hourNow = new Label();
    @FXML
    private final Label minNow = new Label();
    @FXML
    private final Label secNow = new Label();
    @FXML
    private final Label colon1 = new Label(":");
    @FXML
    private final Label colon2 = new Label(":");
    
    // Handles to elements of buttons
    @FXML
    Button buttonMin, buttonMax, buttonClose;
    Image imgMin, imgMax, imgClose;
    
    // Handles to elements of task scope display
    @FXML
    private Label taskScope = new Label(); 
    
    @FXML
    Image img = new Image("/images/clock.png");
    ImageView imgV = new ImageView(img);
    
    // @@author A0113992B-reused   
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
    private Button help;
    
    @FXML
    private Button helpImg;

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
        mainWindow.configure(config.getAppTitle(), config.getSuperbTodoName(), config, prefs, logic);
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
        setIcon(primaryStage, ICON);
        setWindowMinSize();
        setWindowDefaultSize(prefs);
        scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        
        setAccelerators();
        elementSetter();
    }

    // @@author A0113992B
    /**
     * This method sets the shortcut key for help button    
     */
    private void setAccelerators() {
            Platform.runLater(() -> {        
                help.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.F1), () -> {
                    help.fire();
                });
            });
   }
    
    /**
     * This sets the list of default tasks displayed
     */
    public void taskScopeSetter() {
        taskScopePlaceholder.getChildren().add(taskScope);
        taskScope.setText("Viewing Today's Todo Tasks");
        taskScope.setTextFill(Color.web("#f194b6"));
        taskScope.setFont(Font.font("Arial Rounded MT Bold", 24.0));
        FxViewUtil.applyAnchorBoundaryParameters(taskScope, 230.0, 230.0, 15.0, 0.0);
        
    }
    
    /**
     * This method sets the correct label content of task scope
     */
    public void setLabel(int index) {
        if (index == 1) { // today
            //taskScope = new Label();
            taskScope.setText("Viewing Today's Todo Tasks");
            FxViewUtil.applyAnchorBoundaryParameters(taskScope, 50.0, 50.0, 0.0, 0.0);
            taskScopePlaceholder.getChildren().add(taskScope);
        } else if (index == 2) { // tomorrow
            //taskScope = new Label();
            taskScope.setText("Viewing Tomorrow's Todo Tasks");
            FxViewUtil.applyAnchorBoundaryParameters(taskScope, 50.0, 50.0, 0.0, 0.0);
            taskScopePlaceholder.getChildren().add(taskScope);
        } else if (index == 3) { // all 
            taskScope = new Label();
            taskScope.setText("Viewing All Todo Tasks");
            FxViewUtil.applyAnchorBoundaryParameters(taskScope, 50.0, 50.0, 0.0, 0.0);
            taskScopePlaceholder.getChildren().add(taskScope);
        }
    }
    

    
    /** This method initialize settings for all the buttons
     * 
     */
    private void elementSetter() {    
        buttonMin.setStyle("-fx-background-image: url('/images/min1.png')");
        buttonMax.setStyle("-fx-background-image: url('/images/max1.png')"); 
        buttonClose.setStyle("-fx-background-image: url('/images/close1.png')");
        helpImg.setStyle("-fx-background-image: url('/images/helpimg.png')");
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
        colon1.setTextFill(Color.web("#83c6de"));
        colon1.setFont(Font.font("Arial Rounded MT Bold", 17.0));
        colon2.setTextFill(Color.web("#83c6de"));
        colon2.setFont(Font.font("Arial Rounded MT Bold", 17.0));
        dateToday.setTextFill(Color.web("#83c6de"));
        dateToday.setFont(Font.font("Arial Rounded MT Bold", 17.0));
        //builds the clock
        clock.getChildren().addAll(imgV, dateToday,hourNow, colon1, minNow, colon2,
              secNow );
        return clock;
    }
    
    /**
     * This method fills the content of the HBox clock using current time
     * it will be called once every second on the timeline
     *
     */
    private void displayTime() {
        c = Calendar.getInstance();

        hourNow.setText("      Now is " + Integer.toString(c.get(Calendar.HOUR_OF_DAY)));
        hourNow.setTextFill(Color.web("#83c6de"));
        hourNow.setFont(Font.font("Arial Rounded MT Bold", 17.0));

        String minute = "";
        if (c.get(Calendar.MINUTE) < 10) {
            minute = "0" + c.get(Calendar.MINUTE);
        } else {
            minute = Integer.toString(c.get(Calendar.MINUTE));
        }
        minNow.setText(minute);
        minNow.setTextFill(Color.web("#83c6de"));
        minNow.setFont(Font.font("Arial Rounded MT Bold", 17.0));

        String sec = "";
        if (c.get(Calendar.SECOND) < 10) {
            sec = "0" + c.get(Calendar.SECOND);
        } else {
            sec = Integer.toString(c.get(Calendar.SECOND));
        }
        secNow.setText(sec);
        secNow.setTextFill(Color.web("#83c6de"));
        secNow.setFont(Font.font("Arial Rounded MT Bold", 17.0));

        dateToday.setText("Today is " + c.get(Calendar.DATE) + " "
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
                    public void handle(ActionEvent e) {
                        displayTime();
                    }
                }));
        time.play();
    }
    


    // @@author 
    

    /**
     * This method loads various components into SuperbTodo
     */
    void fillInnerParts() {
        taskListPanel = TaskListPanel.load(primaryStage, getTaskListPlaceholder(), logic.getFilteredPersonList());
        resultDisplay = ResultDisplay.load(primaryStage, getResultDisplayPlaceholder());
        commandBox = CommandBox.load(primaryStage, getCommandBoxPlaceholder(), resultDisplay, logic);
    }

    // @@author A0113992B  
    private AnchorPane getCommandBoxPlaceholder() {
        return commandBoxPlaceholder;
    }
    // @@author 

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
    

    /**
     * This method sets the action of the minimise button when mouse enters, exits and clicks
     *
     * @param primaryStage      the current stage that is being shown
     */
    @FXML
    private void setActionMinButton() {
        buttonMin.setOnMouseMoved(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent evt) {
                buttonMin.setStyle("-fx-background-image: url('/images/min2.png')");
            }
        });
        buttonMin.setOnMouseEntered(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent evt) {
                buttonMin.setStyle("-fx-background-image: url('/images/min2.png')");
            }
        });
        buttonMin.setOnMouseExited(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent evt) {
                buttonMin.setStyle("-fx-background-image: url('/images/min1.png')");
            }
        });
        buttonMin.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent evt) {
                primaryStage.setIconified(true);
            }
        });
    }

    /**
     * This method sets the action of the maximise button when mouse enters, exits and clicks
     *
     * @param primaryStage      the current stage that is being shown
     */
    @FXML
    private void setActionMaxButton() {
        buttonMax.setOnMouseMoved(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent evt) {
                buttonMax.setStyle("-fx-background-image: url('/images/max2.png')");
            }
        });
        buttonMax.setOnMouseExited(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent evt) {
                buttonMax.setStyle("-fx-background-image: url('/images/max1.png')");
            }
        });
        buttonMax.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent evt) {
                primaryStage.setMaximized(true);
            }
        });
    };
    
    /**
     * This method sets the action of the close button when mouse enters, exits and clicks
     *
     * @param primaryStage      the current stage that is being shown
     */
    @FXML
    private void setActionCloseButton() {
        buttonClose.setOnMouseEntered(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent evt) {
                buttonClose.setStyle("-fx-background-image: url('/images/close2.png')");
            }
        });
        buttonClose.setOnMouseExited(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent evt) {
                buttonClose.setStyle("-fx-background-image: url('/images/close1.png')");
            }
        });
        buttonClose.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent evt) {
                primaryStage.close();
            }
        });
    }

    
   
}
