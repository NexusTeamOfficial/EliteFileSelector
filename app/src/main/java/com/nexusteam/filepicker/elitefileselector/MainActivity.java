package com.nexusteam.filepicker.elitefileselector;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.nexusteam.filepicker.elitefileselector.callback.FilePickerCallback;
import com.nexusteam.filepicker.elitefileselector.callback.FileSelectionListener;
import com.nexusteam.filepicker.elitefileselector.callback.ProgressCallback;
import com.nexusteam.filepicker.elitefileselector.dialogs.FilePickerBottomSheet;
import com.nexusteam.filepicker.elitefileselector.dialogs.FilePickerDialog;
import com.nexusteam.filepicker.elitefileselector.fragments.FilePickerFragment;
import com.nexusteam.filepicker.elitefileselector.models.FileFilter;
import com.nexusteam.filepicker.elitefileselector.models.FileSortOption;
import com.nexusteam.filepicker.elitefileselector.utils.PermissionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FilePickerCallback, FileSelectionListener {
    
    private static final int REQUEST_PERMISSIONS = 100;
    private static final String TAG_FRAGMENT = "file_picker_fragment";
    
    // Views
    private MaterialButton btnOpenFilePicker;
    private MaterialButton btnOpenDialog;
    private MaterialButton btnOpenBottomSheet;
    private MaterialButton btnOpenFragment;
    private TextView tvSelectedFile, tvFileInfo;
    private ProgressBar progressBar;
    private CircularProgressIndicator progressIndicator;
    private RecyclerView rvSelectedFiles;
    private SelectedFileAdapter selectedFileAdapter;
    private ChipGroup chipGroupFilter, chipGroupSort;
    
    private EliteFilePicker filePicker;
    private List<FileItem> selectedFiles = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        initViews();
        setupChips();
        checkPermissions();
        setupFilePicker();
        setupListeners();
    }
    
    private void initViews() {
        btnOpenFilePicker = findViewById(R.id.btnOpenFilePicker);
        btnOpenDialog = findViewById(R.id.btnOpenDialog);
        btnOpenBottomSheet = findViewById(R.id.btnOpenBottomSheet);
        btnOpenFragment = findViewById(R.id.btnOpenFragment);
        tvSelectedFile = findViewById(R.id.tvSelectedFile);
        tvFileInfo = findViewById(R.id.tvFileInfo);
        progressBar = findViewById(R.id.progressBar);
        progressIndicator = findViewById(R.id.progressIndicator);
        rvSelectedFiles = findViewById(R.id.rvSelectedFiles);
        chipGroupFilter = findViewById(R.id.chipGroupFilter);
        chipGroupSort = findViewById(R.id.chipGroupSort);
        
        rvSelectedFiles.setLayoutManager(new LinearLayoutManager(this));
        selectedFileAdapter = new SelectedFileAdapter(this, selectedFiles);
        rvSelectedFiles.setAdapter(selectedFileAdapter);
    }
    
    private void setupChips() {
        // Filter Chips Setup
        String[] filterOptions = {"All", "Images", "Videos", "Audio", "Documents", "APK"};
        for (int i = 0; i < chipGroupFilter.getChildCount() && i < filterOptions.length; i++) {
            Chip chip = (Chip) chipGroupFilter.getChildAt(i);
            if (chip != null) {
                chip.setText(filterOptions[i]);
                chip.setCheckable(true);
                if (i == 0) chip.setChecked(true);
            }
        }
        
        // Sort Chips Setup
        String[] sortOptions = {"Name ↑", "Name ↓", "Size ↑", "Size ↓", "Date ↑", "Date ↓", "Type"};
        for (int i = 0; i < chipGroupSort.getChildCount() && i < sortOptions.length; i++) {
            Chip chip = (Chip) chipGroupSort.getChildAt(i);
            if (chip != null) {
                chip.setText(sortOptions[i]);
                chip.setCheckable(true);
                if (i == 0) chip.setChecked(true);
            }
        }
    }
    
    private void setupFilePicker() {
        filePicker = EliteFilePicker.getInstance();
        filePicker.init(this, this);
    }
    
    private void setupListeners() {
        // Open Activity File Picker
        btnOpenFilePicker.setOnClickListener(v -> openFilePicker());
        
        // Open Dialog
        btnOpenDialog.setOnClickListener(v -> openFilePickerDialog());
        
        // Open Bottom Sheet
        btnOpenBottomSheet.setOnClickListener(v -> openFilePickerBottomSheet());
        
        // Open Fragment
        btnOpenFragment.setOnClickListener(v -> openFilePickerFragment());
    }
    
    private void openFilePicker() {
        if (!PermissionUtils.hasStoragePermission(this)) {
            PermissionUtils.requestStoragePermission(this);
            return;
        }
        
        EliteFilePicker.Builder builder = new EliteFilePicker.Builder(this)
            .setInitialPath(Environment.getExternalStorageDirectory().getAbsolutePath())
            .setSelectionMode("multiple")
            .setAllowedExtensions("pdf", "doc", "docx", "txt", "jpg", "png", "mp4", "mp3")
            .setMaxSelectionCount(10)
            .setShowHiddenFiles(false)
            .setSortOption(FileSortOption.NAME_ASC)
            .setFileFilter(FileFilter.ALL)
            .setTheme("light")
            .setEnableSearch(true)
            .setEnablePreview(true)
            .setShowRecent(true)
            .setShowOnlyDirectories(false)
            .setShowOnlyFiles(false)
            .setMinFileSize(0)
            .setMaxFileSize(100 * 1024 * 1024); // 100 MB
        
        builder.launch();
    }
    
    private void openFilePickerDialog() {
        if (!PermissionUtils.hasStoragePermission(this)) {
            PermissionUtils.requestStoragePermission(this);
            return;
        }
        
        FilePickerDialog dialog = FilePickerDialog.newInstance(
            "Select Files",
            "multiple",
            10,
            new String[]{"pdf", "jpg", "png", "mp4", "mp3"}
        );
        dialog.setFileSelectionListener(this);
        dialog.show(getSupportFragmentManager(), "file_picker_dialog");
    }
    
    private void openFilePickerBottomSheet() {
        if (!PermissionUtils.hasStoragePermission(this)) {
            PermissionUtils.requestStoragePermission(this);
            return;
        }
        
        FilePickerBottomSheet bottomSheet = FilePickerBottomSheet.newInstance(
            "Choose Files",
            "multiple"
        );
        bottomSheet.setFileSelectionListener(this);
        bottomSheet.show(getSupportFragmentManager(), "file_picker_bottom_sheet");
    }
    
    private void openFilePickerFragment() {
        if (!PermissionUtils.hasStoragePermission(this)) {
            PermissionUtils.requestStoragePermission(this);
            return;
        }
        
        // Remove any existing fragment
        FragmentManager fm = getSupportFragmentManager();
        FilePickerFragment existingFragment = (FilePickerFragment) fm.findFragmentByTag(TAG_FRAGMENT);
        if (existingFragment != null) {
            fm.beginTransaction().remove(existingFragment).commit();
        }
        
        FilePickerFragment fragment = FilePickerFragment.newInstance(
            Environment.getExternalStorageDirectory().getAbsolutePath(),
            "multiple",
            10,
            new String[]{"pdf", "jpg", "png", "mp4", "mp3"}
        );
        fragment.setFileSelectionListener(this);
        
        fm.beginTransaction()
            .replace(R.id.fragmentContainer, fragment, TAG_FRAGMENT)
            .addToBackStack(null)
            .commit();
        
        // Show fragment container
        findViewById(R.id.fragmentContainer).setVisibility(View.VISIBLE);
    }
    
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            List<String> missingPermissions = new ArrayList<>();
            
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) 
                != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(Manifest.permission.READ_MEDIA_IMAGES);
            }
            
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) 
                != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(Manifest.permission.READ_MEDIA_VIDEO);
            }
            
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO) 
                != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(Manifest.permission.READ_MEDIA_AUDIO);
            }
            
            if (!missingPermissions.isEmpty()) {
                String[] permissions = missingPermissions.toArray(new String[0]);
                ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS);
            }
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) 
                   != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, 
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 
                REQUEST_PERMISSIONS);
        }
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (requestCode == REQUEST_PERMISSIONS) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
            
            if (allGranted) {
                Toast.makeText(this, "Permissions granted!", Toast.LENGTH_SHORT).show();
            } else {
                showPermissionDeniedDialog();
            }
        }
    }
    
    private void showPermissionDeniedDialog() {
        new AlertDialog.Builder(this)
            .setTitle("Permission Required")
            .setMessage("Storage permission is required to access files. Please grant permission in settings.")
            .setPositiveButton("Open Settings", (dialog, which) -> {
                // Open app settings
                Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(android.net.Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            })
            .setNegativeButton("Cancel", null)
            .show();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (filePicker != null) {
            filePicker.handleResult(requestCode, resultCode, data);
        }
    }
    
    // ===== FilePickerCallback Implementation (for Activity) =====
    @Override
    public void onSuccess(List<FileItem> files) {
        runOnUiThread(() -> {
            updateSelectedFiles(files);
            Toast.makeText(MainActivity.this, "Selected " + files.size() + " files", Toast.LENGTH_SHORT).show();
        });
    }
    
    @Override
    public void onError(Exception error) {
        runOnUiThread(() -> {
            tvSelectedFile.setText("Error: " + error.getMessage());
            Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
        });
    }
    
    @Override
    public void onCancelled() {
        runOnUiThread(() -> {
            tvSelectedFile.setText("Selection cancelled");
            Toast.makeText(MainActivity.this, "Selection cancelled", Toast.LENGTH_SHORT).show();
        });
    }
    
    @Override
    public void onPermissionDenied(String permission) {
        runOnUiThread(() -> {
            tvSelectedFile.setText("Permission denied: " + permission);
            showPermissionDeniedDialog();
        });
    }
    
    // ===== FileSelectionListener Implementation (for Dialog, Bottom Sheet, Fragment) =====
    @Override
    public void onFileSelected(FileItem file) {
        runOnUiThread(() -> {
            List<FileItem> singleFile = new ArrayList<>();
            singleFile.add(file);
            updateSelectedFiles(singleFile);
            Toast.makeText(this, "Selected: " + file.getName(), Toast.LENGTH_SHORT).show();
        });
    }
    
    @Override
    public void onFilesSelected(List<FileItem> files) {
        runOnUiThread(() -> {
            updateSelectedFiles(files);
            Toast.makeText(this, "Selected " + files.size() + " files", Toast.LENGTH_SHORT).show();
        });
    }
    
    @Override
    public void onSelectionChanged(List<FileItem> selectedFiles) {
        runOnUiThread(() -> {
            // Update UI to show selection count
            tvFileInfo.setText("Selected: " + selectedFiles.size() + " files");
        });
    }
    
    @Override
    public void onSelectionComplete(List<FileItem> selectedFiles) {
        runOnUiThread(() -> {
            updateSelectedFiles(selectedFiles);
            Toast.makeText(this, "Selection complete! " + selectedFiles.size() + " files selected", 
                Toast.LENGTH_SHORT).show();
            
            // Hide fragment container if visible
            findViewById(R.id.fragmentContainer).setVisibility(View.GONE);
            
            // Remove fragment from back stack
            FragmentManager fm = getSupportFragmentManager();
            FilePickerFragment fragment = (FilePickerFragment) fm.findFragmentByTag(TAG_FRAGMENT);
            if (fragment != null) {
                fm.beginTransaction().remove(fragment).commit();
            }
        });
    }
    
    @Override
    public void onSelectionCancelled() {
        runOnUiThread(() -> {
            tvSelectedFile.setText("Selection cancelled");
            Toast.makeText(this, "Selection cancelled", Toast.LENGTH_SHORT).show();
            
            // Hide fragment container if visible
            findViewById(R.id.fragmentContainer).setVisibility(View.GONE);
        });
    }
    
    // ===== Helper Methods =====
    private void updateSelectedFiles(List<FileItem> files) {
        selectedFiles.clear();
        selectedFiles.addAll(files);
        selectedFileAdapter.notifyDataSetChanged();
        
        StringBuilder sb = new StringBuilder();
        for (FileItem file : files) {
            sb.append(file.getName()).append("\n");
            sb.append("Size: ").append(file.getFormattedSize()).append("\n");
            sb.append("Path: ").append(file.getPath()).append("\n");
            sb.append("---\n");
        }
        tvSelectedFile.setText(sb.toString());
        
        // Update info
        tvFileInfo.setText("Total selected: " + files.size() + " files | Total size: " + getTotalSize(files));
    }
    
    private String getTotalSize(List<FileItem> files) {
        long totalSize = 0;
        for (FileItem file : files) {
            totalSize += file.getSize();
        }
        return android.text.format.Formatter.formatFileSize(this, totalSize);
    }
    
    @Override
    public void onBackPressed() {
        // If fragment container is visible, hide it
        if (findViewById(R.id.fragmentContainer).getVisibility() == View.VISIBLE) {
            findViewById(R.id.fragmentContainer).setVisibility(View.GONE);
            FragmentManager fm = getSupportFragmentManager();
            FilePickerFragment fragment = (FilePickerFragment) fm.findFragmentByTag(TAG_FRAGMENT);
            if (fragment != null) {
                fm.beginTransaction().remove(fragment).commit();
            }
            return;
        }
        super.onBackPressed();
    }
}