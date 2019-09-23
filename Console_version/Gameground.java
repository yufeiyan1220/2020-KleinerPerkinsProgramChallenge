/**
 * @author Feiyan Yu
 * <p>
 * Fried Flower Card Game
 */
 
import java.io.*;
import java.net.Socket;
import java.util.*;
import utility.Card;
import utility.Deck;

public class Gameground implements Runnable, GameConstants {
    private Socket p0;
    private Socket p1;

    DataInputStream fromP1;
    DataOutputStream toP1;
    DataInputStream fromP0;
    DataOutputStream toP0;
    List<Card> handP0;
    List<Card> handP1;

    private Deck deck;
    private FriedFlowerComparator cmp;
    private FriedFlowerCardComparator card_cmp;
    //    public static void main(String[] args) {
//        Gameground g = new Gameground(null, null);
//        g.roundInit();
//    }



    public Gameground(Socket p0, Socket p1, DataInputStream fromP0, DataInputStream fromP1, DataOutputStream toP0, DataOutputStream toP1) {
        this.p0 = p0;
        this.p1 = p1;
        this.fromP0 = fromP0;
        this.fromP1 = fromP1;
        this.toP0 = toP0;
        this.toP1 = toP1;

        deck = new Deck(52);
        handP0 = new ArrayList<>();
        handP1 = new ArrayList<>();
        cmp = new FriedFlowerComparator();
        card_cmp = new FriedFlowerCardComparator();
    }


    public List<Card> handInit() {
        List<Card> hand = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            hand.add(deck.getOne());
        }
        Collections.sort(hand, card_cmp);

        for(Card c : hand) {
            System.out.println(c.toString());
        }
        return hand;
    }

    public void roundInit() {
        if(deck.getRemaining() < 6) {
            deck.reset(52);
        }
        handP0 = handInit();
        handP1 = handInit();
    }
    private void dealCard(DataInputStream from, DataOutputStream to, List<Card> hand) {
        try {
            to.writeInt(CARD_INFO);
            for(int i = 0; i < 3; i++) {
                to.writeInt(hand.get(i).getSuitNumber());
                to.writeInt(hand.get(i).getNumber());
            }
        }
        catch (IOException ex) {
            System.err.println(ex + "giveCard Error.");
        }
    }
    private boolean outputRoundResult() {
        try {
            int stateP0 = fromP0.readInt();
            int stateP1 = fromP1.readInt();

            if (stateP0 == PLAYER_READY && stateP1 == PLAYER_READY) {
                int fried_flower_result = cmp.compare(handP0, handP1);
                if(fried_flower_result > 0) {
                    toP0.writeInt(PLAYER0_WIN);
                    toP1.writeInt(PLAYER0_WIN);
                    System.out.println("P0 wins in this round!");
                }
                else if(fried_flower_result == 0) {
                    toP0.writeInt(TIED);
                    toP1.writeInt(TIED);
                    System.out.println("Tied in this round!");
                }
                else {
                    toP0.writeInt(PLAYER1_WIN);
                    toP1.writeInt(PLAYER1_WIN);
                    System.out.println("P1 wins in this round!");
                }
            }
            else if(stateP0 == PLAYER_SURRENDER && stateP1 == PLAYER_SURRENDER) {
                toP0.writeInt(TIED);
                toP1.writeInt(TIED);
                System.out.println("All user surrendered, and tied in this round.");
            }
            else if(stateP0 == PLAYER_SURRENDER){
                toP0.writeInt(PLAYER1_WIN);
                toP1.writeInt(PLAYER1_WIN);
                System.out.println("Player0 surrendered, player1 wins at this round.");
            }
            else if (stateP1 == PLAYER_SURRENDER) {
                toP0.writeInt(PLAYER0_WIN);
                toP1.writeInt(PLAYER0_WIN);
                System.out.println("Player1 surrendered, player0 wins at this round.");
            }
            else {
                System.out.println("System Error!");
                return false;
            }
            dealCard(fromP0, toP0, handP1);
            dealCard(fromP1, toP1, handP0);
        }
        catch (IOException ex) {
            System.err.println(ex + "Result output error.");
        }
        return true;
    }
    @Override
    public void run() {
        // connect the 2 clients and get the state of them.
        try {
            while(fromP1.readInt() == PLAYER_READY && fromP0.readInt() == PLAYER_READY) {
                toP0.writeInt(PLAY_START);
                toP1.writeInt(PLAY_START);

                roundInit();
                //deal card for P0 and P1
                dealCard(fromP0, toP0, handP0);
                dealCard(fromP1, toP1, handP1);
                if(!outputRoundResult()) {
                    System.out.println("Session end, because of some error in the server.");
                    break;
                }
                if(fromP0.readInt() == PLAY_END || fromP1.readInt() == PLAY_END) {
                    break;
                }
            }
            toP0.writeInt(PLAY_END);
            toP1.writeInt(PLAY_END);
            System.out.println("Session end, because someone quit game.");
        }
        catch (IOException ex) {
            System.err.println(ex);
        }
    }
}

