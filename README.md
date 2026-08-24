# Elite File Selector

**Elite File Selector** is a reusable Android file-picker library. It supports single-file and multi-file selection, extension filters, folder-only and file-only modes, sorting, hidden-file visibility, search, size limits, dialogs, bottom sheets, and fragments.

## Installation with JitPack

Add JitPack to the repository list in your root `settings.gradle`:

```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency to your app module's `build.gradle`:

```gradle
dependencies {
    implementation 'com.github.NexusTeamOfficial:EliteFileSelector:1.0.6'
}
```

If `1.0.6` has not appeared in JitPack yet, use the verified earlier release temporarily:

```gradle
implementation 'com.github.NexusTeamOfficial:EliteFileSelector:1.0.3'
```

Then sync the project with Gradle.

## Basic activity implementation

The following example uses the main `EliteFilePicker` API. It includes all required imports, configures a multiple-selection picker, and forwards the activity result back to the library.

```java
package com.example.myapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.nexusteam.filepicker.elitefileselector.EliteFilePicker;
import com.nexusteam.filepicker.elitefileselector.FileItem;
import com.nexusteam.filepicker.elitefileselector.callback.FilePickerCallback;
import com.nexusteam.filepicker.elitefileselector.models.FileFilter;
import com.nexusteam.filepicker.elitefileselector.models.FileSortOption;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_REQUEST = 500;
    private final EliteFilePicker filePicker = EliteFilePicker.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.openPickerButton).setOnClickListener(view -> {
            if (hasPickerPermission()) {
                openFilePicker();
            } else {
                requestPickerPermission();
            }
        });
    }

    private void openFilePicker() {
        filePicker.init(this, new FilePickerCallback() {
            @Override
            public void onSuccess(List<FileItem> files) {
                for (FileItem file : files) {
                    String path = file.getPath();
                    String name = file.getName();
                    long size = file.getSize();
                    // Use path, name, size, or the complete FileItem here.
                }
            }

            @Override
            public void onError(Exception error) {
                error.printStackTrace();
            }

            @Override
            public void onCancelled() {
                // The user closed the picker without selecting a file.
            }

            @Override
            public void onPermissionDenied(String permission) {
                // Explain the missing permission or send the user to Settings.
            }
        });

        new EliteFilePicker.Builder(this)
                .setInitialPath("/storage/emulated/0/Download")
                .setSelectionMode("multiple")
                .setAllowedExtensions("pdf", "doc", "docx", "jpg", "png")
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
                .setMaxFileSize(100L * 1024L * 1024L)
                .launch();
    }

    private boolean hasPickerPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED;
        }
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPickerPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_AUDIO
            }, STORAGE_PERMISSION_REQUEST);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, STORAGE_PERMISSION_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        filePicker.handleResult(requestCode, resultCode, data);
    }
}
```

The example assumes that the activity layout contains a button with the ID `openPickerButton`. Replace that ID with the ID used by your own layout. `FileItem` exposes the selected file information; use its public getters rather than reading internal fields.

## Single-file selection

Use `single` mode when only one file should be returned:

```java
new EliteFilePicker.Builder(this)
        .setSelectionMode("single")
        .setAllowedExtensions("pdf")
        .setFileFilter(FileFilter.FILES_ONLY)
        .launch();
```

The callback still receives a `List<FileItem>` containing one item.

## Folder-only and file-only selection

```java
// Folders only
new EliteFilePicker.Builder(this)
        .setSelectionMode("single")
        .setShowOnlyDirectories(true)
        .launch();

// Files only
new EliteFilePicker.Builder(this)
        .setSelectionMode("multiple")
        .setShowOnlyFiles(true)
        .setMaxSelectionCount(5)
        .launch();
```

Do not enable both `setShowOnlyDirectories(true)` and `setShowOnlyFiles(true)` in the same configuration. If both are enabled, the picker prioritizes the directory-only mode.

## File filters and sorting

Available filter and sort values are defined in the `models` package:

```java
import com.nexusteam.filepicker.elitefileselector.models.FileFilter;
import com.nexusteam.filepicker.elitefileselector.models.FileSortOption;

new EliteFilePicker.Builder(this)
        .setFileFilter(FileFilter.IMAGES)
        .setSortOption(FileSortOption.DATE_DESC)
        .launch();
```

Use your IDE's autocomplete on `FileFilter` and `FileSortOption` to see the complete enum values included in the selected library version.

## Dialog, bottom sheet, and fragment APIs

The updated library also exposes UI components for apps that want to embed the picker in their own navigation flow.

### Dialog

```java
import com.nexusteam.filepicker.elitefileselector.FileItem;
import com.nexusteam.filepicker.elitefileselector.callback.FileSelectionListener;
import com.nexusteam.filepicker.elitefileselector.dialogs.FilePickerDialog;

FilePickerDialog dialog = FilePickerDialog.newInstance(
        "Choose documents",
        "multiple",
        10,
        new String[]{"pdf", "docx"}
);

dialog.setFileSelectionListener(new FileSelectionListener() {
    @Override
    public void onFileSelected(FileItem file) { }

    @Override
    public void onFilesSelected(List<FileItem> files) { }

    @Override
    public void onSelectionChanged(List<FileItem> selectedFiles) { }

    @Override
    public void onSelectionComplete(List<FileItem> selectedFiles) { }

    @Override
    public void onSelectionCancelled() { }
});

dialog.show(getSupportFragmentManager(), "elite_file_picker_dialog");
```

### Bottom sheet

```java
import com.nexusteam.filepicker.elitefileselector.dialogs.FilePickerBottomSheet;

FilePickerBottomSheet sheet = FilePickerBottomSheet.newInstance(
        "Choose a file",
        "single"
);
sheet.setFileSelectionListener(selectionListener);
sheet.show(getSupportFragmentManager(), "elite_file_picker_sheet");
```

### Fragment

```java
import com.nexusteam.filepicker.elitefileselector.fragments.FilePickerFragment;

FilePickerFragment fragment = FilePickerFragment.newInstance(
        "/storage/emulated/0/Download",
        "multiple",
        10,
        new String[]{"pdf", "jpg"}
);
fragment.setFileSelectionListener(selectionListener);

getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.filePickerContainer, fragment)
        .commit();
```

The dialog, bottom-sheet, and fragment APIs use `FileSelectionListener`, while the activity-based `EliteFilePicker` API uses `FilePickerCallback`.

## Android permissions

The library manifest declares the storage and media permissions needed by its file-system picker. Your application must still request the appropriate runtime permission before opening the picker. Android 13 and later use the granular `READ_MEDIA_*` permissions; older versions use `READ_EXTERNAL_STORAGE`. Use the permission flow shown in the basic activity example and tailor it to the media types your app actually supports.

## Dependency coordinates

| Property | Value |
|---|---|
| Group ID | `com.github.NexusTeamOfficial` |
| Artifact ID | `EliteFileSelector` |
| Recommended version | `1.0.6` |
| Verified fallback | `1.0.3` |
| Minimum SDK | 21 |

## License

Add the license terms that should govern distribution of this library before publishing a stable release.
