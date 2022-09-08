package com.groupchat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.groupchat.Client.ClientHandler;

public class Server {
    private ServerSocket serverSocket;
    public static ArrayList<Integer> occupiedSockets = new ArrayList<>();
    public Server(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    public void startServer() {
        System.out.println("Connected to socket: " + serverSocket.getLocalPort());
        try {


            while(!serverSocket.isClosed()){

                Socket socket = serverSocket.accept();
                System.out.println("Client connected");
                ClientHandler clientHandler = new ClientHandler(socket);

                Thread thread = new Thread(clientHandler);
                thread.start();
            }


        } catch (Exception e) {
            closeServerSocket();
        }
    }

    public void closeServerSocket(){
        try {
            if(serverSocket!=null){
                serverSocket.close();
            }
            Server.occupiedSockets.remove(serverSocket.getLocalPort());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(0);
        Server.occupiedSockets.add(serverSocket.getLocalPort());
        Server server = new Server(serverSocket);
        server.startServer();
    }
}
