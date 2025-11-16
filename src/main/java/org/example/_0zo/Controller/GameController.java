package org.example._0zo.Controller;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import org.example._0zo.Model.*;

import java.util.ArrayList;

/**
 * Controlador principal del juego Cincuentazo.
 * Gestiona la lógica del juego, turnos, interfaz de usuario y eventos.
 *
 * @author Sebastian-Javier-Alejandro
 * @version 1.0
 */
public class GameController {

    private static final int INITIAL_CARDS = 4;
    private int NUM_MACHINES = 2;
    private static final int MIN_MACHINE_DELAY = 2;
    private static final int MAX_MACHINE_DELAY = 4;

    @FXML private Label lblTableSum;
    @FXML private Label lblCurrentCard;
    @FXML private Label lblPlayerTurn;
    @FXML private HBox playerHandContainer;
    @FXML private ImageView imgActiveCard;

    private Board board;
    private Deck deck;
    private ArrayList<MachinePlayer> players;
    private int currentPlayerIndex;
    private boolean gameIsOver;
    private boolean waitingForMachine;

    /**
     * Inicializa el controlador. Se ejecuta automáticamente al cargar el FXML.
     */
    @FXML
    public void initialize() {
    }

    /**
     * Configura el número de jugadores máquina e inicia el juego.
     *
     * @param numMachines Número de máquinas (1-3)
     */
    public void setNumMachines(int numMachines) {
        if (numMachines >= 1 && numMachines <= 3) {
            this.NUM_MACHINES = numMachines;
            startNewGame();
        } else {
            System.err.println("Número de máquinas inválido: " + numMachines);
            this.NUM_MACHINES = 2;
            startNewGame();
        }
    }

    /**
     * Inicia una nueva partida con configuración inicial.
     */
    private void startNewGame() {
        this.board = new Board();
        this.deck = new Deck();
        this.players = new ArrayList<>();
        this.currentPlayerIndex = 0;
        this.gameIsOver = false;
        this.waitingForMachine = false;

        setupPlayers();
        dealInitialCards();
        updateUI();
        checkCurrentPlayer();
    }

    /**
     * Crea los jugadores (humano y máquinas).
     */
    private void setupPlayers() {
        players.add(new MachinePlayer("Human"));

        for (int i = 1; i <= NUM_MACHINES; i++) {
            players.add(new MachinePlayer("Machine " + i));
        }
    }

    /**
     * Reparte las cartas iniciales a todos los jugadores y coloca la primera carta en la mesa.
     */
    private void dealInitialCards() {
        for (int i = 0; i < INITIAL_CARDS; i++) {
            for (MachinePlayer player : players) {
                if (!deck.isEmpty()) {
                    player.addCard(deck.getCard());
                }
            }
        }

        if (!deck.isEmpty()) {
            Card initialCard = deck.getCard();
            board.PlaceCard(initialCard);
            System.out.println("Initial card: " + initialCard);
            System.out.println("Initial sum: " + board.GetTotalSum());
        }
    }

    /**
     * Maneja el clic del jugador humano sobre una carta.
     *
     * @param cardSelected Carta seleccionada por el jugador
     */
    private void handleHumanCardClick(Card cardSelected) {
        if (currentPlayerIndex != 0 || gameIsOver || waitingForMachine) {
            return;
        }

        MachinePlayer humanPlayer = players.get(0);

        if (board.CanPlayCard(cardSelected)) {
            playCard(humanPlayer, cardSelected);
            updateUI();
            passTurn();
        } else {
            System.out.println("Invalid play. Would exceed 50.");
        }
    }

