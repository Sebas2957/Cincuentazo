package org.example._0zo.Model;

import java.util.ArrayList;
import java.util.Random;

// Model of the deck
public class Deck {


    private ArrayList<Card> DeckCards;
    private Random random;

    // Constructor
    public Deck() {
        DeckCards = new ArrayList<>();
        random = new Random();
        FullDeck();
        ShuffleDeck();
    }

    // Creates all the cards used in the game
    public void FullDeck() {
        String[] names = {"Sticks", "Swords", "Cups", "Coins"};

        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};

        int Position = 0;

        while (Position < names.length) {

            int rankPosition = 0;

            while (rankPosition < ranks.length) {

                Card newCard = new Card(names[Position], ranks[rankPosition]);
                DeckCards.add(newCard);

                rankPosition++;
            }
            Position++;
        }
    }

    // Shuffles the cards previously created
    public void ShuffleDeck() {
        int cardPosition = DeckCards.size() - 1;

        while (cardPosition > 0) {

            int randomPosition = random.nextInt(cardPosition+1);

            Card tempCard = DeckCards.get(cardPosition);
            DeckCards.set(cardPosition, DeckCards.get(randomPosition));
            DeckCards.set(randomPosition, tempCard);
            cardPosition--;
        }
    }

    // Function to draw a card from the deck when a card is played
    public Card getCard() {
        if (isEmpty()){
            throw new IllegalStateException("El mazo está vacio, no hay cartas para sacar");
        }

        int lastPosition = DeckCards.size() - 1;
        Card getCard = DeckCards.get(lastPosition);
        DeckCards.remove(lastPosition);

        return getCard;
    }

    // Adds the cards to the deck and shuffle them afterward
    public void AddCards(ArrayList<Card> cardsToAdd) {
        if  (cardsToAdd != null && !cardsToAdd.isEmpty()) {
            int position = 0;

            while (position < cardsToAdd.size()) {
                DeckCards.add(cardsToAdd.get(position));
                position++;
            }

            ShuffleDeck();
        }
    }
    // Verifies if the deck is empty
    public boolean isEmpty() {

        return DeckCards.isEmpty();
    }

    // Confirm the amount of cards left in the deck
    public int CardsLeft(){

        return DeckCards.size();
    }

    // Restarts the deck with all the cards for a new game
    public void ResetDeck() {
        DeckCards.clear();
        FullDeck();
        ShuffleDeck();
    }

    @Override
    public String toString(){
        return "Mazo con " + CardsLeft() + " cartas restantes";
    }
}
