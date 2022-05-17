package nl.rug.ai.oop.crazyeights.model;

import java.util.ArrayList;
import java.util.Random;

/**
 * Class to control the player, can be extended to implement a different strategy
 */
public class Player implements CrazyEightsPlayer {


    /**
     * This gets called when the player is expected to play a card, here you can implement your strategy
     *
     * @param topCard The top card of the discard pile
     * @return The card that the player plays
     */
    public Card playCard(ArrayList<Card> hand, Card topCard) {
        for (Card card : hand) {
            if (card.getNumber() == topCard.getNumber() || card.getSuit() == topCard.getSuit() || card.getNumber() == 8) {
                return card;
            }
        }
        return null;
    }

    /**
     * Gets called when the player is expected to change the suit
     *
     * @return The suit that the player picks
     */
    public int chooseSuit(ArrayList<Card> hand, Card topCard) {
        Random random = new Random();
        int suit = random.nextInt(3);
        return suit;
    }
}

