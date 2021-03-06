<!--- @@author A0113992B-reused --->
# User Guide
* [Quick Start](#quick-start)
* [Features](#features)
* [FAQ](#faq)
* [Command Summary](#command-summary)

## Quick Start

0. Ensure you have Java version `1.8.0_60` or later installed in your Computer.<br>
   > Having any Java 8 version is not enough. <br>
   This app will not work with earlier versions of Java 8.
   
1. Download the latest `SuperbTodo.jar` from the [releases](../../../releases) tab.
2. Copy the file to the folder you want to use as the home folder for your SuperbTodo.
3. Double-click the file to start the app. The GUI should appear in a few seconds. 
   > <img src="images/UiMockup.png" width="1000"><br>
   
4. Type the command in the command box and press <kbd>Enter</kbd> to execute it. <br>
   e.g. typing **`help`** and pressing <kbd>Enter</kbd> will open the help window. 
5. Some example commands you can try:
   * **`list`** : lists all undone tasks
   * **`add`**` a task by 4pm today` : 
     adds a timed task to the list.
   * **`delete`**` 3` : deletes the 3rd task shown in the current list
   * **`exit`** : exits the app
6. Refer to the [Features](#features) section below for details of each command.<br>

<!--- @@author A0113992B --->
## Features

> **Command Format**
> * Words in `UPPER_CASE` are the parameters.
> * Items in `SQUARE_BRACKETS` are optional.
> * Items with `...` after them can have multiple instances.
> * The order of parameters is fixed.

#### Viewing help : `help`
Format: `help`

> Help is also shown if you enter an incorrect command e.g. `abcd`
 
#### Adding a task: `add`
Adds a timed task<br>
Format: 
* `add <task description> at/by <time> on <date>`
* `add <task description> on <date> at/by <time>` 
* `add <task description> on/at/by <date> <time>`

> A timed task has a specified completion date and/or time. 

Examples: 
* `add finish homework by 4pm today`
* `add buy shoes on Sep 16 6:30pm`
* `add call grandmother at 15:00 on Oct 17`

Adds an untimed task<br>
Format: 
* `add <task description>` 

> An untimed task doesn't have a specified completion date and time.

Examples: 
* `add buy grocery recently`
* `add be careful across the road`

Adds hashtag<br>
Format: 
* `#<hashtag>` 

> To add hashtag(s) to task entries. 

Examples: 
* `add buy grocery at 7pm #chore`


#### Editing a task: `edit`
Edits a task already in the list<br>
Format: 
* `edit <index> <new description> at/on/by <new date/time> #<new hashtags>`

> New input will replace old task entry completely.   

Examples: 
* `edit 1 buy grocery at 5pm`


#### Marking of a task as done: `done`
Marks a completed task.<br>
Format: `done INDEX`

> Marks the task entry as done at the specified `INDEX`. 
  The index refers to the index number shown in the most recent listing.<br>
  The index **must be a positive integer** 1, 2, 3, ...

Examples: 
* `list`<br>
  `done 2`<br>
  Marks the 2nd task entry in all the undone tasks listed as done.
* `find #grocery`<br> 
  `done 1`<br>
  Marks the 1st task entry in the results of the `find` command as done.
  
#### Marking of a task as undone: `undone`
Unmark a completed task.<br>
Format: `undone INDEX`

> Marks a task entry as undone at the specified `INDEX`. 
  The index refers to the index number shown in the completed task listing.<br>
  The index **must be a positive integer** 1, 2, 3, ...

Examples: 
* `done 2`<br>
  Marks the 2nd task entry in the completed task listing as undone.
   
<!--- @@author A0135763B-reused --->  
#### Listing of tasks : `list`
Lists all tasks<br>
Format: 
* `list all`

> Lists all undone tasks. 


Lists untimed tasks<br>
Format: 
* `list untimed` 

> Lists all untimed tasks. 


Lists timed tasks<br>
Format: 
* `list timed` 

> Lists all timed tasks. 

Lists done tasks<br>
Format: 
* `list done` 

> Lists all completed task. 

Lists undone tasks<br>
Format: 
* `list undone` 

> Lists all uncompleted task. 

Lists overdue tasks<br>
Format: 
* `list overdue`

> Lists tasks not finished by the specified completion date/time. 


Lists tasks for today<br>
Format: 
* `list today`

> Lists tasks to be done today. 


Lists tasks for tomorrow<br>
Format: 
* `list tomorrow`

> Lists tasks to be done tomorrow. 

Lists tasks for a certain timestamp<br>
Format: 
* `list <date>`
* `list <period to period>`

> Lists tasks to be done by the specified timestamp. 

Examples: 
* `list Sep 16`
* `list today to wednesday`

#### Search for a task: `find`
Search for a task using a keyword.<br>
Format: 
*`find KEYWORD [MORE_KEYWORDS] "FULL KEYWORD"`

> * The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
> * Only the keyword is searched.
> * To match a full text add quotation to your search text
	e.g. `Hans Bo` will match `Hans Bo`
> * Persons matching at least one keyword will be returned (i.e. `OR` search).
    e.g. `Hans` will match `Hans Bo`

Examples: 
* `find grocery`<br>
  Returns `grocery bin` and `Grocery`
* `find call mother`<br>
  Returns Any tasks with description `call`, or `mother`


#### Deleting a task : `remove`
Deletes task entry with the specified number.<br>
Format: `remove INDEX`

> Deletes the task entry at the specified `INDEX`. 
  The index refers to the index number shown in the most recent listing.<br>
  The index **must be a positive integer** 1, 2, 3, ...

Examples: 
* `list undone`<br>
  `remove 2`<br>
  Deletes the 2nd task entry among all the undone tasks listed.
* `find grocery`<br> 
  `remove 1`<br>
  Deletes the 1st task entry in the results of the `find` command.
  
#### Undo a command: `undo`
Returns the system to the state before the execution of the last command.<br>
Format: 
* `undo`
* `undo 1`

Returns the system to the state before the execution of the last few commands.<br>


Format: 
* `undo <no. of commands to retract>`

> Undo the last few commands carried out and return the system to the then state. 

Examples: 
* `undo 2`

#### Redo a command: `redo`
Returns the system to the state before the execution of the last undo command.<br>
Format: 
* `redo`

<!--- @@author A0133945B --->

#### Change a saving path: `path`
Change the saving directory of the taskbook data.<br>
Format:
* `path NEWDIRECTORY`
Examples: 
* `path myowndata`<br>
* `path Desktop/mydatafolder`<br>


<!--- @@author A0113992B-reused --->
#### Clearing all entries : `clear`
Clears current command line input.<br>
Format: `clear`  

#### Exiting the program : `exit`
Exits the program.<br>
Format: `exit`  

#### Saving the data 
SuperbTodo data are saved in the hard disk automatically after any command that changes the data.<br>
There is no need to save manually.

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with 
       the file that contains the data of your previous Address Book folder.
<!--- @@author A0133945B --->
       
## Command Summary

Command | Format  
-------- | :-------- 
Add | `add <task description> at/by <time> on <date>`, `add <task description> on <date> at/by <time>`, `add <task description> on/at/by <date> <time>`
Clear | `clear`
Done | `done INDEX`
Edit | `edit <index> <new description> at/on/by <new date/time> #<new hashtags>`
Find | `find KEYWORD [MORE_KEYWORDS] "FULL KEYWORD"`
List | `list all`, `list KEYWORD`
Path | `path NEWDIRECTORY`
Help | `help`
Redo | `redo`
Remove | `remove INDEX`
Undo | `undo`, `undo <no. of commands to retract>`
Undone | `undone INDEX`

