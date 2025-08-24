/*
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

*/
/**
 * Editor — This is our actual notepad where text changes happen.
 *//*

class Editor {
    private StringBuilder text = new StringBuilder();

    public void write(String newText) {
        text.append(newText);
    }

    public void delete(int length) {
        text.delete(text.length() - length, text.length());
    }

    public String getText() {
        return text.toString();
    }
}

*/
/**
 * The Command interface — every command will know how to execute and undo itself.
 *//*

interface Command {
    void execute(); // Do the action
    void undo();    // Undo the action
}

class WriteText implements Command {
    private Editor editor;
    private String text;

    WriteText(Editor editor, String text) {
        this.editor = editor;
        this.text = text;
    }

    @Override
    public void execute() {
        editor.write(text);
    }

    @Override
    public void undo() {
        editor.delete(text.length());
    }
}
*/
/**
 * CommandManager — Manages undo/redo using two stacks.
 *//*

class CommandManager {
    private final Stack<Command> undoStack = new Stack<>();
    private final Stack<Command> redoStack = new Stack<>();

    public void executeCommand(Command command) {
        command.execute();
        undoStack.push(command);
        redoStack.clear(); // Once we do new action, redo history is gone
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            Command lastCommand = undoStack.pop();
            lastCommand.undo();
            redoStack.push(lastCommand);
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            Command commandToRedo = redoStack.pop();
            commandToRedo.execute();
            undoStack.push(commandToRedo);
        }
    }
}

public class BasicNotepad {

    public static void main(String[] args) {
        Editor editor = new Editor();

        CommandManager manager = new CommandManager();
        Command write1 = new WriteText(editor,"Hello Saurabh");
        Command write2 = new WriteText(editor," Hello Gaurav");
        manager.executeCommand(write1);
        manager.executeCommand(write2);
        System.out.println(editor.getText());
        manager.undo();
        System.out.println(editor.getText());
        manager.redo();
        System.out.println(editor.getText());
    }
}
*/

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Command interface for all editor commands.
 */
interface Command {
    void execute();
    void undo();
}

/**
 * The actual editor where text is stored.
 */
class TextEditor {
    private StringBuilder text = new StringBuilder();
    private String clipboard = "";
    private SearchStrategy searchStrategy; // Strategy reference

    public void setSearchStrategy(SearchStrategy searchStrategy) {
        this.searchStrategy = searchStrategy;
    }

    public void write(String newText) {
        text.append(newText);
    }

    public void deleteLast(int length) {
        text.delete(text.length() - length, text.length());
    }

    public void cut(int start, int end) {
        clipboard = text.substring(start, end);
        text.delete(start, end);
    }

    public void copy(int start, int end) {
        clipboard = text.substring(start, end);
    }

    public void paste() {
        text.append(clipboard);
    }

    public String getClipboard() {
        return clipboard;
    }

    public String getText() {
        return text.toString();
    }

    public void search(String query) {
        if (searchStrategy == null) {
            System.out.println("No search strategy set!");
            return;
        }
        List<Integer> positions = searchStrategy.search(text.toString(), query);
        System.out.println("Found at positions: " + positions);
    }
}

/**
 * Command to write text.
 */
class WriteCommand implements Command {
    private TextEditor editor;
    private String text;

    public WriteCommand(TextEditor editor, String text) {
        this.editor = editor;
        this.text = text;
    }

    @Override
    public void execute() {
        editor.write(text);
    }

    @Override
    public void undo() {
        editor.deleteLast(text.length());
    }
}

/**
 * Command to cut text.
 */
class CutCommand implements Command {
    private TextEditor editor;
    private int start, end;
    private String cutText;

    public CutCommand(TextEditor editor, int start, int end) {
        this.editor = editor;
        this.start = start;
        this.end = end;
    }

    @Override
    public void execute() {
        cutText = editor.getText().substring(start, end);
        editor.cut(start, end);
    }

    @Override
    public void undo() {
        StringBuilder sb = new StringBuilder(editor.getText());
        sb.insert(start, cutText);
        editor.write(sb.toString());
    }
}

/**
 * Command to copy text.
 */
class CopyCommand implements Command {
    private TextEditor editor;
    private int start, end;

    public CopyCommand(TextEditor editor, int start, int end) {
        this.editor = editor;
        this.start = start;
        this.end = end;
    }

    @Override
    public void execute() {
        editor.copy(start, end);
    }

    @Override
    public void undo() {
        // Copy doesn't change the text, so no undo needed
    }
}

/**
 * Command to paste text.
 */
class PasteCommand implements Command {
    private TextEditor editor;
    private String pastedText;

    public PasteCommand(TextEditor editor) {
        this.editor = editor;
    }

    @Override
    public void execute() {
        pastedText = editor.getClipboard();
        editor.paste();
    }

    @Override
    public void undo() {
        editor.deleteLast(pastedText.length());
    }
}

/**
 * Command Manager to handle undo/redo.
 */
class CommandManager {
    private final Stack<Command> undoStack = new Stack<>();
    private final Stack<Command> redoStack = new Stack<>();

    public void executeCommand(Command command) {
        command.execute();
        undoStack.push(command);
        redoStack.clear(); // Clear redo when a new command is executed
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            Command cmd = undoStack.pop();
            cmd.undo();
            redoStack.push(cmd);
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            Command cmd = redoStack.pop();
            cmd.execute();
            undoStack.push(cmd);
        }
    }
}

/**
 * Strategy interface for different search algorithms.
 */
interface SearchStrategy {
    List<Integer> search(String text, String query);
}

/**
 * Case-Sensitive Search Strategy
 */
class CaseSensitiveSearch implements SearchStrategy {
    @Override
    public List<Integer> search(String text, String query) {
        List<Integer> positions = new ArrayList<>();
        int index = text.indexOf(query);
        while (index != -1) {
            positions.add(index);
            index = text.indexOf(query, index + query.length());
        }
        return positions;
    }
}

/**
 * Regex Search Strategy
 */
class RegexSearch implements SearchStrategy {
    @Override
    public List<Integer> search(String text, String query) {
        List<Integer> positions = new ArrayList<>();
        Pattern pattern = Pattern.compile(query);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            positions.add(matcher.start());
        }
        return positions;
    }
}

/**
 * Whole-Word Search Strategy
 */
class WholeWordSearch implements SearchStrategy {
    @Override
    public List<Integer> search(String text, String query) {
        List<Integer> positions = new ArrayList<>();
        String[] words = text.split("\\s+");
        int position = 0;
        for (String word : words) {
            if (word.equals(query)) {
                positions.add(position);
            }
            position += word.length() + 1; // +1 for space
        }
        return positions;
    }
}


/**
 * Demo class
 */
public class Notepad {
    public static void main(String[] args) {
        TextEditor editor = new TextEditor();
        CommandManager manager = new CommandManager();

        // Writing text
        manager.executeCommand(new WriteCommand(editor, "Hello "));
        manager.executeCommand(new WriteCommand(editor, " World "));
        System.out.println("Text: " + editor.getText());

        // Undo last write
        manager.undo();
        System.out.println("After Undo: " + editor.getText());

        // Redo
        manager.redo();
        System.out.println("After Redo: " + editor.getText());

        // Copy "Hello"
        manager.executeCommand(new CopyCommand(editor, 0, 5));

        // Paste
        manager.executeCommand(new PasteCommand(editor));
        System.out.println("After Paste: " + editor.getText());

        editor.setSearchStrategy(new WholeWordSearch());
        editor.search("World");
    }
}
