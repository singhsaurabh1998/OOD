Here’s a **text-based diagram** of the **Notepad Editor** after integrating **Command Pattern** for Undo/Redo, and **Strategy Pattern** for Search functionality.

```
+---------------------------------------------+
|                 NotepadEditor               |
|---------------------------------------------|
| - text : StringBuilder                      |
| - clipboard : String                        |
| //- commandHistory : Stack<Command>           |
| //- redoStack : Stack<Command>                 |
| - searchStrategy : SearchStrategy           |
|---------------------------------------------|
| + setSearchStrategy(strategy: SearchStrategy)|
| + executeCommand(cmd: Command)              |
| + undo()                                    |
| + redo()                                    |
| + search(query: String)                     |
+---------------------------------------------+
                    ^
                    |
        +------------------------+
        |        Command          |  <<Interface>>
        +------------------------+
        | + execute()             |
        | + undo()                |
        +------------------------+
        ^            ^            ^
        |            |            |
+---------------+  +---------------+  +---------------+
| AddTextCommand|  | DeleteCommand |  | PasteCommand  |
+---------------+  +---------------+  +---------------+
| - textToAdd   |  | - deletedText |  | - pasteText   |
+---------------+  +---------------+  +---------------+
| + execute()   |  | + execute()   |  | + execute()   |
| + undo()      |  | + undo()      |  | + undo()      |
+---------------+  +---------------+  +---------------+

                +-------------------+
                |    CutCommand     |
                +-------------------+
                | - cutText         |
                +-------------------+
                | + execute()       |
                | + undo()          |
                +-------------------+

                +-------------------+
                |   CopyCommand     |
                +-------------------+
                | + execute()       |
                | + undo()          |
                +-------------------+

---------------------------------------------------------
           Search Strategies (Strategy Pattern)
---------------------------------------------------------
          +-----------------------+
          |    SearchStrategy     |  <<Interface>>
          +-----------------------+
          | + search(text, query) |
          +-----------------------+
                  ^        ^         ^
                  |        |         |
+---------------------+ +-------------------+ +---------------------+
| CaseSensitiveSearch | | RegexSearch       | | WholeWordSearch     |
+---------------------+ +-------------------+ +---------------------+
| + search()          | | + search()        | | + search()          |
+---------------------+ +-------------------+ +---------------------+

```

### **How it works in plain words:**

1. **Command Pattern**

    * Every action (Add, Delete, Cut, Copy, Paste) is wrapped as a **Command object**.
    * We can **undo** or **redo** by popping from history stacks.
    * This makes editing actions **reversible** and cleanly separated.

2. **Strategy Pattern**

    * Searching text can be done in different ways (case-sensitive, regex, whole-word).
    * We set the desired search strategy at runtime, so **no messy if-else logic** in the main editor.

If you want, I can now **implement this entire design in Java** with undo/redo and pluggable search strategies.
That way, you’ll see the patterns in **real working code**.
