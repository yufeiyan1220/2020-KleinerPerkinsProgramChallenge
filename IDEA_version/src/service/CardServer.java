/**
 * @author Feiyan Yu
 * <p>
 * Fried Flower Card Game
 */
package service;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class CardServer {
    public static void main(String[] args) {
        CardServer cs = new CardServer();
    }
    public CardServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(8000);
            System.out.println(new Date() + ": Server started at socket port-8000");
            int sessionNo = 1;
            while (true) {
                // Connect to player0
                System.out.println(new Date() + ": Wait for players to join the game");
                Socket p0 = serverSocket.accept();
                System.out.println(new Date() + ": Player0 joined the game at room " + sessionNo + "!");
                System.out.println(": Player0's IP address is " + p0.getInetAddress().getHostAddress() + "!");

                // Connect to player1
                System.out.println(new Date() + ": Wait for players to join the game");
                Socket p1 = serverSocket.accept();
                System.out.println(new Date() + ": Player1 joined the game at room " + sessionNo + "!");
                System.out.println(": Player1's IP address is " + p1.getInetAddress().getHostAddress() + "!");
                // Notify this player's index
                DataInputStream fromP1 = new DataInputStream(p1.getInputStream());
                DataOutputStream toP1 = new DataOutputStream(p1.getOutputStream());
                DataInputStream fromP0 = new DataInputStream(p0.getInputStream());
                DataOutputStream toP0 = new DataOutputStream(p0.getOutputStream());

                toP0.writeInt(0);
                toP1.writeInt(1);
                System.out.println("Written to P0P1");
                Gameground g = new Gameground(p0, p1, fromP0, fromP1, toP0, toP1);
                new Thread(g).run();
            }
        }
        catch (IOException ex) {
            System.err.println(ex);
        }
    }
}
