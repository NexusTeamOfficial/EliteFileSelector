// Generated file. Do not modify.
package com.nexusteam.filepicker.elitefileselector;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public final class ItemSelectedFileBinding {
    public final LinearLayout rootView;
    public final ImageView ivFileIcon;
    public final LinearLayout linear20;
    public final TextView tvFileName;
    public final TextView tvFileSize;
    public final TextView tvFilePath;

    private ItemSelectedFileBinding(LinearLayout rootView, ImageView ivFileIcon, LinearLayout linear20, TextView tvFileName, TextView tvFileSize, TextView tvFilePath) {
        this.rootView = rootView;
        this.ivFileIcon = ivFileIcon;
        this.linear20 = linear20;
        this.tvFileName = tvFileName;
        this.tvFileSize = tvFileSize;
        this.tvFilePath = tvFilePath;
    }

    public LinearLayout getRoot() {
        return rootView;
    }

    public static ItemSelectedFileBinding inflate(LayoutInflater inflater) {
        return inflate(inflater, null, false);
    }

    public static ItemSelectedFileBinding inflate(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        View root = inflater.inflate(R.layout.item_selected_file, parent, false);
        if (attachToParent) parent.addView(root);
        return bind(root);
    }

    public static ItemSelectedFileBinding bind(View view) {
        LinearLayout rootView = (LinearLayout) view;
        ImageView ivFileIcon = findChildViewById(view, R.id.ivFileIcon);
        LinearLayout linear20 = findChildViewById(view, R.id.linear20);
        TextView tvFileName = findChildViewById(view, R.id.tvFileName);
        TextView tvFileSize = findChildViewById(view, R.id.tvFileSize);
        TextView tvFilePath = findChildViewById(view, R.id.tvFilePath);

        if (ivFileIcon == null || linear20 == null || tvFileName == null || tvFileSize == null || tvFilePath == null) {
             throw new IllegalStateException("Required views are missing");
        }

        return new ItemSelectedFileBinding(rootView, ivFileIcon, linear20, tvFileName, tvFileSize, tvFilePath);
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