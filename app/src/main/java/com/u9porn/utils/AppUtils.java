package com.u9porn.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.widget.ImageView;

import com.u9porn.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author flymegoc
 * @date 2017/10/28
 * @describe
 */
public class AppUtils {
    
    /**
     * drawable 着色
     */
    public static void setImageViewColor(ImageView view, int colorResId) {
        //mutate()
        Drawable modeDrawable = view.getDrawable().mutate();
        Drawable temp = DrawableCompat.wrap(modeDrawable);
        ColorStateList colorStateList = ColorStateList.valueOf(view.getResources().getColor(colorResId));
        DrawableCompat.setTintList(temp, colorStateList);
        view.setImageDrawable(temp);
    }

    public static void setColorSchemeColors(Context context, SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(context, R.color.green), ContextCompat.getColor(context, R.color.lightred), ContextCompat.getColor(context, R.color.yeloo));
    }

    public static String buildTitle(String title, String author, String datetime) {
        return "<h3>" + title + "</h3>"
                + "<div align=\"right\"><span>" +
                "作者：" + author + "&nbsp;&nbsp;&nbsp;时间：" + datetime + "&nbsp;" +
                "</span>\n" +
                "</div>"
                + "<HR style=\"FILTER: progid:DXImageTransform.Microsoft.Glow(color=#987cb9,strength=10)\" width=\"100%\" color=#987cb9 SIZE=1>";
    }

    public static String buildHtml(String data, boolean isNightModel) {
        String backgroundColor;
        String fontColor;
        if (isNightModel) {
            backgroundColor = "#49505A";
            fontColor = "#ADB4BE";
        } else {
            backgroundColor = "#FFFFFF";
            fontColor = "#353C46";
        }
        String dat1 = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no\">" +
                "<style type=\"text/css\">\n" +
                "body\n" +
                "{ \n" +
                "background: " + backgroundColor + " no-repeat fixed center; \n" +
                "color: " + fontColor +
                "}\n" +
                "</style>" +
                "</head>\n" +
                "<body>\n" +
                "<div id=\"app\" style=\"width:100%;height:100%;word-wrap: break-word;word-break:break-all;\">";
        String dat2 = "</div>\n" +
                "\n" +
                "</body>\n" +
                "</html>";
        return dat1 + data + dat2;
    }

    public static String getAssetsAsString(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
