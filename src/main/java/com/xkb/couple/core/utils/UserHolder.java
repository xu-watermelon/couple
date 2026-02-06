package com.xkb.couple.core.utils;

import com.xkb.couple.pojo.entity.User;

public class UserHolder {
    private static final ThreadLocal<Long> userThreadLocal = new ThreadLocal<>();


    public static void setUserId(Long userId) {
        userThreadLocal.set(userId);
    }
    public static Long getUserId() {
        return userThreadLocal.get();
    }
    public static void removeUserId() {
        userThreadLocal.remove();
    }
}
