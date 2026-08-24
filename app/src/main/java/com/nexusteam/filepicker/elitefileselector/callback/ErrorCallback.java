package com.nexusteam.filepicker.elitefileselector.callback;

public interface ErrorCallback {
    void onPermissionError(String permission, String message);
    void onStorageError(String message);
    void onFileNotFoundError(String path);
    void onSecurityError(String message);
    void onUnknownError(String message);
}