package mobi.gxsd.gxsd_android.Tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * 设备信息公共业务处理
 * Created by pengliying on 2018-05-30.
 */

public class ContantsUtils {
    //兼容各个不同版本的sdcard目录文件
    public static final String[] ALL_SD_PATH = {"/mnt/sdcard", "/mnt/external_sd", "/mnt/sdcard2", "/storage/sdcard1", "/sdcard", "/mnt/extSdCard", "/mnt/sdcard/external_sd", "/mnt/external1", "/mnt/sdcard-ext", "/sdcard/_ExternalSD", "/sdcard2", "/mnt/ext_card", "/storage/extSdCard", "/emmc", "/sdcard/external_sd", "/mnt/emmc", "/storage/sdcard0", "/mnt/sdcard/external_storage/sdcard1", "/mnt/extsd"};
    public static final String FILE_DIR = ".usersdata";//定义共享文件夹目录(默认隐藏）
    public static final String FILE_LOGIN_NAME = "login.data";
    public static final String FILE_USERS_NAME = "users.data";

    public static String findSdPath() {
        for (String path : ALL_SD_PATH) {
            File file = new File(path);
            if (file.exists() && file.isDirectory()) {
                return path;
            }
        }
        return "";
    }

    /**
     * 缓存文件总目录路径
     * @param dir
     * @return
     */
    public static String getCacheFileDir(String dir) {
        return findSdPath().concat("/").concat(dir);
    }

    /**
     * 缓存文件获取(login/users信息）路径
     * @param dir
     * @return
     */
    public static String getCacheDataFile(String dir,String fileName) {
        return findSdPath().concat("/").concat(dir).concat(fileName);
    }


    /**
     * 读取文件数据内容
     * @param  filePath 读取指定文件地址
     */
    public static String readFileContent(String filePath) {
        StringBuffer listString = new StringBuffer();
        BufferedReader br = null;
        FileReader fr;
        try {
            fr = new FileReader(new File(filePath));
            br = new BufferedReader(fr);
            String contentLine = br.readLine();
            listString.append(contentLine);
            while (contentLine != null) {
                contentLine = br.readLine();
                if (null != contentLine && !"".equals(contentLine)) {
                    listString.append(contentLine);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                System.out.println("Error in closing the BufferedReader");
            }
        }
        return listString.toString();
    }

    /**
     * 写入内容
     * @param str 字符内容
     * @param saveFile 写入文件路径
     */
    public static void saveStringToFile(String str,String saveFile) {
        try {
            File file = new File(saveFile);
            if (!file.exists()) {
                File dir = new File(file.getParent());
                if ( null != dir && !dir.exists()){
                    dir.mkdirs();
                }
                file.createNewFile();
            }
            FileOutputStream outStream = new FileOutputStream(file);
            outStream.write(str.getBytes());
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
