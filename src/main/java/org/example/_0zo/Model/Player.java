package org.example._0zo.Model;
import java.util.ArrayList;

// Modelo base para cualquier jugador (Humano o Máquina)
public class Player {

    protected ArrayList<Card> hand;
    protected String name;
    protected boolean isEliminated;

    // Constructor
    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.isEliminated = false;
    }

    // Añade una carta a la mano (al robar)
    public void addCard(Card card) {
        if (card != null) {
            hand.add(card);
        }
    }

    // Quita una carta de la mano (al jugar)
    public void removeCard(Card card) {
        hand.remove(card);
    }

    // Devuelve la mano completa
    public ArrayList<Card> getHand() {
        return hand;
    }

    // Devuelve todas las cartas al mazo (al ser eliminado)
    public ArrayList<Card> returnAllCards() {
        ArrayList<Card> cardsToReturn = new ArrayList<>(hand);
        hand.clear();
        return cardsToReturn;
    }

    public String getName() {
        return name;
    }

    public boolean isEliminated() {
        return isEliminated;
    }

    public void eliminate() {
        this.isEliminated = true;
    }

    // *** MÉTODO CLAVE PARA LA ELIMINACIÓN ***
    // Revisa si el jugador tiene al menos una carta que pueda jugar
    public boolean canPlay(Board board) {
        if (isEliminated) {
            return false;
        }

        // Iterar sobre toda la mano
        int position = 0;
        while (position < hand.size()) {
            Card currentCard = hand.get(position);

            // Si al menos UNA carta es jugable, el jugador puede jugar
            if (board.CanPlayCard(currentCard)) {
                return true;
            }
            position++;
        }

        // Si el bucle termina, significa que ninguna carta es jugable
        return false;
    }

    @Override
    public String toString() {
        return name + " (" + hand.size() + " cartas)";
    }
}