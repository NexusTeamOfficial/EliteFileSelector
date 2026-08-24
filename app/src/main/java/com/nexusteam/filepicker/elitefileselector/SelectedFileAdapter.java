package com.nexusteam.filepicker.elitefileselector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class SelectedFileAdapter extends RecyclerView.Adapter<SelectedFileAdapter.ViewHolder> {
    
    private Context context;
    private List<FileItem> files;
    
    public SelectedFileAdapter(Context context, List<FileItem> files) {
        this.context = context;
        this.files = files;
    }
    
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_selected_file, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FileItem item = files.get(position);
        holder.tvFileName.setText(item.getName());
        holder.tvFileSize.setText(item.getFormattedSize());
        holder.tvFilePath.setText(item.getPath());
        
        // Set icon based on file type
        if (item.isDirectory()) {
            holder.ivFileIcon.setImageResource(R.drawable.ic_folder);
        } else {
            holder.ivFileIcon.setImageResource(R.drawable.ic_file);
        }
    }
    
    @Override
    public int getItemCount() {
        return files.size();
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFileIcon;
        TextView tvFileName;
        TextView tvFileSize;
        TextView tvFilePath;
        
        public ViewHolder(View itemView) {
            super(itemView);
            ivFileIcon = itemView.findViewById(R.id.ivFileIcon);
            tvFileName = itemView.findViewById(R.id.tvFileName);
            tvFileSize = itemView.findViewById(R.id.tvFileSize);
            tvFilePath = itemView.findViewById(R.id.tvFilePath);
        }
    }
}