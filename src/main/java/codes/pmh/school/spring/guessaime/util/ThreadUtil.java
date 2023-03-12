package codes.pmh.school.spring.guessaime.util;

import java.util.List;

public class ThreadUtil {
    public static void runThreads (List<Thread> threads) {
        for (Thread thread : threads)
            thread.start();
    }

    public static void waitThreads (List<Thread> threads) {
        for (Thread thread : threads)
            while (thread.isAlive());
    }
}
