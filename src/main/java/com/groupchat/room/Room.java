package com.groupchat.room;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.groupchat.Client.ClientHandler;

public class Room {
    private ServerSocket roomSocket;
    public Room(){        
        try {
            roomSocket = new ServerSocket(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ServerSocket getRoomSocket() {
        return roomSocket;
    }

    public void startRoom() {
        //System.out.println("New Room connected to socket: " + roomSocket.getLocalPort());
        try {

            while(!roomSocket.isClosed()){

                Socket socket = roomSocket.accept();
                System.out.println("Client connected");
                ClientHandler clientHandler = new ClientHandler(socket);

                Thread thread = new Thread(clientHandler);
                thread.start();
            }


        } catch (Exception e) {
            closeRoom();
        }
    }

    public void closeRoom(){
        try {
            if(roomSocket!=null){
                roomSocket.close();
                System.out.println("Room " + roomSocket.getLocalPort() + " is closed!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
