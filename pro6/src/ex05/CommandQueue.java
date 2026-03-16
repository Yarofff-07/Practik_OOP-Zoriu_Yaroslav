package ex05;

import java.util.Vector;
import ex04.Command;

/**
 * Черга команд з робочим потоком.
 * Реалізація шаблону Worker Thread.
 */
public class CommandQueue implements Queue {

    /** Черга задач */
    private Vector<Command> tasks;

    /** Ознака очікування */
    private boolean waiting;

    /** Ознака завершення */
    private boolean shutdown;

    /**
     * Конструктор.
     * Ініціалізує чергу і запускає робочий потік.
     */
    public CommandQueue() {
        tasks = new Vector<Command>();
        waiting = false;
        shutdown = false;
        new Thread(new Worker()).start();
    }

    /**
     * Завершує роботу черги.
     */
    public void shutdown() {
        shutdown = true;
        synchronized (this) {
            notifyAll();
        }
    }

    /**
     * Додає команду в чергу.
     * @param cmd команда
     */
    @Override
    public void put(Command cmd) {
        tasks.add(cmd);
        if (waiting) {
            synchronized (this) {
                notifyAll();
            }
        }
    }

    /**
     * Повертає команду з черги.
     * Якщо задач немає, потік очікує.
     * @return команда або null, якщо черга завершена
     */
    @Override
    public Command take() {
        while (tasks.isEmpty() && !shutdown) {
            synchronized (this) {
                waiting = true;
                try {
                    wait();
                } catch (InterruptedException e) {
                    waiting = false;
                    Thread.currentThread().interrupt();
                    return null;
                }
                waiting = false;
            }
        }

        if (tasks.isEmpty()) {
            return null;
        }

        return tasks.remove(0);
    }

    /**
     * Внутрішній робочий потік.
     */
    private class Worker implements Runnable {
        @Override
        public void run() {
            while (!shutdown || !tasks.isEmpty()) {
                Command command = take();
                if (command != null) {
                    command.execute();
                }
            }
        }
    }
}