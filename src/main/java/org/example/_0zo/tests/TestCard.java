package org.example._0zo.tests;

import org.example._0zo.Model.Card;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Clase de pruebas unitarias para Card
 * Valida la creación de cartas y sus valores según las reglas del juego
 */
public class TestCard {

    @Test
    public void testCardConstructorWithNumberCard() {
        Card card = new Card("Hearts", "7");

        assertEquals("Hearts", card.getCardName());
        assertEquals("7", card.getCardRank());
        assertEquals(7, card.getValue());
    }

    @Test
    public void testCardConstructorWithAce() {
        Card card = new Card("Spades", "A");

        assertEquals("Spades", card.getCardName());
        assertEquals("A", card.getCardRank());
        assertEquals(10, card.getValue()); // El As vale 10 por defecto
    }

    @Test
    public void testCardConstructorWithFaceCard() {
        Card cardJ = new Card("Clubs", "J");
        Card cardQ = new Card("Diamonds", "Q");
        Card cardK = new Card("Hearts", "K");

        assertEquals(-10, cardJ.getValue()); // J resta 10
        assertEquals(-10, cardQ.getValue()); // Q resta 10
        assertEquals(-10, cardK.getValue()); // K resta 10
    }

    @Test
    public void testCardConstructorWithNine() {
        Card card = new Card("Coins", "9");

        assertEquals("9", card.getCardRank());
        assertEquals(0, card.getValue()); // El 9 vale 0
    }

    @Test
    public void testAceValueWhenSumIsLow() {
        Card ace = new Card("Hearts", "A");

        // Si la suma actual es 30, el As puede valer 10 (30+10=40 <= 50)
        int aceValue = ace.AceValue(30);
        assertEquals(10, aceValue);
    }

    @Test
    public void testAceValueWhenSumIsHigh() {
        Card ace = new Card("Spades", "A");

        // Si la suma actual es 45, el As debe valer 1 (45+10=55 > 50)
        int aceValue = ace.AceValue(45);
        assertEquals(1, aceValue);
    }

    @Test
    public void testCanPlayCardWhenValid() {
        Card card = new Card("Clubs", "5");

        // Si la suma es 40, una carta de 5 se puede jugar (40+5=45 <= 50)
        assertTrue(card.CanPlay(40));
    }

    @Test
    public void testCanPlayCardWhenInvalid() {
        Card card = new Card("Diamonds", "8");

        // Si la suma es 45, una carta de 8 NO se puede jugar (45+8=53 > 50)
        assertFalse(card.CanPlay(45));
    }

    @Test
    public void testToStringFormat() {
        Card card = new Card("Spades", "K");
        assertEquals("K of Spades", card.toString());
    }
}