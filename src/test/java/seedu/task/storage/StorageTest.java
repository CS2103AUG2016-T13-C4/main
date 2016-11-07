//@@author A0133945B-reused

package seedu.task.storage;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.google.gson.JsonSyntaxException;

import seedu.task.model.SuperbTodo;
import seedu.task.model.UserPrefs;
import seedu.task.model.tag.Tag;
import seedu.task.model.tag.UniqueTagList;
import seedu.task.model.task.DateTime;
import seedu.task.model.task.DueDateTime;
import seedu.task.model.task.Task;
import seedu.task.model.task.TaskName;
import seedu.task.model.task.UniqueTaskList;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

public class StorageTest {

    private SuperbTodoIO storage;

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();


    @Before
    public void setup() {
    	storage = new SuperbTodoIO(getTempFilePath("ab"), getTempFilePath("prefs"));
    }


    private String getTempFilePath(String fileName) {
        return testFolder.getRoot().getPath() + fileName;
    }


    /*
     * Note: This is an integration test that verifies the StorageManager is properly wired to the
     * {@link JsonUserPrefsStorage} class.
     * More extensive testing of UserPref saving/reading is done in {@link JsonUserPrefsStorageTest} class.
     */

    @Test
    public void prefsReadSave() throws Exception {
        UserPrefs original = new UserPrefs();
        original.setGuiSettings(300, 600, 4, 6);
        storage.saveUserPrefs(original);
        UserPrefs retrieved = storage.readUserPrefs().get();
        assertEquals(original, retrieved);
    }
  //@@author A0133945B
   
    @Test
    public void TaskBookReadSave() throws Exception {
    	//Initialize test data 
    	TaskName name1, name2;
    	DateTime dateTime;
    	DueDateTime dueDateTime;
    	UniqueTagList tags;
    	boolean isDone;
    	UniqueTaskList tasks = new UniqueTaskList();
        Tag t1 = new Tag("work");
        Tag t2 = new Tag("important");
        Tag t3 = new Tag("home");
        name1 = new TaskName("do assignment");
        name2 = new TaskName("play basketball");
        dateTime  = new DateTime("nov 7 5 pm");
        dueDateTime = new DueDateTime("nov 11 5pm");
        tags = new UniqueTagList(t1,t2,t3);
        isDone = true;
        
        Task task1 = new Task(name1, dateTime, dueDateTime, tags, isDone);
        Task task2 = new Task(name2, dateTime, dueDateTime, tags, isDone);
        tasks.add(task1);
        tasks.add(task2);
        SuperbTodo taskbook =  new SuperbTodo(tasks);
        storage.saveTasksIntoFile(taskbook);
        SuperbTodo retrieved = storage.loadTasksFromFile();
        assertEquals(taskbook, retrieved);
    }

    @Test
    public void getTaskBookFilePath(){
        assertNotNull(storage.getTaskbookFilePath());
    }

    @Test
    public void deleteTaskbook() throws Exception{
        storage.deleteTaskbook();
        SuperbTodo retrieved = storage.loadTasksFromFile();
        SuperbTodo expected = new SuperbTodo();
        assertEquals(retrieved, expected);
    }


}
