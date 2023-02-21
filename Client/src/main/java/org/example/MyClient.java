package org.example;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class MyClient {

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
                    printGame(playersList);
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
}
