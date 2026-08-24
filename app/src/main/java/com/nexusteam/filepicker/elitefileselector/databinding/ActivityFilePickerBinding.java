// Generated file. Do not modify.
package com.nexusteam.filepicker.elitefileselector;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public final class ActivityFilePickerBinding {
    public final LinearLayout rootView;
    public final MaterialToolbar toolbar;
    public final LinearLayout linear23;
    public final LinearLayout linear24;
    public final MaterialButton btnBack;
    public final TextView tvCurrentPath;
    public final MaterialButton btnSearch;
    public final LinearLayout llSearchBar;
    public final EditText etSearch;
    public final MaterialButton btnSearchCancel;
    public final ChipGroup chipGroupFilter;
    public final Chip chipAll;
    public final Chip chipImages;
    public final Chip chipVideos;
    public final Chip chipAudio;
    public final Chip chipDocuments;
    public final Chip chipApk;
    public final RecyclerView rvFiles;
    public final LinearLayout llEmptyState;
    public final ImageView imageview2;
    public final TextView textview9;
    public final TextView textview10;
    public final LinearLayout llBottomActions;
    public final TextView tvSelectedCount;
    public final MaterialButton btnSelectAll;
    public final MaterialButton btnDone;

    private ActivityFilePickerBinding(LinearLayout rootView, MaterialToolbar toolbar, LinearLayout linear23, LinearLayout linear24, MaterialButton btnBack, TextView tvCurrentPath, MaterialButton btnSearch, LinearLayout llSearchBar, EditText etSearch, MaterialButton btnSearchCancel, ChipGroup chipGroupFilter, Chip chipAll, Chip chipImages, Chip chipVideos, Chip chipAudio, Chip chipDocuments, Chip chipApk, RecyclerView rvFiles, LinearLayout llEmptyState, ImageView imageview2, TextView textview9, TextView textview10, LinearLayout llBottomActions, TextView tvSelectedCount, MaterialButton btnSelectAll, MaterialButton btnDone) {
        this.rootView = rootView;
        this.toolbar = toolbar;
        this.linear23 = linear23;
        this.linear24 = linear24;
        this.btnBack = btnBack;
        this.tvCurrentPath = tvCurrentPath;
        this.btnSearch = btnSearch;
        this.llSearchBar = llSearchBar;
        this.etSearch = etSearch;
        this.btnSearchCancel = btnSearchCancel;
        this.chipGroupFilter = chipGroupFilter;
        this.chipAll = chipAll;
        this.chipImages = chipImages;
        this.chipVideos = chipVideos;
        this.chipAudio = chipAudio;
        this.chipDocuments = chipDocuments;
        this.chipApk = chipApk;
        this.rvFiles = rvFiles;
        this.llEmptyState = llEmptyState;
        this.imageview2 = imageview2;
        this.textview9 = textview9;
        this.textview10 = textview10;
        this.llBottomActions = llBottomActions;
        this.tvSelectedCount = tvSelectedCount;
        this.btnSelectAll = btnSelectAll;
        this.btnDone = btnDone;
    }

    public LinearLayout getRoot() {
        return rootView;
    }

    public static ActivityFilePickerBinding inflate(LayoutInflater inflater) {
        return inflate(inflater, null, false);
    }

    public static ActivityFilePickerBinding inflate(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        View root = inflater.inflate(R.layout.activity_file_picker, parent, false);
        if (attachToParent) parent.addView(root);
        return bind(root);
    }

    public static ActivityFilePickerBinding bind(View view) {
        LinearLayout rootView = (LinearLayout) view;
        MaterialToolbar toolbar = findChildViewById(view, R.id.toolbar);
        LinearLayout linear23 = findChildViewById(view, R.id.linear23);
        LinearLayout linear24 = findChildViewById(view, R.id.linear24);
        MaterialButton btnBack = findChildViewById(view, R.id.btnBack);
        TextView tvCurrentPath = findChildViewById(view, R.id.tvCurrentPath);
        MaterialButton btnSearch = findChildViewById(view, R.id.btnSearch);
        LinearLayout llSearchBar = findChildViewById(view, R.id.llSearchBar);
        EditText etSearch = findChildViewById(view, R.id.etSearch);
        MaterialButton btnSearchCancel = findChildViewById(view, R.id.btnSearchCancel);
        ChipGroup chipGroupFilter = findChildViewById(view, R.id.chipGroupFilter);
        Chip chipAll = findChildViewById(view, R.id.chipAll);
        Chip chipImages = findChildViewById(view, R.id.chipImages);
        Chip chipVideos = findChildViewById(view, R.id.chipVideos);
        Chip chipAudio = findChildViewById(view, R.id.chipAudio);
        Chip chipDocuments = findChildViewById(view, R.id.chipDocuments);
        Chip chipApk = findChildViewById(view, R.id.chipApk);
        RecyclerView rvFiles = findChildViewById(view, R.id.rvFiles);
        LinearLayout llEmptyState = findChildViewById(view, R.id.llEmptyState);
        ImageView imageview2 = findChildViewById(view, R.id.imageview2);
        TextView textview9 = findChildViewById(view, R.id.textview9);
        TextView textview10 = findChildViewById(view, R.id.textview10);
        LinearLayout llBottomActions = findChildViewById(view, R.id.llBottomActions);
        TextView tvSelectedCount = findChildViewById(view, R.id.tvSelectedCount);
        MaterialButton btnSelectAll = findChildViewById(view, R.id.btnSelectAll);
        MaterialButton btnDone = findChildViewById(view, R.id.btnDone);

        if (toolbar == null || linear23 == null || linear24 == null || btnBack == null || tvCurrentPath == null || btnSearch == null || llSearchBar == null || etSearch == null || btnSearchCancel == null || chipGroupFilter == null || chipAll == null || chipImages == null || chipVideos == null || chipAudio == null || chipDocuments == null || chipApk == null || rvFiles == null || llEmptyState == null || imageview2 == null || textview9 == null || textview10 == null || llBottomActions == null || tvSelectedCount == null || btnSelectAll == null || btnDone == null) {
             throw new IllegalStateException("Required views are missing");
        }

        return new ActivityFilePickerBinding(rootView, toolbar, linear23, linear24, btnBack, tvCurrentPath, btnSearch, llSearchBar, etSearch, btnSearchCancel, chipGroupFilter, chipAll, chipImages, chipVideos, chipAudio, chipDocuments, chipApk, rvFiles, llEmptyState, imageview2, textview9, textview10, llBottomActions, tvSelectedCount, btnSelectAll, btnDone);
    }

    private static <T extends View> T findChildViewById(View rootView, int id) {
         if (rootView instanceof ViewGroup) {
              ViewGroup rootViewGroup = (ViewGroup) rootView;
              for (int i = 0; i < rootViewGroup.getChildCount(); i++) {
                   T view = rootViewGroup.getChildAt(i).findViewById(id);
                   if (view != null) return view;
              }
         }
         return null;
    }
}