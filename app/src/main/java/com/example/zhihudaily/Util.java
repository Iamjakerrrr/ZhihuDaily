package com.example.zhihudaily;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
    private static SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
    private static SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy年MM月dd日 EEEE");
    private static SimpleDateFormat dateFormat3 = new SimpleDateFormat("MM月dd日 EEEE");

    /*
    将Json中的日期字符串格式转换成app要求的格式
    例如：
    如果是今天的日期，输出：今日热闻
    如果不是今天且与今天是同一年，输入：20190517，输出：05月17日 星期五
    如果不是今天且不与今天是同一年，输入：20180517，输出：2018年05月17日 星期四
     */
    public static String formatDateString(String todayDate, String specDate) {
        String res = "";
        if (todayDate.equals(specDate)) {
            res = "今日热闻";
        } else {
            try {
                String nowYear = todayDate.substring(0, 4);
                String specYear = specDate.substring(0, 4);
                Date date1 = dateFormat1.parse(specDate);
                if (nowYear.equals(specYear)) {
                    res = dateFormat3.format(date1);
                } else {
                    res = dateFormat2.format(date1);
                }
            } catch (ParseException e) {
                Log.d("ParseException", Log.getStackTraceString(e));
            }
        }
        return res;
    }

}
