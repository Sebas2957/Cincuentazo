package org.example._0zo.Model;
import java.util.ArrayList;

/**
 * Clase base que representa a cualquier jugador en el juego.
 * Puede ser extendida por jugadores humanos o controlados por IA.
 *
 * @author Sebastian-Javier-Alejandro
 * @version 1.0
 */
public class Player {

    protected ArrayList<Card> hand;
    protected String name;
    protected boolean isEliminated;

    /**
     * Crea un nuevo jugador con el nombre especificado.
     *
     * @param name Nombre del jugador
     */
    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.isEliminated = false;
    }

    /**
     * Añade una carta a la mano del jugador.
     *
     * @param card Carta a añadir
     */
    public void addCard(Card card) {
        if (card != null) {
            hand.add(card);
        }
    }

    /**
     * Remueve una carta de la mano del jugador.
     *
     * @param card Carta a remover
     */
    public void removeCard(Card card) {
        hand.remove(card);
    }

    /**
     * Obtiene la mano completa del jugador.
     *
     * @return ArrayList con todas las cartas en la mano
     */
    public ArrayList<Card> getHand() {
        return hand;
    }

    /**
     * Devuelve todas las cartas del jugador y vacía su mano.
     *
     * @return ArrayList con todas las cartas que tenía el jugador
     */
    public ArrayList<Card> returnAllCards() {
        ArrayList<Card> cardsToReturn = new ArrayList<>(hand);
        hand.clear();
        return cardsToReturn;
    }

    /**
     * Obtiene el nombre del jugador.
     *
     * @return Nombre del jugador
     */
    public String getName() {
        return name;
    }

    /**
     * Verifica si el jugador ha sido eliminado.
     *
     * @return true si el jugador está eliminado, false en caso contrario
     */
    public boolean isEliminated() {
        return isEliminated;
    }

    /**
     * Marca al jugador como eliminado del juego.
     */
    public void eliminate() {
        this.isEliminated = true;
    }

    /**
     * Verifica si el jugador puede realizar al menos una jugada válida.
     *
     * @param board Tablero actual del juego
     * @return true si puede jugar al menos una carta, false en caso contrario
     */
    public boolean canPlay(Board board) {
        if (isEliminated) {
            return false;
        }

        int position = 0;
        while (position < hand.size()) {
            Card currentCard = hand.get(position);

            if (board.CanPlayCard(currentCard)) {
                return true;
            }
            position++;
        }

        return false;
    }

    /**
     * Retorna una representación en texto del jugador.
     *
     * @return String en formato "Nombre (X cartas)"
     */
    @Override
    public String toString() {
        return name + " (" + hand.size() + " cartas)";
    }
}