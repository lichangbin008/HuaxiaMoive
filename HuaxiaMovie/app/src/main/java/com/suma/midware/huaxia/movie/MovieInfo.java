package com.suma.midware.huaxia.movie;

/**
 * 电影信息
 */
public class MovieInfo {

    /**
     * 电影文件名
     */
    private String mFileName;

    /**
     * 电影海报名
     */
    private String mPosterName;

    /**
     * 影片显示名
     */
    private String mDisplayName;

    public String getmFileName() {
        return mFileName;
    }

    public void setmFileName(String mFileName) {
        this.mFileName = mFileName;
    }

    public String getmPosterName() {
        return mPosterName;
    }

    public void setmPosterName(String mPosterName) {
        this.mPosterName = mPosterName;
    }

    public String getmDisplayName() {
        return mDisplayName;
    }

    public void setmDisplayName(String mDisplayName) {
        this.mDisplayName = mDisplayName;
    }

    @Override
    public String toString() {
        return "MovieInfo{" +
                "mFileName='" + mFileName + '\'' +
                ", mPosterName='" + mPosterName + '\'' +
                ", mDisplayName='" + mDisplayName + '\'' +
                '}';
    }
}
