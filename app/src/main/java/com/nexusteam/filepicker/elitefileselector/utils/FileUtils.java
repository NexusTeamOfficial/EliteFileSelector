package com.nexusteam.filepicker.elitefileselector.utils;

import android.content.Context;
import android.webkit.MimeTypeMap;

import com.nexusteam.filepicker.elitefileselector.FileItem;
import com.nexusteam.filepicker.elitefileselector.R;
import com.nexusteam.filepicker.elitefileselector.models.FileFilter;
import com.nexusteam.filepicker.elitefileselector.models.FileSortOption;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FileUtils {
    
    public static List<FileItem> getFiles(String directoryPath, FileFilter filter, 
                                          FileSortOption sortOption, boolean showHidden) {
        List<FileItem> fileItems = new ArrayList<FileItem>();
        File directory = new File(directoryPath);
        
        if (!directory.exists() || !directory.isDirectory()) {
            return fileItems;
        }
        
        try {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    // Check hidden files
                    if (!showHidden && file.isHidden()) {
                        continue;
                    }
                    
                    FileItem item = new FileItem(file);
                    
                    // Apply filter
                    if (applyFilter(item, filter)) {
                        fileItems.add(item);
                    }
                }
                
                // Apply sorting
                applySorting(fileItems, sortOption);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        
        return fileItems;
    }
    
    private static boolean applyFilter(FileItem item, FileFilter filter) {
        switch (filter) {
            case ALL:
                return true;
            case FILES_ONLY:
                return !item.isDirectory();
            case FOLDERS_ONLY:
                return item.isDirectory();
            case IMAGES:
                return !item.isDirectory() && isImageFile(item.getName());
            case VIDEOS:
                return !item.isDirectory() && isVideoFile(item.getName());
            case AUDIO:
                return !item.isDirectory() && isAudioFile(item.getName());
            case DOCUMENTS:
                return !item.isDirectory() && isDocumentFile(item.getName());
            case APK:
                return !item.isDirectory() && item.getName().toLowerCase().endsWith(".apk");
            default:
                return true;
        }
    }
    
    private static void applySorting(List<FileItem> items, FileSortOption sortOption) {
        switch (sortOption) {
            case NAME_ASC:
                Collections.sort(items, new Comparator<FileItem>() {
                    @Override
                    public int compare(FileItem f1, FileItem f2) {
                        return f1.getName().compareToIgnoreCase(f2.getName());
                    }
                });
                break;
            case NAME_DESC:
                Collections.sort(items, new Comparator<FileItem>() {
                    @Override
                    public int compare(FileItem f1, FileItem f2) {
                        return f2.getName().compareToIgnoreCase(f1.getName());
                    }
                });
                break;
            case SIZE_ASC:
                Collections.sort(items, new Comparator<FileItem>() {
                    @Override
                    public int compare(FileItem f1, FileItem f2) {
                        return Long.compare(f1.getSize(), f2.getSize());
                    }
                });
                break;
            case SIZE_DESC:
                Collections.sort(items, new Comparator<FileItem>() {
                    @Override
                    public int compare(FileItem f1, FileItem f2) {
                        return Long.compare(f2.getSize(), f1.getSize());
                    }
                });
                break;
            case DATE_ASC:
                Collections.sort(items, new Comparator<FileItem>() {
                    @Override
                    public int compare(FileItem f1, FileItem f2) {
                        return Long.compare(f1.getLastModified(), f2.getLastModified());
                    }
                });
                break;
            case DATE_DESC:
                Collections.sort(items, new Comparator<FileItem>() {
                    @Override
                    public int compare(FileItem f1, FileItem f2) {
                        return Long.compare(f2.getLastModified(), f1.getLastModified());
                    }
                });
                break;
            case TYPE:
                Collections.sort(items, new Comparator<FileItem>() {
                    @Override
                    public int compare(FileItem f1, FileItem f2) {
                        if (f1.isDirectory() && !f2.isDirectory()) return -1;
                        if (!f1.isDirectory() && f2.isDirectory()) return 1;
                        return f1.getExtension().compareToIgnoreCase(f2.getExtension());
                    }
                });
                break;
        }
    }
    
    public static String getFileExtension(String fileName) {
        int lastDot = fileName.lastIndexOf(".");
        if (lastDot > 0 && lastDot < fileName.length() - 1) {
            return fileName.substring(lastDot + 1).toLowerCase();
        }
        return "";
    }
    
    public static String getMimeType(String fileName) {
        String extension = getFileExtension(fileName);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }
    
    public static boolean isImageFile(String fileName) {
        String ext = getFileExtension(fileName);
        return ext.matches("jpg|jpeg|png|gif|bmp|webp|svg");
    }
    
    public static boolean isVideoFile(String fileName) {
        String ext = getFileExtension(fileName);
        return ext.matches("mp4|avi|mkv|3gp|mov|wmv|flv");
    }
    
    public static boolean isAudioFile(String fileName) {
        String ext = getFileExtension(fileName);
        return ext.matches("mp3|wav|aac|flac|ogg|m4a");
    }
    
    public static boolean isDocumentFile(String fileName) {
        String ext = getFileExtension(fileName);
        return ext.matches("pdf|doc|docx|xls|xlsx|ppt|pptx|txt|csv|rtf");
    }
    
    public static String formatFileSize(long size) {
        if (size <= 0) return "0 B";
        final String[] units = {"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return String.format(Locale.getDefault(), "%.1f %s", 
            size / Math.pow(1024, digitGroups), units[digitGroups]);
    }
    
    public static String formatDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }
    
    public static String getReadablePath(String path) {
        if (path == null) return "/";
        
        if (path.startsWith("/storage/emulated/0")) {
            return "Internal Storage" + path.substring("/storage/emulated/0".length());
        } else if (path.startsWith("/storage")) {
            return "Storage" + path.substring("/storage".length());
        }
        return path;
    }
    
    // Get file icon resource
    public static int getFileIcon(Context context, File file) {
        if (file.isDirectory()) {
            return R.drawable.ic_folder;
        }
        
        String extension = getFileExtension(file.getName());
        if (extension.equals("pdf")) return R.drawable.ic_pdf;
        if (extension.matches("doc|docx")) return R.drawable.ic_doc;
        if (extension.matches("xls|xlsx")) return R.drawable.ic_excel;
        if (extension.matches("ppt|pptx")) return R.drawable.ic_ppt;
        if (extension.matches("jpg|jpeg|png|gif|bmp|webp")) return R.drawable.ic_image;
        if (extension.matches("mp3|wav|aac|flac")) return R.drawable.ic_audio;
        if (extension.matches("mp4|avi|mkv|3gp")) return R.drawable.ic_video;
        if (extension.equals("apk")) return R.drawable.ic_apk;
        if (extension.matches("zip|rar|7z")) return R.drawable.ic_zip;
        
        return R.drawable.ic_file;
    }
}