package org.example._0zo.Model;

import java.util.ArrayList;
import java.util.Random;

/**
 * Representa el mazo (Deck) de cartas del juego.
 * Esta clase es responsable de la creación, mezcla, reparto y gestión
 * de las cartas disponibles para jugar.
 *
 * @author Sebastian-Javier-Alejandro
 * @version 1.0
 */
public class Deck {

    private ArrayList<Card> DeckCards;
    private Random random;

    /**
     * Constructor de la clase Deck.
     * Inicializa el mazo de cartas, crea todas las cartas del juego (baraja completa)
     * y las mezcla inmediatamente.
     */
    public Deck() {
        DeckCards = new ArrayList<>();
        random = new Random();
        FullDeck();
        ShuffleDeck();
    }

    /**
     * Llena el mazo con un juego completo de cartas, creando 52 cartas
     * (4 palos x 13 rangos).
     */
    public void FullDeck() {
        // Asumiendo palos de la baraja española o similar, aunque usa nombres en inglés.
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

    /**
     * Mezcla las cartas actualmente en el mazo utilizando el algoritmo de
     * Fisher-Yates (o Knuth) de manera eficiente.
     */
    public void ShuffleDeck() {
        int cardPosition = DeckCards.size() - 1;

        while (cardPosition > 0) {

            int randomPosition = random.nextInt(cardPosition + 1);

            // Intercambio de la carta en la posición actual con una posición aleatoria
            Card tempCard = DeckCards.get(cardPosition);
            DeckCards.set(cardPosition, DeckCards.get(randomPosition));
            DeckCards.set(randomPosition, tempCard);
            cardPosition--;
        }
    }

    /**
     * Saca y elimina la última carta del mazo (simulando sacar la carta de arriba).
     *
     * @return La carta sacada (objeto {@link Card}).
     * @throws IllegalStateException Si el mazo está vacío al intentar sacar una carta.
     */
    public Card getCard() {
        if (isEmpty()){
            throw new IllegalStateException("El mazo está vacio, no hay cartas para sacar");
        }

        int lastPosition = DeckCards.size() - 1;
        Card getCard = DeckCards.get(lastPosition);
        DeckCards.remove(lastPosition);

        return getCard;
    }

    /**
     * Añade una colección de cartas al mazo y luego lo mezcla.
     * Este método se utiliza para reciclar cartas que han sido jugadas.
     *
     * @param cardsToAdd La lista (ArrayList) de cartas a añadir al mazo.
     */
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

    /**
     * Verifica si el mazo de cartas está completamente vacío.
     *
     * @return {@code true} si no quedan cartas en el mazo, {@code false} en caso contrario.
     */
    public boolean isEmpty() {

        return DeckCards.isEmpty();
    }

    /**
     * Obtiene la cantidad de cartas que quedan actualmente en el mazo.
     *
     * @return El número entero de cartas restantes.
     */
    public int CardsLeft(){

        return DeckCards.size();
    }

    /**
     * Reinicia el mazo para un nuevo juego.
     * Limpia la lista actual de cartas, la llena con la baraja completa y la mezcla.
     */
    public void ResetDeck() {
        DeckCards.clear();
        FullDeck();
        ShuffleDeck();
    }

    /**
     * Devuelve una representación en cadena (String) del estado actual del mazo.
     *
     * @return Un String que indica la cantidad de cartas restantes.
     */
    @Override
    public String toString(){
        return "Mazo con " + CardsLeft() + " cartas restantes";
    }
}