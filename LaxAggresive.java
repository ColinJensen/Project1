//author: Colin Jensen
//Desc: A type of player who bets aggresively. Dont get intimidated!

package Project1;

public class LaxAggresive extends Player{
    @Override
   public int determineBet(Deck river, int pot, int maxBet) {
        if(chips>=maxBet) {
            return maxBet;
        } else {
            return chips;
        }
    
        
        //Lax aggressive loves to go all in. Wait for a good hand to beat
        //will bet as much as player

    }

    LaxAggresive(String name, int chips) {
      super(name, chips);
    }
}
