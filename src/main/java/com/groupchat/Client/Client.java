package com.groupchat.Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String userName;

    public Client(Socket socket, String userName){
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.userName = userName;
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
        
    }

    public void sendMessage(){
        Scanner scanner = new Scanner(System.in);
        try {
            bufferedWriter.write(userName);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            while(socket.isConnected()){
                String message = scanner.nextLine();
                bufferedWriter.write(userName +": "+message);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (Exception e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
            scanner.close();
        }
    }

    public void listenerForMessages(){
        new Thread(new Runnable(){
            @Override
            public void run(){
                String message;
                while(socket.isConnected()){
                    try {
                        message = bufferedReader.readLine();                        
                        System.out.println(message);

                    } catch (IOException e) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }
    public void closeEverything(
        Socket socket,
        BufferedReader bufferedReader,
        BufferedWriter bufferedWriter
    ){
        try {
            if(bufferedReader != null){
                bufferedReader.close();
            }
            if(bufferedWriter!=null){
                bufferedWriter.close();
            }
            if(socket!=null){
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    public static void main(String[] args) throws UnknownHostException, IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username to connect: ");
        String userName = scanner.nextLine();
        System.out.println("Enter chat port to connect: ");
        int socketPort = scanner.nextInt();
        scanner.close();
        System.out.println("Successfully connected!");
        Socket socket = new Socket("localhost",socketPort);
        Client client = new Client(socket, userName);
        client.listenerForMessages();
        client.sendMessage();
    }
}
