package com.nexusteam.filepicker.elitefileselector.callback;

import com.nexusteam.filepicker.elitefileselector.FileItem;

import java.util.List;

public interface FilePickerCallback {
    void onSuccess(List<FileItem> files);
    void onError(Exception error);
    void onCancelled();
    void onPermissionDenied(String permission);
}