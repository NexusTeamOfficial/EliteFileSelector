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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.nexusteam.filepicker.elitefileselector.callback.FilePickerCallback;
import com.nexusteam.filepicker.elitefileselector.callback.ProgressCallback;
import com.nexusteam.filepicker.elitefileselector.models.FileFilter;
import com.nexusteam.filepicker.elitefileselector.models.FileSortOption;
import com.nexusteam.filepicker.elitefileselector.utils.PermissionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FilePickerCallback {
    
    private static final int REQUEST_PERMISSIONS = 100;
    
    private MaterialButton btnOpenFilePicker;
    private TextView tvSelectedFile, tvFileInfo;
    private ProgressBar progressBar;
    private CircularProgressIndicator progressIndicator;
    private RecyclerView rvSelectedFiles;
    private SelectedFileAdapter selectedFileAdapter;
    private ChipGroup chipGroupFilter, chipGroupSort;
    
    private EliteFilePicker filePicker;
    private List<FileItem> selectedFiles = new ArrayList<FileItem>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        initViews();
        checkPermissions();
        setupFilePicker();
        setupListeners();
    }
    
    private void initViews() {
        btnOpenFilePicker = findViewById(R.id.btnOpenFilePicker);
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
    
    private void setupFilePicker() {
        filePicker = EliteFilePicker.getInstance();
        filePicker.init(this, this);
    }
    
    private void setupListeners() {
        btnOpenFilePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilePicker();
            }
        });
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
    
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ - Check granular permissions
            List<String> missingPermissions = new ArrayList<String>();
            
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
            .setPositiveButton("Open Settings", null)
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
    
    // FilePickerCallback implementations
    @Override
    public void onSuccess(List<FileItem> files) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
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
                
                Toast.makeText(MainActivity.this, "Selected " + files.size() + " files", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    @Override
    public void onError(Exception error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvSelectedFile.setText("Error: " + error.getMessage());
                Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    
    @Override
    public void onCancelled() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvSelectedFile.setText("Selection cancelled");
                Toast.makeText(MainActivity.this, "Selection cancelled", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    @Override
    public void onPermissionDenied(String permission) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvSelectedFile.setText("Permission denied: " + permission);
                showPermissionDeniedDialog();
            }
        });
    }
}