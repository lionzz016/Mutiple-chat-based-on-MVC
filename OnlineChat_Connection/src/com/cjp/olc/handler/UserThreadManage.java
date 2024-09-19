package com.cjp.olc.handler;

import java.util.HashMap;

/**
 * @author CJP
 * @version 1.0
 */
public class UserThreadManage {
    private static HashMap<String, UserThread> hashMap = new HashMap<>();

    public static void addUserThread(String id, UserThread userThread) {
        hashMap.put(id, userThread);
    }

    public static UserThread getUserThread(String id) {
        return hashMap.get(id);
    }
}
