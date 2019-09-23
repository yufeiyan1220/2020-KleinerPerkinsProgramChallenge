/**
 * @author Feiyan Yu
 * <p>
 * Fried Flower Card Game
 */
package service;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CardClient implements GameConstants{
    private DataOutputStream toServer;
    private DataInputStream fromServer;
    private int player_index;
    private List<Card> hand;
    private List<Card> opponentHand;
    private Scanner input;
    private static final String UNDERLINE = "-------------------------------------------------------------";
    public static void main(String[] args) {
        CardClient client = new CardClient();
        client.startGame();
    }
    public CardClient() {
        input = new Scanner(System.in);
        try {
            hand = new ArrayList<>();
            opponentHand = new ArrayList<>();
            Socket socket = new Socket("localhost", 8000);
            fromServer = new DataInputStream(socket.getInputStream());
            toServer = new DataOutputStream(socket.getOutputStream());
            System.out.println("Connected");
            player_index = fromServer.readInt();
            System.out.println("You are:" + player_index);
        }
        catch (IOException ex) {
            System.err.println(ex);
        }
    }
    private void gameReady(){
        try {
            System.out.println("Are you ready?\n(press Y to start, N to quit.)");
            System.out.println(UNDERLINE);
            String ready = input.next();
            if(!ready.equals("Y") && !ready.equals("N")) {
                System.out.println("Please press the right word");
                ready = input.next();
            }

            if(ready.equals("N")) {
                System.out.println("Quit Game");
                toServer.writeInt(PLAY_END);

            }
            else {
                System.out.println("Game Start");
                toServer.writeInt(PLAYER_READY);
            }
        }
        catch (IOException ex){
            System.err.println(ex);
        }
    }
    private boolean getCardFromDealer(List<Card> h, DataInputStream from) {
        boolean result = true;
        try {
            if(fromServer.readInt() == CARD_INFO) {
                h.clear();
                for(int i = 0; i < 3; i++) {
                    int suit = from.readInt();
                    int num = from.readInt();
                    Card cur = new Card(num, suit);
                    h.add(cur);
                }
            }
            else {
                result = false;
            }
        }
        catch (IOException ex) {
            System.err.println(ex + "Cannot get Card from dealer.");
        }
        return result;
    }
    private boolean inGame() {
        boolean result = true;
        try {
            System.out.println("Get 3 Card from Deck");
            boolean isGetCard = getCardFromDealer(hand, fromServer);
            if(isGetCard) {
                System.out.println("My cards:");
                for(Card c : hand) {
                    System.out.print(c.toString() + "  ");
                }
                System.out.println();
                System.out.println(UNDERLINE);
                System.out.println("Press 1 to make a bet with your opponent, press others to surrender.");
                int nextOperation = input.nextInt();
                if(nextOperation == 1) {
                    toServer.writeInt(PLAYER_READY);
                }
                else {
                    toServer.writeInt(PLAYER_SURRENDER);
                }

                int winner = fromServer.readInt();
                if(winner == PLAYER0_WIN) {
                    System.out.println("Winner is Player 0");
                }
                else if(winner == PLAYER1_WIN) {
                    System.out.println("Winner is Player 1");
                }
                else {
                    System.out.println("Tied game");
                }
                System.out.println(UNDERLINE);
                System.out.println("Your opponent's hand are:");
                if(getCardFromDealer(opponentHand, fromServer)) {
                    for(Card c : opponentHand) {
                        System.out.println(c.toString() + "  ");
                    }
                    System.out.println();
                    System.out.println(UNDERLINE);
                    System.out.println("Do you want the next round? Press 1 to start, others to quit game.");
                    int nextRound = input.nextInt();

                    if(nextRound == 1)  result = true;
                    else result = false;
                }
                else {
                    System.out.println("Error in getting the hand of opponent.");
                    result = false;
                }
            }
            else {
                System.out.println("Error Occur when get the card");
                result = false;
            }
        }
        catch (IOException ex) {
            System.err.println(ex);
        }
        return result;
    }

    public void startGame() {
        try{
            while(true) {
                gameReady();
                int nextStep = fromServer.readInt();
                if(nextStep == PLAY_START) {
                    boolean isNextRound = inGame();
                    if(!isNextRound) toServer.writeInt(PLAY_END);
                    else toServer.writeInt(PLAYER_READY);

                }
                else if(nextStep == PLAY_END) {
                    System.out.println("Player quits game, game end.");
                    break;
                }
            }
        }
        catch (IOException ex) {
            System.err.println(ex);
        }

    }
}
