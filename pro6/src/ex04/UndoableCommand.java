package ex04;

public interface UndoableCommand extends Command {
    void undo();
}