package ex05;

import ex04.Command;

/**
 * Інтерфейс черги задач.
 * Використовується в шаблоні Worker Thread.
 */
public interface Queue {
    /**
     * Додає команду в чергу.
     * @param cmd команда
     */
    void put(Command cmd);

    /**
     * Забирає команду з черги.
     * @return команда або null, якщо роботу завершено
     */
    Command take();
}