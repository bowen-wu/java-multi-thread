package com.github.hcsp.multithread;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MultiThreadWordCount2 {
    private static Map<String, Integer> resultMap = new ConcurrentHashMap<>();

    public static Map<String, Integer> count(int threadNum, List<File> files) {
        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
        for (File file : files) {
            Future submit = executorService.submit(new Temp(file));
            try {
                submit.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

        return resultMap;
    }

    private static class Temp implements Callable {
        private final File FILE;

        Temp(File file) {
            this.FILE = file;
        }

        @Override
        public Map<String, Integer> call() {
            return ProcessFile.convertWordsInFileToMap(FILE, resultMap);
        }
    }
}
