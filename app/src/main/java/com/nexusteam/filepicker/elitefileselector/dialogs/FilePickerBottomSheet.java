package com.nexusteam.filepicker.elitefileselector.dialogs;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
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

public class FilePickerBottomSheet extends BottomSheetDialogFragment 
        implements FileAdapter.OnFileClickListener {
    
    private RecyclerView rvFiles;
    private TextView tvCurrentPath, tvTitle;
    private MaterialButton btnCancel, btnDone;
    
    private FileAdapter adapter;
    private List<FileItem> fileItems = new ArrayList<>();
    private String currentPath;
    private String selectionMode = "single";
    private String title = "Select Files";
    private int maxSelectionCount = 10;
    
    private FileSelectionListener selectionListener;
    private List<FileItem> selectedFiles = new ArrayList<>();
    
    public static FilePickerBottomSheet newInstance(String title, String selectionMode) {
        FilePickerBottomSheet sheet = new FilePickerBottomSheet();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("selection_mode", selectionMode);
        sheet.setArguments(args);
        return sheet;
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_file_picker, container, false);
        
        if (getArguments() != null) {
            title = getArguments().getString("title", "Select Files");
            selectionMode = getArguments().getString("selection_mode", "single");
        }
        
        currentPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        initViews(view);
        setupListeners();
        loadFiles(currentPath);
        
        return view;
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
        btnCancel.setOnClickListener(v -> {
            if (selectionListener != null) {
                selectionListener.onSelectionCancelled();
            }
            dismiss();
        });
        
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
        
        List<FileItem> items = FileUtils.getFiles(currentPath, FileFilter.ALL, 
            FileSortOption.NAME_ASC, false);
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