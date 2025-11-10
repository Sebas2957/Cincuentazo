package org.example._0zo.Model;

import java.util.ArrayList;

// Model of the game board
public class Board {

    private Card activeCard;
    private int totalSum;
    private ArrayList<Card> cardsOnTable;

    //Constructor
    public Board() {
        cardsOnTable = new ArrayList<>();
        totalSum = 0;
        activeCard = null;
    }

    // Function to review the card that will be played
    public void PlaceCard(Card card) {
        int cardValue = card.getValue();

        if (card.getCardRank().equals("A")) {
            cardValue = card.AceValue(totalSum);
        }
        if (totalSum + cardValue > 50) {
            throw new IllegalStateException("No se puede jugar esta carta. Excedería 50.");
        }

        totalSum = totalSum + cardValue;
        activeCard = card;
        cardsOnTable.add(card);
    }

    // Reuses the cards played back to the deck
    public ArrayList<Card> RecycleCards() {
        ArrayList<Card> recycledCards = new ArrayList<>();

        if (cardsOnTable.size() <= 1) {
            return recycledCards;
        }

        int index = 0;
        int limit = cardsOnTable.size() - 1;

        while (index < limit) {
            recycledCards.add(cardsOnTable.get(index));
            index++;
        }

        Card lastPlayedCard = cardsOnTable.get(cardsOnTable.size() - 1);
        cardsOnTable.clear();
        cardsOnTable.add(lastPlayedCard);

        return recycledCards;
    }

    // Function to review if the card would exceed the 50 points
    public boolean CanPlayCard(Card card) {
        int cardValue = card.getValue();

        // Adjust A value depending on the total sum
        if (card.getCardRank().equals("A")) {
            cardValue = card.AceValue(totalSum);
        }

        return (totalSum + cardValue) <= 50;
    }

    // Total sum of the cards played
    public int GetTotalSum() {
        return totalSum;
    }

    // Confirms what is the last card played
    public Card GetActiveCard() {
        return activeCard;
    }

    // Total number of cards played
    public int GetCardsCount() {
        return cardsOnTable.size();
    }

    // Resets the board (Deck, cards played, players cards)
    public void ClearTable() {
        cardsOnTable.clear();
        totalSum = 0;
        activeCard = null;
    }

    @Override
    public String toString() {
        String cardInfo = (activeCard != null) ? activeCard.toString() : "Ninguna";
        return "Mesa → Suma: " + totalSum + " | Carta visible: " + cardInfo
                + " | Cartas jugadas: " + cardsOnTable.size();
    }
}