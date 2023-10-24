package Project1;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;

public class PlayPoker {
    public static void main(String[] args) {
        

        Player p1 = new TightPassivePlayer("Timb",100);
        Player p2 = new TightPassivePlayer("Tom",100);
        Player p3 = new TightPassivePlayer("Sally",100);
        Player p4 = new TightPassivePlayer("Destiny",100);
        Player user = new PlayerChar(100);


        Deck deck, river;
        int pot=0, maxHandValue=0, maxRank=0;
        ArrayList<Player> winningPlayers = new ArrayList<Player>();

        //Here is a game of poker
        boolean exit=false;
        while(!exit) {
            //Initialize variables
            deck = new Deck(true);//new full deck
            river = new Deck(false); //make new empty river
            pot = 0;
            deck.Shuffle();//shuffle it
            //deal in players. There will be 4 plus human player
                deck.Deal(user); 
                deck.Deal(p1); 
                deck.Deal(p2); 
                deck.Deal(p3); 
                deck.Deal(p4); 
                Scanner enter1 = new Scanner(System.in);//because closing a scanner closes system.in for some reason. we cant close it in a function or else we can't open a scanner again i guess.
            if(user.getChips()>0 && user.getChips() != 100*5) { //user does not have no chips or all chips
                pot = bettingRound(p1, p2, p3, p4, pot, user, river, enter1);
                deck.popCard();//burn 1
                river.addCard(deck.popCard()); //flip 3
                river.addCard(deck.popCard());
                river.addCard(deck.popCard());
                pot = bettingRound(p1, p2, p3, p4, pot, user, river, enter1);
                deck.popCard();//burn 1
                river.addCard(deck.popCard()); //flip 1
                pot = bettingRound(p1, p2, p3, p4, pot, user, river, enter1);
                deck.popCard();//burn 1
                river.addCard(deck.popCard()); //flip 1
                pot = bettingRound(p1, p2, p3, p4, pot, user, river, enter1);
                //handle win
                if(user.isIn()) {//if p1 is in, they are better than nothing
                    maxHandValue = user.determineValue(river);
                    winningPlayers.add(user);
                }
                if(p1.isIn()) { 
                    if(p1.determineValue(river) == maxHandValue) { //if p2 shares the currently highest value
                        winningPlayers.add(p1); //add them to the list
                    } else if(p1.determineValue(river) > maxHandValue) {//if they are higher
                        maxHandValue = p1.determineValue(river); //clear the list and add themself
                        winningPlayers.clear();
                        winningPlayers.add(p1);
                    }
                }

                if(p2.isIn()) { 
                    if(p2.determineValue(river) == maxHandValue) { //if p2 shares the currently highest value
                        winningPlayers.add(p2); //add them to the list
                    } else if(p2.determineValue(river) > maxHandValue) {//if they are higher
                        maxHandValue = p2.determineValue(river); //clear the list and add themself
                        winningPlayers.clear();
                        winningPlayers.add(p2);
                    }
                }
                if(p3.isIn()) { 
                    if(p2.determineValue(river) == maxHandValue) { //if p3 shares the currently highest value
                        winningPlayers.add(p3); 
                    } else if(p3.determineValue(river) > maxHandValue) {
                        maxHandValue = p3.determineValue(river); 
                        winningPlayers.clear();
                        winningPlayers.add(p3);
                    }
                }
                if(p4.isIn()) { 
                    if(p2.determineValue(river) == maxHandValue) { //same for p4
                        winningPlayers.add(p4); 
                    } else if(p4.determineValue(river) > maxHandValue) {
                        maxHandValue = p4.determineValue(river); 
                        winningPlayers.clear();
                        winningPlayers.add(p4);
                    }
                }

                if(winningPlayers.size() == 1) {//if only one person of a value tier, they did the best
                    winningPlayers.get(0).addChips(pot);
                    System.out.println(winningPlayers.get(0).getName() + " won " + pot + " chips!");
                } else {//handle logic for multiple winners
                    //Pre sort the hands and river for easy determination
                    Collections.sort(river.getList());//sorts river's list
                    for(Player p: winningPlayers){
                        Collections.sort(p.getHand().getList());//sort every player's hand
                    }
                    if(maxHandValue == 1) {//no good cards, high card wins
                        for(Player p: winningPlayers) {
                            if(p.getHand().getList().get(1).getRank()>maxRank) {
                                maxRank = p.getHand().getList().get(1).getRank();
                            }
                        }
                        //now max rank is correct. Check against river
                        //if river has highest value, all tie.
                        //if not, remove any hand that is worse. 
                            //if multiple remain, they tie
                            //otherwise last remaining has the best hand and wins
                        if(river.getList().get(4).getRank()>=maxRank) {
                            pot=pot/winningPlayers.size();
                            for(Player p:winningPlayers) {
                                p.addChips(pot);//split winnings
                            }
                        } else{
                            for(Player p:winningPlayers) {
                                if(p.getHand().getList().get(1).getRank()<maxRank) {
                                    winningPlayers.remove(p);
                                }
                            }
                            pot=pot/winningPlayers.size();//split winnings among remaining players
                            for(Player p:winningPlayers) {
                                p.addChips(pot);//split winnings
                            }
                        }



                    } else if(maxHandValue == 2) {//pairs. Highest Wins
                        for(int j=0;j<=2;j++) { //this needs to run twice. The first loop finds the maximum pair, the second weeds out any pair that is lower

                            for(int k=0;k<winningPlayers.size();k++) {
                                ArrayList<Card> fullHand = new ArrayList<Card>();
                                for(Card c:river.getList()){
                                    fullHand.add(c);//add river to hand
                                }
                                for(Card c:winningPlayers.get(k).getHand().getList()){
                                    fullHand.add(c); //add player hand to full hand
                                }
                                Collections.sort(fullHand);//to make logic easier
                                for(int i=0; i<6;i++) {
                                    if(fullHand.get(i).getRank()==fullHand.get(i+1).getRank()) {
                                        if(maxRank<fullHand.get(i).getRank()) {
                                            maxRank=fullHand.get(i).getRank();
                                        } else if(maxRank>fullHand.get(i).getRank()) {
                                            winningPlayers.remove(k);
                                        }
                                    }
                                }
                            }
                        }
                        //Now players have been filtered. Split winnings
                        //note unlike high card, river was included in these calculations
                        pot=pot/winningPlayers.size();
                        for(Player p:winningPlayers) {
                            p.addChips(pot);
                        }

                    } else if(maxHandValue == 3) { //2 pair or 3 of a kind. 3 of a kind beats 2 pair
                        boolean foundTripple = false;
                        for(int j=0;j<=2;j++) {//Run through twice. The first time will eventually find the highest value. The second will filter any hand that is worse
                            for(int k=0; k<winningPlayers.size();k++) {
                                ArrayList<Card> fullHand = new ArrayList<Card>();
                                for(Card c:river.getList()){
                                    fullHand.add(c);//add river to hand
                                }
                                for(Card c:winningPlayers.get(k).getHand().getList()){
                                    fullHand.add(c); //add player hand to full hand
                                }
                                Collections.sort(fullHand);

                                for(int i=0; i<5;i++) {
                                    if(fullHand.get(i).getRank()==fullHand.get(i+1).getRank()&&fullHand.get(i+2).getRank()==fullHand.get(i+1).getRank()) {
                                        //if a tripple found
                                        foundTripple=true;
                                        if(fullHand.get(i).getRank()>=maxRank) {
                                            maxRank = fullHand.get(i).getRank();//new highest tripple
                                        } else { //lower than best
                                            winningPlayers.remove(k);
                                        }
                                    } else {//this is not a tripple
                                        if(foundTripple) {
                                            //this isnt three of a kind(tripple) and one exists. drop this
                                            winningPlayers.remove(k);
                                        } else {
                                            //no three of a kind. Find highest pair
                                            for(i=6;i!=3;i--) {//this hand has 2 pair. the highest pair WILL
                                                //be between 7 and 3 indexed because lowest possible is 0,1 and 2,3 pairs
                                                if(fullHand.get(i).getRank()==fullHand.get(i-1).getRank()) {
                                                    //I is index of highest pair in the hand, last card in order
                                                    //if pair is indexes 6,7 i will be 7
                                                    if(fullHand.get(i).getRank()>=maxHandValue) {
                                                        maxHandValue = fullHand.get(i).getRank();//new highest/equal to, set
                                                    } else {
                                                        //there is no tripple, however there is a higher pair than the highest pair in
                                                        //this hand. this hand lost
                                                        winningPlayers.remove(k);
                                                    }

                                                }

                                            }
                                        }
                                    }
                                }


                            }
                        }
                        //losing players have been filtered out
                        pot=pot/winningPlayers.size();//split winnings among remaining players
                        for(Player p:winningPlayers) {
                            p.addChips(pot);//split winnings
                        }

                    } else if(maxHandValue == 5) {//4 is impossible (river is 5), 5 is a straight
                        //These are unlikely, and in my rules its going to be a tie
                        
                        pot=pot/winningPlayers.size();//split winnings among remaining players
                        for(Player p:winningPlayers) {
                            p.addChips(pot);//split winnings
                        }
                    } else if(maxHandValue == 6) {//Flush. high card wins
                        pot=pot/winningPlayers.size();//split winnings among remaining players
                        for(Player p:winningPlayers) {
                            p.addChips(pot);//split winnings
                        }
                    } else if(maxHandValue == 7) {//full house. higher card wins.
                        pot=pot/winningPlayers.size();//split winnings among remaining players
                        for(Player p:winningPlayers) {
                            p.addChips(pot);//split winnings
                        }
                    } else if(maxHandValue == 9) {//8 and 9 were combined. Highest card wins

                    }
                }


            } else {
                System.out.println("And thats the game! Thanks for playing");
                exit = true;
            }



        }


    }
    //this function is called for every betting round. It allows the player to bet, and then the AI determine if they want to match bet.
    //Afterwards, the pot is totaled up and returned
    private static int bettingRound(Player p1, Player p2, Player p3, Player p4, int pot, Player thePlayer, Deck river, Scanner enter) {
        //In this version of poker, Player bets, NPCs decide to call.
        
        int playerBet = -1, botBet;
        System.out.println("\nNew Round\n");
        System.out.println("Your Hand: " + thePlayer.getHand());
        System.out.println("Your Chips: " + thePlayer.getChips() + "   The pot: " + pot);
        System.out.println("The River: " + river);
        System.out.println("Please enter your bet: ");
        
        while(playerBet == -1) {
            try {
                playerBet = enter.nextInt();
                
                if(playerBet<0 || playerBet>thePlayer.getChips()) {
                    playerBet = -1;//loop again
                    System.out.println("Please enter a positive integer");
                }
            } catch (Exception e) {
                playerBet = -1;
                System.out.println("Please enter a valid number");
            }
        }   
        pot+=playerBet;
        thePlayer.setChips(thePlayer.getChips() - playerBet);//handle player's chips
        //Now the player has a bet. If the other AI players are fine with it, they can match
        if(p1.isIn()) {
            botBet = p1.determineBet(river, pot, playerBet);
            if(botBet==-1) {
                p1.Fold();
            } else {
                System.out.println(p1.name + " bet " + botBet);
                pot+=botBet;
            }
        } else {
            System.out.println(p1.name + " is out!");
        }

        if(p2.isIn()) {
            botBet = p2.determineBet(river, pot, playerBet);
            if(botBet==-1) {
                p2.Fold();
            } else {
                System.out.println(p2.name + " bet " + botBet);
                pot+=botBet;
            }
        } else {
            System.out.println(p2.name + " is out!");
        }

        if(p3.isIn()) {
            botBet = p3.determineBet(river, pot, playerBet);
            if(botBet==-1) {
                p3.Fold();
            } else {
                System.out.println(p3.name + " bet " + botBet);
                pot+=botBet;
            }
        } else {
            System.out.println(p3.name + " is out!");
        }
        if(p4.isIn()) {
            botBet = p4.determineBet(river, pot, playerBet);
            if(botBet==-1) {
                p4.Fold();
            } else {
                System.out.println(p4.name + " bet " + botBet);
                pot+=botBet;
            }
        } else {
            System.out.println(p4.name + " is out!");
        }
       
        

        
        
        return pot;
    }


}
