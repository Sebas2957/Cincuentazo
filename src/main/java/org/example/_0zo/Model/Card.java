package org.example._0zo.Model;

/**
 * Representa una carta del juego Cincuentazo.
 * Cada carta tiene un nombre de palo, un rango y un valor numérico asociado.
 *
 * @author Sebastian-Javier-Alejandro
 * @version 1.0
 */
public class Card {

    private String cardName;
    private String cardRank;
    private int value;

    /**
     * Crea una nueva carta con el palo y rango especificados.
     * El valor se calcula automáticamente según el rango.
     *
     * @param cardName Nombre del palo (Sticks, Swords, Cups, Coins)
     * @param cardRank Rango de la carta (2-10, J, Q, K, A)
     */
    public Card(String cardName, String cardRank) {
        this.cardName = cardName;
        this.cardRank = cardRank;
        this.value = RankValue(cardRank);
    }

    /**
     * Calcula el valor numérico de una carta según su rango.
     * A = 10, J/Q/K = -10, 9 = 0, demás cartas = su número
     *
     * @param cardRank Rango de la carta
     * @return Valor numérico de la carta
     */
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

    /**
     * Calcula el valor del As (A) dinámicamente según la suma actual.
     * Devuelve 10 si no excede 50, de lo contrario devuelve 1.
     *
     * @param currentSum Suma actual en la mesa
     * @return 10 si currentSum + 10 <= 50, caso contrario 1
     */
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

    /**
     * Verifica si esta carta puede jugarse sin exceder 50 puntos.
     *
     * @param currentSum Suma actual en la mesa
     * @return true si la carta puede jugarse, false en caso contrario
     */
    public boolean CanPlay(int currentSum) {
        int valueToUse = value;

        if (cardRank.equals("A")) {
            valueToUse = AceValue(currentSum);
        }

        return (currentSum + valueToUse) <= 50;
    }

    /**
     * Obtiene el nombre del palo de la carta.
     *
     * @return Nombre del palo
     */
    public String getCardName() {
        return cardName;
    }

    /**
     * Obtiene el rango de la carta.
     *
     * @return Rango de la carta
     */
    public String getCardRank() {
        return cardRank;
    }

    /**
     * Obtiene el valor numérico base de la carta.
     *
     * @return Valor numérico de la carta
     */
    public int getValue() {
        return value;
    }

    /**
     * Retorna una representación en texto de la carta.
     *
     * @return String en formato "Rango of Palo"
     */
    @Override
    public String toString() {
        return cardRank + " of " + cardName;
    }
}