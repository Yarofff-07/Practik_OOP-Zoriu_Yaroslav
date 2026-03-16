package ex02;

import java.io.IOException;

/**
 * Інтерфейс для відображення результатів.
 */
public interface View {
    void viewHeader();
    void viewBody();
    void viewFooter();
    void viewShow();
    void viewInit();
    void viewSave() throws IOException;
    void viewRestore() throws Exception;
}