package com.suma.midware.huaxia.movie;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.google.gson.Gson;
import com.suma.midware.huaxia.movie.mvp.base.BaseMvpPresenter;
import com.suma.midware.huaxia.movie.util.ClassUtil;
import com.suma.midware.huaxia.movie.util.SystemPropertyUtil;

import org.w3c.dom.Text;

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

    /**
     * 扩展名和mime类型扩展支持集合
     */
    private final Map<String, String> MIME_MAP = new HashMap<String, String>();

    /**
     * 是否检测json文件
     */
    private boolean mIsCheckJson = false;


    public MoviePresenter(IMovieFilesContract.IView view) {
        super(view);

        mMovieList = new ArrayList<>();
//        mRootPath="/mnt/sda/sda1/ftp2";
        mRootPath = SystemPropertyUtil.getSystemPropertie("persist.sys.local.path");
//        Log.d(TAG, "-->>mRootPath=" + mRootPath);
        if (TextUtils.isEmpty(mRootPath)) {
            mRootPath = "/mnt/sda/sda1/ftp";
        }

        int lastLen = mRootPath.length() - 1;
        Log.d(TAG, "-->>lastLen=" + lastLen);
        if (mRootPath.substring(lastLen).equals("/")) {
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
        mMovieList.clear();
        //加载本地文件
        File file = new File(mRootPath);
        if (file == null) {
            getView().showNoList();
            return;
        }
        File[] tempList = file.listFiles();
        MovieInfo movieInfo;
        if (tempList == null || tempList.length == 0) {
            getView().showNoList();
            return;
        }
        for (int i = 0; i < tempList.length; i++) {
            if (!tempList[i].isFile()) {
                if (mIsCheckJson) {
                    String jsonFile = getJsonFromFiles(mRootPath, tempList[i].getName());
                    if (TextUtils.isEmpty(jsonFile)) {
                        continue;
                    }
                    movieInfo = new Gson().fromJson(jsonFile, MovieInfo.class);
                    if (movieInfo == null) {
                        continue;
                    }
                    String posterPath = getNewFilePath(
                            mRootPath, tempList[i].getName(), movieInfo.getPosterName());
                    String moviePath = getPosterPath(
                            mRootPath, tempList[i].getName(), movieInfo.getFileName());
                    movieInfo.setPosterName(posterPath);
                    movieInfo.setFileName(moviePath);
                } else {
                    String movieName = getFilePathByExtionsion(
                            mRootPath, tempList[i].getName(), "ts");
                    Log.i(TAG, "-->>movieName=" + movieName);
                    if (TextUtils.isEmpty(movieName)) {
                        Log.e(TAG, "moviePath is empty");
                        continue;
                    }
                    String posterPath = getPosterPath(
                            mRootPath, tempList[i].getName(), movieName);
                    movieInfo = new MovieInfo();
                    movieInfo.setFileName(mRootPath + "/" + tempList[i].getName() + "/" + movieName);
                    movieInfo.setPosterName(posterPath);
//                    Log.d(TAG, "-->name=" + movieInfo.getFileName());
//                    Log.d(TAG, "-->poster=" + movieInfo.getPosterName());
                }
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

        if (mMovieList.isEmpty()) {
            getView().showNoList();
            return;
        }
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
    private String getJsonFromFiles(String rootPath, String folderName) {
        String fileName = rootPath + "/" + folderName;
        File movieFile = new File(fileName);
        File[] files = movieFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                String extension = getExtensionName(files[i].getName());
                Log.d(TAG, "-->>extension=" + extension);
                if ("json".equals(extension)) {
                    //找到了json文件
//                    Log.d(TAG, "-->>name=" + files[i].getName());
                    String json = readString(fileName, files[i].getName());
                    return json;
//                Log.i(TAG, "-->>json=" + json);
                }
            }

        }
        return null;
    }

    /**
     * 通过扩展名获取文件路径
     *
     * @param rootPath   根路径
     * @param folderName 文件夹名
     * @param ext        扩展名
     * @return
     */
    private String getFilePathByExtionsion(String rootPath, String folderName, String ext) {
        String fileName = rootPath + "/" + folderName;
        File movieFile = new File(fileName);
        File[] files = movieFile.listFiles();
        if (files == null || files.length == 0) {
            Log.i(TAG, "-->>getFilePathByExtionsion files is empty");
            return null;
        }
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                String extension = getExtensionName(files[i].getName());
//                Log.d(TAG, "-->>extension=" + extension);
                if (ext.equals(extension)) {
                    String path = files[i].getName();
//                    Log.i(TAG, "-->>path=" + path);
                    return path;
                }
            }

        }
        return null;
    }

    /**
     * 获取海报路径
     *
     * @param rootPath
     * @param folderName
     * @param movieName
     * @return
     */
    private String getPosterPath(String rootPath, String folderName, String movieName) {
        String fileName = rootPath + "/" + folderName;
        File movieFile = new File(fileName);
        File[] files = movieFile.listFiles();
        if (files == null || files.length == 0) {
            Log.e(TAG, "getPosterPath files is empty");
            return null;
        }
        if (TextUtils.isEmpty(movieName) || movieName.length() < 3) {
            Log.e(TAG, "getPosterPath movieName is empty");
            return null;
        }
        movieName = movieName.substring(0, movieName.length() - 3);
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                String extension = getExtensionName(files[i].getName());
                if (files[i].getName().contains(movieName) &&
                        ("jpg".equals(extension) || "png".equals(extension))) {
                    String path = fileName + "/" + files[i].getName();
                    Log.i(TAG, "-->>path=" + path);
                    return path;
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

    /**
     * 获取扩展名
     *
     * @param fileName 文件路径
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


