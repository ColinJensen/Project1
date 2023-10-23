package Project1;

import java.util.ArrayList;
import java.util.Collections;

//Author Colin Jensen
//Deck: Contains list of cards, methods to mess with cards

public class Deck {
    private ArrayList<Card> cards = new ArrayList<Card>();

    //Constructs standard 52 card deck
    Deck(Boolean isFull) { 
        String validSuits[] = {"Hearts", "Diamonds", "Clubs", "Spades"};

        if(isFull) {
            for(String suit : validSuits) {
                for(int i=2;i<=14;i++) {
                    Card c = new Card(i, suit);
                    cards.add(c);
                }
            }
        }
    }

    Deck(Deck copyFromDeck) { //this is used to make a deep copy
        //source: https://stackoverflow.com/questions/869033/how-do-i-copy-an-object-in-javass
        this.cards = copyFromDeck.cards;
    }

    Deck(Card firstCard) {
        cards.add(firstCard);
    }

    public void addCard(Card c) {
        cards.add(c);
    }

    //Pop card, same as in War example. Need way to pop from a list
    public Card popCard() { 
        if (!cards.isEmpty()) {
            return cards.remove(0);
        }
        else {
            return null;
        }
    }

    public int getSize() {
        return cards.size();
    }

    public boolean isEmpty() {
        if (cards.size() < 1) {
            return true;
        } else {
            return false;
        }
    }

    public void Shuffle() {
        Collections.shuffle(cards);
    }

    public ArrayList<Card> getList() { //NOTICE THIS PASSES BY REFERENCE. different from deep copy
        return cards;
    }

    public String toString() {
        String s = "";
        for(Card c : cards) {
            s+=c.toString() + " ";
            
        }
        return s;
    }

    public void Deal(Player p) {//give player 2 cards
        if(p.getChips()<1) {
            p.Fold();

        } else {
            p.dealtCard(this.popCard());//uses helper function to assign cards
            p.dealtCard(this.popCard());
        }
    }

}
