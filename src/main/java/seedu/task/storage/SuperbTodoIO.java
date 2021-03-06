//@@author A0133945B
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
package seedu.task.storage;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Logger;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import seedu.task.commons.core.ComponentManager;
import seedu.task.commons.core.LogsCenter;
import seedu.task.commons.events.model.SuperbTodoChangedEvent;
import seedu.task.commons.events.storage.DataSavingExceptionEvent;
import seedu.task.commons.exceptions.DataConversionException;
import seedu.task.commons.util.FileUtil;
import seedu.task.model.SuperbTodo;
import seedu.task.model.UserPrefs;
import seedu.task.model.task.*;

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
	
	public static SuperbTodo loadTasksFromFile() throws IOException, JsonSyntaxException {
		if (!IsFileExist()) {
			return new SuperbTodo();
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
        	return new SuperbTodo(new UniqueTaskList(taskList));
		} catch(IOException e) {
			return new SuperbTodo();
		}
	}
	
	
	/**
	 * Use Gson library and PrintWriter to save content in temporary arraylist into the local file in JSON format
	 * @throws IOException 
	*/
	public static void saveTasksIntoFile(SuperbTodo taskBook) throws IOException {
		Gson gson = new Gson();
		Type type = new TypeToken<ObservableList<Task>>() {
		}.getType();
		UniqueTaskList tempList = taskBook.getUniquePersonList();
		ObservableList<Task> taskList = tempList.getInternalList();
		String jsonTasks = gson.toJson(taskList,type);
        File file = new File(taskbookFilePath);
        FileUtil.createIfMissing(file);
		PrintWriter writer = new PrintWriter(taskbookFilePath, "UTF-8");
		writer.println(jsonTasks);
		writer.close();
	}
	

	
	public static void deleteTaskbook() {
        Path currentPath = Paths.get(taskbookFilePath);
        String s = currentPath.toAbsolutePath().toString();
        int endIndex = s.lastIndexOf( '/' );
        if (endIndex == -1) {
        	endIndex = s.lastIndexOf('\\');
        }
        String temp = 	s.substring(0, endIndex);	
        String deleteCmd = "rm -r " + temp;
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec(deleteCmd);
        } catch (IOException e) { }
	}
	
    @Subscribe
    public void handleTaskBookChangedEvent(SuperbTodoChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
        	SuperbTodoIO.saveTasksIntoFile((SuperbTodo) event.data);
        } catch (IOException e) {
            raise (new DataSavingExceptionEvent(e));
        }
    }
    
    public String getTaskbookFilePath() {
    	return this.taskbookFilePath;
    }
    
    public static void changeTaskbookPath(String newpath) {
    	taskbookFilePath = newpath;
    }

    //User Preference
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    public void saveUserPrefs(UserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


}