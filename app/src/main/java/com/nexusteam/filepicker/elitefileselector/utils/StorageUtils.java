package com.nexusteam.filepicker.elitefileselector.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class StorageUtils {
    
    public static List<StorageVolume> getStorageVolumes(Context context) {
        List<StorageVolume> volumes = new ArrayList<>();
        
        // Internal Storage
        File internalStorage = Environment.getExternalStorageDirectory();
        if (internalStorage != null && internalStorage.exists()) {
            volumes.add(new StorageVolume(
                "Internal Storage",
                internalStorage.getAbsolutePath(),
                getAvailableSpace(internalStorage),
                getTotalSpace(internalStorage),
                true
            ));
        }
        
        // External SD Card (if available)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            File[] externalDirs = context.getExternalFilesDirs(null);
            if (externalDirs != null) {
                for (File dir : externalDirs) {
                    if (dir != null && !dir.equals(internalStorage)) {
                        String path = dir.getAbsolutePath();
                        int index = path.indexOf("/Android");
                        if (index > 0) {
                            String rootPath = path.substring(0, index);
                            File root = new File(rootPath);
                            if (root.exists()) {
                                volumes.add(new StorageVolume(
                                    "SD Card",
                                    rootPath,
                                    getAvailableSpace(root),
                                    getTotalSpace(root),
                                    false
                                ));
                            }
                        }
                    }
                }
            }
        }
        
        return volumes;
    }
    
    public static long getAvailableSpace(File file) {
        try {
            StatFs stat = new StatFs(file.getAbsolutePath());
            return stat.getAvailableBlocksLong() * stat.getBlockSizeLong();
        } catch (Exception e) {
            return 0;
        }
    }
    
    public static long getTotalSpace(File file) {
        try {
            StatFs stat = new StatFs(file.getAbsolutePath());
            return stat.getBlockCountLong() * stat.getBlockSizeLong();
        } catch (Exception e) {
            return 0;
        }
    }
    
    public static String getFormattedAvailableSpace(Context context, File file) {
        return Formatter.formatFileSize(context, getAvailableSpace(file));
    }
    
    public static class StorageVolume {
        private String name;
        private String path;
        private long availableSpace;
        private long totalSpace;
        private boolean isInternal;
        
        public StorageVolume(String name, String path, long availableSpace, long totalSpace, boolean isInternal) {
            this.name = name;
            this.path = path;
            this.availableSpace = availableSpace;
            this.totalSpace = totalSpace;
            this.isInternal = isInternal;
        }
        
        public String getName() { return name; }
        public String getPath() { return path; }
        public long getAvailableSpace() { return availableSpace; }
        public long getTotalSpace() { return totalSpace; }
        public boolean isInternal() { return isInternal; }
    }
}