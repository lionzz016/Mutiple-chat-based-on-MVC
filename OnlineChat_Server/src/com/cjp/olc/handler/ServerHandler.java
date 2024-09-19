package com.cjp.olc.handler;

import com.cjp.olc.common.Message;
import com.cjp.olc.common.MessageState;
import com.cjp.olc.common.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * @author CJP
 * @version 1.0
 * 进行用户验证相关的处理，接收User对象
 */
public class ServerHandler {
    private ServerSocket serverSocket;
    private static final HashMap<String, User> validUsers = new HashMap<>();

    static {
        validUsers.put("100", new User("100", "123456"));
        validUsers.put("200", new User("200", "123456"));
        validUsers.put("300", new User("300", "123456"));
        validUsers.put("400", new User("400", "123456"));
    }


    public static boolean UserIsValid(String id, String pwd) {
        if (validUsers.get(id) == null) return false;
        return validUsers.get(id).getUserPwd().equals(pwd);
    }

    public ServerHandler() {
        try {
            serverSocket = new ServerSocket(9999);
            while (true) {
                Socket socket = serverSocket.accept();

                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                User or = (User) ois.readObject();

                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                if (UserIsValid(or.getUserID(), or.getUserPwd())) {
                    oos.writeObject(new Message("Server",
                            or.getUserID(),
                            "Connected",
                            LocalDateTime.now(), MessageState.MSG_LOGIN_SUCCESS));
                    ServerThread st = new ServerThread(or.getUserID(), socket);
                    st.start();
                    ServerThreadManage.addServerThread(or.getUserID(), st);
                    System.out.println("USER " + or.getUserID() + " has loginIn.");

                } else {
                    oos.writeObject(new Message("Server",
                            or.getUserID(),
                            "Disconnected",
                            LocalDateTime.now(), MessageState.MSG_LOGIN_FAILED));
                    socket.close();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
