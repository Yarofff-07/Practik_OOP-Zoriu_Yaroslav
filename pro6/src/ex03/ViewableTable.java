package ex03;

import ex02.View;
import ex02.ViewableResult;

/**
 * ConcreteCreator для шаблону Factory Method.
 * Створює об'єкти типу ViewTable.
 */
public class ViewableTable extends ViewableResult {

    /**
     * Повертає новий об'єкт для відображення у вигляді таблиці.
     * @return об'єкт View
     */
    @Override
    public View getView() {
        return new ViewTable();
    }
}