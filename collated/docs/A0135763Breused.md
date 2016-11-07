# A0135763Breused
###### /UserGuide.md
``` md
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
*`find <keyword> [MORE_KEYWORDS]`

> * The search is case sensitive. e.g `hans` will not match `Hans`
> * The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
> * Only the keyword is searched.
> * Only full words will be matched e.g. `Han` will not match `Hans`
> * Persons matching at least one keyword will be returned (i.e. `OR` search).
    e.g. `Hans` will match `Hans Bo`

Examples: 
* `find grocery`<br>
  Returns `grocery bin` but not `Grocery`
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
* `find #grocery`<br> 
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

```
