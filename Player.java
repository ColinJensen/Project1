package Project1;

import java.util.ArrayList;
import java.util.Collections;

public abstract class Player {
    protected int chips, currentBet; //Blind is 0 no, 1 little, 2 big
    protected String name;
    protected Deck hand = new Deck(false);
    protected boolean isIn;
    


    
    public abstract int determineBet(Deck river, int pot, int maxBet);

    Player(String name, int chips) {
        this.name = name;
        this.chips = chips;
        hand = new Deck(false);
        currentBet = 0;
        isIn = true;
    }

    public int getChips() {
        return chips;
    }

    public void addChips(int mod) {
        this.chips=chips+mod;
    }

    public void setChips(int chips) {
        this.chips = chips;
    }

    public void clearHand() { //empty hand for between rounds
        hand = new Deck(false);
    }

    public String getName() {
        return name;
    }

    public Deck getHand() {
        return hand;
    }

    public void dealtCard(Card c) {
        hand.addCard(c);
    }
    //This function returns an integer representation of the value of their current hand
    public int determineValue(Deck riverOriginal) {
        int riverSize = riverOriginal.getSize(), straightStreak = 0, maxStraightStreak = 0, hearts = 0, clubs = 0, diamonds = 0, spades = 0, maxFlush = 0;
        Deck river = new Deck(riverOriginal);
        Deck fullHand = new Deck(hand);//create a copy so as not to modify actual player hand
        int currentPairCheck = 0, pairSize = 1;
        boolean multiplePairs=false, fullHouse = false;
        ArrayList<Card> fullHandList= fullHand.getList();
        while(!river.isEmpty()) { //add river to current hand to check all possible cards
            fullHand.addCard(river.popCard());
        }
        Collections.sort(fullHand.getList()); //sort to make search easier and more efficient

        //If this is the first round of betting, there will be no river and we just check the hand.
        //In this case, we check for pairs and high cards.
        //pairs above are 7, playable. If 5 or above, decent.
        //one card 9 or above is decent. two is playable One king is decent

        if(fullHandList.size()==2) {
            int rank1 = fullHandList.get(0).getRank(), rank2 = fullHandList.get(1).getRank();
            if(rank1==rank2) {
                if(rank1>6) { //pair above 7
                    return 5;
                } else if(rank1>4) {
                    return 3;
                }
            }

            if(rank2>8) { //rank 2 is highest card
                if(rank1>9) {
                    return 5;//not pair, both cards above 9 so at least 9 and 10
                } else if(rank2>13 && rank1>8) {
                    return 3; //King/ace and one good card
                }
                return 2; //Only 1 9 or higher
            }
            return 1;


        }
        //If not, check best values against list below. this is the values that will be returned

        //1 : Ace high
            //at the end, if nothing else
        //2: pair
        //3: of a kind
            //if pair
        //4-5: straight (4 is as big as river, 5 is full)
            //handle with sort
        //6:flush:
        //7: full house
        //8: 4 of a kind
            //if 3 of kind
        //9: straight flush
            //royal is one


        //check longest streak
        for(int i=0;i< fullHandList.size()-2; i++) {//will crash if hand size is 1, but it cant be
            if(fullHandList.get(i).getRank()+1 == fullHandList.get(i+1).getRank()) { //if next card in order also same
                straightStreak+=1;
                if(straightStreak>maxStraightStreak){
                    maxStraightStreak=straightStreak;
                }
            } else {
                straightStreak = 0;
            }
        }
        for(int i=0;i< fullHandList.size()-1; i++) { //Find maximum flush
            if(fullHandList.get(i).getSuit().equals("Hearts")) {
                hearts++;
                if(hearts>maxFlush) {
                    maxFlush=hearts;
                }
            } else if(fullHandList.get(i).getSuit().equals("Diamonds")) {
                diamonds++;
                if(diamonds>maxFlush) {
                    maxFlush=diamonds;
                }
            } else if(fullHandList.get(i).getSuit().equals("Clubs")) {
                clubs++;
                if(clubs>maxFlush) {
                    maxFlush=clubs;
                }
            } else if(fullHandList.get(i).getSuit().equals("Spades")) {
                spades++;
                if(spades>maxFlush) {
                    maxFlush=spades;
                }
            }
        }
        if(maxFlush>riverSize && maxStraightStreak>riverSize) { //if on pace for flush, return 9
            return 9;
        }

        //Check for pairs, and similar. Now we can pop

        for(int i=0; i<fullHandList.size()-1;i++) {
            if(fullHandList.get(i).getRank() == fullHandList.get(i+1).getRank()) { //on finding a pair
                if(fullHandList.get(i).getRank() == currentPairCheck) {//another of the card we already have a pair for
                    pairSize++;//increase the max pair
                } else if(currentPairCheck==0) {//this fires upon finding the first pair
                    currentPairCheck = fullHandList.get(i).getRank();//have it check for this new pair
                    pairSize++;//increase pairSize (to 2)
                } else {//this is a NEW pair
                    if(pairSize<3) { //no full house yet
                        multiplePairs=true;
                    } else {//full house
                        //VERY technically we need to make sure theres no 4 of a kind
                        fullHouse=true;
                    }
                }
            }
        } //after this function we have a count of the most cards of a kind, and info for full house/2 pair
        if(pairSize==4){ //4 of a kind
            return 8;
        } if(fullHouse) { //full house
            return 7;
        } if(maxFlush>=riverSize) {
            return 6;
        } if(maxStraightStreak==5) {
            return 5;
        } if(maxStraightStreak>=riverSize) {
            return 4;
        } if(pairSize==3) { //3 of a kind
            return 3;
        } if(multiplePairs) {//this is a worse value, but much higher chance of becoming better
            return 3;
        } if(pairSize==2) {// one pair
            return 2;
        }

        return 1; //nothing lol

    }

    public void Fold(){
        System.out.println(name + " folded!");
        isIn = false;
    }

    public boolean isIn(){
        return isIn;
    }


}
