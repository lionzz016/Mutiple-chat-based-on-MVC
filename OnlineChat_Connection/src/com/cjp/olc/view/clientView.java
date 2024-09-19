package com.cjp.olc.view;

import com.cjp.olc.handler.UserHandler;
import com.cjp.olc.handler.UserThread;
import com.cjp.olc.utility.Utilities;


/**
 * @author CJP
 * @version 1.0
 */
public class clientView {
    private boolean loop = true;
    private UserHandler userHandler = new UserHandler();

    public void Menu() {
        while (loop) {
            System.out.println("==========欢迎登录网络通讯系统==========");
            System.out.println("\t\t1-登录系统\t\t");
            System.out.println("\t\t9-退出系统\t\t");
            System.out.println("请输入你的选择：");
            switch (Utilities.getInput_Int()) {
                case 1 -> {
                    System.out.println("请输入账户号(UserID)：");
                    String id = Utilities.getInput_Str();
                    System.out.println("请输入账户密码(UserPwd)：");
                    String pwd = Utilities.getInput_Str();
                    if (userHandler.CheckUser(id, pwd)) {
                        while (loop) {
                            System.out.println("==========用户" + id + ",欢迎回来==========");
                            System.out.println("==========请选择想要做的事：==========");
                            System.out.println("==========1-显示在线用户列表==========");
                            System.out.println("==========2-群发消息==========");
                            System.out.println("==========3-私发消息==========");
                            System.out.println("==========4-发送文件==========");
                            System.out.println("==========9-退出系统==========");
                            System.out.println("请输入选择：");
                            switch (Utilities.getInput_Int()) {
                                case 1 -> {
                                    userHandler.getUserLists();
                                }
                                case 2 -> {
                                    System.out.println("请输入需要群发的消息内容：");
                                    String contents = Utilities.getInput_Str();
                                    userHandler.GroupChat(contents);
                                }
                                case 3 -> {
                                    boolean chatIsEnd = true;
                                    System.out.println("请选择要聊天的用户：");
                                    String uid = Utilities.getInput_Str();
                                    while (chatIsEnd) {
                                        System.out.println("请输入聊天内容：");
                                        System.out.println("或输入[9]以结束聊天");

                                        String content = Utilities.getInput_Str();
                                        if (!content.equals("9")) {
                                            userHandler.UserChat(uid, content);
                                        } else {
                                            chatIsEnd = false;
                                        }
                                    }

                                }
                                case 9 -> {
                                    System.out.println("==========确定退出吗？==========");
                                    System.out.println("\t\t1-确定\t\t");
                                    System.out.println("\t\t2-返回\t\t");
                                    loop = !(Utilities.getInput_Int() == 1);
                                    userHandler.UserLoginOut();
                                }
                            }
                        }
                    }
                }
                case 9 -> {
                    System.out.println("==========确定退出吗？==========");
                    System.out.println("\t\t1-确定\t\t");
                    System.out.println("\t\t2-返回\t\t");
                    loop = !(Utilities.getInput_Int() == 1);
                }
            }
        }
    }
}
