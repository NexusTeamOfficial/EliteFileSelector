package com.nexusteam.filepicker.elitefileselector.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.nexusteam.filepicker.elitefileselector.FileItem;
import com.nexusteam.filepicker.elitefileselector.R;
import com.nexusteam.filepicker.elitefileselector.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> {
    
    private Context context;
    private List<FileItem> fileItems;
    private OnFileClickListener listener;
    private List<FileItem> selectedItems = new ArrayList<FileItem>();
    
    public interface OnFileClickListener {
        void onFileClick(FileItem fileItem);
        void onFileLongClick(FileItem fileItem);
        void onFolderClick(FileItem fileItem);
        void onSelectionChanged(int count);
    }
    
    public FileAdapter(Context context, List<FileItem> fileItems, OnFileClickListener listener) {
        this.context = context;
        this.fileItems = fileItems;
        this.listener = listener;
    }
    
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_file, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final FileItem item = fileItems.get(position);
        File file = new File(item.getPath());
        
        holder.tvFileName.setText(item.getName());
        holder.tvFileSize.setText(item.isDirectory() ? "Folder" : item.getFormattedSize());
        holder.tvFileDate.setText(FileUtils.formatDate(item.getLastModified()));
        
        // Set file icon
        holder.ivFileIcon.setImageResource(FileUtils.getFileIcon(context, file));
        
        // Selection mode
        if (item.isSelected()) {
            holder.cvSelection.setVisibility(View.VISIBLE);
            holder.cvSelection.setCardBackgroundColor(
                context.getResources().getColor(R.color.purple_500));
            holder.ivCheck.setVisibility(View.VISIBLE);
        } else {
            holder.cvSelection.setVisibility(View.GONE);
            holder.ivCheck.setVisibility(View.GONE);
        }
        
        // Click listeners
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.isDirectory()) {
                    if (listener != null) {
                        listener.onFolderClick(item);
                    }
                } else {
                    if (listener != null) {
                        listener.onFileClick(item);
                    }
                }
            }
        });
        
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (listener != null) {
                    listener.onFileLongClick(item);
                }
                return true;
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return fileItems.size();
    }
    
    public List<FileItem> getSelectedItems() {
        selectedItems.clear();
        for (FileItem item : fileItems) {
            if (item.isSelected()) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }
    
    public int getSelectedCount() {
        int count = 0;
        for (FileItem item : fileItems) {
            if (item.isSelected()) {
                count++;
            }
        }
        return count;
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFileIcon;
        TextView tvFileName;
        TextView tvFileSize;
        TextView tvFileDate;
        CardView cvSelection;
        ImageView ivCheck;
        
        public ViewHolder(View itemView) {
            super(itemView);
            ivFileIcon = itemView.findViewById(R.id.ivFileIcon);
            tvFileName = itemView.findViewById(R.id.tvFileName);
            tvFileSize = itemView.findViewById(R.id.tvFileSize);
            tvFileDate = itemView.findViewById(R.id.tvFileDate);
            cvSelection = itemView.findViewById(R.id.cvSelection);
            ivCheck = itemView.findViewById(R.id.ivCheck);
        }
    }
}