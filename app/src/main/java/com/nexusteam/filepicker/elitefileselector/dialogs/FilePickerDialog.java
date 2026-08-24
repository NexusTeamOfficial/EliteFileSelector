package com.nexusteam.filepicker.elitefileselector.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.nexusteam.filepicker.elitefileselector.FileItem;
import com.nexusteam.filepicker.elitefileselector.R;
import com.nexusteam.filepicker.elitefileselector.adapters.FileAdapter;
import com.nexusteam.filepicker.elitefileselector.callback.FileSelectionListener;
import com.nexusteam.filepicker.elitefileselector.models.FileFilter;
import com.nexusteam.filepicker.elitefileselector.models.FileSortOption;
import com.nexusteam.filepicker.elitefileselector.utils.FileUtils;
import com.nexusteam.filepicker.elitefileselector.utils.PermissionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FilePickerDialog extends AppCompatDialogFragment implements FileAdapter.OnFileClickListener {
    
    private RecyclerView rvFiles;
    private TextView tvCurrentPath, tvTitle;
    private MaterialButton btnCancel, btnDone;
    
    private FileAdapter adapter;
    private List<FileItem> fileItems = new ArrayList<>();
    private String currentPath;
    private String selectionMode = "single";
    private String title = "Select Files";
    private String[] allowedExtensions;
    private int maxSelectionCount = 10;
    private boolean showHiddenFiles = false;
    private FileSortOption sortOption = FileSortOption.NAME_ASC;
    private FileFilter fileFilter = FileFilter.ALL;
    
    private FileSelectionListener selectionListener;
    private List<FileItem> selectedFiles = new ArrayList<>();
    
    public static FilePickerDialog newInstance(String title, String selectionMode, 
                                               int maxSelection, String[] extensions) {
        FilePickerDialog dialog = new FilePickerDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("selection_mode", selectionMode);
        args.putInt("max_selection", maxSelection);
        args.putStringArray("allowed_extensions", extensions);
        dialog.setArguments(args);
        return dialog;
    }
    
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_file_picker, null);
        
        if (getArguments() != null) {
            title = getArguments().getString("title", "Select Files");
            selectionMode = getArguments().getString("selection_mode", "single");
            maxSelectionCount = getArguments().getInt("max_selection", 10);
            allowedExtensions = getArguments().getStringArray("allowed_extensions");
        }
        
        currentPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        initViews(view);
        setupListeners();
        loadFiles(currentPath);
        
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        
        return dialog;
    }
    
    private void initViews(View view) {
        rvFiles = view.findViewById(R.id.rvFiles);
        tvCurrentPath = view.findViewById(R.id.tvCurrentPath);
        tvTitle = view.findViewById(R.id.tvTitle);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnDone = view.findViewById(R.id.btnDone);
        
        tvTitle.setText(title);
        rvFiles.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FileAdapter(getContext(), fileItems, this);
        rvFiles.setAdapter(adapter);
    }
    
    private void setupListeners() {
        btnCancel.setOnClickListener(v -> dismiss());
        btnDone.setOnClickListener(v -> {
            if (selectionMode.equals("multiple")) {
                if (selectedFiles.isEmpty()) {
                    Toast.makeText(getContext(), "No files selected", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (selectionListener != null) {
                    selectionListener.onSelectionComplete(selectedFiles);
                }
                dismiss();
            }
        });
    }
    
    private void loadFiles(String path) {
        if (!PermissionUtils.hasStoragePermission(requireContext())) {
            PermissionUtils.requestStoragePermission(requireActivity());
            return;
        }
        
        currentPath = path;
        tvCurrentPath.setText(FileUtils.getReadablePath(currentPath));
        
        List<FileItem> items = FileUtils.getFiles(currentPath, fileFilter, sortOption, showHiddenFiles);
        fileItems.clear();
        fileItems.addAll(items);
        adapter.notifyDataSetChanged();
    }
    
    @Override
    public void onFileClick(FileItem fileItem) {
        if (selectionMode.equals("single")) {
            if (selectionListener != null) {
                selectionListener.onFileSelected(fileItem);
            }
            dismiss();
        } else {
            toggleSelection(fileItem);
        }
    }
    
    @Override
    public void onFileLongClick(FileItem fileItem) {
        if (selectionMode.equals("multiple")) {
            toggleSelection(fileItem);
        }
    }
    
    @Override
    public void onFolderClick(FileItem fileItem) {
        loadFiles(fileItem.getPath());
    }
    
    @Override
    public void onSelectionChanged(int count) {
        updateTitle(count);
    }
    
    private void toggleSelection(FileItem fileItem) {
        if (fileItem.isDirectory()) return;
        
        if (fileItem.isSelected()) {
            fileItem.setSelected(false);
            selectedFiles.remove(fileItem);
        } else {
            if (selectedFiles.size() >= maxSelectionCount) {
                Toast.makeText(getContext(), "Maximum " + maxSelectionCount + " files allowed", 
                    Toast.LENGTH_SHORT).show();
                return;
            }
            fileItem.setSelected(true);
            selectedFiles.add(fileItem);
        }
        
        adapter.notifyDataSetChanged();
        updateTitle(selectedFiles.size());
        
        if (selectionListener != null) {
            selectionListener.onSelectionChanged(selectedFiles);
        }
    }
    
    private void updateTitle(int count) {
        if (count > 0) {
            tvTitle.setText(title + " (" + count + ")");
        } else {
            tvTitle.setText(title);
        }
    }
    
    public void setFileSelectionListener(FileSelectionListener listener) {
        this.selectionListener = listener;
    }
}