package com.nexusteam.filepicker.elitefileselector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.nexusteam.filepicker.elitefileselector.callback.FilePickerCallback;
import com.nexusteam.filepicker.elitefileselector.callback.ProgressCallback;
import com.nexusteam.filepicker.elitefileselector.models.FileFilter;
import com.nexusteam.filepicker.elitefileselector.models.FileSortOption;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EliteFilePicker {
    
    private static EliteFilePicker instance;
    private Builder builder;
    private FilePickerCallback callback;
    private Activity currentActivity;
    private static final int REQUEST_CODE_PICKER = 1001;
    
    private EliteFilePicker() {}
    
    public static EliteFilePicker getInstance() {
        if (instance == null) {
            instance = new EliteFilePicker();
        }
        return instance;
    }
    
    // For Activity (Without ActivityResultLauncher)
    public void init(Activity activity, FilePickerCallback callback) {
        this.currentActivity = activity;
        this.callback = callback;
    }
    
    public void launch(Builder builder) {
        this.builder = builder;
        Intent intent = new Intent(builder.context, FilePickerActivity.class);
        
        // Pass all configuration
        intent.putExtra("initial_path", builder.initialPath);
        intent.putExtra("selection_mode", builder.selectionMode);
        intent.putExtra("allow_multiple", builder.allowMultiple);
        intent.putExtra("allowed_extensions", builder.allowedExtensions);
        intent.putExtra("max_selection_count", builder.maxSelectionCount);
        intent.putExtra("show_hidden_files", builder.showHiddenFiles);
        intent.putExtra("show_file_size", builder.showFileSize);
        intent.putExtra("show_file_date", builder.showFileDate);
        intent.putExtra("sort_option", builder.sortOption.name());
        intent.putExtra("filter_type", builder.fileFilter.name());
        intent.putExtra("theme", builder.theme);
        intent.putExtra("enable_search", builder.enableSearch);
        intent.putExtra("enable_preview", builder.enablePreview);
        intent.putExtra("show_recent", builder.showRecent);
        intent.putExtra("show_only_dirs", builder.showOnlyDirectories);
        intent.putExtra("show_only_files", builder.showOnlyFiles);
        intent.putExtra("min_file_size", builder.minFileSize);
        intent.putExtra("max_file_size", builder.maxFileSize);
        
        if (currentActivity != null) {
            currentActivity.startActivityForResult(intent, REQUEST_CODE_PICKER);
        } else {
            builder.context.startActivity(intent);
        }
    }
    
    // Handle result in Activity's onActivityResult
    public void handleResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICKER && resultCode == Activity.RESULT_OK && data != null) {
            try {
                if (data.hasExtra("selected_files")) {
                    ArrayList<FileItem> files = (ArrayList<FileItem>) data.getSerializableExtra("selected_files");
                    if (callback != null && files != null && !files.isEmpty()) {
                        callback.onSuccess(files);
                    } else if (callback != null) {
                        callback.onCancelled();
                    }
                } else if (data.hasExtra("selected_file")) {
                    FileItem file = (FileItem) data.getSerializableExtra("selected_file");
                    if (callback != null && file != null) {
                        List<FileItem> list = new ArrayList<FileItem>();
                        list.add(file);
                        callback.onSuccess(list);
                    } else if (callback != null) {
                        callback.onCancelled();
                    }
                }
            } catch (Exception e) {
                if (callback != null) {
                    callback.onError(e);
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            if (callback != null) {
                callback.onCancelled();
            }
        }
    }
    
    public static class Builder {
        private Context context;
        private String initialPath = "/storage/emulated/0";
        private String selectionMode = "single";
        private boolean allowMultiple = false;
        private String[] allowedExtensions = null;
        private int maxSelectionCount = 10;
        private boolean showHiddenFiles = false;
        private boolean showFileSize = true;
        private boolean showFileDate = true;
        private FileSortOption sortOption = FileSortOption.NAME_ASC;
        private FileFilter fileFilter = FileFilter.ALL;
        private String theme = "light";
        private boolean enableSearch = true;
        private boolean enablePreview = true;
        private boolean showRecent = true;
        private boolean showOnlyDirectories = false;
        private boolean showOnlyFiles = false;
        private long minFileSize = 0;
        private long maxFileSize = Long.MAX_VALUE;
        private ProgressCallback progressCallback;
        
        public Builder(Context context) {
            this.context = context;
        }
        
        public Builder setInitialPath(String path) {
            this.initialPath = path;
            return this;
        }
        
        public Builder setSelectionMode(String mode) {
            this.selectionMode = mode;
            this.allowMultiple = mode.equals("multiple");
            return this;
        }
        
        public Builder setAllowedExtensions(String... extensions) {
            this.allowedExtensions = extensions;
            return this;
        }
        
        public Builder setMaxSelectionCount(int count) {
            this.maxSelectionCount = count;
            return this;
        }
        
        public Builder setShowHiddenFiles(boolean show) {
            this.showHiddenFiles = show;
            return this;
        }
        
        public Builder setSortOption(FileSortOption option) {
            this.sortOption = option;
            return this;
        }
        
        public Builder setFileFilter(FileFilter filter) {
            this.fileFilter = filter;
            return this;
        }
        
        public Builder setTheme(String theme) {
            this.theme = theme;
            return this;
        }
        
        public Builder setEnableSearch(boolean enable) {
            this.enableSearch = enable;
            return this;
        }
        
        public Builder setEnablePreview(boolean enable) {
            this.enablePreview = enable;
            return this;
        }
        
        public Builder setShowRecent(boolean show) {
            this.showRecent = show;
            return this;
        }
        
        public Builder setShowOnlyDirectories(boolean show) {
            this.showOnlyDirectories = show;
            return this;
        }
        
        public Builder setShowOnlyFiles(boolean show) {
            this.showOnlyFiles = show;
            return this;
        }
        
        public Builder setMinFileSize(long size) {
            this.minFileSize = size;
            return this;
        }
        
        public Builder setMaxFileSize(long size) {
            this.maxFileSize = size;
            return this;
        }
        
        public Builder setProgressCallback(ProgressCallback callback) {
            this.progressCallback = callback;
            return this;
        }
        
        public void launch() {
            EliteFilePicker.getInstance().launch(this);
        }
    }
}