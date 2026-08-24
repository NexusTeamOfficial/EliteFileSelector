package com.nexusteam.filepicker.elitefileselector;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.nexusteam.filepicker.elitefileselector.adapters.FileAdapter;
import com.nexusteam.filepicker.elitefileselector.models.FileFilter;
import com.nexusteam.filepicker.elitefileselector.models.FileSortOption;
import com.nexusteam.filepicker.elitefileselector.utils.FileUtils;
import com.nexusteam.filepicker.elitefileselector.utils.PermissionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FilePickerActivity extends AppCompatActivity implements FileAdapter.OnFileClickListener {
    
    private RecyclerView rvFiles;
    private TextView tvCurrentPath;
    private MaterialButton btnBack;
    private Toolbar toolbar;
    
    private FileAdapter adapter;
    private List<FileItem> fileItems = new ArrayList<FileItem>();
    private String currentPath;
    private String selectionMode = "single";
    private boolean allowMultiple = false;
    private String[] allowedExtensions;
    private int maxSelectionCount = 10;
    private boolean showHiddenFiles = false;
    private FileSortOption sortOption = FileSortOption.NAME_ASC;
    private FileFilter fileFilter = FileFilter.ALL;
    private boolean enableSearch = true;
    private boolean enablePreview = true;
    private boolean showRecent = true;
    private boolean showOnlyDirectories = false;
    private boolean showOnlyFiles = false;
    private long minFileSize = 0;
    private long maxFileSize = Long.MAX_VALUE;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_picker);
        
        // Get intent extras
        getIntentData();
        
        initViews();
        setupToolbar();
        checkPermissions();
        loadFiles(currentPath);
    }
    
    private void getIntentData() {
        Intent intent = getIntent();
        currentPath = intent.getStringExtra("initial_path");
        if (currentPath == null) {
            currentPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        selectionMode = intent.getStringExtra("selection_mode");
        if (selectionMode == null) selectionMode = "single";
        allowMultiple = intent.getBooleanExtra("allow_multiple", false);
        allowedExtensions = intent.getStringArrayExtra("allowed_extensions");
        maxSelectionCount = intent.getIntExtra("max_selection_count", 10);
        showHiddenFiles = intent.getBooleanExtra("show_hidden_files", false);
        
        String sortStr = intent.getStringExtra("sort_option");
        if (sortStr != null) {
            try {
                sortOption = FileSortOption.valueOf(sortStr);
            } catch (IllegalArgumentException e) {
                sortOption = FileSortOption.NAME_ASC;
            }
        }
        
        String filterStr = intent.getStringExtra("filter_type");
        if (filterStr != null) {
            try {
                fileFilter = FileFilter.valueOf(filterStr);
            } catch (IllegalArgumentException e) {
                fileFilter = FileFilter.ALL;
            }
        }
        
        enableSearch = intent.getBooleanExtra("enable_search", true);
        enablePreview = intent.getBooleanExtra("enable_preview", true);
        showRecent = intent.getBooleanExtra("show_recent", true);
        showOnlyDirectories = intent.getBooleanExtra("show_only_dirs", false);
        showOnlyFiles = intent.getBooleanExtra("show_only_files", false);
        minFileSize = intent.getLongExtra("min_file_size", 0);
        maxFileSize = intent.getLongExtra("max_file_size", Long.MAX_VALUE);
    }
    
    private void initViews() {
        setContentView(R.layout.activity_file_picker);
        
        rvFiles = findViewById(R.id.rvFiles);
        tvCurrentPath = findViewById(R.id.tvCurrentPath);
        btnBack = findViewById(R.id.btnBack);
        toolbar = findViewById(R.id.toolbar);
        
        rvFiles.setLayoutManager(new LinearLayoutManager(this));
        
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
    }
    
    private void goBack() {
        if (!currentPath.equals("/storage/emulated/0") && !currentPath.equals("/")) {
            File parent = new File(currentPath).getParentFile();
            if (parent != null && parent.exists()) {
                loadFiles(parent.getAbsolutePath());
            }
        } else {
            Toast.makeText(this, "Already at root", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Select File");
        }
    }
    
    private void checkPermissions() {
        if (!PermissionUtils.hasStoragePermission(this)) {
            PermissionUtils.requestStoragePermission(this);
        }
    }
    
    private void loadFiles(String path) {
        currentPath = path;
        tvCurrentPath.setText(FileUtils.getReadablePath(currentPath));
        
        // Apply filter based on settings
        FileFilter filter = fileFilter;
        if (showOnlyDirectories) {
            filter = FileFilter.FOLDERS_ONLY;
        } else if (showOnlyFiles) {
            filter = FileFilter.FILES_ONLY;
        }
        
        List<FileItem> items = FileUtils.getFiles(currentPath, filter, sortOption, showHiddenFiles);
        fileItems.clear();
        
        // Apply size filter
        for (FileItem item : items) {
            if (item.getSize() >= minFileSize && item.getSize() <= maxFileSize) {
                // Apply extension filter
                if (allowedExtensions != null && allowedExtensions.length > 0 && !item.isDirectory()) {
                    String ext = item.getExtension();
                    boolean allowed = false;
                    for (String allowedExt : allowedExtensions) {
                        if (ext.equalsIgnoreCase(allowedExt)) {
                            allowed = true;
                            break;
                        }
                    }
                    if (allowed) {
                        fileItems.add(item);
                    }
                } else {
                    fileItems.add(item);
                }
            }
        }
        
        if (adapter == null) {
            adapter = new FileAdapter(this, fileItems, this);
            rvFiles.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }
    
    @Override
    public void onFileClick(FileItem fileItem) {
        if (selectionMode.equals("single")) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selected_file", fileItem);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        } else {
            // Multiple selection - toggle selection
            fileItem.setSelected(!fileItem.isSelected());
            adapter.notifyDataSetChanged();
            
            // Update toolbar title
            int selectedCount = adapter.getSelectedCount();
            if (selectedCount > 0) {
                getSupportActionBar().setTitle(selectedCount + " selected");
            } else {
                getSupportActionBar().setTitle("Select Files");
            }
        }
    }
    
    @Override
    public void onFileLongClick(FileItem fileItem) {
        // Enable selection mode on long press
        if (!selectionMode.equals("single")) {
            fileItem.setSelected(!fileItem.isSelected());
            adapter.notifyDataSetChanged();
            
            int selectedCount = adapter.getSelectedCount();
            if (selectedCount > 0) {
                getSupportActionBar().setTitle(selectedCount + " selected");
            } else {
                getSupportActionBar().setTitle("Select Files");
            }
        }
    }
    
    @Override
    public void onFolderClick(FileItem fileItem) {
        loadFiles(fileItem.getPath());
    }
    
    @Override
    public void onSelectionChanged(int count) {
        if (count > 0) {
            getSupportActionBar().setTitle(count + " selected");
        } else {
            getSupportActionBar().setTitle("Select Files");
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.file_picker_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        
        if (id == R.id.action_select_all) {
            selectAllFiles();
            return true;
        } else if (id == R.id.action_done) {
            doneSelection();
            return true;
        } else if (id == R.id.action_sort) {
            toggleSort();
            return true;
        } else if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    private void selectAllFiles() {
        if (selectionMode.equals("multiple")) {
            for (FileItem item : fileItems) {
                if (!item.isDirectory()) {
                    item.setSelected(true);
                }
            }
            adapter.notifyDataSetChanged();
            int count = adapter.getSelectedCount();
            getSupportActionBar().setTitle(count + " selected");
        }
    }
    
    private void doneSelection() {
        if (selectionMode.equals("multiple")) {
            List<FileItem> selected = adapter.getSelectedItems();
            if (selected.isEmpty()) {
                Toast.makeText(this, "No files selected", Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (selected.size() > maxSelectionCount) {
                Toast.makeText(this, "Maximum " + maxSelectionCount + " files allowed", Toast.LENGTH_SHORT).show();
                return;
            }
            
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selected_files", new ArrayList<FileItem>(selected));
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
    }
    
    private void toggleSort() {
        // Cycle through sort options
        FileSortOption[] options = FileSortOption.values();
        int currentIndex = 0;
        for (int i = 0; i < options.length; i++) {
            if (options[i] == sortOption) {
                currentIndex = i;
                break;
            }
        }
        int nextIndex = (currentIndex + 1) % options.length;
        sortOption = options[nextIndex];
        
        Toast.makeText(this, "Sort: " + sortOption.name(), Toast.LENGTH_SHORT).show();
        loadFiles(currentPath);
    }
    
    @Override
    public void onBackPressed() {
        if (!currentPath.equals("/storage/emulated/0") && !currentPath.equals("/")) {
            File parent = new File(currentPath).getParentFile();
            if (parent != null && parent.exists()) {
                loadFiles(parent.getAbsolutePath());
                return;
            }
        }
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
}