package com.suma.midware.huaxia.movie;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.google.gson.Gson;
import com.suma.midware.huaxia.movie.mvp.base.BaseMvpPresenter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoviePresenter extends BaseMvpPresenter<IMovieFilesContract.IView>
        implements IMovieFilesContract.IPrenseter {

    private static final String TAG = "MoviePresenter";

    /**
     * 根路径
     */
    private String mRootPath;

    /**
     * 影片列表
     */
    private List<MovieInfo> mMovieList;

    /**
     * 文件URI
     */
    private Uri mUri;

    /**
     * 文件类型
     */
    private String mFileType;

    /* 扩展名和mime类型扩展支持集合 */
    private final Map<String, String> MIME_MAP = new HashMap<String, String>();


    public MoviePresenter(IMovieFilesContract.IView view) {
        super(view);

        mMovieList = new ArrayList<>();
//        mRootPath="/mnt/sda/sda1/ftp2";
        mRootPath = getSystemPropertie("sys.local.path");
        if (TextUtils.isEmpty(mRootPath)) {
            mFileType = "/mnt/sda/sda1/ftp";
        }
        if (mRootPath.substring(mRootPath.length() - 1).equals("/")) {
            mRootPath = mRootPath.substring(0, mRootPath.length() - 1);
        }
//        Log.d(TAG, "-->>mRootPath=" + mRootPath);
    }

    @Override
    protected IMovieFilesContract.IView getEmptyView() {
        return IMovieFilesContract.emptyView;
    }

    @Override
    public void loadLocalFile() {
        //加载本地文件
        File file = new File(mRootPath);
        File[] tempList = file.listFiles();
        MovieInfo movieInfo;
        for (int i = 0; i < tempList.length; i++) {
            if (!tempList[i].isFile()) {
                String jsonFile = getMovieInfoFromFiles(mRootPath, tempList[i].getName());
                if (TextUtils.isEmpty(jsonFile)) {
                    continue;
                }
//                Log.i(TAG, "-->>jsonFile=" +jsonFile);
                movieInfo = new Gson().fromJson(jsonFile, MovieInfo.class);
                if (movieInfo == null) {
                    continue;
                }
                String posterPath = getNewFilePath(
                        mRootPath, tempList[i].getName(), movieInfo.getPosterName());
                String moviePath = getNewFilePath(
                        mRootPath, tempList[i].getName(), movieInfo.getFileName());
                movieInfo.setPosterName(posterPath);
                movieInfo.setFileName(moviePath);
                mMovieList.add(movieInfo);
            }
        }

        //更新影片列表
//        for (int i = 0;i < 10;i++){
//            MovieInfo movieInfo = new MovieInfo();
//            String index = String.valueOf(i+1);
//            movieInfo.setmDisplayName(index+"-边境迷雾电影名称测试滚动");
//            mMovieList.add(movieInfo);
//        }

        getView().updataMovieList(mMovieList);
    }

    @Override
    public void getFileUri(int position) {
        String moviePath = mMovieList.get(position).getFileName();
        mUri = Uri.fromFile(new File(moviePath));

    }

    @Override
    public void getFileType(int position) {
        String moviePath = mMovieList.get(position).getFileName();
        mFileType = getFileMimeType(moviePath);
        if (TextUtils.isEmpty(mFileType)) {
            mFileType = getExtensionMimeType(moviePath, true);
        }

//        Log.i(TAG, "-->>Uri="+mUri+"type="+mFileType);
        getView().startPlay(mUri, mFileType);
    }

    /**
     * 从文件夹中获取影片信息字符串
     *
     * @param rootPath   根路径
     * @param folderName 文件夹名
     * @return
     */
    private String getMovieInfoFromFiles(String rootPath, String folderName) {
        String fileName = rootPath + "/" + folderName;
        File movieFile = new File(fileName);
        File[] files = movieFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()){
                String extension = getExtensionName(files[i].getName());
                Log.d(TAG, "-->>extension="+extension);
                if (files[i].isFile() && "json".equals(extension)) {
                    //找到了json文件
                    Log.d(TAG, "-->>name="+files[i].getName());
                    String json = readString(fileName, files[i].getName());
                    return json;
//                Log.i(TAG, "-->>json=" + json);
                }
            }

        }
        return null;
    }

    /**
     * 读取json字符串
     *
     * @param rootPath 父路径
     * @param fileName 文件名
     * @return
     */
    private String readString(String rootPath, String fileName) {
        File file = new File(rootPath + "/" + fileName);
        FileInputStream fis = null;
        String json = null;
        try {
            fis = new FileInputStream(file);
            int length = fis.available();
            byte[] buffer = new byte[length];
            fis.read(buffer);
            json = new String(buffer, "UTF-8");
            fis.close();
            return json;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * 获取海报路径
     *
     * @param rootPath   根路径
     * @param folderPath 文件夹名称
     * @param fileName   海报文件名
     * @return
     */
    private String getNewFilePath(String rootPath, String folderPath, String fileName) {
        StringBuilder sb = new StringBuilder(rootPath);
        sb.append("/");
        sb.append(folderPath);
        sb.append("/");
        sb.append(fileName);
        return sb.toString();
    }

    /**
     * 获取指定路径文件的mimeType描述
     *
     * @param path 路径
     * @return 文件mimeType描述
     */
    private String getFileMimeType(String path) {
        if (TextUtils.isEmpty(path)) {
            Log.e(TAG, "getFileMimeType illegal path=" + path);
            return null;
        }
        Class<?> mediaCls = ClassUtil.loadClass("android.media.MediaFile");
        if (mediaCls != null) {
            Object obj = ClassUtil.callMethod(ClassUtil.getMethod(mediaCls,
                    "getMimeTypeForFile", String.class), null, path);
            if (obj instanceof String) {
                return (String) obj;
            } else {
                Log.w(TAG,
                        "getFileMimeType illegal getMimeTypeForFile return value");
            }
        } else {
            Log.w(TAG, "getFileMimeType not found MediaFile class");
        }
        return null;
    }

    /**
     * 获取指定扩展后缀名称的mimeType描述
     *
     * @param extension 扩展名称
     * @return mimeType描述
     */
    private String getExtensionMimeType(String extension) {
        return getExtensionMimeType(extension, false);
    }

    /**
     * 获取指定扩展后缀名称的mimeType描述
     *
     * @param extension 扩展名称
     * @param isExtend  是否使用自身扩展的内容
     * @return mimeType描述
     */
    private String getExtensionMimeType(String extension,
                                        boolean isExtend) {
        if (TextUtils.isEmpty(extension)) {
            Log.e(TAG, "getExtensionMimeType illegal extension=" + extension);
            return null;
        }
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                extension);
        if (TextUtils.isEmpty(mimeType) && isExtend) {
            mimeType = MIME_MAP.get(extension.toUpperCase());
        }
        return mimeType;
    }

    private String getSystemPropertie(String key) {
        String value = "";
        try {
            Method method = Class.forName("android.os.SystemProperties").getMethod("get", String.class, String.class);
            value = ((String) method.invoke(null, key, "")).toString();
            // 对部分非法数据进行处理
            if (value == null || value.equalsIgnoreCase("unknown")) {
                value = "";
            } else {
                value = value.trim();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 获取扩展名
     *
     * @param fileName
     * @return
     */
    private String getExtensionName(String fileName) {
//        Log.d(TAG, "-->>getExtensionName name="+fileName);
        if (!TextUtils.isEmpty(fileName)) {
            int dot = fileName.lastIndexOf('.');
            if ((dot > -1) && (dot < (fileName.length() - 1))) {
                return fileName.substring(dot + 1);
            }
        }
        return fileName;
    }
}


