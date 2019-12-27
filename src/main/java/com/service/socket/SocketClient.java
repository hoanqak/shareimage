package com.service.socket;

import java.io.*;
import java.net.Socket;

public class SocketClient
{


    private static void connectToServer(String host, int port, String username) throws IOException
    {
        Socket socket =new Socket(host, port);
        System.out.println("Connected");
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Thread read = new Thread(){
            @Override
            public void run()
            {
                try
                {
                    while (true)
                    {
                        System.out.println(dataInputStream.readUTF());
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        };

        read.start();
        while (true){
            dataOutputStream.writeUTF(username +": "+br.readLine());
            dataOutputStream.flush();
        }
    }

    public void test(String[] args) throws IOException
    {
        connectToServer("localhost", 9999, "Hoangth");
    }
}
