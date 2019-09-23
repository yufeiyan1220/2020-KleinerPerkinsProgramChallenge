/**
 * @author Feiyan Yu
 * <p>
 * Fried Flower Card Game
 */
package service;

public class Card implements java.io.Serializable{
    private int number;
    private int suit_number;
    private static final String[] huase = {"Spade","Heart","diamond","Club","Red Joker","Black Joker"};
    private static final String[] cardValue ={"A","2","3","4","5","6","7","8","9","10","J","Q","K"};
    public Card(int number, int suit_number) {
        this.number = number;
        this.suit_number = suit_number;
    }
    public int getSuitNumber() {
        return suit_number;
    }
    public int getNumber() {
        return number;
    }
    @Override
    public boolean equals(Object card) {
        if (getClass() == card.getClass()) {
            Card target = (Card)card;
            return target.suit_number == this.suit_number && target.number == this.number;
        }
        else return false;
    }
    @Override
    public String toString() {
        return huase[suit_number] + " " + cardValue[number];
    }
}
