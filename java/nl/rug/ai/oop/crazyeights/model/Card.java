package nl.rug.ai.oop.crazyeights.model;

/**
 * Represents a playing card
 */
public class Card {

    private int suit;
    private int number;

    /**
     * Constructor to make a card with a specific suit and number
     * @param suit
     * @param number
     */
    public Card(int suit, int number) {
        this.number = number;
        this.suit = suit;
    }

    /**
     * Constructor to make a copy of a card so players can't acces the actual card
     * @param card Card to be copied
     */
    public Card(Card card) {
        this.number = card.getNumber();
        this.suit = card.getSuit();
    }

    /**
     * Gets number of card
     * @return number of card
     */
    public int getNumber() {
        return number;
    }

    /**
     * Gets suit of card
     * @return suit of card
     */
    public int getSuit() {
        return suit;
    }

    /**
     * Method we use to change the suit of a card only if it is an 8
     * @param newSuit The suit that it should be changed to
     */
    public void changeSuit(int newSuit) {
        if(number == 8){
            this.suit = newSuit;
        }
        return;
    }

}
