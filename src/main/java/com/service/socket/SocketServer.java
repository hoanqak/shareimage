package com.service.socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer
{

    public static void main(String[] args) throws IOException
    {
        ServerSocket serverSocket = new ServerSocket(9999);
        Socket socket = serverSocket.accept();
        System.out.println("Connected " + socket);

        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String str = "", str2 = "";

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
//            System.out.println(dataInputStream.readUTF());
            str = reader.readLine();

            dataOutputStream.writeUTF(str);
            dataOutputStream.flush();
        }

    }

}
