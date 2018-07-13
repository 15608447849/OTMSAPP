package ping.otmsapp.utils;

import java.io.File;
import java.io.FileInputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;

/**
 * Created by user on 2018/3/13.
 */

public class MD5Util {
    /**
     * 获取一段字节数组的md5
     * @param buffer
     * @return
     */
    public static byte[] getBytesMd5(byte[] buffer) {
        byte[] result = null;
        try {
            result =  MessageDigest.getInstance("MD5").digest(buffer);
        } catch (Exception e) {
        }
        return result;
    }

    /**
     * byte->16进制字符串
     * @param bytes
     * @return
     */
    public static String byteToHexString(byte[] bytes) {
        StringBuffer hexStr = new StringBuffer();
        int num;
        for (int i = 0; i < bytes.length; i++) {
            num = bytes[i];
            if(num < 0) {
                num += 256;
            }
            if(num < 16){
                hexStr.append("0");
            }
            hexStr.append(Integer.toHexString(num));
        }
        return hexStr.toString().toUpperCase();
    }


    /**
     * MD5 string 加密
     * @param str
     * @return
     */
    public static String encryption(String str){
        return byteToHexString(getBytesMd5(str.getBytes()));
    }

    /**
     * 获取文件MD5的String
     * @param file
     * @return
     */
    public static String getFileMd5ByString(File file) throws Exception {
        return byteToHexString(getFileMd5(file));
    }
    /**
     * 获取文件md5的byte值
     *
     * @param file
     * @return
     */
    public static byte[] getFileMd5(File file){
        byte[] result = null;
        try {
            try(DigestInputStream digestInputStream = new DigestInputStream(new FileInputStream(file),MessageDigest.getInstance("MD5"))){
                byte[] buffer =new byte[512];
                while (digestInputStream.read(buffer) > 0);
                result = digestInputStream.getMessageDigest().digest();
            }
        } catch (Exception e) {
        }
        return result;
    }
}
