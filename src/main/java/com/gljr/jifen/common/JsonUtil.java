package com.gljr.jifen.common;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Map;

public class JsonUtil {

    private final static Gson gson = new Gson();

    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    public static <T> Map<String, T> jsonToMap(String json, Class<T> clazz) {
        return gson.fromJson(json, new TypeToken<Map<String, T>>(){}.getType());
    }

}
