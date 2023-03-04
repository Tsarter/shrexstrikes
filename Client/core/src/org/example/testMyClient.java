package org.example;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

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

import static com.badlogic.gdx.Gdx.input;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import static com.badlogic.gdx.Gdx.input;

public class testMyClient extends ApplicationAdapter {
    private final Client client;
    private Player[] players = new Player[0];
    private TextField inputField;
    private Label[][] boardLabels;
    private Stage stage;
    @Override
    public void create() {
        // Gdx.gl.glClearColor(1, 0, 0, 1);
        // Create the window with width and height of 800 pixels
        Gdx.graphics.setWindowedMode(800, 800);
        // Set up the stage
        stage = new Stage(new ScreenViewport());
        input.setInputProcessor(stage);
        // Create the game board
        String[][] board = new String[10][10];
        Arrays.stream(board).forEach(a -> Arrays.fill(a, "."));

        for (Player player : players) {
            board[player.y][player.x] = "O";
        }
        FileHandle test = Gdx.files.internal("C:\\Users\\Tanel\\Documents\\AA_PROJECTS\\AA TalTech stuff\\ShrexStrikes\\Client\\assets\\uiskin.json");
        if (test.exists()) {
            System.out.println("File exists!");
        } else {
            System.out.println("File does not exist.");
        }
        // Create the skin
        Skin skin = new Skin(Gdx.files.internal("C:\\Users\\Tanel\\Documents\\AA_PROJECTS\\AA TalTech stuff\\ShrexStrikes\\Client\\assets\\uiskin.json"));

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
        try {
            client.start();
            client.connect(5000, "localhost", 3000, 3001);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
        waitForUserInput();
    }

    public testMyClient() throws IOException {
        client = new Client();
        Network.register(client);
        client.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof Player[]) {
                    Player[] playersList = (Player[]) object;
                    // printGame(playersList);
                    players = playersList;
                    // new Basic3DTest().create();
                    // render(); // Don't call render() from here
                }
            }
        });
    }

    private void waitForUserInput() {
        if (client.isConnected()) {
            new Thread(() -> {
                Scanner scanner = new Scanner(System.in);

                while (true) {
                    if (scanner.hasNext()) {
                        String input = scanner.next();
                        System.out.println("new input" + input);
                        Character direction = input.charAt(0);
                        client.sendUDP(direction);
                    }
                }
            }).start();
        }
        }
}
