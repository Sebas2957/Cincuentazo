package org.example._0zo.Model;

// Modelo para el jugador IA, hereda de Player
public class MachinePlayer extends Player {

    // Constructor
    public MachinePlayer(String name) {
        super(name); // Llama al constructor de Player
    }

    /**
     * Esta es la "IA". Decide qué carta jugar.
     * Requisito: [cite: 83] La IA debe ser "inteligente" (no suicida).
     * @param board El estado actual de la mesa.
     * @return La carta que la IA ha decidido jugar, o null si no puede jugar.
     */
    public Card findCardToPlay(Board board) {
        Card bestCardToPlay = null;

        // Estrategia simple: jugar la primera carta válida que no sea un 'A'
        for (Card card : hand) {
            if (board.CanPlayCard(card) && !card.getCardRank().equals("A")) {
                bestCardToPlay = card;
                break; // Encontramos una buena
            }
        }

        // Si no encontramos una carta "segura", y seguimos teniendo una
        // opción, buscaremos un 'A' como último recurso.
        if (bestCardToPlay == null) {
            for (Card card : hand) {
                if (board.CanPlayCard(card)) {
                    bestCardToPlay = card;
                    break;
                }
            }
        }

        // Si bestCardToPlay sigue siendo null, la IA no tiene jugadas
        // (El GameController usará canPlay() para detectar esto y eliminarla)

        return bestCardToPlay;
    }
}