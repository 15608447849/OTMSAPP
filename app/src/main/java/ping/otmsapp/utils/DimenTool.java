package ping.otmsapp.utils;

import com.amap.api.maps.model.LatLng;
import com.amap.api.trace.TraceLocation;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leeping on 2018/3/23.
 * email: 793065165@qq.com
 * 快速生成适配工具类
 */

public class DimenTool {
    public static void gen() {
        //以此文件夹下的dimens.xml文件内容为初始值参照
        File file = new File("./app/src/main/res/values/dimens.xml");
        String sw240file = "./app/src/main/res/values-sw240dp/dimens.xml";
        BufferedReader reader = null;
        StringBuilder sw240= new StringBuilder();
        try {

            reader = new BufferedReader(new FileReader(file));
            String tempString;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                if (tempString.contains("</dimen>")) {
                    String start = tempString.substring(0, tempString.indexOf(">") + 1);
                    String end = tempString.substring(tempString.lastIndexOf("<") - 2);
                    //截取<dimen></dimen>标签内的内容，从>右括号开始，到左括号减2，取得配置的数字
                    Double num = Double.parseDouble
                            (tempString.substring(tempString.indexOf(">") + 1,
                                    tempString.indexOf("</dimen>") - 2));
                    //根据不同的尺寸，计算新的值，拼接新的字符串，并且结尾处换行。
                    sw240.append(start).append( num * 0.75).append(end).append("\r\n");
                } else {
                    sw240.append(tempString).append("");
                }
                line++;
            }
            reader.close();
            //将新的内容，写入到指定的文件中去
            new File(sw240file.substring(0,sw240file.lastIndexOf("/"))).mkdirs();
            writeFile(sw240file, sw240.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {

                    e1.printStackTrace();
                }
            }
        }
        System.out.println("自动生成不同分辨率完成");
    }


    /**
     * 写入方法
     *
     */

    public static void writeFile(String file, String text) {

        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            out.println(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.close();

    }
    public static void main(String[] args) {
//        gen();
    }

    public static List<TraceLocation> getLatLon() {
         String json = "[{\n" +
                 "\t\"time\": 1522671776000,\n" +
                 "\t\"speed\": 0,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.194896,\n" +
                 "\t\t\"longitude\": 112.994802\n" +
                 "\t},\n" +
                 "\t\"bearing\": 0\n" +
                 "}, {\n" +
                 "\t\"time\": 1522671824000,\n" +
                 "\t\"speed\": 0,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.194889,\n" +
                 "\t\t\"longitude\": 112.994265\n" +
                 "\t},\n" +
                 "\t\"bearing\": 0\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672361000,\n" +
                 "\t\"speed\": 31,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.194472,\n" +
                 "\t\t\"longitude\": 112.993838\n" +
                 "\t},\n" +
                 "\t\"bearing\": 271\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672365000,\n" +
                 "\t\"speed\": 28,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.194775,\n" +
                 "\t\t\"longitude\": 112.993405\n" +
                 "\t},\n" +
                 "\t\"bearing\": 271\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672371000,\n" +
                 "\t\"speed\": 16,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.194847,\n" +
                 "\t\t\"longitude\": 112.992891\n" +
                 "\t},\n" +
                 "\t\"bearing\": 273\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672417000,\n" +
                 "\t\"speed\": 0,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.195042,\n" +
                 "\t\t\"longitude\": 112.992383\n" +
                 "\t},\n" +
                 "\t\"bearing\": 0\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672423000,\n" +
                 "\t\"speed\": 11,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.195057,\n" +
                 "\t\t\"longitude\": 112.992196\n" +
                 "\t},\n" +
                 "\t\"bearing\": 282\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672431000,\n" +
                 "\t\"speed\": 19,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.195112,\n" +
                 "\t\t\"longitude\": 112.991997\n" +
                 "\t},\n" +
                 "\t\"bearing\": 336\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672438000,\n" +
                 "\t\"speed\": 29,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.195567,\n" +
                 "\t\t\"longitude\": 112.991882\n" +
                 "\t},\n" +
                 "\t\"bearing\": 345\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672444000,\n" +
                 "\t\"speed\": 31,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.196063,\n" +
                 "\t\t\"longitude\": 112.991805\n" +
                 "\t},\n" +
                 "\t\"bearing\": 349\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672450000,\n" +
                 "\t\"speed\": 0,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.196541,\n" +
                 "\t\t\"longitude\": 112.991756\n" +
                 "\t},\n" +
                 "\t\"bearing\": 0\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672454000,\n" +
                 "\t\"speed\": 24,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.196756,\n" +
                 "\t\t\"longitude\": 112.991668\n" +
                 "\t},\n" +
                 "\t\"bearing\": 347\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672462000,\n" +
                 "\t\"speed\": 6,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.197055,\n" +
                 "\t\t\"longitude\": 112.991692\n" +
                 "\t},\n" +
                 "\t\"bearing\": 21\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672471000,\n" +
                 "\t\"speed\": 12,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.197204,\n" +
                 "\t\t\"longitude\": 112.991685\n" +
                 "\t},\n" +
                 "\t\"bearing\": 341\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672477000,\n" +
                 "\t\"speed\": 26,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.197649,\n" +
                 "\t\t\"longitude\": 112.991525\n" +
                 "\t},\n" +
                 "\t\"bearing\": 350\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672532000,\n" +
                 "\t\"speed\": 5,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.197623,\n" +
                 "\t\t\"longitude\": 112.991367\n" +
                 "\t},\n" +
                 "\t\"bearing\": 216\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672614000,\n" +
                 "\t\"speed\": 18,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.197767,\n" +
                 "\t\t\"longitude\": 112.991297\n" +
                 "\t},\n" +
                 "\t\"bearing\": 331\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672619000,\n" +
                 "\t\"speed\": 0,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.198257,\n" +
                 "\t\t\"longitude\": 112.991272\n" +
                 "\t},\n" +
                 "\t\"bearing\": 0\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672624000,\n" +
                 "\t\"speed\": 0,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.198772,\n" +
                 "\t\t\"longitude\": 112.991288\n" +
                 "\t},\n" +
                 "\t\"bearing\": 0\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672711000,\n" +
                 "\t\"speed\": 19,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.203665,\n" +
                 "\t\t\"longitude\": 112.992392\n" +
                 "\t},\n" +
                 "\t\"bearing\": 300\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672726000,\n" +
                 "\t\"speed\": 2,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.203652,\n" +
                 "\t\t\"longitude\": 112.991954\n" +
                 "\t},\n" +
                 "\t\"bearing\": 214\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672740000,\n" +
                 "\t\"speed\": 13,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.203649,\n" +
                 "\t\t\"longitude\": 112.991793\n" +
                 "\t},\n" +
                 "\t\"bearing\": 260\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672749000,\n" +
                 "\t\"speed\": 17,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.203654,\n" +
                 "\t\t\"longitude\": 112.991268\n" +
                 "\t},\n" +
                 "\t\"bearing\": 260\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672760000,\n" +
                 "\t\"speed\": 15,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.203775,\n" +
                 "\t\t\"longitude\": 112.990802\n" +
                 "\t},\n" +
                 "\t\"bearing\": 322\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672764000,\n" +
                 "\t\"speed\": 17,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.203946,\n" +
                 "\t\t\"longitude\": 112.990792\n" +
                 "\t},\n" +
                 "\t\"bearing\": 8\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672771000,\n" +
                 "\t\"speed\": 30,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.20443,\n" +
                 "\t\t\"longitude\": 112.990913\n" +
                 "\t},\n" +
                 "\t\"bearing\": 8\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672777000,\n" +
                 "\t\"speed\": 40,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.204999,\n" +
                 "\t\t\"longitude\": 112.990988\n" +
                 "\t},\n" +
                 "\t\"bearing\": 4\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672781000,\n" +
                 "\t\"speed\": 47,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.205475,\n" +
                 "\t\t\"longitude\": 112.991013\n" +
                 "\t},\n" +
                 "\t\"bearing\": 1\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672790000,\n" +
                 "\t\"speed\": 40,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.206446,\n" +
                 "\t\t\"longitude\": 112.991155\n" +
                 "\t},\n" +
                 "\t\"bearing\": 6\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672796000,\n" +
                 "\t\"speed\": 30,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.206929,\n" +
                 "\t\t\"longitude\": 112.991325\n" +
                 "\t},\n" +
                 "\t\"bearing\": 5\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672803000,\n" +
                 "\t\"speed\": 19,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.207433,\n" +
                 "\t\t\"longitude\": 112.991254\n" +
                 "\t},\n" +
                 "\t\"bearing\": 10\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672809000,\n" +
                 "\t\"speed\": 34,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.207927,\n" +
                 "\t\t\"longitude\": 112.991302\n" +
                 "\t},\n" +
                 "\t\"bearing\": 6\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672815000,\n" +
                 "\t\"speed\": 33,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.208442,\n" +
                 "\t\t\"longitude\": 112.991451\n" +
                 "\t},\n" +
                 "\t\"bearing\": 11\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672822000,\n" +
                 "\t\"speed\": 29,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.20897,\n" +
                 "\t\t\"longitude\": 112.99153\n" +
                 "\t},\n" +
                 "\t\"bearing\": 5\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672828000,\n" +
                 "\t\"speed\": 34,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.209479,\n" +
                 "\t\t\"longitude\": 112.991615\n" +
                 "\t},\n" +
                 "\t\"bearing\": 13\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672834000,\n" +
                 "\t\"speed\": 25,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.209931,\n" +
                 "\t\t\"longitude\": 112.991732\n" +
                 "\t},\n" +
                 "\t\"bearing\": 12\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672841000,\n" +
                 "\t\"speed\": 3,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.210147,\n" +
                 "\t\t\"longitude\": 112.99174\n" +
                 "\t},\n" +
                 "\t\"bearing\": 88\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672856000,\n" +
                 "\t\"speed\": 8,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.210298,\n" +
                 "\t\t\"longitude\": 112.991789\n" +
                 "\t},\n" +
                 "\t\"bearing\": 3\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672863000,\n" +
                 "\t\"speed\": 30,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.210765,\n" +
                 "\t\t\"longitude\": 112.99171\n" +
                 "\t},\n" +
                 "\t\"bearing\": 3\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672865000,\n" +
                 "\t\"speed\": 34,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.210967,\n" +
                 "\t\t\"longitude\": 112.991717\n" +
                 "\t},\n" +
                 "\t\"bearing\": 359\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672867000,\n" +
                 "\t\"speed\": 32,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.211155,\n" +
                 "\t\t\"longitude\": 112.991796\n" +
                 "\t},\n" +
                 "\t\"bearing\": 4\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672871000,\n" +
                 "\t\"speed\": 39,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.211661,\n" +
                 "\t\t\"longitude\": 112.991839\n" +
                 "\t},\n" +
                 "\t\"bearing\": 3\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672878000,\n" +
                 "\t\"speed\": 21,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.212091,\n" +
                 "\t\t\"longitude\": 112.992082\n" +
                 "\t},\n" +
                 "\t\"bearing\": 11\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672891000,\n" +
                 "\t\"speed\": 8,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.212446,\n" +
                 "\t\t\"longitude\": 112.991989\n" +
                 "\t},\n" +
                 "\t\"bearing\": 331\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672907000,\n" +
                 "\t\"speed\": 7,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.212847,\n" +
                 "\t\t\"longitude\": 112.992027\n" +
                 "\t},\n" +
                 "\t\"bearing\": 8\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672912000,\n" +
                 "\t\"speed\": 2,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.212983,\n" +
                 "\t\t\"longitude\": 112.9921\n" +
                 "\t},\n" +
                 "\t\"bearing\": 89\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672921000,\n" +
                 "\t\"speed\": 16,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.213105,\n" +
                 "\t\t\"longitude\": 112.992657\n" +
                 "\t},\n" +
                 "\t\"bearing\": 95\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672930000,\n" +
                 "\t\"speed\": 18,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.213107,\n" +
                 "\t\t\"longitude\": 112.993191\n" +
                 "\t},\n" +
                 "\t\"bearing\": 91\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672937000,\n" +
                 "\t\"speed\": 24,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.213068,\n" +
                 "\t\t\"longitude\": 112.993756\n" +
                 "\t},\n" +
                 "\t\"bearing\": 87\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672941000,\n" +
                 "\t\"speed\": 35,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.21298,\n" +
                 "\t\t\"longitude\": 112.994262\n" +
                 "\t},\n" +
                 "\t\"bearing\": 95\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672948000,\n" +
                 "\t\"speed\": 26,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.213076,\n" +
                 "\t\t\"longitude\": 112.99478\n" +
                 "\t},\n" +
                 "\t\"bearing\": 88\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672956000,\n" +
                 "\t\"speed\": 23,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.213059,\n" +
                 "\t\t\"longitude\": 112.995351\n" +
                 "\t},\n" +
                 "\t\"bearing\": 89\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672963000,\n" +
                 "\t\"speed\": 22,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.213076,\n" +
                 "\t\t\"longitude\": 112.995878\n" +
                 "\t},\n" +
                 "\t\"bearing\": 85\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672982000,\n" +
                 "\t\"speed\": 11,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.213194,\n" +
                 "\t\t\"longitude\": 112.99618\n" +
                 "\t},\n" +
                 "\t\"bearing\": 30\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672988000,\n" +
                 "\t\"speed\": 15,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.213421,\n" +
                 "\t\t\"longitude\": 112.996277\n" +
                 "\t},\n" +
                 "\t\"bearing\": 353\n" +
                 "}, {\n" +
                 "\t\"time\": 1522672996000,\n" +
                 "\t\"speed\": 2,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.213669,\n" +
                 "\t\t\"longitude\": 112.996384\n" +
                 "\t},\n" +
                 "\t\"bearing\": 286\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673000000,\n" +
                 "\t\"speed\": 10,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.213791,\n" +
                 "\t\t\"longitude\": 112.996469\n" +
                 "\t},\n" +
                 "\t\"bearing\": 18\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673008000,\n" +
                 "\t\"speed\": 24,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.214291,\n" +
                 "\t\t\"longitude\": 112.996541\n" +
                 "\t},\n" +
                 "\t\"bearing\": 4\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673014000,\n" +
                 "\t\"speed\": 23,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.214694,\n" +
                 "\t\t\"longitude\": 112.99679\n" +
                 "\t},\n" +
                 "\t\"bearing\": 27\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673021000,\n" +
                 "\t\"speed\": 17,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.215047,\n" +
                 "\t\t\"longitude\": 112.997169\n" +
                 "\t},\n" +
                 "\t\"bearing\": 50\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673032000,\n" +
                 "\t\"speed\": 14,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.21537,\n" +
                 "\t\t\"longitude\": 112.997531\n" +
                 "\t},\n" +
                 "\t\"bearing\": 53\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673042000,\n" +
                 "\t\"speed\": 12,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.215704,\n" +
                 "\t\t\"longitude\": 112.997877\n" +
                 "\t},\n" +
                 "\t\"bearing\": 42\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673055000,\n" +
                 "\t\"speed\": 11,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.216028,\n" +
                 "\t\t\"longitude\": 112.998255\n" +
                 "\t},\n" +
                 "\t\"bearing\": 34\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673063000,\n" +
                 "\t\"speed\": 2,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.216176,\n" +
                 "\t\t\"longitude\": 112.998473\n" +
                 "\t},\n" +
                 "\t\"bearing\": 86\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673072000,\n" +
                 "\t\"speed\": 14,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.216264,\n" +
                 "\t\t\"longitude\": 112.998599\n" +
                 "\t},\n" +
                 "\t\"bearing\": 34\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673079000,\n" +
                 "\t\"speed\": 26,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.216635,\n" +
                 "\t\t\"longitude\": 112.998928\n" +
                 "\t},\n" +
                 "\t\"bearing\": 33\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673091000,\n" +
                 "\t\"speed\": 18,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.217066,\n" +
                 "\t\t\"longitude\": 112.999163\n" +
                 "\t},\n" +
                 "\t\"bearing\": 26\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673099000,\n" +
                 "\t\"speed\": 26,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.217509,\n" +
                 "\t\t\"longitude\": 112.999359\n" +
                 "\t},\n" +
                 "\t\"bearing\": 21\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673104000,\n" +
                 "\t\"speed\": 35,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.217964,\n" +
                 "\t\t\"longitude\": 112.999546\n" +
                 "\t},\n" +
                 "\t\"bearing\": 21\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673110000,\n" +
                 "\t\"speed\": 23,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.21841,\n" +
                 "\t\t\"longitude\": 112.999728\n" +
                 "\t},\n" +
                 "\t\"bearing\": 34\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673120000,\n" +
                 "\t\"speed\": 20,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.218767,\n" +
                 "\t\t\"longitude\": 113.000145\n" +
                 "\t},\n" +
                 "\t\"bearing\": 59\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673129000,\n" +
                 "\t\"speed\": 15,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.219087,\n" +
                 "\t\t\"longitude\": 113.000527\n" +
                 "\t},\n" +
                 "\t\"bearing\": 58\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673136000,\n" +
                 "\t\"speed\": 22,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.219352,\n" +
                 "\t\t\"longitude\": 113.001019\n" +
                 "\t},\n" +
                 "\t\"bearing\": 67\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673143000,\n" +
                 "\t\"speed\": 11,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.219618,\n" +
                 "\t\t\"longitude\": 113.001203\n" +
                 "\t},\n" +
                 "\t\"bearing\": 20\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673153000,\n" +
                 "\t\"speed\": 7,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.21995,\n" +
                 "\t\t\"longitude\": 113.001548\n" +
                 "\t},\n" +
                 "\t\"bearing\": 49\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673163000,\n" +
                 "\t\"speed\": 9,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.220273,\n" +
                 "\t\t\"longitude\": 113.001908\n" +
                 "\t},\n" +
                 "\t\"bearing\": 37\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673193000,\n" +
                 "\t\"speed\": 22,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.220622,\n" +
                 "\t\t\"longitude\": 113.002286\n" +
                 "\t},\n" +
                 "\t\"bearing\": 40\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673197000,\n" +
                 "\t\"speed\": 35,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.22092,\n" +
                 "\t\t\"longitude\": 113.002684\n" +
                 "\t},\n" +
                 "\t\"bearing\": 49\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673202000,\n" +
                 "\t\"speed\": 34,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.221262,\n" +
                 "\t\t\"longitude\": 113.003083\n" +
                 "\t},\n" +
                 "\t\"bearing\": 46\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673207000,\n" +
                 "\t\"speed\": 26,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.221603,\n" +
                 "\t\t\"longitude\": 113.003481\n" +
                 "\t},\n" +
                 "\t\"bearing\": 43\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673213000,\n" +
                 "\t\"speed\": 31,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.222035,\n" +
                 "\t\t\"longitude\": 113.003829\n" +
                 "\t},\n" +
                 "\t\"bearing\": 27\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673219000,\n" +
                 "\t\"speed\": 25,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.222469,\n" +
                 "\t\t\"longitude\": 113.004047\n" +
                 "\t},\n" +
                 "\t\"bearing\": 23\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673229000,\n" +
                 "\t\"speed\": 25,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.222836,\n" +
                 "\t\t\"longitude\": 113.004381\n" +
                 "\t},\n" +
                 "\t\"bearing\": 56\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673236000,\n" +
                 "\t\"speed\": 18,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.2231,\n" +
                 "\t\t\"longitude\": 113.004822\n" +
                 "\t},\n" +
                 "\t\"bearing\": 56\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673253000,\n" +
                 "\t\"speed\": 21,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.223339,\n" +
                 "\t\t\"longitude\": 113.005324\n" +
                 "\t},\n" +
                 "\t\"bearing\": 72\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673260000,\n" +
                 "\t\"speed\": 27,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.223437,\n" +
                 "\t\t\"longitude\": 113.005875\n" +
                 "\t},\n" +
                 "\t\"bearing\": 84\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673268000,\n" +
                 "\t\"speed\": 17,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.223449,\n" +
                 "\t\t\"longitude\": 113.006411\n" +
                 "\t},\n" +
                 "\t\"bearing\": 82\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673367000,\n" +
                 "\t\"speed\": 0,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.223559,\n" +
                 "\t\t\"longitude\": 113.006776\n" +
                 "\t},\n" +
                 "\t\"bearing\": 246\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673375000,\n" +
                 "\t\"speed\": 7,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.223641,\n" +
                 "\t\t\"longitude\": 113.006898\n" +
                 "\t},\n" +
                 "\t\"bearing\": 43\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673385000,\n" +
                 "\t\"speed\": 29,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.224171,\n" +
                 "\t\t\"longitude\": 113.00702\n" +
                 "\t},\n" +
                 "\t\"bearing\": 352\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673389000,\n" +
                 "\t\"speed\": 30,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.224551,\n" +
                 "\t\t\"longitude\": 113.007009\n" +
                 "\t},\n" +
                 "\t\"bearing\": 3\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673391000,\n" +
                 "\t\"speed\": 29,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.224696,\n" +
                 "\t\t\"longitude\": 113.006986\n" +
                 "\t},\n" +
                 "\t\"bearing\": 351\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673398000,\n" +
                 "\t\"speed\": 21,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.225188,\n" +
                 "\t\t\"longitude\": 113.006913\n" +
                 "\t},\n" +
                 "\t\"bearing\": 353\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673403000,\n" +
                 "\t\"speed\": 24,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.225465,\n" +
                 "\t\t\"longitude\": 113.006905\n" +
                 "\t},\n" +
                 "\t\"bearing\": 1\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673405000,\n" +
                 "\t\"speed\": 24,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.2256,\n" +
                 "\t\t\"longitude\": 113.006885\n" +
                 "\t},\n" +
                 "\t\"bearing\": 355\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673408000,\n" +
                 "\t\"speed\": 20,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.225758,\n" +
                 "\t\t\"longitude\": 113.006895\n" +
                 "\t},\n" +
                 "\t\"bearing\": 3\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673414000,\n" +
                 "\t\"speed\": 15,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.226075,\n" +
                 "\t\t\"longitude\": 113.006784\n" +
                 "\t},\n" +
                 "\t\"bearing\": 323\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673418000,\n" +
                 "\t\"speed\": 28,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.226563,\n" +
                 "\t\t\"longitude\": 113.006681\n" +
                 "\t},\n" +
                 "\t\"bearing\": 344\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673424000,\n" +
                 "\t\"speed\": 32,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.227074,\n" +
                 "\t\t\"longitude\": 113.006751\n" +
                 "\t},\n" +
                 "\t\"bearing\": 359\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673430000,\n" +
                 "\t\"speed\": 0,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.227552,\n" +
                 "\t\t\"longitude\": 113.006728\n" +
                 "\t},\n" +
                 "\t\"bearing\": 0\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673447000,\n" +
                 "\t\"speed\": 10,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.227712,\n" +
                 "\t\t\"longitude\": 113.00664\n" +
                 "\t},\n" +
                 "\t\"bearing\": 352\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673450000,\n" +
                 "\t\"speed\": 13,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.227846,\n" +
                 "\t\t\"longitude\": 113.006666\n" +
                 "\t},\n" +
                 "\t\"bearing\": 2\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673471000,\n" +
                 "\t\"speed\": 3,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.22913,\n" +
                 "\t\t\"longitude\": 113.006849\n" +
                 "\t},\n" +
                 "\t\"bearing\": 93\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673479000,\n" +
                 "\t\"speed\": 2,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.229266,\n" +
                 "\t\t\"longitude\": 113.006881\n" +
                 "\t},\n" +
                 "\t\"bearing\": 346\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673492000,\n" +
                 "\t\"speed\": 1,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.229367,\n" +
                 "\t\t\"longitude\": 113.006775\n" +
                 "\t},\n" +
                 "\t\"bearing\": 6\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673506000,\n" +
                 "\t\"speed\": 3,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.229502,\n" +
                 "\t\t\"longitude\": 113.006824\n" +
                 "\t},\n" +
                 "\t\"bearing\": 52\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673572000,\n" +
                 "\t\"speed\": 5,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.229734,\n" +
                 "\t\t\"longitude\": 113.00707\n" +
                 "\t},\n" +
                 "\t\"bearing\": 111\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673585000,\n" +
                 "\t\"speed\": 0,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.229801,\n" +
                 "\t\t\"longitude\": 113.007351\n" +
                 "\t},\n" +
                 "\t\"bearing\": 0\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673597000,\n" +
                 "\t\"speed\": 3,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.229785,\n" +
                 "\t\t\"longitude\": 113.007504\n" +
                 "\t},\n" +
                 "\t\"bearing\": 95\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673611000,\n" +
                 "\t\"speed\": 4,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.229681,\n" +
                 "\t\t\"longitude\": 113.007699\n" +
                 "\t},\n" +
                 "\t\"bearing\": 153\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673623000,\n" +
                 "\t\"speed\": 2,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.229512,\n" +
                 "\t\t\"longitude\": 113.007788\n" +
                 "\t},\n" +
                 "\t\"bearing\": 107\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673651000,\n" +
                 "\t\"speed\": 2,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.229329,\n" +
                 "\t\t\"longitude\": 113.008052\n" +
                 "\t},\n" +
                 "\t\"bearing\": 153\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673687000,\n" +
                 "\t\"speed\": 2,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.229237,\n" +
                 "\t\t\"longitude\": 113.008173\n" +
                 "\t},\n" +
                 "\t\"bearing\": 59\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673698000,\n" +
                 "\t\"speed\": 3,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.229121,\n" +
                 "\t\t\"longitude\": 113.008298\n" +
                 "\t},\n" +
                 "\t\"bearing\": 137\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673737000,\n" +
                 "\t\"speed\": 0,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.22898,\n" +
                 "\t\t\"longitude\": 113.008489\n" +
                 "\t},\n" +
                 "\t\"bearing\": 0\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673752000,\n" +
                 "\t\"speed\": 2,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.228869,\n" +
                 "\t\t\"longitude\": 113.008593\n" +
                 "\t},\n" +
                 "\t\"bearing\": 139\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673801000,\n" +
                 "\t\"speed\": 1,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.228612,\n" +
                 "\t\t\"longitude\": 113.008847\n" +
                 "\t},\n" +
                 "\t\"bearing\": 75\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673840000,\n" +
                 "\t\"speed\": 1,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.228529,\n" +
                 "\t\t\"longitude\": 113.009001\n" +
                 "\t},\n" +
                 "\t\"bearing\": 165\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673863000,\n" +
                 "\t\"speed\": 1,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.22837,\n" +
                 "\t\t\"longitude\": 113.009124\n" +
                 "\t},\n" +
                 "\t\"bearing\": 107\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673905000,\n" +
                 "\t\"speed\": 1,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.228152,\n" +
                 "\t\t\"longitude\": 113.009415\n" +
                 "\t},\n" +
                 "\t\"bearing\": 157\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673947000,\n" +
                 "\t\"speed\": 3,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.228076,\n" +
                 "\t\t\"longitude\": 113.009562\n" +
                 "\t},\n" +
                 "\t\"bearing\": 93\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673959000,\n" +
                 "\t\"speed\": 2,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.228008,\n" +
                 "\t\t\"longitude\": 113.009701\n" +
                 "\t},\n" +
                 "\t\"bearing\": 144\n" +
                 "}, {\n" +
                 "\t\"time\": 1522673982000,\n" +
                 "\t\"speed\": 3,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.227919,\n" +
                 "\t\t\"longitude\": 113.00989\n" +
                 "\t},\n" +
                 "\t\"bearing\": 91\n" +
                 "}, {\n" +
                 "\t\"time\": 1522674004000,\n" +
                 "\t\"speed\": 3,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.227796,\n" +
                 "\t\t\"longitude\": 113.010047\n" +
                 "\t},\n" +
                 "\t\"bearing\": 172\n" +
                 "}, {\n" +
                 "\t\"time\": 1522674033000,\n" +
                 "\t\"speed\": 1,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.227719,\n" +
                 "\t\t\"longitude\": 113.010244\n" +
                 "\t},\n" +
                 "\t\"bearing\": 120\n" +
                 "}, {\n" +
                 "\t\"time\": 1522674066000,\n" +
                 "\t\"speed\": 3,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.227627,\n" +
                 "\t\t\"longitude\": 113.010439\n" +
                 "\t},\n" +
                 "\t\"bearing\": 177\n" +
                 "}, {\n" +
                 "\t\"time\": 1522674081000,\n" +
                 "\t\"speed\": 2,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.227525,\n" +
                 "\t\t\"longitude\": 113.010557\n" +
                 "\t},\n" +
                 "\t\"bearing\": 112\n" +
                 "}, {\n" +
                 "\t\"time\": 1522674128000,\n" +
                 "\t\"speed\": 2,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.227244,\n" +
                 "\t\t\"longitude\": 113.010838\n" +
                 "\t},\n" +
                 "\t\"bearing\": 160\n" +
                 "}, {\n" +
                 "\t\"time\": 1522674159000,\n" +
                 "\t\"speed\": 2,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.227077,\n" +
                 "\t\t\"longitude\": 113.011009\n" +
                 "\t},\n" +
                 "\t\"bearing\": 114\n" +
                 "}, {\n" +
                 "\t\"time\": 1522674181000,\n" +
                 "\t\"speed\": 2,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.227083,\n" +
                 "\t\t\"longitude\": 113.011164\n" +
                 "\t},\n" +
                 "\t\"bearing\": 58\n" +
                 "}, {\n" +
                 "\t\"time\": 1522674199000,\n" +
                 "\t\"speed\": 1,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.227144,\n" +
                 "\t\t\"longitude\": 113.01133\n" +
                 "\t},\n" +
                 "\t\"bearing\": 116\n" +
                 "}, {\n" +
                 "\t\"time\": 1522674249000,\n" +
                 "\t\"speed\": 1,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.227335,\n" +
                 "\t\t\"longitude\": 113.011515\n" +
                 "\t},\n" +
                 "\t\"bearing\": 65\n" +
                 "}, {\n" +
                 "\t\"time\": 1522674272000,\n" +
                 "\t\"speed\": 0,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.227441,\n" +
                 "\t\t\"longitude\": 113.011626\n" +
                 "\t},\n" +
                 "\t\"bearing\": 4\n" +
                 "}, {\n" +
                 "\t\"time\": 1522674296000,\n" +
                 "\t\"speed\": 2,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.227543,\n" +
                 "\t\t\"longitude\": 113.011787\n" +
                 "\t},\n" +
                 "\t\"bearing\": 97\n" +
                 "}, {\n" +
                 "\t\"time\": 1522674309000,\n" +
                 "\t\"speed\": 4,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.227651,\n" +
                 "\t\t\"longitude\": 113.011921\n" +
                 "\t},\n" +
                 "\t\"bearing\": 40\n" +
                 "}, {\n" +
                 "\t\"time\": 1522674322000,\n" +
                 "\t\"speed\": 2,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.227674,\n" +
                 "\t\t\"longitude\": 113.012075\n" +
                 "\t},\n" +
                 "\t\"bearing\": 103\n" +
                 "}, {\n" +
                 "\t\"time\": 1522674337000,\n" +
                 "\t\"speed\": 1,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.227779,\n" +
                 "\t\t\"longitude\": 113.012182\n" +
                 "\t},\n" +
                 "\t\"bearing\": 48\n" +
                 "}, {\n" +
                 "\t\"time\": 1522674367000,\n" +
                 "\t\"speed\": 2,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.227989,\n" +
                 "\t\t\"longitude\": 113.012335\n" +
                 "\t},\n" +
                 "\t\"bearing\": 133\n" +
                 "}, {\n" +
                 "\t\"time\": 1522674425000,\n" +
                 "\t\"speed\": 2,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.228101,\n" +
                 "\t\t\"longitude\": 113.012501\n" +
                 "\t},\n" +
                 "\t\"bearing\": 80\n" +
                 "}, {\n" +
                 "\t\"time\": 1522674439000,\n" +
                 "\t\"speed\": 2,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.228183,\n" +
                 "\t\t\"longitude\": 113.012812\n" +
                 "\t},\n" +
                 "\t\"bearing\": 34\n" +
                 "}, {\n" +
                 "\t\"time\": 1522674457000,\n" +
                 "\t\"speed\": 2,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.228355,\n" +
                 "\t\t\"longitude\": 113.012895\n" +
                 "\t},\n" +
                 "\t\"bearing\": 96\n" +
                 "}, {\n" +
                 "\t\"time\": 1522674493000,\n" +
                 "\t\"speed\": 1,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.228471,\n" +
                 "\t\t\"longitude\": 113.013008\n" +
                 "\t},\n" +
                 "\t\"bearing\": 6\n" +
                 "}, {\n" +
                 "\t\"time\": 1522674507000,\n" +
                 "\t\"speed\": 1,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.228599,\n" +
                 "\t\t\"longitude\": 113.01308\n" +
                 "\t},\n" +
                 "\t\"bearing\": 59\n" +
                 "}, {\n" +
                 "\t\"time\": 1522674517000,\n" +
                 "\t\"speed\": 3,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.228686,\n" +
                 "\t\t\"longitude\": 113.013205\n" +
                 "\t},\n" +
                 "\t\"bearing\": 353\n" +
                 "}, {\n" +
                 "\t\"time\": 1522674537000,\n" +
                 "\t\"speed\": 3,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.228743,\n" +
                 "\t\t\"longitude\": 113.013352\n" +
                 "\t},\n" +
                 "\t\"bearing\": 30\n" +
                 "}, {\n" +
                 "\t\"time\": 1522674555000,\n" +
                 "\t\"speed\": 0,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.228886,\n" +
                 "\t\t\"longitude\": 113.013535\n" +
                 "\t},\n" +
                 "\t\"bearing\": 327\n" +
                 "}, {\n" +
                 "\t\"time\": 1522674573000,\n" +
                 "\t\"speed\": 3,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.228999,\n" +
                 "\t\t\"longitude\": 113.013624\n" +
                 "\t},\n" +
                 "\t\"bearing\": 33\n" +
                 "}, {\n" +
                 "\t\"time\": 1522674582000,\n" +
                 "\t\"speed\": 4,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.229135,\n" +
                 "\t\t\"longitude\": 113.013582\n" +
                 "\t},\n" +
                 "\t\"bearing\": 355\n" +
                 "}, {\n" +
                 "\t\"time\": 1522674613000,\n" +
                 "\t\"speed\": 3,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.22928,\n" +
                 "\t\t\"longitude\": 113.013638\n" +
                 "\t},\n" +
                 "\t\"bearing\": 21\n" +
                 "}, {\n" +
                 "\t\"time\": 1522674641000,\n" +
                 "\t\"speed\": 2,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.229492,\n" +
                 "\t\t\"longitude\": 113.013668\n" +
                 "\t},\n" +
                 "\t\"bearing\": 353\n" +
                 "}, {\n" +
                 "\t\"time\": 1522674660000,\n" +
                 "\t\"speed\": 2,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.22965,\n" +
                 "\t\t\"longitude\": 113.013657\n" +
                 "\t},\n" +
                 "\t\"bearing\": 22\n" +
                 "}, {\n" +
                 "\t\"time\": 1522674713000,\n" +
                 "\t\"speed\": 2,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.229886,\n" +
                 "\t\t\"longitude\": 113.013727\n" +
                 "\t},\n" +
                 "\t\"bearing\": 68\n" +
                 "}, {\n" +
                 "\t\"time\": 1522674729000,\n" +
                 "\t\"speed\": 1,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.230072,\n" +
                 "\t\t\"longitude\": 113.013889\n" +
                 "\t},\n" +
                 "\t\"bearing\": 334\n" +
                 "}, {\n" +
                 "\t\"time\": 1522674753000,\n" +
                 "\t\"speed\": 3,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.23017,\n" +
                 "\t\t\"longitude\": 113.013998\n" +
                 "\t},\n" +
                 "\t\"bearing\": 16\n" +
                 "}, {\n" +
                 "\t\"time\": 1522674781000,\n" +
                 "\t\"speed\": 1,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.230343,\n" +
                 "\t\t\"longitude\": 113.014076\n" +
                 "\t},\n" +
                 "\t\"bearing\": 334\n" +
                 "}, {\n" +
                 "\t\"time\": 1522674797000,\n" +
                 "\t\"speed\": 3,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.230378,\n" +
                 "\t\t\"longitude\": 113.014231\n" +
                 "\t},\n" +
                 "\t\"bearing\": 102\n" +
                 "}, {\n" +
                 "\t\"time\": 1522674824000,\n" +
                 "\t\"speed\": 5,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.230318,\n" +
                 "\t\t\"longitude\": 113.014538\n" +
                 "\t},\n" +
                 "\t\"bearing\": 159\n" +
                 "}, {\n" +
                 "\t\"time\": 1522674839000,\n" +
                 "\t\"speed\": 2,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.230349,\n" +
                 "\t\t\"longitude\": 113.014689\n" +
                 "\t},\n" +
                 "\t\"bearing\": 90\n" +
                 "}, {\n" +
                 "\t\"time\": 1522674851000,\n" +
                 "\t\"speed\": 5,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.230271,\n" +
                 "\t\t\"longitude\": 113.014837\n" +
                 "\t},\n" +
                 "\t\"bearing\": 143\n" +
                 "}, {\n" +
                 "\t\"time\": 1522674864000,\n" +
                 "\t\"speed\": 2,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.230333,\n" +
                 "\t\t\"longitude\": 113.014974\n" +
                 "\t},\n" +
                 "\t\"bearing\": 83\n" +
                 "}, {\n" +
                 "\t\"time\": 1522674914000,\n" +
                 "\t\"speed\": 4,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.230354,\n" +
                 "\t\t\"longitude\": 113.015241\n" +
                 "\t},\n" +
                 "\t\"bearing\": 141\n" +
                 "}, {\n" +
                 "\t\"time\": 1522674929000,\n" +
                 "\t\"speed\": 3,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.230176,\n" +
                 "\t\t\"longitude\": 113.015287\n" +
                 "\t},\n" +
                 "\t\"bearing\": 195\n" +
                 "}, {\n" +
                 "\t\"time\": 1522674933000,\n" +
                 "\t\"speed\": 2,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.230325,\n" +
                 "\t\t\"longitude\": 113.015364\n" +
                 "\t},\n" +
                 "\t\"bearing\": 353\n" +
                 "}, {\n" +
                 "\t\"time\": 1522676843000,\n" +
                 "\t\"speed\": 0,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.231036,\n" +
                 "\t\t\"longitude\": 113.015596\n" +
                 "\t},\n" +
                 "\t\"bearing\": 0\n" +
                 "}, {\n" +
                 "\t\"time\": 1522676972000,\n" +
                 "\t\"speed\": 5,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.230904,\n" +
                 "\t\t\"longitude\": 113.015643\n" +
                 "\t},\n" +
                 "\t\"bearing\": 140\n" +
                 "}, {\n" +
                 "\t\"time\": 1522677452000,\n" +
                 "\t\"speed\": 0,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.230789,\n" +
                 "\t\t\"longitude\": 113.015678\n" +
                 "\t},\n" +
                 "\t\"bearing\": 0\n" +
                 "}, {\n" +
                 "\t\"time\": 1522677639000,\n" +
                 "\t\"speed\": 0,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.230811,\n" +
                 "\t\t\"longitude\": 113.015717\n" +
                 "\t},\n" +
                 "\t\"bearing\": 0\n" +
                 "}, {\n" +
                 "\t\"time\": 1522680266000,\n" +
                 "\t\"speed\": 0,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.230643,\n" +
                 "\t\t\"longitude\": 113.01556\n" +
                 "\t},\n" +
                 "\t\"bearing\": 0\n" +
                 "}, {\n" +
                 "\t\"time\": 1522680422000,\n" +
                 "\t\"speed\": 0,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.230646,\n" +
                 "\t\t\"longitude\": 113.01558\n" +
                 "\t},\n" +
                 "\t\"bearing\": 0\n" +
                 "}, {\n" +
                 "\t\"time\": 1522680723000,\n" +
                 "\t\"speed\": 0,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.230682,\n" +
                 "\t\t\"longitude\": 113.015604\n" +
                 "\t},\n" +
                 "\t\"bearing\": 0\n" +
                 "}, {\n" +
                 "\t\"time\": 1522680917000,\n" +
                 "\t\"speed\": 0,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.230728,\n" +
                 "\t\t\"longitude\": 113.015617\n" +
                 "\t},\n" +
                 "\t\"bearing\": 0\n" +
                 "}, {\n" +
                 "\t\"time\": 1522680981000,\n" +
                 "\t\"speed\": 0,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.230727,\n" +
                 "\t\t\"longitude\": 113.015617\n" +
                 "\t},\n" +
                 "\t\"bearing\": 0\n" +
                 "}, {\n" +
                 "\t\"time\": 1522681054000,\n" +
                 "\t\"speed\": 0,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.230727,\n" +
                 "\t\t\"longitude\": 113.015617\n" +
                 "\t},\n" +
                 "\t\"bearing\": 0\n" +
                 "}, {\n" +
                 "\t\"time\": 1522681246000,\n" +
                 "\t\"speed\": 0,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.230053,\n" +
                 "\t\t\"longitude\": 113.015637\n" +
                 "\t},\n" +
                 "\t\"bearing\": 0\n" +
                 "}, {\n" +
                 "\t\"time\": 1522681295000,\n" +
                 "\t\"speed\": 1,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.230572,\n" +
                 "\t\t\"longitude\": 113.015621\n" +
                 "\t},\n" +
                 "\t\"bearing\": 239\n" +
                 "}, {\n" +
                 "\t\"time\": 1522681566000,\n" +
                 "\t\"speed\": 0,\n" +
                 "\t\"latLng\": {\n" +
                 "\t\t\"latitude\": 28.230641,\n" +
                 "\t\t\"longitude\": 113.015608\n" +
                 "\t},\n" +
                 "\t\"bearing\": 0\n" +
                 "}]";





         List<A> list = AppUtil.jsonToJavaBean(json,new TypeToken<List<A>>() {}.getType());

         List<TraceLocation> tList = new ArrayList<>();

        for (A a: list){
            TraceLocation traceLocation = new TraceLocation();
                traceLocation.setLongitude(a.getLatLng().longitude);
                traceLocation.setLatitude(a.getLatLng().latitude);
                traceLocation.setBearing(a.getBearing());
                traceLocation.setSpeed(a.getSpeed());
                traceLocation.setTime(a.getTime());
            tList.add(traceLocation);
        }
        return tList;
    }


    public static class A{
        private LatLng latLng;
        private long time;
        private long bearing;
        private long speed;

        public LatLng getLatLng() {
            return latLng;
        }

        public void setLatLng(LatLng latLng) {
            this.latLng = latLng;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public long getBearing() {
            return bearing;
        }

        public void setBearing(long bearing) {
            this.bearing = bearing;
        }

        public long getSpeed() {
            return speed;
        }

        public void setSpeed(long speed) {
            this.speed = speed;
        }
    }

}