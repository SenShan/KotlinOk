package com.pekon.kotlin.base;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author zhaosengshan
 */
public class LogUtil {
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static void printJson(String msg) {
        String message;
        try {
            if (msg.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(msg);
                message = jsonObject.toString(4);
            } else if (msg.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(msg);
                message = jsonArray.toString(4);
            } else {
                message = msg;
            }
        } catch (JSONException e) {
            message = msg;
        }
        message =LINE_SEPARATOR + message;
        String[] lines = new String[0];
        if (LINE_SEPARATOR != null) {
            lines = message.split(LINE_SEPARATOR);
        }
        for (String line : lines) {
            System.out.println(line);
        }
    }
}
