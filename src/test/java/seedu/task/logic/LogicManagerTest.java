package seedu.task.logic;
//@@author A0135763B
import com.google.common.eventbus.Subscribe;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import seedu.task.commons.core.EventsCenter;
import seedu.task.commons.events.model.SuperbTodoChangedEvent;
import seedu.task.commons.events.ui.JumpToListRequestEvent;
import seedu.task.commons.events.ui.ShowHelpRequestEvent;
import seedu.task.commons.exceptions.IllegalValueException;
import seedu.task.logic.Logic;
import seedu.task.logic.LogicManager;
import seedu.task.logic.commands.*;
import seedu.task.model.Model;
import seedu.task.model.ModelManager;
import seedu.task.model.ReadOnlySuperbTodo;
import seedu.task.model.SuperbTodo;
import seedu.task.model.tag.Tag;
import seedu.task.model.tag.UniqueTagList;
import seedu.task.model.task.*;
import seedu.task.model.task.UniqueTaskList.DuplicateTaskException;
import seedu.task.storage.SuperbTodoIO;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.task.commons.core.Messages.*;

public class LogicManagerTest {
	
	private final int DEFAULT_INDEX = -1;
	
    /**
     * See https://github.com/junit-team/junit4/wiki/rules#temporaryfolder-rule
     */
	@Rule
	public final ExpectedException exception = ExpectedException.none();
	
    @Rule
    public TemporaryFolder saveFolder = new TemporaryFolder();

    private Model model;
    private Logic logic;

    //These are for checking the correctness of the events raised
    private ReadOnlySuperbTodo latestSavedSuperbTodo;
    private boolean helpShown;
    private int targetedJumpIndex;

    @Subscribe
    private void handleLocalModelChangedEvent(SuperbTodoChangedEvent abce) {
        latestSavedSuperbTodo = new SuperbTodo(abce.data);
    }

    @Subscribe
    private void handleShowHelpRequestEvent(ShowHelpRequestEvent she) {
        helpShown = true;
    }

    @Subscribe
    private void handleJumpToListRequestEvent(JumpToListRequestEvent je) {
        targetedJumpIndex = je.targetIndex;
    }

    @Before
    public void setup() {
        model = new ModelManager();
        String tempSuperbTodoFile = saveFolder.getRoot().getPath() + "TempSuperbTodo.xml";
        String tempPreferencesFile = saveFolder.getRoot().getPath() + "TempPreferences.json";
        logic = new LogicManager(model, new SuperbTodoIO(tempSuperbTodoFile, tempPreferencesFile));
        EventsCenter.getInstance().registerHandler(this);

        latestSavedSuperbTodo = new SuperbTodo(model.getSuperbTodo()); // last saved assumed to be up to date before.
        helpShown = false;
        targetedJumpIndex = -1; // non yet
    }

    @After
    public void teardown() {
        EventsCenter.clearSubscribers();
    }

    @Test
    public void execute_invalid() throws Exception {
        String invalidCommand = "       ";
        assertCommandBehavior(invalidCommand,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
    }

    /**
     * Executes the command and confirms that the result message is correct.
     * Both the 'superbTodo' and the 'last shown list' are expected to be empty.
     * @see #assertCommandBehavior(String, String, ReadOnlySuperbTodo, List)
     */
    private void assertCommandBehavior(String inputCommand, String expectedMessage) throws Exception {
        assertCommandBehavior(inputCommand, expectedMessage, model.getSuperbTodo(), model.getFilteredTaskList());
    }

    /**
     * Executes the command and confirms that the result message is correct and
     * also confirms that the following three parts of the LogicManager object's state are as expected:<br>
     *      - the internal superbTodo data are same as those in the {@code expectedsuperbTodo} <br>
     *      - the backing list shown by UI matches the {@code shownList} <br>
     *      - {@code expectedsuperbTodo} was saved to the storage file. <br>
     */
    private void assertCommandBehavior(String inputCommand, String expectedMessage,
                                       ReadOnlySuperbTodo expectedSuperbTodo,
                                       List<? extends ReadOnlyTask> expectedShownList) throws Exception {

        //Execute the command
        CommandResult result = logic.execute(inputCommand);

        //Confirm the ui display elements should contain the right data
        assertEquals(expectedMessage, result.feedbackToUser);
        assertEquals(expectedShownList, model.getFilteredTaskList());

        //Confirm the state of data (saved and in-memory) is as expected
        assertEquals(expectedSuperbTodo, model.getSuperbTodo());
        assertEquals(expectedSuperbTodo, latestSavedSuperbTodo);
    }


    @Test
    public void execute_unknownCommandWord() throws Exception {
        String unknownCommand = "uicfhmowqewca";
        assertCommandBehavior(unknownCommand, MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void execute_help() throws Exception {
        assertCommandBehavior("help", HelpCommand.SHOWING_HELP_MESSAGE);
        assertTrue(helpShown);
    }

    @Test
    public void execute_exit() throws Exception {
        assertCommandBehavior("exit", ExitCommand.MESSAGE_EXIT_ACKNOWLEDGEMENT);
    }

    @Test
    public void execute_clear() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        model.addTask(helper.generateTask(1), DEFAULT_INDEX);
        model.addTask(helper.generateTask(2), DEFAULT_INDEX);
        model.addTask(helper.generateTask(3), DEFAULT_INDEX);
        assertCommandBehavior("clear", ClearCommand.MESSAGE_SUCCESS, new SuperbTodo(), Collections.emptyList());
    }

/**
 * havent changed test  cases content yet
 * @throws Exception
 */
    @Test
    public void execute_add_invalidArgsFormat() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);
        assertCommandBehavior(
                "add ", expectedMessage);
    }

