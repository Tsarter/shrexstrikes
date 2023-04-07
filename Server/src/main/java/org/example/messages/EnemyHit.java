package org.example.messages;

public class EnemyHit {
    public int idOfEnemyHit;
    public int idOfPlayerWhoHit;
    public int damage;
    public boolean isDead;

    public EnemyHit(int idOfEnemyHit, int idOfPlayerWhoHit, int damage, boolean isDead) {
        this.idOfEnemyHit = idOfEnemyHit;
        this.idOfPlayerWhoHit = idOfPlayerWhoHit;
        this.damage = damage;
        this.isDead = isDead;
    }
}
