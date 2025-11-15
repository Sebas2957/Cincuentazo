package org.example._0zo.Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.example._0zo.Model.*; // Importa Player y MachinePlayer

import java.util.ArrayList;

public class GameController {

    // --- Constantes del Juego ---
    private static final int INITIAL_CARDS = 4;
    private static final int NUM_PLAYERS = 3; // 1 Humano, 2 Máquinas

    // --- Referencias a la Vista (@FXML) ---
    @FXML private Label lblTableSum;
    @FXML private Label lblCurrentCard;
    @FXML private Label lblPlayerTurn; // Etiqueta para mostrar de quién es el turno
    @FXML private HBox playerHandContainer; // Contenedor para las cartas visuales
    @FXML private Button btnEndTurn; // Botón (opcional) o se puede manejar al clic de carta

    // --- Referencias al Modelo ---
    private Board board;
    private Deck deck;
    private ArrayList<Player> players;
    private int currentPlayerIndex;
    private boolean gameIsOver;

    // --- 1. Inicialización ---

    @FXML
    public void initialize() {
        // Este método se ejecuta automáticamente al cargar la ventana
        startNewGame();
    }

    private void startNewGame() {
        this.board = new Board();
        this.deck = new Deck();
        this.players = new ArrayList<>();
        this.currentPlayerIndex = 0;
        this.gameIsOver = false;

        setupPlayers();
        dealInitialCards();

        updateUI();
        lblPlayerTurn.setText("Turno de: " + players.get(currentPlayerIndex).getName());
    }

    private void setupPlayers() {
        players.add(new Player("Humano")); // El jugador 0 es el humano
        players.add(new MachinePlayer("Máquina 1"));
        players.add(new MachinePlayer("Máquina 2"));
    }

    /**
     * [cite: 11] Requisito: Repartir 4 cartas a cada jugador.
     */
    private void dealInitialCards() {
        for (int i = 0; i < INITIAL_CARDS; i++) {
            for (Player player : players) {
                if (!deck.isEmpty()) {
                    player.addCard(deck.getCard());
                }
            }
        }
    }

    // --- 2. Lógica del Jugador Humano ---

    /**
     * Este método debe ser llamado por el evento de clic de la CARTA VISUAL.
     * (Lo configuraremos en 'renderPlayerHand')
     */
    private void handleHumanCardClick(Card cardSelected) {
        // No hacer nada si no es el turno del humano o el juego terminó
        if (currentPlayerIndex != 0 || gameIsOver) return;

        Player humanPlayer = players.get(0);

        // [cite: 10] Validar si la carta es jugable
        if (board.CanPlayCard(cardSelected)) {
            // 1. Jugar la carta
            board.PlaceCard(cardSelected);
            humanPlayer.removeCard(cardSelected);

            // 2. Robar una nueva carta
            drawCardForPlayer(humanPlayer);

            // 3. Actualizar y pasar turno
            updateUI();
            passTurn();

        } else {
            // Informar al usuario que la jugada no es válida
            System.out.println("Jugada no válida. La suma superaría 50.");
            // (Aquí podrías poner un Label de error)
        }
    }

    // --- 3. Lógica del Jugador Máquina (IA) ---

    private void playMachineTurn(MachinePlayer machine) {
        lblPlayerTurn.setText("Turno de: " + machine.getName() + " (Pensando...)");

        // [cite: 48] Requisito: Implementar dos hilos (uno por máquina)
        Thread machineThread = new Thread(() -> {
            try {
                // [cite: 94] [cite: 102] Requisito: La máquina debe tardar entre 2 a 4 segundos
                int waitTime = 2000 + (int)(Math.random() * 2000);
                Thread.sleep(waitTime);

                // [cite: 94] Requisito: Actualizar UI desde el hilo principal
                Platform.runLater(() -> {
                    if (gameIsOver) return;

                    // [cite: 83] Requisito: La IA debe ser "inteligente"
                    Card cardToPlay = machine.findCardToPlay(board);

                    // (La lógica de 'findCardToPlay' ya aseguró que la carta es válida)
                    if (cardToPlay != null) {
                        board.PlaceCard(cardToPlay);
                        machine.removeCard(cardToPlay);
                        System.out.println(machine.getName() + " jugó " + cardToPlay);

                        // Robar carta
                        drawCardForPlayer(machine);
                    }

                    // Actualizar y pasar turno
                    updateUI();
                    passTurn();
                });

            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt(); // Buena práctica
            }
        });

        machineThread.start();
    }

