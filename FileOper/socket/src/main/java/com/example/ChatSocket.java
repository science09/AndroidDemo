package com.example;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by apple on 16/1/24.
 */
public class ChatSocket extends Thread {
    Socket socket;

    public ChatSocket(Socket s){
        this.socket = s;
    }

    public void out(String out){
        try {
            socket.getOutputStream().write(out.getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        int count = 0;
        while (true) {
            try {
                count++;
                out("Loop:" + count);
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        try {
//            BufferedWriter bw = new BufferedWriter(
//                    new OutputStreamWriter(socket.getOutputStream()));
//            int count = 0;
//            while (true){
//                bw.write("Loop:" + count);
//                sleep(1000);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}
