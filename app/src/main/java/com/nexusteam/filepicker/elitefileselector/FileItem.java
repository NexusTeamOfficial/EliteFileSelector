package com.nexusteam.filepicker.elitefileselector;

import java.io.File;
import java.io.Serializable;
import java.util.UUID;

public class FileItem implements Serializable {
    private String id;
    private String name;
    private String path;
    private long size;
    private long lastModified;
    private boolean isDirectory;
    private boolean isSelected;
    private String extension;
    private String mimeType;
    private String parentPath;
    private boolean isHidden;
    private boolean canRead;
    private boolean canWrite;
    private int fileIconRes;
    
    public FileItem(File file) {
        this.id = UUID.randomUUID().toString();
        this.name = file.getName();
        this.path = file.getAbsolutePath();
        this.size = file.length();
        this.lastModified = file.lastModified();
        this.isDirectory = file.isDirectory();
        this.isSelected = false;
        this.isHidden = file.isHidden();
        this.canRead = file.canRead();
        this.canWrite = file.canWrite();
        
        if (!isDirectory && name.contains(".")) {
            this.extension = name.substring(name.lastIndexOf(".") + 1).toLowerCase();
        } else {
            this.extension = "";
        }
        
        if (file.getParentFile() != null) {
            this.parentPath = file.getParent();
        }
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
    public long getSize() { return size; }
    public void setSize(long size) { this.size = size; }
    public long getLastModified() { return lastModified; }
    public void setLastModified(long lastModified) { this.lastModified = lastModified; }
    public boolean isDirectory() { return isDirectory; }
    public void setDirectory(boolean directory) { isDirectory = directory; }
    public boolean isSelected() { return isSelected; }
    public void setSelected(boolean selected) { isSelected = selected; }
    public String getExtension() { return extension; }
    public void setExtension(String extension) { this.extension = extension; }
    public String getMimeType() { return mimeType; }
    public void setMimeType(String mimeType) { this.mimeType = mimeType; }
    public String getParentPath() { return parentPath; }
    public void setParentPath(String parentPath) { this.parentPath = parentPath; }
    public boolean isHidden() { return isHidden; }
    public void setHidden(boolean hidden) { isHidden = hidden; }
    public boolean isCanRead() { return canRead; }
    public void setCanRead(boolean canRead) { this.canRead = canRead; }
    public boolean isCanWrite() { return canWrite; }
    public void setCanWrite(boolean canWrite) { this.canWrite = canWrite; }
    public int getFileIconRes() { return fileIconRes; }
    public void setFileIconRes(int fileIconRes) { this.fileIconRes = fileIconRes; }
    
    public String getFormattedSize() {
        if (size < 1024) return size + " B";
        int exp = (int) (Math.log(size) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.1f %sB", size / Math.pow(1024, exp), pre);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        FileItem other = (FileItem) obj;
        return path != null && path.equals(other.path);
    }
    
    @Override
    public int hashCode() {
        return path != null ? path.hashCode() : 0;
    }
}