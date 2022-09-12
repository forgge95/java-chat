package com.groupchat.server;

import java.awt.Container;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import com.groupchat.room.Room;

public class Server implements ActionListener, WindowListener{
    private JFrame frame = new JFrame("TextArea");
    private JTextArea tArea = new JTextArea(10,20);
    private JButton button = new JButton("Click");
    private JScrollPane pane = new JScrollPane(tArea);
    private SwingWorker worker;
    private String s ="";
    private ArrayList<Room> roomList = new ArrayList<>();
    public static ArrayList<Integer> portList = new ArrayList<>();
    public void prepareAndShowGUI()
    {
        Container container = frame.getContentPane();
        container.add(pane);container.add(button,BorderLayout.NORTH);
        tArea.setLineWrap(true);
        tArea.setWrapStyleWord(true) ;
        button.addActionListener(this);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    public void actionPerformed(ActionEvent evt)
    {
        if(evt.getSource()==button)
        {
            Room room = new Room();
            roomList.add(room);
            s="New Room connected to socket: " + room.getRoomSocket().getLocalPort()+"\n";
            portList.add(room.getRoomSocket().getLocalPort());
            System.out.println("New Room connected to socket: " + room.getRoomSocket().getLocalPort());
            if (worker!=null)
            {
                worker.cancel(true);
            }
            worker = new SwingWorker()
            {
                @Override
                protected Integer doInBackground()//Perform the required GUI update here.
                {
                    try
                    {
                        for(int i = 0;i<s.length();i++)
                        {
                            tArea.append(String.valueOf(s.charAt(i)));
                            //Thread.sleep(5);
                        }
                    }catch(Exception ex){}
                    return 0;
                }       
            };
            worker.execute();//Schedules this SwingWorker for execution on a worker thread.
        }
    }

    

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.prepareAndShowGUI();
    }
    @Override
    public void windowOpened(WindowEvent e) {

    }
    @Override
    public void windowClosing(WindowEvent e) {
        for (Room room : roomList) {
            room.closeRoom();
        }
    }
    @Override
    public void windowClosed(WindowEvent e) {
        for (Room room : roomList) {
            room.closeRoom();
        }        
    }
    @Override
    public void windowIconified(WindowEvent e) {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void windowDeiconified(WindowEvent e) {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void windowActivated(WindowEvent e) {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void windowDeactivated(WindowEvent e) {
        // TODO Auto-generated method stub
        
    }
}
