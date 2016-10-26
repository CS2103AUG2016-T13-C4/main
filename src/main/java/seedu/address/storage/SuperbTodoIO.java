/** 
 *---------------------------------------------------------------------------------- STORAGE CLASS ----------------------------------------------------------------------------------
 * 
 *
 * This class handles the storing and retrieving of existing tasks from the local file "Tasks.txt".
 *
 * It has the following operations:
 *
 * -loadTasksFromFile <retrieves list of tasks (JSON format) from local file and converts it into ArrayList<Task> for easy manipulation>
 * -saveTasksIntoFile <converts ArrayList<Task> to (JSON format) String and store it in local file "Tasks.txt">
 *
 *
**/
package seedu.address.storage;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Logger;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.AddressBookChangedEvent;
import seedu.address.commons.events.storage.DataSavingExceptionEvent;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.SuperbToDo;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.UserPrefs;
import seedu.address.model.task.*;

import com.google.common.eventbus.Subscribe;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class SuperbTodoIO extends ComponentManager{
	/**
	 * Use Gson library and BufferedReader to load content in local file into temporary arraylist of task objects
	*/
    private static final Logger logger = LogsCenter.getLogger(SuperbTodoIO.class);
	public static String taskbookFilePath;
    public static UserPrefsStorage userPrefsStorage;

	public SuperbTodoIO(String FilePath, String userPrefPath) {
		this.taskbookFilePath = FilePath;
        this.userPrefsStorage = new JsonUserPrefsStorage(userPrefPath);
	}
	
    public static boolean IsFileExist() {
        Path path = Paths.get(taskbookFilePath);
    	if (Files.notExists(path)) {
    		return false;
    	} else 
    		return true;
    }
	
	public static SuperbToDo loadTasksFromFile() throws IOException, JsonSyntaxException {
		if (!IsFileExist()) {
			return new SuperbToDo();
		}
		Gson gson = new Gson();
		String jsonTasks = "";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(taskbookFilePath));
			String line = null;
			while ((line = reader.readLine()) != null) {
				jsonTasks = line;
			}
			Type type = new TypeToken<ArrayList<Task>>() {
        		}.getType();
        	ArrayList<Task> tempList = gson.fromJson(jsonTasks, type);
        	ObservableList<Task> taskList = FXCollections.observableArrayList(tempList);
        	reader.close();
        	return new SuperbToDo(new UniqueTaskList(taskList));
		} catch(IOException e) {
			return new SuperbToDo();
		}
	}
	
	
	/**
	 * Use Gson library and PrintWriter to save content in temporary arraylist into the local file in JSON format
	*/
	public static void saveTasksIntoFile(ReadOnlyAddressBook addressBook) throws FileNotFoundException, UnsupportedEncodingException {
		Gson gson = new Gson();
		Type type = new TypeToken<ObservableList<Task>>() {
		}.getType();
		UniqueTaskList tempList = addressBook.getUniquePersonList();
		ObservableList<Task> taskList = tempList.getInternalList();
		String jsonTasks = gson.toJson(taskList,type);
		PrintWriter writer = new PrintWriter(taskbookFilePath, "UTF-8");
		writer.println(jsonTasks);
		writer.close();
	}
	
    @Subscribe
    public void handleAddressBookChangedEvent(AddressBookChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
        	SuperbTodoIO.saveTasksIntoFile(event.data);
        } catch (IOException e) {
            raise (new DataSavingExceptionEvent(e));
        }
    }
    //User Preference
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    public void saveUserPrefs(UserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }
}