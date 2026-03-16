package ex04;

import java.util.ArrayList;

import ex01.Item2d;
import ex02.View;
import ex02.ViewResult;

public class ChangeConsoleCommand implements ConsoleCommand, UndoableCommand {
    private View view;
    private ArrayList<Item2d> itemsBefore = new ArrayList<Item2d>();
    private double offset = 2.0;

    public ChangeConsoleCommand(View view) {
        this.view = view;
    }

    @Override
    public void execute() {
        itemsBefore.clear();

        ArrayList<Item2d> items = ((ViewResult) view).getItems();

        if (items.size() == 0) {
            System.out.println("No data to change.");
            return;
        }

        for (Item2d item : items) {
            Item2d copy = new Item2d();
            copy.setXY(item.getX(), item.getY());
            itemsBefore.add(copy);

            item.setY(item.getY() * offset);
        }

        System.out.println("Data changed.");
        view.viewShow();

        Application.getInstance().addToHistory(this);
    }

    @Override
    public void undo() {
        ArrayList<Item2d> items = ((ViewResult) view).getItems();

        for (int i = 0; i < items.size() && i < itemsBefore.size(); i++) {
            items.get(i).setXY(itemsBefore.get(i).getX(), itemsBefore.get(i).getY());
        }

        System.out.println("Last change canceled.");
        view.viewShow();
    }

    @Override
    public char getKey() {
        return 'c';
    }

    @Override
    public String toString() {
        return "'c' - change data";
    }
}