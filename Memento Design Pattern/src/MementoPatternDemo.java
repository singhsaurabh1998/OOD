import java.util.Stack;

class EditorMemento {
    private final String content;

    public EditorMemento(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}

class Caretaker {
    private final Stack<EditorMemento> history = new Stack<>();

    public void saveState(TextEditor editor) {
        history.push(editor.save());
    }

    public void undo(TextEditor editor) {
        if (!history.isEmpty()) {
            editor.restore(history.pop());
        } else {
            System.out.println("Nothing to undo!");
        }
    }
}

class TextEditor {
    private String content;

    public void type(String words) {
        content = (content == null ? "" : content) + words;
    }

    public String getContent() {
        return content;
    }

    // Save current state
    public EditorMemento save() {
        return new EditorMemento(content);
    }

    // Restore to saved state
    public void restore(EditorMemento memento) {
        content = memento.getContent();
    }
}


public class MementoPatternDemo {

    public static void main(String[] args) {
        TextEditor editor = new TextEditor();
        Caretaker caretaker = new Caretaker();

        editor.type("Hello ");
        caretaker.saveState(editor);  // Save state 1

        editor.type("World!");
        caretaker.saveState(editor);  // Save state 2

        editor.type(" Goodbye!");
        System.out.println("Current: " + editor.getContent());

        caretaker.undo(editor);  // Undo to state 2
        System.out.println("After Undo 1: " + editor.getContent());

        caretaker.undo(editor);  // Undo to state 1
        System.out.println("After Undo 2: " + editor.getContent());

    }
}
