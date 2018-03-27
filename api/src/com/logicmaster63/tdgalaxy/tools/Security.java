package com.logicmaster63.tdgalaxy.tools;

public class Security {

    public static void main(String[] args) {
        SoundHandler.get().loadAudios(null);
    }

    private static final String[] APPROVED_CLASSES = new String[] {"TDGalaxy", "tools.FileHandler"};

    /**
     * Ensure that the caller of the method that called this method has permission to access it
     */
    public static void ensurePermission() {
        StringBuilder builder = new StringBuilder("com.logicmaster63.tdgalaxy.");
        for(String string: APPROVED_CLASSES) {
            builder.append(string);
            if(Thread.currentThread().getStackTrace()[3].getClassName().equals(builder.toString()))
                return;
            builder.delete(27, builder.length());
        }
        throw new SecurityException(Thread.currentThread().getStackTrace()[3].getClassName() + " is not authorized to access \"" + Thread.currentThread().getStackTrace()[2].getMethodName() + "\"");
    }
}
