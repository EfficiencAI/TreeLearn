package io.github.EfficiencAI.utils;

public class FileOperationUtil {
    public static boolean makeSurePathExists(String path){
        java.io.File dir = new java.io.File(path);
        if (!dir.exists()) {
            return dir.mkdirs();
        }else {
            return true;
        }
    }
}
