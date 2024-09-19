package com.cjp.olc.handler;

import com.cjp.olc.common.Message;
import com.cjp.olc.common.MessageState;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

/**
 * @author CJP
 * @version 1.0
 * 验证User对象成功后，进行线程Thread的创建
 */
public class ServerThread extends Thread {
    private String ClientID;
    private Socket socket;

    public ServerThread(String clientID, Socket socket) {
        this.socket = socket;
        this.ClientID = clientID;
    }

    public String getClientID() {
        return ClientID;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void run() {
        while (true) {
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message or = (Message) ois.readObject();

                if (or.getMsgState().equals(Message.MSG_GET_REQUIRE)) {
                    Message message = new Message("Server",
                            or.getMsgReceiver(),
                            ServerThreadManage.OnlineListThread(),
                            LocalDateTime.now(), MessageState.MSG_RETURN_REQUIRE);
                    System.out.println(LocalDateTime.now() + " USER " + or.getMsgSender() + " required userLists.");

                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message);
                }

                if (or.getMsgState().equals(Message.MSG_LOGIN_OUT)) {
                    ServerThreadManage.shutdownThread(or.getMsgSender());
                    Thread.sleep(3);
                    socket.close();
                    System.out.println(LocalDateTime.now() + " USER " + or.getMsgSender() + " has loginOuted.");
                    break;
                }

                if (or.getMsgState().equals(MessageState.MSG_SINGLE_CHAT_SEND)) {
                    ObjectOutputStream oos = new ObjectOutputStream(ServerThreadManage.getServerThread(or.getMsgReceiver()).getSocket().getOutputStream());
                    oos.writeObject(or);
                    System.out.println(LocalDateTime.now() + " USER " + or.getMsgSender() + " sent contents to " + or.getMsgReceiver() + ".");

                }

                if (or.getMsgState().equals(MessageState.MSG_GROUP_CHAT_SEND)) {
                    for (ServerThread o : ServerThreadManage.getThreadSet()) {
                        if (o.getClientID().equals(or.getMsgSender())) continue;
                        ObjectOutputStream oos = new ObjectOutputStream(o.getSocket().getOutputStream());
                        oos.writeObject(or);
                        System.out.println(LocalDateTime.now() + " USER " + or.getMsgSender() + " sent a group content.");
                    }

                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }
}
