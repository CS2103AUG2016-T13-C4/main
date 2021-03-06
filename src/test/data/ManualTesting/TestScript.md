<!-- @@author A0113992B -->
# Test Script

## Loading Sample Data

1. Launch SuperbTodo.jar.
2. Enter `add demo` and exit the application.
3. A data file `taskbook.xml` will be generated in the sub-folder `data` where SupebTodo.jar is located.
4. Replace contents of the original `taskbook.xml` with `SampleData.xml`.

## Feature Testing

#### Add a task 
| Command | Expected Result |
|----------------------------------------------------|-----------------------------------------------------------------------|
| add do assignment | Adds a floating task named “do assignment” |
| add go out on nov 5 | Adds a deadline task named “go out“ on on nov 5th at the time of the task is added
| add feed birds by 3pm oct 5 | Adds a deadline task named “feed birds” due on oct 5th 3pm |
| add visit grandma garden from 3pm dec 5 to 5pm dec 9 | Adds an event named “visit grandma’s garden” starting on dec 5th 3pm and ending on 5pm dec 9th|
| add do assignments by today | Adds a floating task on the date of task entered |
| add visit gardens by the bay by 3pm today | Adds a deadline task named “visit gardens by the bay” due at 3pm on the day the task is entered  |
| add “spare me lord” camp | “Task names should be spaces or alphanumeric characters” appears in the result display

#### Done a task

| Command | Expected Result |
|----------------------------------------------------|-----------------------------------------------------------------------|
| done 1 | “Done Task to: do assignment” appears in result display. This task is marked as done and will appear in done list when listed. |
| done 4 | “Done Task to: visit grandma from 3pm oct 5 to 5pm oct 9” appears in result display. This task is marked as done and will appear in done list when listed.  |
| done do assignment | “Invalid command format! 
done:Mark done the task identified by the index number used in the last task listing.
Parameters: INDEX (must be a positive integer)
Example: done 1”  appears in result display. |
| done 1000 | “The task index provided is invalid” appears in result display. |

#### Undone a task

| Command | Expected Result |
|----------------------------------------------------|-----------------------------------------------------------------------|
| undone 1 | “unDone Task to: do assignment” appears in result display. This task is marked as done and will appear in done list when listed. |
| undone do assignment | “Invalid command format! 
undone:Mark undone the task identified by the index number used in the last task listing.
Parameters: INDEX (must be a positive integer)
Example: undone 1”  appears in result display. |
| undone 1000 | “The task index provided is invalid” appears in result display. |

#### List tasks

| Command | Expected Result |
|----------------------------------------------------|-----------------------------------------------------------------------|
| list all | All tasks entered will be listed and “Listed all tasks and events” appears in result display.|
| list today | Today’s tasks will be listed and “Listed all task for today” appears in result display.|
| list tomorrow | Today’s tasks will be listed and “Listed all task for tomorrow” appears in result display. |
| list /all | “SuperbTodo cannot understand your listing criteria, try another criteria.” appears in the result display |
| list nov 5 | All tasks due nov 5th will be listed and “Listed all tasks and events” appears in result display. |


#### Remove tasks

| Command | Expected Result |
|----------------------------------------------------|-----------------------------------------------------------------------|
| remove 1 | “Removed Task: do assignment” appears in result display and it is removed from taskbook|
| remove 1000 | "The task index provided is invalid" appears in result display. |
| delete do assignment |  "Invalid command format! 
remove: Remove the task identified by the index number used in the last person listing.
Parameters: INDEX (must be a positive integer)
Example: remove 1" appears in the result display |

#### Find tasks by keyword(s)

| Command | Expected Result |
|----------------------------------------------------|-----------------------------------------------------------------------|
| find do assignment | deadline task and floating task containing do assignment are both listed and “2 tasks listed!” appears in the result display|
| find visit garden | tasks with either “visit” or “garden” are listed |
| find "visit gardens" | tasks containing the whole word "visit gardens" are listed |
| find OuterSpace | "0 tasks listed!" appears in result display |


#### Edit a task

| Command | Expected Result |
|----------------------------------------------------|-----------------------------------------------------------------------|
| edit 3 “go lunch“ | “Edits Task to:go out” appears in the result display and task 3 is replaced with the new task description “go lunch” as a floating task|
| edit 5 spare me lord camp from nov 5 2pm to nov 7 3pm | “Edits Task to:spare me lord camp” appears in the result display and task 5 is replaced with the new task description “spare me lord camp” as an event |


#### Clear tasks

| Command | Expected Result |
|----------------------------------------------------|-----------------------------------------------------------------------|
| clear | "All tasks have been cleared!" appears in result display |
| claer | "Unknown command" appears in result display |


#### Undo

| Command | Expected Result |
|----------------------------------------------------|-----------------------------------------------------------------------|
| add task | a floating task named “task” is added.|
| undo | “undo add command” appears in the result display |
| edit 5 play | task 5 is edited with the new description “play”. |
| undo | “undo edit command“ appears in result display |
| remove 5 | task 5 is removed from taskbook |
| undo | “undo remove command“ appears in result display |

#### Redo

| Command | Expected Result |
|----------------------------------------------------|-----------------------------------------------------------------------|
| redo | Redo the last action and “Redoing action” appears in the result display. |


#### Help

| Command | Expected Result |
|----------------------------------------------------|-----------------------------------------------------------------------|
| help | Loads a browser window to the user guide. "Opened help window.” appears in result display. |
| hlep |  "Unknown Command" appears in result display. |           



#### Exit

| Command | Expected Result |
|----------------------------------------------------|-----------------------------------------------------------------------|
| Exit | App is closed. |
