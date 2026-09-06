# Kiaw User Guide

Kiaw is a task management chatbot that helps users keep track of todos, deadlines, and events through simple text commands.

## Adding todos

Adds a task without an associated date.

Example:

`todo read book`

Kiaw adds the todo to the task list.

## Adding deadlines

Adds a task that must be completed by a specific date.

Format:

`deadline DESCRIPTION /by yyyy-MM-dd`

Example:

`deadline submit assignment /by 2026-09-10`

Kiaw adds the deadline and displays the specified due date.

## Adding events

Adds an event occurring between two dates.

Format:

`event DESCRIPTION /from yyyy-MM-dd /to yyyy-MM-dd`

Example:

`event project meeting /from 2026-10-05 /to 2026-10-06`

Kiaw adds the event with its start and end dates.

## Listing tasks

Displays all tasks currently stored in Kiaw.

Command:

`list`

## Finding tasks

Finds tasks whose descriptions contain the specified keyword.

Format:

`find KEYWORD`

Example:

`find book`

Kiaw displays the tasks whose descriptions contain `book`.

## Marking tasks

Marks a task as completed.

Format:

`mark TASK_NUMBER`

Example:

`mark 2`

## Unmarking tasks

Marks a completed task as not completed.

Format:

`unmark TASK_NUMBER`

Example:

`unmark 2`

## Deleting tasks

Deletes a task from the task list.

Format:

`delete TASK_NUMBER`

Example:

`delete 3`

## Sorting tasks by date

Sorts the task list chronologically.

Command:

`sort`

Deadlines are sorted using their due dates, while events are sorted using
their start dates. Tasks without dates, such as todos, are placed after all
dated tasks.

Tasks with the same sorting date retain their existing relative order.
Todos also retain their existing relative order.

The sorted order is saved automatically and remains after Kiaw is restarted.

Example:

Before sorting:

```text
1.[T][ ] read book
2.[D][ ] submit report (by: Dec 20 2026)
3.[E][ ] meeting (from: Oct 05 2026 to: Oct 06 2026)
4.[D][ ] homework (by: Nov 15 2026)
```

After entering `sort`:

```text
I've sorted your tasks by date:
1.[E][ ] meeting (from: Oct 05 2026 to: Oct 06 2026)
2.[D][ ] homework (by: Nov 15 2026)
3.[D][ ] submit report (by: Dec 20 2026)
4.[T][ ] read book
```

The `sort` command does not take additional arguments. For example,
`sort date` is not a valid command.

## Exiting Kiaw

Exits the application.

Command:

`bye`
