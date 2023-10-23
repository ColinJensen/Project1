package Project1;
//Author: Colin Jensen
//Card type: Just contains rank and suit. Immutable

public class Card implements Comparable<Card>{
    private String suit;
    private int rank;

    Card(int rank, String suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public int getRank() {
        return rank;
    }

    public String getSuit() {
        return suit;
    }

    public String toString() {
        if(rank<11){
        return "The " + rank + " of " + suit; //1-10
        } else {    //Jack, Queen, King, and Ace get called their respective names
            String title;
            if(rank==11) {
                title = "Jack";
            } else if(rank == 12) {
                title = "Queen";
            } else if(rank==13) {
                title = "King";
            } else if(rank==14) {
                title = "Ace"; //Ace is high card. technically this means ace-5 is not a straight, dont know if thats right, but house rules I guess.
            } else {
                title = "ERROR"; //shouldn't happen
            }
            return "The " + title + " of " + suit;
        }
    }



    public int compareTo(Card c1) {
        return rank-c1.getRank();
    }
}
