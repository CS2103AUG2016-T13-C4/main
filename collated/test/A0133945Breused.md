# A0133945Breused
###### /java/seedu/task/commons/core/ConfigTest.java
``` java

    @Test
    public void equalsMethod(){
        Config defaultConfig = new Config();
        assertFalse(defaultConfig.equals(null));
        assertTrue(defaultConfig.equals(defaultConfig));
    }


}
```
###### /java/seedu/task/commons/core/VersionTest.java
``` java

package seedu.task.commons.core;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.task.commons.core.Version;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class VersionTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void versionParsing_acceptableVersionString_parsedVersionCorrectly() {
        verifyVersionParsedCorrectly("V0.0.0ea", 0, 0, 0, true);
        verifyVersionParsedCorrectly("V3.10.2", 3, 10, 2, false);
        verifyVersionParsedCorrectly("V100.100.100ea", 100, 100, 100, true);
    }

    @Test
    public void versionParsing_wrongVersionString_throwIllegalArgumentException() {
        thrown.expect(IllegalArgumentException.class);
        Version.fromString("This is not a version string");
    }

    @Test
    public void versionConstructor_correctParameter_valueAsExpected() {
        Version version = new Version(19, 10, 20, true);

        assertEquals(19, version.getMajor());
        assertEquals(10, version.getMinor());
        assertEquals(20, version.getPatch());
        assertEquals(true, version.isEarlyAccess());
    }

    @Test
    public void versionToString_validVersion_correctStringRepresentation() {
        // boundary at 0
        Version version = new Version(0, 0, 0, true);
        assertEquals("V0.0.0ea", version.toString());

        // normal values
        version = new Version(4, 10, 5, false);
        assertEquals("V4.10.5", version.toString());

        // big numbers
        version = new Version(100, 100, 100, true);
        assertEquals("V100.100.100ea", version.toString());
    }

    @Test
    public void versionComparable_validVersion_compareToIsCorrect() {
        Version one, another;

        // Tests equality
        one = new Version(0, 0, 0, true);
        another = new  Version(0, 0, 0, true);
        assertTrue(one.compareTo(another) == 0);

        one = new Version(11, 12, 13, false);
        another = new  Version(11, 12, 13, false);
        assertTrue(one.compareTo(another) == 0);

        // Tests different patch
        one = new Version(0, 0, 5, false);
        another = new  Version(0, 0, 0, false);
        assertTrue(one.compareTo(another) > 0);

        // Tests different minor
        one = new Version(0, 0, 0, false);
        another = new  Version(0, 5, 0, false);
        assertTrue(one.compareTo(another) < 0);

        // Tests different major
        one = new Version(10, 0, 0, true);
        another = new  Version(0, 0, 0, true);
        assertTrue(one.compareTo(another) > 0);

        // Tests high major vs low minor
        one = new Version(10, 0, 0, true);
        another = new  Version(0, 1, 0, true);
        assertTrue(one.compareTo(another) > 0);

        // Tests high patch vs low minor
        one = new Version(0, 0, 10, false);
        another = new  Version(0, 1, 0, false);
        assertTrue(one.compareTo(another) < 0);

        // Tests same major minor different patch
        one = new Version(2, 15, 0, false);
        another = new  Version(2, 15, 5, false);
        assertTrue(one.compareTo(another) < 0);

        // Tests early access vs not early access on same version number
        one = new Version(2, 15, 0, true);
        another = new  Version(2, 15, 0, false);
        assertTrue(one.compareTo(another) < 0);

        // Tests early access lower version vs not early access higher version compare by version number first
        one = new Version(2, 15, 0, true);
        another = new  Version(2, 15, 5, false);
        assertTrue(one.compareTo(another) < 0);

        // Tests early access higher version vs not early access lower version compare by version number first
        one = new Version(2, 15, 0, false);
        another = new  Version(2, 15, 5, true);
        assertTrue(one.compareTo(another) < 0);
    }

    @Test
    public void versionComparable_validVersion_hashCodeIsCorrect() {
        Version version = new Version(100, 100, 100, true);
        assertEquals(100100100, version.hashCode());

        version = new Version(10, 10, 10, false);
        assertEquals(1010010010, version.hashCode());
    }

    @Test
    public void versionComparable_validVersion_equalIsCorrect() {
        Version one, another;

        one = new Version(0, 0, 0, false);
        another = new  Version(0, 0, 0, false);
        assertTrue(one.equals(another));

        one = new Version(100, 191, 275, true);
        another = new  Version(100, 191, 275, true);
        assertTrue(one.equals(another));
    }

    private void verifyVersionParsedCorrectly(String versionString,
                                              int major, int minor, int patch, boolean isEarlyAccess) {
        assertEquals(new Version(major, minor, patch, isEarlyAccess), Version.fromString(versionString));
    }
}
```
###### /java/seedu/task/storage/JsonUserPrefsStorageTest.java
``` java
package seedu.task.storage;


import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import seedu.task.commons.exceptions.DataConversionException;
import seedu.task.commons.util.FileUtil;
import seedu.task.model.UserPrefs;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class JsonUserPrefsStorageTest {

    private static String TEST_DATA_FOLDER = FileUtil.getPath("./src/test/data/JsonUserPrefsStorageTest/");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void readUserPrefs_nullFilePath_assertionFailure() throws DataConversionException {
        thrown.expect(AssertionError.class);
        readUserPrefs(null);
    }

    private Optional<UserPrefs> readUserPrefs(String userPrefsFileInTestDataFolder) throws DataConversionException {
        String prefsFilePath = addToTestDataPathIfNotNull(userPrefsFileInTestDataFolder);
        return new JsonUserPrefsStorage(prefsFilePath).readUserPrefs(prefsFilePath);
    }

    @Test
    public void readUserPrefs_missingFile_emptyResult() throws DataConversionException {
        assertFalse(readUserPrefs("NonExistentFile.json").isPresent());
    }

    @Test
    public void readUserPrefs_notJasonFormat_exceptionThrown() throws DataConversionException {

        thrown.expect(DataConversionException.class);
        readUserPrefs("NotJsonFormatUserPrefs.json");

        /* IMPORTANT: Any code below an exception-throwing line (like the one above) will be ignored.
         * That means you should not have more than one exception test in one method
         */
    }

    private String addToTestDataPathIfNotNull(String userPrefsFileInTestDataFolder) {
        return userPrefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER + userPrefsFileInTestDataFolder
                : null;
    }

    @Test
    public void readUserPrefs_fileInOrder_successfullyRead() throws DataConversionException {
        UserPrefs expected = new UserPrefs();
        expected.setGuiSettings(1000, 500, 300, 100);
        UserPrefs actual = readUserPrefs("TypicalUserPref.json").get();
        assertEquals(expected, actual);
    }

    @Test
    public void readUserPrefs_valuesMissingFromFile_defaultValuesUsed() throws DataConversionException {
        UserPrefs actual = readUserPrefs("EmptyUserPrefs.json").get();
        assertEquals(new UserPrefs(), actual);
    }

    @Test
    public void readUserPrefs_extraValuesInFile_extraValuesIgnored() throws DataConversionException {
        UserPrefs expected = new UserPrefs();
        expected.setGuiSettings(1000, 500, 300, 100);
        UserPrefs actual = readUserPrefs("ExtraValuesUserPref.json").get();

        assertEquals(expected, actual);
    }

    @Test
    public void savePrefs_nullPrefs_assertionFailure() throws IOException {
        thrown.expect(AssertionError.class);
        saveUserPrefs(null, "SomeFile.json");
    }

    @Test
    public void saveUserPrefs_nullFilePath_assertionFailure() throws IOException {
        thrown.expect(AssertionError.class);
        saveUserPrefs(new UserPrefs(), null);
    }

    private void saveUserPrefs(UserPrefs userPrefs, String prefsFileInTestDataFolder) throws IOException {
        new JsonUserPrefsStorage(addToTestDataPathIfNotNull(prefsFileInTestDataFolder)).saveUserPrefs(userPrefs);
    }

    @Test
    public void saveUserPrefs_allInOrder_success() throws DataConversionException, IOException {

        UserPrefs original = new UserPrefs();
        original.setGuiSettings(1200, 200, 0, 2);

        String pefsFilePath = testFolder.getRoot() + File.separator + "TempPrefs.json";
        JsonUserPrefsStorage jsonUserPrefsStorage = new JsonUserPrefsStorage(pefsFilePath);

        //Try writing when the file doesn't exist
        jsonUserPrefsStorage.saveUserPrefs(original);
        UserPrefs readBack = jsonUserPrefsStorage.readUserPrefs().get();
        assertEquals(original, readBack);

        //Try saving when the file exists
        original.setGuiSettings(5, 5, 5, 5);
        jsonUserPrefsStorage.saveUserPrefs(original);
        readBack = jsonUserPrefsStorage.readUserPrefs().get();
        assertEquals(original, readBack);
    }

}
```
###### /java/seedu/task/storage/StorageTest.java
``` java

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
```
###### /java/seedu/task/TestApp.java
``` java

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
```
###### /java/seedu/task/TestApp.java
``` java

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
```
