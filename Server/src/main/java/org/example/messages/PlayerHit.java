package org.example.messages;

public class PlayerHit {
    public int idOfPlayerHit;
    public int idOfPlayerWhoHit;
    public int damage;


    public PlayerHit(int idOfPlayerHit, int idOfPlayerWhoHi, int damage) {
        this.idOfPlayerHit = idOfPlayerHit;
        this.idOfPlayerWhoHit = idOfPlayerWhoHit;
        this.damage = damage;
    }
}
