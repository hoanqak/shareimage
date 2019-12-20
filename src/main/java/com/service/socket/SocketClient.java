package com.service.socket;

import java.io.*;
import java.net.Socket;

public class SocketClient
{

    public static void main(String[] args) throws IOException
    {
        Socket socket =new Socket("localhost", 9999);
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
            dataOutputStream.writeUTF(br.readLine());
            dataOutputStream.flush();
        }
    }
}
