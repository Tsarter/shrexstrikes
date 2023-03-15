package org.example.messages;

public class PlayerHit {
    public int idOfPlayerHit;
    public int idOfPlayerWhoHit;

    public PlayerHit(int idOfPlayerHit, int idOfPlayerWhoHit) {
        this.idOfPlayerHit = idOfPlayerHit;
        this.idOfPlayerWhoHit = idOfPlayerWhoHit;
    }
}
