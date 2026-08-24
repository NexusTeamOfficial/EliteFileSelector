package com.nexusteam.filepicker.elitefileselector.callback;

import java.io.Serializable;

public interface ProgressCallback extends Serializable {
    void onProgressUpdated(int progress, String message);
    void onComplete();
    void onError(String errorMessage);
}