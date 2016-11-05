package seedu.task.storage;

import java.io.IOException;
import java.util.Optional;

import seedu.task.commons.core.Config;
import seedu.task.commons.exceptions.DataConversionException;
import seedu.task.commons.util.ConfigUtil;
import seedu.task.storage.SuperbTodoIO;

public class HandlePathChange {
	
    private static Config config;

	//no need logger
	private static Config initConfig(String configFilePath) {
        Config initializedConfig;
        String configFilePathUsed;
        
        configFilePathUsed = Config.DEFAULT_CONFIG_FILE;

        if(configFilePath != null) {
            configFilePathUsed = configFilePath;
        }
        try {
            Optional<Config> configOptional = ConfigUtil.readConfig(configFilePathUsed);
            initializedConfig = configOptional.orElse(new Config());
        } catch (DataConversionException e) {
            initializedConfig = new Config();
        }

        //Update config file in case it was missing to begin with or there are new/unused fields

        
        return initializedConfig;
	}
	
	private static void savaConfig(Config initializedConfig,  String configFilePathUsed) {
        try {
            ConfigUtil.saveConfig(initializedConfig, configFilePathUsed);
        } catch (IOException e) {
        }
	}
	
	public static void handleChange(String newpath) {
        config = initConfig("");
        config.setTaskBookFilePath(newpath);
        savaConfig(config, "config.json");
        SuperbTodoIO.deleteTaskbook();
        SuperbTodoIO.changeTaskbookPath(newpath);
	}

}
