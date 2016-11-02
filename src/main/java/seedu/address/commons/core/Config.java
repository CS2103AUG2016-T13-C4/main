package seedu.address.commons.core;

import java.util.Objects;
import java.util.logging.Level;

/**
 * Config values used by the app
 */
public class Config {

    public static final String DEFAULT_CONFIG_FILE = "config.json";

    // Config values customizable through config file
    private String appTitle = "SuperbTodo";
    private Level logLevel = Level.INFO;
    private String userPrefsFilePath = "preferences.json";
    private String superbTodoFilePath = "data/addressbook.xml";
    private String superbTodoName = "MySuperTodoList";


    public Config() {
    }

    public String getAppTitle() {
        return appTitle;
    }

    public void setAppTitle(String appTitle) {
        this.appTitle = appTitle;
    }

    public Level getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(Level logLevel) {
        this.logLevel = logLevel;
    }

    public String getUserPrefsFilePath() {
        return userPrefsFilePath;
    }

    public void setUserPrefsFilePath(String userPrefsFilePath) {
        this.userPrefsFilePath = userPrefsFilePath;
    }

    public String getAddressBookFilePath() {
        return superbTodoFilePath;
    }

    public void setAddressBookFilePath(String addressBookFilePath) {
        this.superbTodoFilePath = addressBookFilePath;
    }

    public String getSuperbTodoName() {
        return superbTodoName;
    }

    public void setAddressBookName(String addressBookName) {
        this.superbTodoName = addressBookName;
    }


    @Override
    public boolean equals(Object other) {
        if (other == this){
            return true;
        }
        if (!(other instanceof Config)){ //this handles null as well.
            return false;
        }

        Config o = (Config)other;

        return Objects.equals(appTitle, o.appTitle)
                && Objects.equals(logLevel, o.logLevel)
                && Objects.equals(userPrefsFilePath, o.userPrefsFilePath)
                && Objects.equals(superbTodoFilePath, o.superbTodoFilePath)
                && Objects.equals(superbTodoName, o.superbTodoName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appTitle, logLevel, userPrefsFilePath, superbTodoFilePath, superbTodoName);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("App title : " + appTitle);
        sb.append("\nCurrent log level : " + logLevel);
        sb.append("\nPreference file Location : " + userPrefsFilePath);
        sb.append("\nLocal data file location : " + superbTodoFilePath);
        sb.append("\nAddressBook name : " + superbTodoName);
        return sb.toString();
    }

}
