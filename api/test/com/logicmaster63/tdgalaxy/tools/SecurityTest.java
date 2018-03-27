package com.logicmaster63.tdgalaxy.tools;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class SecurityTest {

    @Test
    void test() {
        Throwable throwable = assertThrows(SecurityException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                Security.ensurePermission();
            }
        });
        assertEquals("org.junit.jupiter.api.AssertThrows is not authorized to access \"execute\"", throwable.getMessage(), "ensurePermission() leaked permission");

        final AtomicBoolean errored = new AtomicBoolean(false);
        final Thread thread = new Thread() {
            @Override
            public StackTraceElement[] getStackTrace() {
                return new StackTraceElement[]{null, null, new StackTraceElement("", "loadAudios", "", 0), new StackTraceElement("com.logicmaster63.tdgalaxy.TDGalaxy", "", "", 0)};
            }
            @Override
            public void run() {
                Security.ensurePermission();
            }
        };
        thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                errored.set(true);
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertFalse(errored.get(), "ensurePermission() failed with correct stack");
    }
}