package Project1;

public class PlayerChar extends Player{

    @Override //player character wont use this method
    public int determineBet(Deck river, int pot, int maxBet) {
        return 0;
    }

    PlayerChar(int chips) {
        super("Player", chips);
    }
    
}
