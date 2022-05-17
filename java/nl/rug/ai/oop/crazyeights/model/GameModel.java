package nl.rug.ai.oop.crazyeights.model;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author Sander Hofstra and Fabian Verheul
 */
public class GameModel {

    private ArrayList<Card> deck = new ArrayList<Card>();
    private ArrayList<Card> discardPile = new ArrayList<Card>();
    private ArrayList<Player> players = new ArrayList<Player>();

    private ArrayList<ArrayList<Card>> hands = new ArrayList<>();

    private int playerAmount;
    private Player currentPlayer;
    private int currentPlayerID = 0;
    private int playDirection = 1;

    private Card playedCard;

    /**
     * Method where we handle the game's flow and its rules
     * @param playerAmount amount of players
     */
    public final void runGame(int playerAmount, GameModel gameController) {
        createDeck();

        for (int i = 0; i < playerAmount; i++) {
            Player player = new Player();
            players.add(player);
            createHands(playerAmount);
            drawCards(i, 5);
        }
        Card card = drawFromDeck();
        discardPile.add(card);

        while (true) {
            currentPlayer = players.get(currentPlayerID);
            playedCard = currentPlayer.playCard(getHand(currentPlayerID), getTopCard());
            if(playedCard == null || !checkLegal(playedCard) && hands.get(currentPlayerID).contains(playedCard)) {
                drawCards(currentPlayerID, 1);
                System.out.println("Player " + currentPlayerID + " drew a card");
            } else {
                hands.get(currentPlayerID).remove(findCardInArray(playedCard, hands.get(currentPlayerID)));
                discardPile.add(playedCard);
                int num = playedCard.getNumber();
                int suit = playedCard.getSuit();
                System.out.println("Player " + currentPlayerID + " played a " + num + " of " + suit);

                if(num == 1) {
                    System.out.println("An ace has been played, the order of play has been reversed.");
                    playDirection *= -1;
                }
                if(num == 2) {
                    drawCards(players.indexOf(players.get(nextPlayer(currentPlayerID, playerAmount))), 2);
                    System.out.println("Player " + currentPlayerID + " forces the next player to draw 2 cards!");
                }
                if(num == 8){
                    suit = currentPlayer.chooseSuit(getHand(currentPlayerID), getTopCard());
                    playedCard.changeSuit(suit);
                    System.out.println("Player " + currentPlayerID + " changes the suit to " + suit);
                }
                if(num == 12){
                    currentPlayerID = nextPlayer(currentPlayerID, playerAmount);
                    System.out.println("Player " + currentPlayerID + " skips their turn!");
                }
            }

            if(checkWinner(currentPlayerID) == true) {
                break;
            }
            currentPlayerID = nextPlayer(currentPlayerID, playerAmount);
        }
        System.out.println("PLAYER " + currentPlayerID + " HAS WON!");
        return;
    }

    /**
     * Method to return the player after the current player, keeping the turn order in mind
     * @param nextPlayerID is first the currentPlayerID but gets changed to the ID of the next player
     * @param playerAmount amount of players in the game
     * @return the ID of the next player
     */
    private int nextPlayer (int nextPlayerID, int playerAmount) {
        nextPlayerID += playDirection;
        if(nextPlayerID < 0) {
            return playerAmount - 1;
        }
        if(nextPlayerID > playerAmount - 1) {
            return 0;
        }
        return nextPlayerID;
    }

    /**
     * Check if a card can be legally played
     * @param card Card that is trying to be played
     * @return whether it is legal to play this card
     */
    private boolean checkLegal(Card card) {
        Card topCard = getTopCard();
        if(card.getNumber() == topCard.getNumber() || card.getSuit() == topCard.getSuit() || card.getNumber() == 8) {
            return true;
        }
        return false;
    }

    /**
     * We do this so players can not remove other cards from their hand other than the one they play
     * @param card Card to be checked
     * @return whether the card is the same as the played card
     */
    public final boolean isPlayedCard(Card card) {
        if(card == playedCard) {
            return true;
        }
        return false;
    }

    /**
     * Checks if the player has won the game
     * @param playerID The player that is checked
     * @return whether the player has won
     */
    private boolean checkWinner(int playerID) {
        if(hands.get(playerID).size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * Gets the top card of the discard pile
     * @return the top card of the discard pile
     */
    public Card getTopCard() {
        Card card = discardPile.get(discardPile.size() - 1);
        return new Card(card);
    }

    /**
     * Makes a player draw a certain amount of cards
     * @param playerID Player that needs to draw the cards
     * @param amount Amount of cards that need to be drawn
     */
    public void drawCards(int playerID, int amount) {
        for (int i = 0; i < amount; i++) {
            if(deck.size() < 2) {
                shuffleDiscardPile();
            }
            Card card = drawFromDeck();
            hands.get(playerID).add(card);
        }
    }

    /**
     * Take one random card from the deck
     * @return the card taken from the deck
     */
    public Card drawFromDeck() {
        Random random = new Random();
        int randomNumber = random.nextInt(deck.size() - 1);
        Card card = deck.get(randomNumber);
        deck.remove(randomNumber);
        return card;
    }

    /**
     * Shuffle the discard pile except for the top card back in to the deck
     */
    public void shuffleDiscardPile() {
        System.out.println("SHUFFLE");
        for (int i = 0; i < discardPile.size() - 1; i++) {
            deck.add(discardPile.get(i));
            discardPile.remove(i);
        }
    }

    private Card findCardInArray(Card card, ArrayList<Card> hand) {
        for (Card c : hand) {
            if(c.getNumber() == card.getNumber() && c.getSuit() == card.getSuit()) {
                return c;
            }
        }
        return null;
    }

    /**
     * Method that returns a copy of the players hand so they can't access and change the real cards
     * @return Copy of hand
     */
    public final ArrayList<Card> getHand(int playerID) {
        ArrayList<Card> handCopy = new ArrayList<Card>();
        for (Card card: hands.get(playerID)) {
            Card copyCard = new Card(card);
            handCopy.add(copyCard);
        }
        return handCopy;
    }

    void createHands(int amount) {
        for (int i = 0; i < amount; i++) {
            ArrayList<Card> hand = new ArrayList<>();
            hands.add(hand);
        }
    }

    /**
     * Creates the deck of cards
     * for the suits:
     * 0 == HEARTS
     * 1 == DIAMONDS
     * 2 == CLUBS
     * 3 == SPADES
     * for the faces:
     * 1 == ACE
     * numbers are just their numbers
     * 11 == JACK
     * 12 == QUEEN
     * 13 == KING
     */
    public void createDeck() {
        for (int i = 0; i < 4; i++) {
            for (int j = 1; j < 14; j++) {
                Card card = new Card(i, j);
                deck.add(card);
            }
        }
    }
}
