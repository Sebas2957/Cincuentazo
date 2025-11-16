package org.example._0zo.Model;

/**
 * Representa un jugador controlado por inteligencia artificial.
 * Implementa lógica automática para seleccionar cartas según estrategia definida.
 *
 * @author Sebastian-Javier-Alejandro
 * @version 1.0
 */
public class MachinePlayer extends Player {

    /**
     * Crea un nuevo jugador IA con el nombre especificado.
     *
     * @param name Nombre del jugador IA
     */
    public MachinePlayer(String name) {
        super(name);
    }

    /**
     * Encuentra la mejor carta para jugar según estrategia de la IA.
     * Prioriza cartas que no sean As, solo usa As si no hay otra opción.
     *
     * @param board Tablero actual para verificar cartas válidas
     * @return Carta seleccionada para jugar, null si no hay cartas jugables
     */
    public Card findCardToPlay(Board board) {
        Card bestCardToPlay = null;

        for (Card card : hand) {
            if (board.CanPlayCard(card) && !card.getCardRank().equals("A")) {
                bestCardToPlay = card;
                break;
            }
        }

        if (bestCardToPlay == null) {
            for (Card card : hand) {
                if (board.CanPlayCard(card)) {
                    bestCardToPlay = card;
                    break;
                }
            }
        }
        return bestCardToPlay;
    }
}