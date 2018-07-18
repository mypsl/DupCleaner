package com.mypsl.util;

public class FileInfo implements Comparable<FileInfo> {
	
	private String url;
	private Long size;
	private Long modifyTime;
	private String killFlag;
	
	public static final String KILL_FLAG = "*";
	public static final String KEEP_FLAG = "";

	FileInfo(String url, Long size, Long modifyTime) {
		this.setUrl(url);
		this.setSize(size);
		this.setModifyTime(modifyTime);
		this.setKillFlag(KILL_FLAG);
	}
	
	FileInfo() {}
	
	public String getKillFlag() {
		return killFlag;
	}

	public void setKillFlag(String killFlag) {
		this.killFlag = killFlag;
	}

	public Long getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Long modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FileInfo)) return false;

        FileInfo f = (FileInfo) o;
        if (size != f.size) return false;
        if (modifyTime != f.modifyTime) return false;
        // 相同路径不会出现同名文件
//        return url != null ? url.equals(f.url) : f.url == null;
        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = (int) (31 * result + size);
        result = (int) (31 * result + modifyTime);
        return result;
    }

	public int compareTo(FileInfo info) {
		 return this.getModifyTime().compareTo(info.getModifyTime());
	}
}
