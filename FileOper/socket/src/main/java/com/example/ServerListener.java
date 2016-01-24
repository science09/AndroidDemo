package com.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

/**
 * Created by apple on 16/1/24.
 */
public class ServerListener extends Thread {
    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(5555);
            while (true){
                Socket socket = serverSocket.accept();
                JOptionPane.showMessageDialog(null, "有客户端链接到5555端口");
                //每个客户端链接上就启动一个线程
                new ChatSocket(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
