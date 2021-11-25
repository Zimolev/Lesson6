package com.company;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class ServerChat {
    private ServerSocket socketServer;
    private Socket clientChat;


    public ServerChat() {

        start();
        communication();
    }

    private void start() {
        try {
            socketServer = new ServerSocket(8865);
            System.out.println("Ждем подключения клиета....");
            clientChat = socketServer.accept();
            System.out.println("Клиет подключился " + clientChat);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void communication() {
        try {
            DataInputStream in = new DataInputStream(clientChat.getInputStream());
            DataOutputStream out = new DataOutputStream(clientChat.getOutputStream());
            Scanner scanner = new Scanner(System.in);
            AtomicBoolean isTernOff = new AtomicBoolean(true);

             new Thread(() -> {
                try {

                    while (true) {
                        String inboxMessage = in.readUTF();
                        if (inboxMessage.equals("/end"))
                        {
                            isTernOff.set(false);
                            System.out.println("Клиент отключился нажмите Enter ");
                            break;
                        }
                        System.out.println("Клиент сказал:" + inboxMessage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            new Thread(() -> {
                try {
            while (true) {
                String messag = scanner.nextLine();
                if (!isTernOff.get())
                {
                    out.writeUTF("/end");
                    break;
                }
                out.writeUTF(messag);
            }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        public static void main (String[]args){

            new ServerChat();
        }

}