    /**
     * Ejecuta el turno de un jugador máquina con delay aleatorio entre 2-4 segundos.
     *
     * @param machine Jugador máquina que jugará
     */
    private void playMachineTurn(MachinePlayer machine) {
        waitingForMachine = true;
        lblPlayerTurn.setText("Turn: " + machine.getName() + " (Thinking...)");

        int delaySeconds = MIN_MACHINE_DELAY + (int)(Math.random() * (MAX_MACHINE_DELAY - MIN_MACHINE_DELAY + 1));

        PauseTransition pause = new PauseTransition(Duration.seconds(delaySeconds));
        pause.setOnFinished(event -> {
            if (gameIsOver) {
                return;
            }

            Card cardToPlay = machine.findCardToPlay(board);

            if (cardToPlay != null) {
                playCard(machine, cardToPlay);
                System.out.println(machine.getName() + " played " + cardToPlay);
            }

            waitingForMachine = false;
            updateUI();
            passTurn();
        });

        pause.play();
    }

    /**
     * Juega una carta en la mesa y roba una nueva del mazo.
     *
     * @param player Jugador que juega la carta
     * @param card Carta a jugar
     */
    private void playCard(MachinePlayer player, Card card) {
        board.PlaceCard(card);
        player.removeCard(card);
        drawCardForPlayer(player);
    }

    /**
     * Hace que el jugador robe una carta del mazo.
     * Si el mazo está vacío, recicla las cartas de la mesa.
     *
     * @param player Jugador que robará la carta
     */
    private void drawCardForPlayer(MachinePlayer player) {
        if (deck.isEmpty()) {
            recycleDeck();
        }

        if (!deck.isEmpty()) {
            player.addCard(deck.getCard());
        }
    }

    /**
     * Recicla las cartas de la mesa al mazo cuando se acaban.
     * Mantiene la última carta jugada en la mesa.
     */
    private void recycleDeck() {
        System.out.println("--- Recycling deck ---");
        ArrayList<Card> recycledCards = board.RecycleCards();
        deck.AddCards(recycledCards);
        deck.ShuffleDeck();
    }

    /**
     * Pasa el turno al siguiente jugador no eliminado.
     */
    private void passTurn() {
        if (gameIsOver) {
            return;
        }

        if (checkWinCondition()) {
            return;
        }

        findNextPlayer();
        checkCurrentPlayer();
    }

    /**
     * Encuentra el siguiente jugador no eliminado en orden circular.
     */
    private void findNextPlayer() {
        do {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        } while (players.get(currentPlayerIndex).isEliminated());
    }

    /**
     * Verifica el estado del jugador actual y ejecuta su turno.
     * Si no puede jugar ninguna carta, es eliminado.
     */
    private void checkCurrentPlayer() {
        if (gameIsOver) {
            return;
        }

        MachinePlayer currentPlayer = players.get(currentPlayerIndex);
        lblPlayerTurn.setText("Turn: " + currentPlayer.getName());

        if (!currentPlayer.canPlay(board)) {
            eliminatePlayer(currentPlayer);
            passTurn();
            return;
        }

        if (currentPlayerIndex != 0) {
            playMachineTurn(currentPlayer);
        }
    }

    /**
     * Verifica si hay un ganador (solo queda 1 jugador activo).
     *
     * @return true si el juego terminó, false en caso contrario
     */
    private boolean checkWinCondition() {
        int activePlayers = 0;
        MachinePlayer winner = null;

        for (MachinePlayer p : players) {
            if (!p.isEliminated()) {
                activePlayers++;
                winner = p;
            }
        }

        if (activePlayers <= 1) {
            endGame(winner);
            return true;
        }

        return false;
    }

    /**
     * Elimina un jugador del juego y devuelve sus cartas al mazo.
     *
     * @param player Jugador a eliminar
     */
    private void eliminatePlayer(MachinePlayer player) {
        player.eliminate();
        System.out.println("PLAYER ELIMINATED: " + player.getName() + "!");

        ArrayList<Card> cardsToReturn = player.returnAllCards();
        deck.AddCards(cardsToReturn);
        deck.ShuffleDeck();
    }

