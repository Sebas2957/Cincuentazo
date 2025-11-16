package org.example._0zo.Model;

import java.util.ArrayList;

/**
 *
 * @author Sebastian-Javier-Alejandro
 * Representa el tablero de juego (la "Mesa").
 * Esta clase gestiona el estado del juego, incluyendo la suma total de las cartas jugadas,
 * la última carta jugada (activa) y la lista de todas las cartas actualmente en la mesa.
 */
public class Board {

    private Card activeCard;
    private int totalSum;
    private ArrayList<Card> cardsOnTable;

    /**
     * Constructor de la clase Board.
     * Inicializa la mesa con una suma total de 0, sin carta activa (null)
     * y una lista vacía de cartas en la mesa.
     */
    public Board() {
        cardsOnTable = new ArrayList<>();
        totalSum = 0;
        activeCard = null;
    }

    /**
     * Intenta jugar una carta sobre la mesa.
     * La carta se añade a la mesa si su valor, sumado al total actual,
     * no excede el límite de 50. Maneja el valor especial del As (A).
     *
     * @param card La carta que se intenta jugar.
     * @throws IllegalStateException Si jugar la carta excede la suma límite de 50.
     */
    public void PlaceCard(Card card) {
        int cardValue = card.getValue();

        // Determina el valor del As (1 u 11) basado en la suma actual
        if (card.getCardRank().equals("A")) {
            cardValue = card.AceValue(totalSum);
        }

        // Valida si la carta es jugable
        if (totalSum + cardValue > 50) {
            // Lanza una excepción si la suma excede 50
            throw new IllegalStateException("No se puede jugar esta carta. Excedería 50.");
        }

        // Actualiza el estado de la mesa
        totalSum = totalSum + cardValue;
        activeCard = card;
        cardsOnTable.add(card);
    }

    /**
     * Recoge todas las cartas jugadas en la mesa, excepto la última.
     * Este método se usa para "reciclar" las cartas y devolverlas al mazo.
     * La última carta jugada permanece en la mesa para continuar el juego.
     *
     * @return Una lista (ArrayList) de cartas para ser devueltas al mazo.
     * Si hay 1 o menos cartas en la mesa, devuelve una lista vacía.
     */
    public ArrayList<Card> RecycleCards() {
        ArrayList<Card> recycledCards = new ArrayList<>();

        // Si solo hay una carta (o ninguna), no hay nada que reciclar
        if (cardsOnTable.size() <= 1) {
            return recycledCards;
        }

        int index = 0;
        int limit = cardsOnTable.size() - 1; // Excluye la última carta

        // Añade todas las cartas excepto la última a la lista de reciclaje
        while (index < limit) {
            recycledCards.add(cardsOnTable.get(index));
            index++;
        }

        // Limpia la mesa y vuelve a añadir solo la última carta jugada
        Card lastPlayedCard = cardsOnTable.get(cardsOnTable.size() - 1);
        cardsOnTable.clear();
        cardsOnTable.add(lastPlayedCard);

        return recycledCards;
    }

    /**
     * Verifica si una carta específica puede ser jugada sin exceder la suma límite de 50.
     * Este es un método de validación que no modifica el estado del tablero.
     *
     * @param card La carta que se desea verificar.
     * @return {@code true} si la carta puede ser jugada (la suma es <= 50),
     * {@code false} en caso contrario.
     */
    public boolean CanPlayCard(Card card) {
        int cardValue = card.getValue();

        // Ajusta el valor del As (A) dependiendo de la suma total
        if (card.getCardRank().equals("A")) {
            cardValue = card.AceValue(totalSum);
        }

        return (totalSum + cardValue) <= 50;
    }

    /**
     * Obtiene la suma total actual de los valores de las cartas en la mesa.
     *
     * @return El valor entero de la suma total.
     */
    public int GetTotalSum() {
        return totalSum;
    }

    /**
     * Obtiene la última carta que fue jugada en la mesa.
     *
     * @return El objeto {@link Card} que representa la carta activa,
     * o {@code null} si no se ha jugado ninguna carta.
     */
    public Card GetActiveCard() {
        return activeCard;
    }

    /**
     * Obtiene el número total de cartas que se han jugado en la mesa en la ronda actual.
     *
     * @return El conteo total de cartas en la mesa.
     */
    public int GetCardsCount() {
        return cardsOnTable.size();
    }

    /**
     * Limpia completamente la mesa.
     * Reinicia la suma total a 0, la carta activa a null y vacía la lista
     * de cartas en la mesa. Usado para empezar una nueva ronda o juego.
     */
    public void ClearTable() {
        cardsOnTable.clear();
        totalSum = 0;
        activeCard = null;
    }

    /**
     * Devuelve una representación en cadena (String) del estado actual del tablero.
     *
     * @return Un String que describe la suma total, la carta activa (visible)
     * y el número de cartas jugadas.
     */
    @Override
    public String toString() {
        String cardInfo = (activeCard != null) ? activeCard.toString() : "Ninguna";
        return "Mesa → Suma: " + totalSum + " | Carta visible: " + cardInfo
                + " | Cartas jugadas: " + cardsOnTable.size();
    }
}