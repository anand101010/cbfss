package com.incede.nbfc.notification.util;

public class RequestUrlHolder
{
    private static final ThreadLocal<String> urlHolder = new ThreadLocal<>();

    public static void setUrl(String url) {
        urlHolder.set(url);
    }

    public static String getUrl() {
        return urlHolder.get();
    }

    public static void clear() {
        urlHolder.remove(); // very important to prevent memory leaks
    }
}
