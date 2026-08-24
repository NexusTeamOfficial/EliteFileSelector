# Elite File Selector

A feature-rich Android file picker library with filtering, sorting, search, single/multiple selection, and configurable file-size constraints.

## Installation with JitPack

Add the JitPack repository to your root `settings.gradle` or `build.gradle`:

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
    implementation 'com.github.NexusTeamOfficial:EliteFileSelector:1.0.2'
}
```

You can also use a Git tag or commit supported by JitPack, for example:

```gradle
implementation 'com.github.NexusTeamOfficial:EliteFileSelector:1.0.2'
```

## Usage

Create the picker with `EliteFilePicker.Builder`, launch it, and forward the activity result to `handleResult`:

```java
private final EliteFilePicker filePicker = EliteFilePicker.getInstance();

private void openPicker() {
    filePicker.init(this, new FilePickerCallback() {
        @Override
        public void onSuccess(List<FileItem> files) {
            // Use selected files.
        }

        @Override
        public void onCancelled() {
            // Picker was cancelled.
        }

        @Override
        public void onError(Exception error) {
            error.printStackTrace();
        }

        @Override
        public void onPermissionDenied(String permission) {
            // Request or explain the missing runtime permission.
        }
    });

    new EliteFilePicker.Builder(this)
            .setSelectionMode("multiple")
            .setAllowedExtensions("pdf", "docx", "jpg", "png")
            .setMaxSelectionCount(10)
            .setEnableSearch(true)
            .setShowHiddenFiles(false)
            .setFileFilter(FileFilter.ALL)
            .launch();
}

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (filePicker != null) {
        filePicker.handleResult(requestCode, resultCode, data);
    }
}
```

The exact callback signatures can be checked in `callback/FilePickerCallback.java`. The library declares the storage and media permissions required by its file-system picker; applications should request the appropriate runtime permissions for their target Android version before launching it.

## Coordinates

| Property | Value |
|---|---|
| Group ID | `com.github.NexusTeamOfficial` |
| Artifact ID | `EliteFileSelector` |
| Version | `1.0.2` |
| Minimum SDK | 21 |

## License

Add the license terms that should govern distribution of this library before publishing a stable release.
