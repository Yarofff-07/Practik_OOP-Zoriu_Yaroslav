package ex04;

import ex01.Item2d;

public class ChangeItemCommand implements UndoableCommand {
    private Item2d item;
    private double offset;

    private double oldX;
    private double oldY;

    public void setItem(Item2d item) {
        this.item = item;
    }

    public Item2d getItem() {
        return item;
    }

    public void setOffset(double offset) {
        this.offset = offset;
    }

    public double getOffset() {
        return offset;
    }

    @Override
    public void execute() {
        if (item == null) {
            return;
        }

        oldX = item.getX();
        oldY = item.getY();

        item.setY(item.getY() * offset);
    }

    @Override
    public void undo() {
        if (item == null) {
            return;
        }

        item.setXY(oldX, oldY);
    }
}