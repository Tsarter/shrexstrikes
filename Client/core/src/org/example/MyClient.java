package org.example;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.util.Arrays;

import static com.badlogic.gdx.Gdx.input;


public class MyClient extends ApplicationAdapter {

    private final Client client;

    public MyClient() throws IOException {
        client = new Client();  // initialize client
        Network.register(client);  // register all the classes that are sent over the network

        // Add listener to tell the client, what to do after something is sent over the network
        client.addListener(new Listener() {

            /**
             * We recieved something from the server.
             * In this case, it is probably a list of all players.
             */
            public void received(Connection connection, Object object) {
                if (object instanceof Player[]) {

                    // get the list of players
                    Player[] playersList = (Player[]) object;

                    // print the game state into the console
                    // printGame(playersList);
                    // new Basic3DTest().create();
                    //create();
                }
            }
        });

        /**
         * Connect the client to the server.
         * If server is on a local machine, "localhost" should be used as host.
         * Ports should be the same as in the server.
         */
        client.start();
        client.connect(5000, "localhost", 3000, 3001);

        /**
         * If this player inputs anything, send it to the server
         */
        while (client.isConnected()) {
            // this is like input() in python.
            Scanner scanner = new Scanner(System.in);
            String input = scanner.next();
            Character direction = input.charAt(0);  // We only get the first character.
            //render();
            /**
             * Send this movement to the server.
             * The server should move "my player" and then send the updated board to all players.
             * So they know that this client moved aswell.
             */
            client.sendUDP(direction);
        }
    }

    /**
     * Prints the game state.
     * The game is a 10x10 grid where players can move.
     *
     * @param players the list of players that are in the game.
     */
    public static void printGame(Player[] players) {
        String[][] board = new String[10][10];
        Arrays.stream(board).forEach(a -> Arrays.fill(a, "."));

        for (Player player : players) {
            board[player.y][player.x] = "O";
        }

        System.out.println("----------Current board----------");
        for (String[] row : board) {
            System.out.println(String.join("", row));
        }
        System.out.println("Enter direction to move in (NESW):");
    }
    private Player[] players;
    private TextField inputField;
    private Label[][] boardLabels;
    private Stage stage;
    @Override
    public void create() {
        // Set up the stage
        stage = new Stage(new ScreenViewport());
        input.setInputProcessor(stage);

        // Create the game board
        String[][] board = new String[10][10];
        Arrays.stream(board).forEach(a -> Arrays.fill(a, "."));

        for (Player player : players) {
            board[player.y][player.x] = "O";
        }

        // Create the skin
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Create the UI components
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label boardLabel = new Label("Current board:", skin);
        boardLabel.setFontScale(2f);
        boardLabel.setAlignment(Align.center);
        table.add(boardLabel).colspan(10).padBottom(10);
        table.row();

        boardLabels = new Label[10][10];
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                Label label = new Label(board[row][col], skin);
                label.setFontScale(2f);
                label.setAlignment(Align.center);
                label.setColor(Color.WHITE);
                table.add(label).width(50).height(50);
                boardLabels[row][col] = label;
            }
            table.row();
        }

        Label promptLabel = new Label("Enter direction to move in (NESW):", skin);
        promptLabel.setFontScale(2f);
        table.add(promptLabel).colspan(10).padTop(10);
        table.row();

        inputField = new TextField("", skin);
        table.add(inputField).colspan(10);
    }
    @Override
    public void render() {
        // Update the game board
        String[][] board = new String[10][10];
        Arrays.stream(board).forEach(a -> Arrays.fill(a, "."));

        for (Player player : players) {
            board[player.y][player.x] = "O";
        }

        // Update the board labels
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                boardLabels[row][col].setText(board[row][col]);
            }
        }

        // Handle input
        if (input.isKeyPressed(Input.Keys.ENTER)) {
            String input = inputField.getText();
        }
        if (input.toString().length() > 0) {
            // Process the input
            // ...

            // Clear the input field
            inputField.setText("");
        }

        // Clear the screen and draw the UI
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
    }


}
