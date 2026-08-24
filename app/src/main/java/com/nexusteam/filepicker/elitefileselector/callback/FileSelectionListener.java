package com.nexusteam.filepicker.elitefileselector.callback;

import com.nexusteam.filepicker.elitefileselector.FileItem;

import java.util.List;

public interface FileSelectionListener {
    void onFileSelected(FileItem file);
    void onFilesSelected(List<FileItem> files);
    void onSelectionChanged(List<FileItem> selectedFiles);
    void onSelectionComplete(List<FileItem> selectedFiles);
    void onSelectionCancelled();
}