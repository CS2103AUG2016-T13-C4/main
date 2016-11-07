//@@author A0133945B-reused

package seedu.task;

import javafx.stage.Screen;
import javafx.stage.Stage;
import seedu.task.MainApp;
import seedu.task.commons.core.Config;
import seedu.task.commons.core.GuiSettings;
import seedu.task.model.ReadOnlySuperbTodo;
import seedu.task.model.SuperbTodo;
import seedu.task.model.UserPrefs;
import seedu.task.storage.SuperbTodoIO;
import seedu.task.testutil.TestUtil;

import java.io.IOException;
import java.util.function.Supplier;

/**
 * This class is meant to override some properties of MainApp so that it will be suited for
 * testing
 */
public class TestApp extends MainApp {

    public static final String SAVE_LOCATION_FOR_TESTING = TestUtil.getFilePathInSandboxFolder("sampleData.txt");
    protected static final String DEFAULT_PREF_FILE_LOCATION_FOR_TESTING = TestUtil.getFilePathInSandboxFolder("pref_testing.json");
    public static final String APP_TITLE = "Test App";
    protected static final String TASK_BOOK_NAME = "Test";
    protected Supplier<SuperbTodo> initialDataSupplier = () -> null;
    protected String saveFileLocation = SAVE_LOCATION_FOR_TESTING;

    public TestApp() {
    }
  //@@author A0133945B

    public TestApp(Supplier<SuperbTodo> initialDataSupplier, String saveFileLocation) {
        super();
        this.initialDataSupplier = initialDataSupplier;
        this.saveFileLocation = saveFileLocation;

        // If some initial local data has been provided, write those to the file
        if (initialDataSupplier.get() != null) {
        	try {
				SuperbTodoIO.saveTasksIntoFile(this.initialDataSupplier.get());
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
    }

    @Override
    protected Config initConfig(String configFilePath) {
        Config config = super.initConfig(configFilePath);
        config.setAppTitle(APP_TITLE);
        config.setTaskBookFilePath(saveFileLocation);
        config.setUserPrefsFilePath(DEFAULT_PREF_FILE_LOCATION_FOR_TESTING);
        config.setTaskBookName(TASK_BOOK_NAME);
        return config;
    }
  //@@author A0133945B-reused

    @Override
    protected UserPrefs initPrefs(Config config) {
        UserPrefs userPrefs = super.initPrefs(config);
        double x = Screen.getPrimary().getVisualBounds().getMinX();
        double y = Screen.getPrimary().getVisualBounds().getMinY();
        userPrefs.updateLastUsedGuiSetting(new GuiSettings(600.0, 600.0, (int) x, (int) y));
        return userPrefs;
    }


    @Override
    public void start(Stage primaryStage) {
        ui.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
