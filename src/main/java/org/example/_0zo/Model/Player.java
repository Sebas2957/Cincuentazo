package org.example._0zo.Model;

import java.util.ArrayList;

public class Player {

    private String name;
    private ArrayList<Card> hand;
    private boolean active;

    // Creates the player
    public Player(String playerName) {
        name = playerName;
        hand = new ArrayList<>();
        active = true;
    }

    // Adds up to 4 cards to the player's hand
    public void TakeCard(Card card) {
        if (hand.size() >= 4) {
            throw new IllegalStateException(name + " ya tiene 4 cartas.");
        }

        hand.add(card);
    }

    // Creates a copy of the cards and eliminates the player
    public ArrayList<Card> RemovePlayer() {
        active = false;

        ArrayList<Card> cards = new ArrayList<>(hand);

        hand.clear();

        return cards;
    }
    // Player's name
    public String GetName() {
        return name;
    }

    // Lists and gets the cards for the player
    public ArrayList<Card> GetHand() {
        return hand;
    }

    // Gets the amount of cards for the player
    public int GetHandSize() {
        return hand.size();
    }

    // Confirms if the player is playing
    public boolean IsActive() {
        return active;
    }

    // Resets the player cards for a new game
    public void Reset() {
        hand.clear();
        active = true;
    }

    @Override
    public String toString() {
        String status = active ? "Activo" : "Eliminado";
        return name + " (" + status + ") - " + hand.size() + " cartas";
    }

    // Plays the card selected by the player
    public Card PlayCard(int position) {
        // Verificar que la posición sea válida
        if (position < 0 || position >= hand.size()) {
            throw new IndexOutOfBoundsException("Posición inválida: " + position);
        }

        return hand.remove(position);
    }

    // Confirms if any of the cards can be used without going over 50 points
    public boolean HasPlayableCard(int currentSum) {
        int index = 0;

        while (index < hand.size()) {
            Card card = hand.get(index);

            if (card.CanPlay(currentSum)) {
                return true;
            }
            index++;
        }

        return false;
    }

    // Makes a list of the cards that can be played
    public ArrayList<Integer> GetPlayablePositions(int currentSum) {
        ArrayList<Integer> validPositions = new ArrayList<>();

        int position = 0;

        while (position < hand.size()) {
            Card card = hand.get(position);

            if (card.CanPlay(currentSum)) {
                validPositions.add(position);
            }

            position++;
        }

        return validPositions;
    }
}