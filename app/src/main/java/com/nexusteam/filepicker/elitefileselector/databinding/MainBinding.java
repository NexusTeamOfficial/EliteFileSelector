// Generated file. Do not modify.
package com.nexusteam.filepicker.elitefileselector;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.progressindicator.CircularProgressIndicator;

public final class MainBinding {
    public final CoordinatorLayout Coordinator;
    public final AppBarLayout AppBar;
    public final Toolbar Toolbar;
    public final LinearLayout linear10;
    public final TextView textview4;
    public final TextView textview5;
    public final ChipGroup chipGroupFilter;
    public final Chip chip11;
    public final Chip chip12;
    public final Chip chip13;
    public final Chip chip14;
    public final Chip chip15;
    public final ChipGroup chipGroupSort;
    public final Chip chip16;
    public final Chip chip17;
    public final Chip chip18;
    public final MaterialButton btnOpenFilePicker;
    public final CircularProgressIndicator progressIndicator;
    public final ProgressBar progressBar;
    public final TextView tvFileInfo;
    public final TextView tvSelectedFile;
    public final TextView textview6;
    public final RecyclerView rvSelectedFiles;

    private MainBinding(CoordinatorLayout Coordinator, AppBarLayout AppBar, Toolbar Toolbar, LinearLayout linear10, TextView textview4, TextView textview5, ChipGroup chipGroupFilter, Chip chip11, Chip chip12, Chip chip13, Chip chip14, Chip chip15, ChipGroup chipGroupSort, Chip chip16, Chip chip17, Chip chip18, MaterialButton btnOpenFilePicker, CircularProgressIndicator progressIndicator, ProgressBar progressBar, TextView tvFileInfo, TextView tvSelectedFile, TextView textview6, RecyclerView rvSelectedFiles) {
        this.Coordinator = Coordinator;
        this.AppBar = AppBar;
        this.Toolbar = Toolbar;
        this.linear10 = linear10;
        this.textview4 = textview4;
        this.textview5 = textview5;
        this.chipGroupFilter = chipGroupFilter;
        this.chip11 = chip11;
        this.chip12 = chip12;
        this.chip13 = chip13;
        this.chip14 = chip14;
        this.chip15 = chip15;
        this.chipGroupSort = chipGroupSort;
        this.chip16 = chip16;
        this.chip17 = chip17;
        this.chip18 = chip18;
        this.btnOpenFilePicker = btnOpenFilePicker;
        this.progressIndicator = progressIndicator;
        this.progressBar = progressBar;
        this.tvFileInfo = tvFileInfo;
        this.tvSelectedFile = tvSelectedFile;
        this.textview6 = textview6;
        this.rvSelectedFiles = rvSelectedFiles;
    }

    public CoordinatorLayout getRoot() {
        return Coordinator;
    }

    public static MainBinding inflate(LayoutInflater inflater) {
        return inflate(inflater, null, false);
    }

    public static MainBinding inflate(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        View root = inflater.inflate(R.layout.main, parent, false);
        if (attachToParent) parent.addView(root);
        return bind(root);
    }

    public static MainBinding bind(View view) {
        CoordinatorLayout Coordinator = (CoordinatorLayout) view;
        AppBarLayout AppBar = findChildViewById(view, R.id._app_bar);
        Toolbar Toolbar = findChildViewById(view, R.id._toolbar);
        LinearLayout linear10 = findChildViewById(view, R.id.linear10);
        TextView textview4 = findChildViewById(view, R.id.textview4);
        TextView textview5 = findChildViewById(view, R.id.textview5);
        ChipGroup chipGroupFilter = findChildViewById(view, R.id.chipGroupFilter);
        Chip chip11 = findChildViewById(view, R.id.chip11);
        Chip chip12 = findChildViewById(view, R.id.chip12);
        Chip chip13 = findChildViewById(view, R.id.chip13);
        Chip chip14 = findChildViewById(view, R.id.chip14);
        Chip chip15 = findChildViewById(view, R.id.chip15);
        ChipGroup chipGroupSort = findChildViewById(view, R.id.chipGroupSort);
        Chip chip16 = findChildViewById(view, R.id.chip16);
        Chip chip17 = findChildViewById(view, R.id.chip17);
        Chip chip18 = findChildViewById(view, R.id.chip18);
        MaterialButton btnOpenFilePicker = findChildViewById(view, R.id.btnOpenFilePicker);
        CircularProgressIndicator progressIndicator = findChildViewById(view, R.id.progressIndicator);
        ProgressBar progressBar = findChildViewById(view, R.id.progressBar);
        TextView tvFileInfo = findChildViewById(view, R.id.tvFileInfo);
        TextView tvSelectedFile = findChildViewById(view, R.id.tvSelectedFile);
        TextView textview6 = findChildViewById(view, R.id.textview6);
        RecyclerView rvSelectedFiles = findChildViewById(view, R.id.rvSelectedFiles);

        if (AppBar == null || Toolbar == null || linear10 == null || textview4 == null || textview5 == null || chipGroupFilter == null || chip11 == null || chip12 == null || chip13 == null || chip14 == null || chip15 == null || chipGroupSort == null || chip16 == null || chip17 == null || chip18 == null || btnOpenFilePicker == null || progressIndicator == null || progressBar == null || tvFileInfo == null || tvSelectedFile == null || textview6 == null || rvSelectedFiles == null) {
             throw new IllegalStateException("Required views are missing");
        }

        return new MainBinding(Coordinator, AppBar, Toolbar, linear10, textview4, textview5, chipGroupFilter, chip11, chip12, chip13, chip14, chip15, chipGroupSort, chip16, chip17, chip18, btnOpenFilePicker, progressIndicator, progressBar, tvFileInfo, tvSelectedFile, textview6, rvSelectedFiles);
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