    // --- 4. Gestión del Flujo del Juego (Turnos y Eliminación) ---

    private void passTurn() {
        if (gameIsOver) return;

        // [cite: 120] Requisito: Comprobar condición de victoria (solo 1 jugador activo)
        int activePlayers = 0;
        Player winner = null;
        for (Player p : players) {
            if (!p.isEliminated()) {
                activePlayers++;
                winner = p;
            }
        }

        if (activePlayers <= 1) {
            endGame(winner);
            return;
        }

        // --- Bucle para encontrar al *siguiente* jugador válido ---
        Player nextPlayer;
        do {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            nextPlayer = players.get(currentPlayerIndex);
        } while (nextPlayer.isEliminated()); // Salta jugadores ya eliminados

        lblPlayerTurn.setText("Turno de: " + nextPlayer.getName());

        // [cite: 108] Requisito: Comprobar si el jugador debe ser eliminado
        if (!nextPlayer.canPlay(board)) {
            eliminatePlayer(nextPlayer);
            passTurn(); // Llamada recursiva para pasar al *siguiente* jugador
            return;
        }

        // Si el siguiente jugador es una máquina, iniciar su turno
        if (nextPlayer instanceof MachinePlayer) {
            playMachineTurn((MachinePlayer) nextPlayer);
        }
        // Si es humano (index 0), el controlador simplemente espera
        // a que 'handleHumanCardClick' sea disparado por la UI.
    }

    private void eliminatePlayer(Player player) {
        player.eliminate();
        System.out.println("¡JUGADOR ELIMINADO: " + player.getName() + "!");

        // Devolver sus cartas al mazo
        ArrayList<Card> cardsToReturn = player.returnAllCards();
        deck.AddCards(cardsToReturn);
        deck.ShuffleDeck();
    }

    private void endGame(Player winner) {
        gameIsOver = true;
        if (winner != null) {
            lblPlayerTurn.setText("¡Juego Terminado!");
            lblTableSum.setText("GANADOR: " + winner.getName());
        } else {
            lblPlayerTurn.setText("¡Juego Terminado!");
            lblTableSum.setText("Error: No se encontró ganador.");
        }
        playerHandContainer.getChildren().clear(); // Limpiar la mano
    }

    // --- 5. Métodos de Utilidad y UI ---

    /**
     * [cite: 23] Requisito: Robar una carta después de jugar.
     */
    private void drawCardForPlayer(Player player) {
        // [cite: 31] Requisito: Reciclar mazo si está vacío
        if (deck.isEmpty()) {
            recycleDeck();
        }

        if (!deck.isEmpty()) {
            player.addCard(deck.getCard());
        }
    }

    private void recycleDeck() {
        System.out.println("--- Reciclando mazo ---");
        ArrayList<Card> recycledCards = board.RecycleCards();
        deck.AddCards(recycledCards);
        deck.ShuffleDeck();
    }

    /**
     * Actualiza todos los elementos visuales del juego.
     */
    private void updateUI() {
        if (gameIsOver) return;

        // Actualizar contadores
        lblTableSum.setText(String.valueOf(board.GetTotalSum()));

        Card activeCard = board.GetActiveCard();
        lblCurrentCard.setText(activeCard != null ? activeCard.toString() : "Mesa Vacía");

        // Renderizar la mano del jugador humano
        renderPlayerHand();
    }

    /**
     * Este es el puente VISTA-CONTROLADOR.
     * Crea los nodos visuales para la mano del humano.
     */
    private void renderPlayerHand() {
        playerHandContainer.getChildren().clear(); // Limpiar la mano actual
        Player humanPlayer = players.get(0);

        for (Card card : humanPlayer.getHand()) {
            // --- ¡IMPORTANTE! ---
            // Aquí debes crear tu representación visual de la carta.
            // Puede ser un ImageView, un StackPane, o un simple Botón.
            // Usaré un Botón como ejemplo:

            Button cardButton = new Button(card.toString());
            cardButton.setPrefSize(80, 120); // Tamaño de ejemplo

            // Asignar el evento de clic
            cardButton.setOnAction(event -> {
                handleHumanCardClick(card); // Llama al método del controlador
            });

            playerHandContainer.getChildren().add(cardButton);
        }
    }
}