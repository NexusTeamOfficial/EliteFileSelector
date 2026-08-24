package com.nexusteam.filepicker.elitefileselector.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
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

public class FilePickerFragment extends Fragment implements FileAdapter.OnFileClickListener {
    
    private static final String ARG_INITIAL_PATH = "initial_path";
    private static final String ARG_SELECTION_MODE = "selection_mode";
    private static final String ARG_MAX_SELECTION = "max_selection";
    private static final String ARG_ALLOWED_EXTENSIONS = "allowed_extensions";
    private static final String ARG_SHOW_HIDDEN = "show_hidden";
    private static final String ARG_FILE_FILTER = "file_filter";
    private static final String ARG_SORT_OPTION = "sort_option";
    
    private RecyclerView rvFiles;
    private TextView tvCurrentPath, tvEmptyState;
    private MaterialButton btnBack;
    private Toolbar toolbar;
    
    private FileAdapter adapter;
    private List<FileItem> fileItems = new ArrayList<>();
    private String currentPath;
    private String selectionMode = "single";
    private int maxSelectionCount = 10;
    private String[] allowedExtensions;
    private boolean showHiddenFiles = false;
    private FileFilter fileFilter = FileFilter.ALL;
    private FileSortOption sortOption = FileSortOption.NAME_ASC;
    
    private FileSelectionListener selectionListener;
    private List<FileItem> selectedFiles = new ArrayList<>();
    
    public static FilePickerFragment newInstance(String initialPath, String selectionMode, 
                                                  int maxSelection, String[] extensions) {
        FilePickerFragment fragment = new FilePickerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_INITIAL_PATH, initialPath);
        args.putString(ARG_SELECTION_MODE, selectionMode);
        args.putInt(ARG_MAX_SELECTION, maxSelection);
        args.putStringArray(ARG_ALLOWED_EXTENSIONS, extensions);
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        
        if (getArguments() != null) {
            currentPath = getArguments().getString(ARG_INITIAL_PATH, 
                Environment.getExternalStorageDirectory().getAbsolutePath());
            selectionMode = getArguments().getString(ARG_SELECTION_MODE, "single");
            maxSelectionCount = getArguments().getInt(ARG_MAX_SELECTION, 10);
            allowedExtensions = getArguments().getStringArray(ARG_ALLOWED_EXTENSIONS);
        }
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file_picker, container, false);
        
        rvFiles = view.findViewById(R.id.rvFiles);
        tvCurrentPath = view.findViewById(R.id.tvCurrentPath);
        tvEmptyState = view.findViewById(R.id.tvEmptyState);
        btnBack = view.findViewById(R.id.btnBack);
        toolbar = view.findViewById(R.id.toolbar);
        
        setupToolbar();
        setupRecyclerView();
        setupListeners();
        loadFiles(currentPath);
        
        return view;
    }
    
    private void setupToolbar() {
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        if (((AppCompatActivity) requireActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) requireActivity()).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) requireActivity()).getSupportActionBar()
                .setTitle("Select Files");
        }
    }
    
    private void setupRecyclerView() {
        rvFiles.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FileAdapter(getContext(), fileItems, this);
        rvFiles.setAdapter(adapter);
    }
    
    private void setupListeners() {
        btnBack.setOnClickListener(v -> goBack());
    }
    
    private void goBack() {
        File parent = new File(currentPath).getParentFile();
        if (parent != null && parent.exists()) {
            loadFiles(parent.getAbsolutePath());
        } else {
            Toast.makeText(getContext(), "Already at root", Toast.LENGTH_SHORT).show();
        }
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
        
        for (FileItem item : items) {
            if (allowedExtensions != null && allowedExtensions.length > 0 && !item.isDirectory()) {
                String ext = item.getExtension();
                for (String allowed : allowedExtensions) {
                    if (ext.equalsIgnoreCase(allowed)) {
                        fileItems.add(item);
                        break;
                    }
                }
            } else {
                fileItems.add(item);
            }
        }
        
        updateEmptyState();
        adapter.notifyDataSetChanged();
    }
    
    private void updateEmptyState() {
        if (fileItems.isEmpty()) {
            tvEmptyState.setVisibility(View.VISIBLE);
            rvFiles.setVisibility(View.GONE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
            rvFiles.setVisibility(View.VISIBLE);
        }
    }
    
    @Override
    public void onFileClick(FileItem fileItem) {
        if (selectionMode.equals("single")) {
            if (selectionListener != null) {
                selectionListener.onFileSelected(fileItem);
            }
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
        updateToolbarTitle(count);
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
        updateToolbarTitle(selectedFiles.size());
        
        if (selectionListener != null) {
            selectionListener.onSelectionChanged(selectedFiles);
        }
    }
    
    private void updateToolbarTitle(int count) {
        if (count > 0) {
            toolbar.setTitle(count + " selected");
        } else {
            toolbar.setTitle("Select Files");
        }
    }
    
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.file_picker_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
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
            requireActivity().onBackPressed();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    private void selectAllFiles() {
        if (selectionMode.equals("multiple")) {
            selectedFiles.clear();
            for (FileItem item : fileItems) {
                if (!item.isDirectory() && selectedFiles.size() < maxSelectionCount) {
                    item.setSelected(true);
                    selectedFiles.add(item);
                }
            }
            adapter.notifyDataSetChanged();
            updateToolbarTitle(selectedFiles.size());
            
            if (selectionListener != null) {
                selectionListener.onSelectionChanged(selectedFiles);
            }
        }
    }
    
    private void doneSelection() {
        if (selectionMode.equals("multiple") && selectionListener != null) {
            selectionListener.onSelectionComplete(selectedFiles);
        }
    }
    
    private void toggleSort() {
        FileSortOption[] options = FileSortOption.values();
        int currentIndex = 0;
        for (int i = 0; i < options.length; i++) {
            if (options[i] == sortOption) {
                currentIndex = i;
                break;
            }
        }
        sortOption = options[(currentIndex + 1) % options.length];
        loadFiles(currentPath);
        Toast.makeText(getContext(), "Sort: " + sortOption.name(), Toast.LENGTH_SHORT).show();
    }
    
    public void setFileSelectionListener(FileSelectionListener listener) {
        this.selectionListener = listener;
    }
    
    public List<FileItem> getSelectedFiles() {
        return new ArrayList<>(selectedFiles);
    }
}