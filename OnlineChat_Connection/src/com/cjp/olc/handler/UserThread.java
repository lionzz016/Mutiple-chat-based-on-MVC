package com.cjp.olc.handler;

import com.cjp.olc.common.Message;
import com.cjp.olc.common.MessageState;

import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * @author CJP
 * @version 1.0
 */
public class UserThread extends Thread {
    private Socket socket;
    private boolean isRun = true;

    public UserThread(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setRun(boolean run) {
        isRun = run;
    }

    @Override
    public void run() {
        while (isRun) {
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message or = (Message) ois.readObject();

                if (or.getMsgState().equals(Message.MSG_RETURN_REQUIRE)) {
                    System.out.println("==========当前在线用户==========");
                    String[] userList = or.getMsgContent().split(" ");
                    for (String s : userList) {
                        System.out.println("用户" + s);
                    }
                }

                if (or.getMsgState().equals(MessageState.MSG_SINGLE_CHAT_SEND)) {
                    System.out.println("用户 " + or.getMsgSender() + " 对你说：");
                    System.out.println("[" + or.getMsgDate().toString() + "] " + or.getMsgContent());
                }

                if (or.getMsgState().equals(Message.MSG_GROUP_CHAT_SEND)) {
                    System.out.println("用户 " + or.getMsgSender() + " 对你说：");
                    System.out.println("[" + or.getMsgDate().toString() + "] " + or.getMsgContent());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
