package com.nexusteam.filepicker.elitefileselector.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionUtils {
    
    public static final int REQUEST_CODE_STORAGE = 1001;
    public static final int REQUEST_CODE_MANAGE_STORAGE = 1002;
    public static final int REQUEST_CODE_MEDIA = 1003;
    
    public static String[] getRequiredPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            return new String[]{
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.READ_MEDIA_AUDIO
            };
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { // Android 11-12
            return new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE
            };
        } else { // Android 10 and below
            return new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
        }
    }
    
    public static boolean hasStoragePermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ needs granular permissions
            boolean hasImages = ContextCompat.checkSelfPermission(context, 
                Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED;
            boolean hasVideos = ContextCompat.checkSelfPermission(context, 
                Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED;
            boolean hasAudio = ContextCompat.checkSelfPermission(context, 
                Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED;
            
            // For file picker, we need at least one media permission
            return hasImages || hasVideos || hasAudio;
        } else {
            return ContextCompat.checkSelfPermission(context, 
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }
    
    public static void requestStoragePermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Request granular permissions
            List<String> permissions = new ArrayList<>();
            
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_MEDIA_IMAGES) 
                != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_MEDIA_IMAGES);
            }
            
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_MEDIA_VIDEO) 
                != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_MEDIA_VIDEO);
            }
            
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_MEDIA_AUDIO) 
                != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_MEDIA_AUDIO);
            }
            
            if (!permissions.isEmpty()) {
                String[] perms = permissions.toArray(new String[0]);
                ActivityCompat.requestPermissions(activity, perms, REQUEST_CODE_MEDIA);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11-12
            ActivityCompat.requestPermissions(activity, 
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 
                REQUEST_CODE_STORAGE);
        } else {
            // Android 10 and below
            ActivityCompat.requestPermissions(activity, 
                new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 
                REQUEST_CODE_STORAGE);
        }
    }
    
    public static boolean shouldShowRationale(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ActivityCompat.shouldShowRequestPermissionRationale(activity, 
                Manifest.permission.READ_MEDIA_IMAGES) ||
                ActivityCompat.shouldShowRequestPermissionRationale(activity, 
                Manifest.permission.READ_MEDIA_VIDEO) ||
                ActivityCompat.shouldShowRequestPermissionRationale(activity, 
                Manifest.permission.READ_MEDIA_AUDIO);
        } else {
            return ActivityCompat.shouldShowRequestPermissionRationale(activity, 
                Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }
}