class FriedFlowerCardComparator implements Comparator<Card> {
    @Override
    public int compare(Card o1, Card o2) {
        if(o1.getNumber() != 0 && o2.getNumber() != 0) return o1.getNumber() - o2.getNumber();
        else if(o1.getNumber() == 0 && o2.getNumber() == 0) return 0;
        else if(o1.getNumber() == 0) return 1;
        else return -1;
    }
}

class FriedFlowerComparator implements Comparator<List<Card>> {

    private FriedFlowerCardComparator card_cmp = new FriedFlowerCardComparator();
    @Override
    public int compare(List<Card> l1, List<Card> l2) {
        int type1 = getType(l1);
        int type2 = getType(l2);
        if(isSpecial(l1) && type2 == 5) return 1;
        else if(isSpecial(l2) && type1 == 5) return -1;
        else {
            if(type1 != type2) return type1 - type2;
            else {
                // Straight
                if(type1 == 2 || type1 == 4) {
                    return card_cmp.compare(l1.get(1), l2.get(1));
                }
                else {
                    // Pair
                    if(type1 == 1) {
                        if(l1.get(0) == l1.get(1) && l2.get(0) == l2.get(1)) {
                            if(l1.get(0).getNumber() == l2.get(0).getNumber()) return card_cmp.compare(l1.get(2), l2.get(2));
                            else return card_cmp.compare(l1.get(0), l2.get(0));
                        }
                        else if(l1.get(0) == l1.get(1) && l2.get(1) == l2.get(2)) {
                            if(l1.get(0).getNumber() == l2.get(1).getNumber()) return card_cmp.compare(l1.get(2), l2.get(0));
                            else return card_cmp.compare(l1.get(0), l2.get(1));
                        }
                        else if(l1.get(1) == l1.get(2) && l2.get(0) == l2.get(1)) {
                            if(l1.get(1).getNumber() == l2.get(0).getNumber()) return card_cmp.compare(l1.get(0), l2.get(2));
                            else return card_cmp.compare(l1.get(1), l2.get(0));
                        }
                        else {
                            if(l1.get(1).getNumber() == l2.get(1).getNumber()) return card_cmp.compare(l1.get(0), l2.get(0));
                            else return card_cmp.compare(l1.get(1), l2.get(1));
                        }
                    }
                    else {
                        for(int i = 2; i >= 0; i--) {
                            if(card_cmp.compare(l1.get(i), l2.get(i)) != 0) return card_cmp.compare(l1.get(i), l2.get(i));
                        }
                    }
                }
            }
        }
        return 0;
    }
    private boolean isSpecial(List<Card> li) {
        if(li.get(0).getNumber() == 2 && li.get(1).getNumber() == 3 && li.get(2).getNumber() == 5) return true;
        return false;
    }
    private int getType(List<Card> l1) {
        int res = 0;
        Card first = l1.get(0);
        Card second = l1.get(1);
        Card third = l1.get(2);
        // bomb or pair
        if(first.getNumber() == second.getNumber() && second.getNumber() == third.getNumber()) {
            res = 5;
        }
        else if(first.getNumber() == second.getNumber() || second.getNumber() == third.getNumber()) {
            res = 1;
        }

        // Straight
        if(third.getNumber() != 0) {
            if(third.getNumber() == first.getNumber() - 1 && first.getNumber() == second.getNumber() - 1) res = 2;
            else if(first.getNumber() == 11 && second.getNumber() == 12) res = 2;
        }
        else {
            if(first.getNumber() == second.getNumber() - 1 && second.getNumber() == third.getNumber() - 1) {
                res = 2;
            }
        }

        // Gold
        if(first.getSuitNumber() == second.getSuitNumber() && second.getSuitNumber() == third.getSuitNumber()) {
            // Straight Gold
            if(res != 2) res = 3;
            else res = 4;
        }

        return res;
    }

}