    /**
     * Finaliza el juego y muestra el ganador en la interfaz.
     *
     * @param winner Jugador ganador
     */
    private void endGame(MachinePlayer winner) {
        gameIsOver = true;
        if (winner != null) {
            lblPlayerTurn.setText("Game Over!");
            lblTableSum.setText("WINNER: " + winner.getName());
        } else {
            lblPlayerTurn.setText("Game Over!");
            lblTableSum.setText("Error: No winner found.");
        }
        playerHandContainer.getChildren().clear();
    }

    /**
     * Actualiza todos los elementos de la interfaz de usuario.
     */
    private void updateUI() {
        if (gameIsOver) {
            return;
        }

        updateTableSum();
        updateActiveCard();
        renderPlayerHand();
    }

    /**
     * Actualiza el label con la suma actual de la mesa.
     */
    private void updateTableSum() {
        lblTableSum.setText("Sum: " + board.GetTotalSum());
    }

    /**
     * Actualiza la carta activa visible en la mesa.
     */
    private void updateActiveCard() {
        Card activeCard = board.GetActiveCard();

        if (activeCard != null) {
            lblCurrentCard.setText(activeCard.toString());
            displayCardImage(activeCard, imgActiveCard);
        } else {
            lblCurrentCard.setText("Empty table");
            imgActiveCard.setVisible(false);
        }
    }

    /**
     * Muestra la imagen de una carta en un ImageView.
     *
     * @param card Carta a mostrar
     * @param imageView ImageView donde mostrar la carta
     */
    private void displayCardImage(Card card, ImageView imageView) {
        String imagePath = getCardImagePath(card);

        try {
            Image cardImage = new Image(getClass().getResourceAsStream(imagePath));
            imageView.setImage(cardImage);
            imageView.setVisible(true);
        } catch (Exception e) {
            imageView.setVisible(false);
        }
    }

    /**
     * Renderiza las cartas del jugador humano en la interfaz.
     */
    private void renderPlayerHand() {
        playerHandContainer.getChildren().clear();
        MachinePlayer humanPlayer = players.get(0);

        for (Card card : humanPlayer.getHand()) {
            StackPane cardContainer = createCardContainer(card);
            playerHandContainer.getChildren().add(cardContainer);
        }
    }

    /**
     * Crea un contenedor visual para una carta con efectos y eventos.
     *
     * @param card Carta a representar
     * @return StackPane con la carta visual
     */
    private StackPane createCardContainer(Card card) {
        StackPane cardContainer = new StackPane();
        cardContainer.setPrefSize(80, 120);
        cardContainer.setStyle("-fx-cursor: hand;");

        addCardVisual(cardContainer, card);
        addCardHoverEffect(cardContainer);
        addCardClickHandler(cardContainer, card);

        if (!board.CanPlayCard(card)) {
            cardContainer.setOpacity(0.4);
            cardContainer.setDisable(true);
        }

        return cardContainer;
    }

    /**
     * Añade la imagen o label de una carta al contenedor.
     *
     * @param container Contenedor donde añadir la visual
     * @param card Carta a representar
     */
    private void addCardVisual(StackPane container, Card card) {
        String imagePath = getCardImagePath(card);

        try {
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            ImageView cardImage = new ImageView(image);
            cardImage.setFitWidth(80);
            cardImage.setFitHeight(120);
            cardImage.setPreserveRatio(true);
            container.getChildren().add(cardImage);
        } catch (Exception e) {
            Label cardLabel = new Label(card.getCardRank());
            cardLabel.setStyle(
                    "-fx-background-color: white;" +
                            "-fx-border-color: black;" +
                            "-fx-border-width: 2;" +
                            "-fx-font-size: 18px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-alignment: center;" +
                            "-fx-pref-width: 80;" +
                            "-fx-pref-height: 120;"
            );
            container.getChildren().add(cardLabel);
        }
    }

