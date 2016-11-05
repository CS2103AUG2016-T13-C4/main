//@@author A0133945B
package seedu.task;

import com.google.common.eventbus.Subscribe;
import com.google.gson.JsonSyntaxException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import seedu.task.commons.core.Config;
import seedu.task.commons.core.EventsCenter;
import seedu.task.commons.core.LogsCenter;
import seedu.task.commons.core.Version;
import seedu.task.commons.events.ui.ExitAppRequestEvent;
import seedu.task.commons.exceptions.DataConversionException;
import seedu.task.commons.util.ConfigUtil;
import seedu.task.commons.util.StringUtil;
import seedu.task.logic.Logic;
import seedu.task.logic.LogicManager;
import seedu.task.model.*;
import seedu.task.storage.SuperbTodoIO;
import seedu.task.ui.Ui;
import seedu.task.ui.UiManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * The main entry point to the application.
 */
public class MainApp extends Application {
    private static final Logger logger = LogsCenter.getLogger(MainApp.class);

    public static final Version VERSION = new Version(1, 0, 0, true);
    
    public static final String DEFAULT_COMMAND_TEXT = "list today";

    protected Ui ui;
    protected Logic logic;
    protected SuperbTodoIO storage;
    protected Model model;
    protected Config config;
    protected UserPrefs userPrefs;

    public MainApp() {}

    
    @Override
    public void init() throws Exception {
        logger.info("=============================[ Initializing SuperbTodo ]===========================");
        super.init();

        config = initConfig(getApplicationParameter("config"));
        storage = new SuperbTodoIO(config.getTaskBookFilePath(),config.getUserPrefsFilePath());

        userPrefs = initPrefs(config);

        initLogging(config);
        
        model = initModelManager(storage, userPrefs);

        logic = new LogicManager(model, storage);
        
        ui = new UiManager(logic, config, userPrefs);

        initEventsCenter();
        
        defaultTaskListing();
    }
    
    //@@author A0135763B
    /**
     * The default task list view shown to the user.
     * 
     * Control the task that the user sees upon initialization
     * 
     */
	private void defaultTaskListing() {
		logic.execute(DEFAULT_COMMAND_TEXT);
	}
	
	//@@author A0133945B
    private String getApplicationParameter(String parameterName){
        Map<String, String> applicationParameters = getParameters().getNamed();
        return applicationParameters.get(parameterName);
    }
    // initialize with empty TaskBook in case of no data, wrong file
    private Model initModelManager(SuperbTodoIO storage, UserPrefs userPrefs) throws JsonSyntaxException, IOException {
        SuperbTodo initialData;
        try {
            initialData = storage.loadTasksFromFile();
        } catch (JsonSyntaxException e) {
          logger.warning("Data file not in the correct format. Will be starting with an empty AddressBook");
          initialData = new SuperbTodo();
        } catch (IOException e) {
          logger.warning("Problem while reading from the file. . Will be starting with an empty AddressBook");
          initialData = new SuperbTodo();
        }

        return new ModelManager(initialData, userPrefs);
    }

    private void initLogging(Config config) {
        LogsCenter.init(config);
    }

    // specify filepaths 
    protected Config initConfig(String configFilePath) {
        Config initializedConfig;
        String configFilePathUsed;

        configFilePathUsed = Config.DEFAULT_CONFIG_FILE;

        if(configFilePath != null) {
            logger.info("Custom Config file specified " + configFilePath);
            configFilePathUsed = configFilePath;
        }

        logger.info("Using config file : " + configFilePathUsed);

        try {
            Optional<Config> configOptional = ConfigUtil.readConfig(configFilePathUsed);
            initializedConfig = configOptional.orElse(new Config());
        } catch (DataConversionException e) {
            logger.warning("Config file at " + configFilePathUsed + " is not in the correct format. " +
                    "Using default config properties");
            initializedConfig = new Config();
        }

        //Update config file in case it was missing to begin with or there are new/unused fields
        try {
            ConfigUtil.saveConfig(initializedConfig, configFilePathUsed);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }
        return initializedConfig;
    }

    protected UserPrefs initPrefs(Config config) {
        assert config != null;
        String prefsFilePath = config.getUserPrefsFilePath();
        logger.info("Using prefs file : " + prefsFilePath);

        UserPrefs initializedPrefs;
        try {
            Optional<UserPrefs> prefsOptional = storage.readUserPrefs();
            initializedPrefs = prefsOptional.orElse(new UserPrefs());
        } catch (DataConversionException e) {
            logger.warning("UserPrefs file at " + prefsFilePath + " is not in the correct format. " +
                    "Using default user prefs");
            initializedPrefs = new UserPrefs();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. . Will be starting with an empty AddressBook");
            initializedPrefs = new UserPrefs();
        }

//        Update prefs file in case it was missing to begin with or there are new/unused fields
        try {
            storage.saveUserPrefs(initializedPrefs);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }

        return initializedPrefs;
    }

    private void initEventsCenter() {
        EventsCenter.getInstance().registerHandler(this);
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting SuperbToDo " + MainApp.VERSION);
        ui.start(primaryStage);
    }

    @Override
    public void stop() {
        logger.info("============================ [ Stopping SuperbTodo ] =============================");
        ui.stop();
        try {
            storage.saveUserPrefs(userPrefs);
        } catch (IOException e) {
            logger.severe("Failed to save preferences " + StringUtil.getDetails(e));
        }
        Platform.exit();
        System.exit(0);
    }

    @Subscribe
    public void handleExitAppRequestEvent(ExitAppRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        this.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