    @Test
    public void execute_addDuplicate_notAllowed() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Task toBeAdded = helper.adam();
        SuperbTodo expectedAB = new SuperbTodo();
        expectedAB.addTask(toBeAdded);
        
        exception.expect(DuplicateTaskException.class);
        // setup starting state
        model.addTask(toBeAdded, DEFAULT_INDEX); // Task already in internal address book
    }


    @Test
    public void execute_list_showsAllTasks() throws Exception {
        // prepare expectations
        TestDataHelper helper = new TestDataHelper();
        SuperbTodo expectedAB = helper.generateSuperbTodo(2);
        List<? extends ReadOnlyTask> expectedList = expectedAB.getTaskList();

        // prepare address book state

        assertCommandBehavior("list all",
                ListCommand.MESSAGE_SUCCESS_ALL,
                model.getSuperbTodo(),
                expectedList);
    }


    /**
     * Confirms the 'invalid argument index number behaviour' for the given command
     * targeting a single Task in the shown list, using visible index.
     * @param commandWord to test assuming it targets a single Task in the last shown list based on visible index.
     */
    private void assertIncorrectIndexFormatBehaviorForCommand(String commandWord, String expectedMessage) throws Exception {
        assertCommandBehavior(commandWord , expectedMessage); //index missing
        assertCommandBehavior(commandWord + " +1", expectedMessage); //index should be unsigned
        assertCommandBehavior(commandWord + " -1", expectedMessage); //index should be unsigned
        assertCommandBehavior(commandWord + " 0", expectedMessage); //index cannot be 0
        assertCommandBehavior(commandWord + " not_a_number", expectedMessage);
    }

    /**
     * Confirms the 'invalid argument index number behaviour' for the given command
     * targeting a single Task in the shown list, using visible index.
     * @param commandWord to test assuming it targets a single Task in the last shown list based on visible index.
     */
    private void assertIndexNotFoundBehaviorForCommand(String commandWord) throws Exception {
        String expectedMessage = MESSAGE_INVALID_TASK_DISPLAYED_INDEX;
        TestDataHelper helper = new TestDataHelper();
        List<Task> TaskList = helper.generateTaskList(2);

        // set AB state to 2 Tasks
        model.resetData(new SuperbTodo());
        for (Task p : TaskList) {
        	model.addTask(p, DEFAULT_INDEX);
        }

        assertCommandBehavior(commandWord + " 3", expectedMessage, model.getSuperbTodo(), TaskList);
    }

    @Test
    public void execute_selectInvalidArgsFormat_errorMessageShown() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectCommand.MESSAGE_USAGE);
        assertIncorrectIndexFormatBehaviorForCommand("select", expectedMessage);
    }

    @Test
    public void execute_selectIndexNotFound_errorMessageShown() throws Exception {
        assertIndexNotFoundBehaviorForCommand("select");
    }

    @Test
    public void execute_deleteInvalidArgsFormat_errorMessageShown() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE);
        assertIncorrectIndexFormatBehaviorForCommand("remove", expectedMessage);
    }

    @Test
    public void execute_deleteIndexNotFound_errorMessageShown() throws Exception {
        assertIndexNotFoundBehaviorForCommand("remove");
    }

    @Test
    public void execute_find_invalidArgsFormat() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE);
        assertCommandBehavior("find ", expectedMessage);
    }

    @Test
    public void execute_find_isNotCaseSensitive() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task p1 = helper.generateTaskWithName("test bla KEY bla");
        Task p2 = helper.generateTaskWithName("test KEY bla bceofeia");
        Task p3 = helper.generateTaskWithName("key test");
        Task p4 = helper.generateTaskWithName("KEy test asdw");

        List<Task> fourTasks = helper.generateTaskList(p3, p1, p4, p2);
        SuperbTodo expectedAB = helper.generateSuperbTodo(fourTasks);
        List<Task> expectedList = fourTasks;

        assertCommandBehavior("find KEY",
                Command.getMessageForTaskListShownSummary(expectedList.size()));
    }

    @Test
    public void execute_find_matchesIfAnyKeywordPresent() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task pTarget1 = helper.generateTaskWithName("bla bla KEY bla");
        Task pTarget2 = helper.generateTaskWithName("bla rAnDoM bla bceofeia");
        Task pTarget3 = helper.generateTaskWithName("key key");
        Task p1 = helper.generateTaskWithName("sduauo");

        List<Task> fourTasks = helper.generateTaskList(pTarget1, p1, pTarget2, pTarget3);
        SuperbTodo expectedAB = helper.generateSuperbTodo(fourTasks);
        List<Task> expectedList = helper.generateTaskList(pTarget1, pTarget2, pTarget3);
        //helper.addToModel(model, fourTasks);

        assertCommandBehavior("find key rAnDoM",
                Command.getMessageForTaskListShownSummary(expectedList.size()));
    }


    /**
     * A utility class to generate test data.
     */
    class TestDataHelper{

        Task adam() throws Exception {
            TaskName name = new TaskName("buy grocery");
            DateTime dateTime = new DateTime("jan 5 8pm");
            DueDateTime dueDateTime = new DueDateTime("jan 5 9pm");
            Tag tag1 = new Tag("impt");
            Tag tag2 = new Tag("chore");
            UniqueTagList tags = new UniqueTagList(tag1, tag2);
            return new Task(name, dateTime, dueDateTime,tags, false);
        }

        /**
         * Generates a valid Task using the given seed.
         * Running this function with the same parameter values guarantees the returned Task will have the same state.
         * Each unique seed will generate a unique Task object.
         *
         * @param seed used to generate the Task data field values
         */
        Task generateTask(int seed) throws Exception {
            return new Task(
                    new TaskName("Task " + (seed + model.getFilteredTaskList().size())),
                    new DateTime("today"),
                    new DueDateTime("tomorrow"),
                    new UniqueTagList(new Tag("tag" + Math.abs(seed)), new Tag("tag" + Math.abs(seed + 1))),
                    false
            );
        }

        /** Generates the correct add command based on the Task given */
        String generateAddCommand(Task p) {
            StringBuffer cmd = new StringBuffer();

            cmd.append("add ");

            cmd.append(p.getName().toString());
            cmd.append(p.getDateTime());
            cmd.append(p.getDueTime());

            UniqueTagList tags = p.getTags();
            for(Tag t: tags){
                cmd.append(" t/").append(t.tagName);
            }

            return cmd.toString();
        }

        /**
         * Generates a SuperbTodo with auto-generated tasks.
         */
        SuperbTodo generateSuperbTodo(int numGenerated) throws Exception{
            SuperbTodo superbTodo = new SuperbTodo();
            addToSuperbTodo(superbTodo, numGenerated);
            return superbTodo;
        }

        /**
         * Generates an SuperbTodo based on the list of Tasks given.
         */
        SuperbTodo generateSuperbTodo(List<Task> tasks) throws Exception{
            SuperbTodo superbTodo = new SuperbTodo();
            addTosuperbTodo(superbTodo, tasks);
            return superbTodo;
        }

        /**
         * Adds auto-generated Task objects to the given superbTodo
         * @param superbTodo The superbTodo to which the Tasks will be added
         */
        void addToSuperbTodo(SuperbTodo superbTodo, int numGenerated) throws Exception{
            addTosuperbTodo(superbTodo, generateTaskList(numGenerated));
        }

        /**
         * Adds the given list of Tasks to the given superbTodo
         */
        void addTosuperbTodo(SuperbTodo superbTodo, List<Task> TasksToAdd) throws Exception{
            for(Task p: TasksToAdd){
                superbTodo.addTask(p);
            }
        }

        /**
         * Adds auto-generated Task objects to the given model
         * @param model The model to which the Tasks will be added
         */
        void addToModel(Model model, int numGenerated) throws Exception{
            addToModel(model, generateTaskList(numGenerated));
        }

        /**
         * Adds the given list of Tasks to the given model
         */
        void addToModel(Model model, List<Task> TasksToAdd) throws Exception{
            for(Task p: TasksToAdd){
            	model.addTask(p, DEFAULT_INDEX);
            }
        }

        /**
         * Generates a list of Tasks based on the flags.
         */
        List<Task> generateTaskList(int numGenerated) throws Exception{
            List<Task> Tasks = new ArrayList<>();
            for(int i = 1; i <= numGenerated; i++){
                Tasks.add(generateTask(i));
            }
            return Tasks;
        }

        List<Task> generateTaskList(Task... Tasks) {
            return Arrays.asList(Tasks);
        }

        /**
         * Generates a Task object with given name. Other fields will have some dummy values.
         */
        Task generateTaskWithName(String name) throws Exception {
            return new Task(
                    new TaskName(name),
                    new DateTime("jan 5 8pm"),
                    new DueDateTime("jan 5 9pm"),
                    new UniqueTagList(new Tag("impt")),
                    false
            );
        }
    }
}
