/**
 * @author Feiyan Yu
 * <p>
 * Fried Flower Card Game
 */

package utility;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class Deck implements java.io.Serializable{
    private List<Card> cards;
    private int N;
    public void reset(int N) {
        cards = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < Math.min(N / 4, 13); j++) {
                cards.add(new Card(j, i));
            }
        }
        if(N >= 54) {
            cards.add(new Card(0, 4));
            cards.add(new Card(0, 5));
        }
        shuffle();
    }
    public int getRemaining() {
        return cards.size();
    }
    public Deck(int N) {
        if(N < 54) this.N = 52;
        else this.N = 54;
        reset(N);
    }
    public void shuffle() {
        Collections.shuffle(cards);
    }
    public Card getOne() {
        Card cur = cards.get(cards.size() - 1);
        cards.remove(cards.size() - 1);
        return cur;
    }
}
