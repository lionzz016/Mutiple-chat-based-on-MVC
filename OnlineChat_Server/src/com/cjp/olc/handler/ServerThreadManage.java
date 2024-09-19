package com.cjp.olc.handler;

import org.w3c.dom.ls.LSException;

import javax.imageio.stream.IIOByteBuffer;
import java.util.*;

/**
 * @author CJP
 * @version 1.0
 */
public class ServerThreadManage {
    private static HashMap<String, ServerThread> hashMap = new HashMap<>();


    public static void addServerThread(String ClientID, ServerThread serverThread) {
        hashMap.put(ClientID, serverThread);
    }

    public static ServerThread getServerThread(String ClientID) {
        return hashMap.get(ClientID);
    }

    public static String OnlineListThread() {
        Set<String> strings = hashMap.keySet();
        StringBuilder Lists = new StringBuilder();
        for (Object o : strings) {
            Lists.append(o.toString()).append(" ");
        }
        return Lists.toString();
    }

    public static void shutdownThread(String ClientID) {
        hashMap.remove(ClientID);
    }

    public static Collection<ServerThread> getThreadSet() {
        return hashMap.values();
    }
}
