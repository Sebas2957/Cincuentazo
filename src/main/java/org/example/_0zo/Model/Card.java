package org.example._0zo.Model;

// Model of the cards
public class Card {

    private String cardName;
    private String cardRank;
    private int value;

    // Constructor
    public Card(String cardName, String cardRank) {
        this.cardName = cardName;
        this.cardRank = cardRank;
        this.value = RankValue(cardRank);
    }

    // Verifies the card value
    private int RankValue(String cardRank) {
        int Sum = 0;
        int attempts = 0;

        while (attempts < 4) {

            if (attempts == 0 && cardRank.equals("A")) {
                return 10;
            }
            if (attempts == 1 && (cardRank.equals("J") || cardRank.equals("Q") || cardRank.equals("K"))) {
                return -10;
            }
            if (attempts == 2 && cardRank.equals("9")) {
                return 0;
            }
            if (attempts == 3) {
                try {
                    return Integer.parseInt(cardRank);
                } catch (NumberFormatException e) {
                    return 0;
                }
            }

            attempts++;
        }
        return Sum;
    }

    // Adds 10 for the A if it does not exceed the 50 points. Otherwise, it adds just 1
    public int AceValue(int currentSum) {
        if (cardRank.equals("A")) {
            if (currentSum + 10 <= 50) {
                return 10;
            } else {
                return 1;
            }
        }
        return value;
    }

    // Function to confirm if the card can be played without losing the game
    public boolean CanPlay(int currentSum) {
        int valueToUse = value;

        if (cardRank.equals("A")) {
            valueToUse = AceValue(currentSum);
        }

        return (currentSum + valueToUse) <= 50;
    }

    // Getters
    public String getCardName() {
        return cardName;
    }

    public String getCardRank() {
        return cardRank;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return cardRank + " of " + cardName;
    }
}
