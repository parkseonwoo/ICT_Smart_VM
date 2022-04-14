package net.embsys.homeiot;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientThread extends Thread {
    static Socket socket=null;
    static String clientId = "BSJ_SMP";
    static String clientPw = "PASSWD";
    static String serverIp = "192.168.1.139";
    static int serverPort = 5000;
    final static String devId = "11";
    final static String dbId = "11";
    private String ipStr;
    private String idStr;

    private String data;
    ClientThread(String ipStr, String idStr) {
        this.ipStr = ipStr;
        this.idStr = idStr;
    }

    @Override
    public void run() {
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(ipStr, serverPort));
            String[] splitLists = socket.getRemoteSocketAddress().toString().split(":|/"); //  /127.0.0.1:5001
//            displayText("[Connected: " + splitLists[1] + "]");
            Thread.sleep(100);
            sendData("[" + idStr + ":" + clientPw + "]");
        } catch (Exception e) {
//            displayText("[Server Disconnected]");
            if (!socket.isClosed()) {
                stopClient();
            }
            return;
        }
        while(true) {
            try {
                byte[] byteArr = new byte[100];
                InputStream inputStream = socket.getInputStream();
                int readByteCount = inputStream.read(byteArr);
                Log.d("readByte",":"+readByteCount);
                if (readByteCount  <= 0) {
                    throw new IOException();
                }
                String data = new String(byteArr, 0, readByteCount, "UTF-8");
//                displayText("[Recv Ok] " + data);
 //               recvDataProcess(data);
                //String[] datalist = data.split("@");
                //displayText("msg" + datalist[1]);
                displayText(data);
            } catch (Exception e) {
                stopClient();
                break;
            }
        }
    }

    void sendDataCheck(String data) {
        Log.d("sendDataCheck",data);
        if (data.length() > 0) {
            if (data.indexOf('\n') == -1)
                data += "\n";
            if (data.charAt(0) != '[')
                data = "[ALLMSG]" + data;
            sendData(data);
        }
    }

    void stopClient() {
        try {
            if (socket != null && !socket.isClosed()) {
//                displayText("[Client Stop]");
                socket.close();
                socket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    synchronized void sendData(final String data) { // final data
        Thread sendThread = new Thread() {
            @Override
            public void run() {
                try {
                    byte[] byteArr = data.getBytes("UTF-8");
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write(byteArr);
                    outputStream.flush();
//                    displayText("[Send Ok]");
                } catch (Exception e) {
//                    displayText("[Server Disconnected1]");
                }
            }
        };
        sendThread.start();
    }
    synchronized void displayText(String text) {
        Message message = MainActivity.mainHandler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putString("msg",text);
        message.setData(bundle);
        MainActivity.mainHandler.sendMessage(message);
    }

    synchronized String getData() {
        return this.data;
    }


};
