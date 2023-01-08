package com.rhul.fyp.asecav.Hypatia;

import android.os.FileObserver;

import java.io.File;
import java.util.HashSet;
import java.util.Stack;

/**
 * Copyright (C) 2012 Bartek Przybylski
 * Copyright (C) 2015 ownCloud Inc.
 * Copyright (C) 2016 Daniel Gultsch
 * Taken from siacs/Conversations and tweaked a bit
 */

/**
 * Observes and monitors all files in a given directory. Automatically starts monitoring new folders/files if created after the starting the process.
 */
abstract class RecursiveFileObserver {

    private final String path;
    private final HashSet<SingleFileObserver> mObservers = new HashSet<>();

    public RecursiveFileObserver(String path) {
        this.path = path;
    }

    public final synchronized void startWatching() {
        Stack<String> stack = new Stack<>();
        stack.push(path);

        while (!stack.empty()) {
            String parent = stack.pop();
            mObservers.add(new SingleFileObserver(parent));
            final File path = new File(parent);
            final File[] files = path.listFiles();
            if (files == null) {
                continue;
            }
            for (File file : files) {
                if (file.isDirectory() && !file.getName().equals(".") && !file.getName().equals("..")) {
                    final String currentPath = file.getAbsolutePath();
                    if (depth(file) <= 8 && !stack.contains(currentPath) && !observing(currentPath)) {
                        stack.push(currentPath);
                    }
                }
            }
        }
        for (FileObserver observer : mObservers) {
            observer.startWatching();
        }
    }

    private static int depth(File file) {
        int depth = 0;
        while ((file = file.getParentFile()) != null) {
            depth++;
        }
        return depth;
    }

    private boolean observing(String path) {
        for (SingleFileObserver observer : mObservers) {
            if (path.equals(observer.path)) {
                return true;
            }
        }
        return false;
    }

    public final synchronized void stopWatching() {
        for (FileObserver observer : mObservers) {
            observer.stopWatching();
        }
        mObservers.clear();
    }

    abstract public void onEvent(int event, String path);

    private class SingleFileObserver extends FileObserver {

        private final String path;

        public SingleFileObserver(String path) {
            super(path);
            this.path = path;
        }

        @Override
        public final void onEvent(int event, String filename) {
            RecursiveFileObserver.this.onEvent(event, path + '/' + filename);
        }

    }
}