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
import javafx.stage.Stage;
import javafx.util.Duration;
import seedu.address.commons.core.Config;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.events.ui.ExitAppRequestEvent;
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
   // private CurrentTImePanel currentTimePanel;
    private PersonListPanel personListPanel;
    private ResultDisplay resultDisplay;
    private CommandBox commandBox;
    private Config config;
    private UserPrefs userPrefs;

    // Handles to elements of this Ui container   
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
    
    
    private VBox rootLayout;
    private Scene scene;

    private String addressBookName;

    @FXML
    private AnchorPane currentTimePanelPlaceholder;

    @FXML
    private AnchorPane commandBoxPlaceholder;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private AnchorPane personListPanelPlaceholder;

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
    public HBox buildClock() {
        HBox clock = new HBox();
        clock.setLayoutX(20.0);
        clock.setSnapToPixel(true);
        clock.setSpacing(5.0);
        colon1.setTextFill(Color.RED);
        colon2.setTextFill(Color.RED);
        date_string.setTextFill(Color.RED);
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

        currentHour.setText(Integer.toString(c.get(Calendar.HOUR_OF_DAY)));
        currentHour.setTextFill(Color.RED);

        String minute = "";
        if (c.get(Calendar.MINUTE) < 10) {
            minute = "0" + c.get(Calendar.MINUTE);
        } else {
            minute = Integer.toString(c.get(Calendar.MINUTE));
        }
        currentMin.setText(minute);
        currentMin.setTextFill(Color.RED);

        String sec = "";
        if (c.get(Calendar.SECOND) < 10) {
            sec = "0" + c.get(Calendar.SECOND);
        } else {
            sec = Integer.toString(c.get(Calendar.SECOND));
        }
        currentSec.setText(sec);
        currentSec.setTextFill(Color.RED);

        date_string.setText(c.get(Calendar.DATE) + " "
                + months[c.get(Calendar.MONTH)] + ", " + c.get(Calendar.YEAR));

    }
    
    /**
     * This method creates the timeline call when loading the pane
     *
     */
    
    public void timelineCall() {
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

    void fillInnerParts() {
        personListPanel = PersonListPanel.load(primaryStage, getPersonListPlaceholder(), logic.getFilteredPersonList());
        resultDisplay = ResultDisplay.load(primaryStage, getResultDisplayPlaceholder());
        commandBox = CommandBox.load(primaryStage, getCommandBoxPlaceholder(), resultDisplay, logic);
    }

    private AnchorPane getCommandBoxPlaceholder() {
        return commandBoxPlaceholder;
    }

    private AnchorPane getResultDisplayPlaceholder() {
        return resultDisplayPlaceholder;
    }

    public AnchorPane getPersonListPlaceholder() {
        return personListPanelPlaceholder;
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

    public PersonListPanel getPersonListPanel() {
        return this.personListPanel;
    }

}
