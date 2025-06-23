package io.github.EfficiencAI.utils;

import java.io.File;

public class FileOperationUtil {
    public static boolean makeSurePathExists(String path){
        File dir = new File(path);
        return dir.exists() || dir.mkdirs();
    }
}
