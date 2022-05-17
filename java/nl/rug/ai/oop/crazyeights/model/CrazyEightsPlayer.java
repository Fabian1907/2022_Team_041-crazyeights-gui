package nl.rug.ai.oop.crazyeights.model;

import java.util.ArrayList;
import java.util.List;
/**
 * Player functions for the Crazy Eights game
 */
public interface CrazyEightsPlayer {
    /**
     * Asks the player to select a card from their hand to play,
     * or null if they want to draw a card instead
     * @param hand List of Cards in the player's hand
     * @return Card to be played, or null to draw a card
     */
    Card playCard(ArrayList<Card> hand, Card topCard);

    /**
     * Asks the player to select a suit from the four possibilities
     * Clubs, Hearts, Spades, and Diamonds
     * @return Card.Suit chosen by the player
     */
    int chooseSuit(ArrayList<Card> hand, Card topCard);
}