    /**
     * Añade efecto de hover (agrandar) a una carta.
     *
     * @param container Contenedor de la carta
     */
    private void addCardHoverEffect(StackPane container) {
        container.setOnMouseEntered(event -> {
            container.setScaleX(1.15);
            container.setScaleY(1.15);
        });

        container.setOnMouseExited(event -> {
            container.setScaleX(1.0);
            container.setScaleY(1.0);
        });
    }

    /**
     * Añade handler de clic a una carta para que el jugador pueda jugarla.
     *
     * @param container Contenedor de la carta
     * @param card Carta asociada
     */
    private void addCardClickHandler(StackPane container, Card card) {
        container.setOnMouseClicked(event -> handleHumanCardClick(card));
    }

    /**
     * Obtiene la ruta del archivo de imagen de una carta.
     * Convierte los nombres de palos españoles a sus equivalentes visuales.
     *
     * @param card Carta a buscar
     * @return Ruta del archivo de imagen
     */
    private String getCardImagePath(Card card) {
        String rank = card.getCardRank();
        String cardName = card.getCardName().toLowerCase();

        String suitName = switch (cardName) {
            case "cups" -> "heart";
            case "swords" -> "spade";
            case "coins" -> "diamond";
            case "sticks" -> "club";
            default -> cardName;
        };

        String fileName;
        if (rank.equals("A")) {
            fileName = "as_" + suitName + ".png";
        } else if (rank.equals("10") && suitName.equals("club")) {
            fileName = "10_clubs.png";
        } else {
            fileName = rank + "_" + suitName + ".png";
        }

        return "/org/example/_0zo/imagenes/" + fileName;
    }

    /**
     * Muestra un diálogo con las instrucciones completas del juego.
     *
     * @param event Evento del botón de instrucciones
     */
    public void ClickInstructions(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Instrucciones");
        alert.setHeaderText("Instrucciones para jugar al Cincuentazo");
        alert.setContentText(
                "1. En la mesa existe una suma que no debe exceder 50 (>50).\n" +
                        "2. Se reparten 4 cartas aleatorias a cada jugador al inicio.\n" +
                        "3. Se coloca una carta aleatoria en la mesa boca arriba para iniciar la suma.\n" +
                        "4. El resto de las cartas quedan en el mazo boca abajo.\n" +
                        "5. El juego se desarrolla por turnos.\n" +
                        "6. En su turno, el jugador debe seleccionar una carta de su mano.\n" +
                        "7. Las cartas con números del 2 al 8 y el 10 suman su número.\n" +
                        "8. Las cartas con número 9 ni suman ni restan (valor 0).\n" +
                        "9. Las cartas con letras J, Q, K restan 10.\n" +
                        "10. Las cartas con letra A suman 1 o 10, según convenga.\n" +
                        "11. La carta seleccionada queda boca arriba en la mesa encima de la carta anterior.\n" +
                        "12. La suma de la mesa se modifica con el valor de la carta jugada.\n" +
                        "13. Después de jugar, el jugador toma una carta del mazo para mantener siempre 4 cartas en su mano.\n" +
                        "14. Si el jugador no puede jugar ninguna carta porque excedería 50, queda eliminado.\n" +
                        "15. La suma de la mesa puede iniciar en 0 (carta 9), 1 (carta A) o -10 (cartas J, Q, K).\n" +
                        "16. Si las cartas del mazo se terminan, se toman las cartas de la mesa excepto la última jugada, se barajan y quedan disponibles en el mazo.\n" +
                        "17. La suma de la mesa no se modifica cuando se reciclan las cartas.\n" +
                        "18. Las cartas del jugador eliminado se envían al final del mazo y quedan disponibles.\n" +
                        "19. El jugador máquina selecciona una carta en un tiempo entre 2 a 4 segundos.\n" +
                        "20. El objetivo es ser el último jugador en quedar en juego.\n" +
                        "21. El juego finaliza cuando solo queda un jugador en juego.\n" +
                        "22. Se declara ganador al único jugador que queda en juego.\n");
        alert.showAndWait();
    }
}