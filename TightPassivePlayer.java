package Project1;

public class TightPassivePlayer extends Player {

   @Override
   public int determineBet(Deck river, int pot, int maxBet) {
         int bet = 0;
         int riverSize = river.getSize(), handValue = super.determineValue(river);
         System.out.println(handValue);
         System.out.println(super.hand);
         if(chips==0){
            System.out.println(name + " went all in.");
            return 0;
         }


         if(riverSize<2) { //Pre-flop
            if(handValue==1) { //pre flop fold if worse than a 3 cause tight passive
               return -1;//code for fold
            } if(handValue==2) {
               bet = 25;
            } else {
               bet = 75; //Tight passive is hesitant round 1. Only goes in with good enough hand
            }
         } else if(riverSize==3) { //first 3
            if(handValue<1) {
               bet=0;
            } else if(handValue>6) {
               bet = 75;
            } else if(handValue>2){
               bet=50;
            } else {
               bet = 25;
            }
         }
         
         else if(riverSize==4) {//4 flipped
            bet= 25;
         } else { //last round
            bet= 25;
         }
         if(bet<=chips){ //if the bet would be too high run else
            if(bet<maxBet) {//if bet isnt high enough, fold
               return -1;
            }//if chips for bet AND current bet is not too high, bet
            chips = chips - maxBet; //bet gets added on top of current bet
            return maxBet;//match player's bet
         } else { //and go all in
            currentBet = chips;
            chips = 0;
            return currentBet;
         }
         


    }

    TightPassivePlayer(String name, int chips) {
      super(name, chips);
    }


    
}
