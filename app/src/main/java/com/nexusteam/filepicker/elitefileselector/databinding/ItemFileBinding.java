// Generated file. Do not modify.
package com.nexusteam.filepicker.elitefileselector;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView;

public final class ItemFileBinding {
    public final CardView rootView;
    public final RelativeLayout relativelayout2;
    public final CardView cvSelection;
    public final ImageView ivCheck;
    public final ImageView ivFileIcon;
    public final LinearLayout linear27;
    public final TextView tvFileName;
    public final LinearLayout linear28;
    public final TextView tvFileSize;
    public final TextView tvFileDate;

    private ItemFileBinding(CardView rootView, RelativeLayout relativelayout2, CardView cvSelection, ImageView ivCheck, ImageView ivFileIcon, LinearLayout linear27, TextView tvFileName, LinearLayout linear28, TextView tvFileSize, TextView tvFileDate) {
        this.rootView = rootView;
        this.relativelayout2 = relativelayout2;
        this.cvSelection = cvSelection;
        this.ivCheck = ivCheck;
        this.ivFileIcon = ivFileIcon;
        this.linear27 = linear27;
        this.tvFileName = tvFileName;
        this.linear28 = linear28;
        this.tvFileSize = tvFileSize;
        this.tvFileDate = tvFileDate;
    }

    public CardView getRoot() {
        return rootView;
    }

    public static ItemFileBinding inflate(LayoutInflater inflater) {
        return inflate(inflater, null, false);
    }

    public static ItemFileBinding inflate(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        View root = inflater.inflate(R.layout.item_file, parent, false);
        if (attachToParent) parent.addView(root);
        return bind(root);
    }

    public static ItemFileBinding bind(View view) {
        CardView rootView = (CardView) view;
        RelativeLayout relativelayout2 = findChildViewById(view, R.id.relativelayout2);
        CardView cvSelection = findChildViewById(view, R.id.cvSelection);
        ImageView ivCheck = findChildViewById(view, R.id.ivCheck);
        ImageView ivFileIcon = findChildViewById(view, R.id.ivFileIcon);
        LinearLayout linear27 = findChildViewById(view, R.id.linear27);
        TextView tvFileName = findChildViewById(view, R.id.tvFileName);
        LinearLayout linear28 = findChildViewById(view, R.id.linear28);
        TextView tvFileSize = findChildViewById(view, R.id.tvFileSize);
        TextView tvFileDate = findChildViewById(view, R.id.tvFileDate);

        if (relativelayout2 == null || cvSelection == null || ivCheck == null || ivFileIcon == null || linear27 == null || tvFileName == null || linear28 == null || tvFileSize == null || tvFileDate == null) {
             throw new IllegalStateException("Required views are missing");
        }

        return new ItemFileBinding(rootView, relativelayout2, cvSelection, ivCheck, ivFileIcon, linear27, tvFileName, linear28, tvFileSize, tvFileDate);
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