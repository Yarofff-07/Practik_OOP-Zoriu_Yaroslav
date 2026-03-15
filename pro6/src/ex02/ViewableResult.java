package ex02;

/**
 * Фабрика для створення ViewResult.
 */
public class ViewableResult implements Viewable {

    @Override
    public View getView() {
        return new ViewResult();
    }
}