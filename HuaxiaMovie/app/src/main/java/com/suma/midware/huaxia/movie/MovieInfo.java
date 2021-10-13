package com.suma.midware.huaxia.movie;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 电影信息
 */
public class MovieInfo implements Serializable {

    /**
     * 电影文件名
     */
    @SerializedName("fileName")
    private String mFileName;

    /**
     * 电影海报名
     */
    @SerializedName("posterName")
    private String mPosterName;

    /**
     * 影片显示名
     */
    @SerializedName("displayName")
    private String mDisplayName;

    public String getFileName() {
        return mFileName;
    }

    public void setFileName(String fileName) {
        mFileName = fileName;
    }

    public String getPosterName() {
        return mPosterName;
    }

    public void setPosterName(String posterName) {
        mPosterName = posterName;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public void setDisplayName(String displayName) {
        mDisplayName = displayName;
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
