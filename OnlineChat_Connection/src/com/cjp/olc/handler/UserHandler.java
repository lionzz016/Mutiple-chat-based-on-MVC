package com.cjp.olc.handler;

import com.cjp.olc.common.Message;
import com.cjp.olc.common.MessageState;
import com.cjp.olc.common.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author CJP
 * @version 1.0
 */
public class UserHandler {
    private User users = new User();
    private Socket socket;

    public boolean CheckUser(String id, String pwd) {
        boolean stat = false;
        users.setUserID(id);
        users.setUserPwd(pwd);

        try {
            socket = new Socket(InetAddress.getByName("127.0.0.1"), 9999);
            ObjectOutputStream obj = new ObjectOutputStream(socket.getOutputStream());
            obj.writeObject(users);

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message or = (Message) ois.readObject();
            if (or.getMsgState().equals(MessageState.MSG_LOGIN_SUCCESS)) {
                System.out.println("于" +
                        DateTimeFormatter.ofPattern("yyyy/MM/dd/HH:mm:ss").format(or.getMsgDate())
                        + "登录成功");
                UserThread ut = new UserThread(socket);
                ut.start();
                UserThreadManage.addUserThread(users.getUserID(), ut);

                stat = true;
            } else {
                socket.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return stat;
    }

    public void getUserLists() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(UserThreadManage.getUserThread(users.getUserID()).getSocket().getOutputStream());
            oos.writeObject(new Message(users.getUserID(),
                    "Server",
                    "Require",
                    LocalDateTime.now(), MessageState.MSG_GET_REQUIRE));


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void UserLoginOut() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(UserThreadManage.getUserThread(this.users.getUserID()).getSocket().getOutputStream());
            oos.writeObject(new Message(users.getUserID(), "Server", "LoginOut",
                    LocalDateTime.now(), MessageState.MSG_LOGIN_OUT));
            System.out.println("用户 " + users.getUserID() + " 安全退出");
            UserThreadManage.getUserThread(this.users.getUserID()).setRun(false);
            System.exit(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void UserChat(String targetId, String msgContent) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(UserThreadManage.getUserThread(this.users.getUserID()).getSocket().getOutputStream());
            oos.writeObject(new Message(this.users.getUserID(),
                    targetId,
                    msgContent,
                    LocalDateTime.now(),
                    Message.MSG_SINGLE_CHAT_SEND));

            System.out.println("你对 用户 " + targetId + " 说：");
            System.out.println("[" + LocalDateTime.now() + "] " + msgContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void GroupChat(String msgContent) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(UserThreadManage.getUserThread(this.users.getUserID()).getSocket().getOutputStream());
            Message message = new Message();
            message.setMsgSender(this.users.getUserID());
            message.setMsgContent(msgContent);
            message.setMsgDate(LocalDateTime.now());
            message.setMsgState(MessageState.MSG_GROUP_CHAT_SEND);
            oos.writeObject(message);

            System.out.println("你对大家说：");
            System.out.println(message.getMsgContent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
