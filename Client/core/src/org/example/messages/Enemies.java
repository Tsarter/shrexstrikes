package org.example.messages;

import org.example.spawner.Enemy;

public class Enemies {
    public Enemy[] enemies;

    public Enemies() { }
    public Enemies(Enemy[] enemies) {
        this.enemies = enemies;
    }
    public void removeEnemy(Enemy enemy) {
        Enemy[] newEnemies = new Enemy[enemies.length - 1];
        int index = 0;
        for (Enemy e : enemies) {
            if (e != enemy) {
                newEnemies[index] = e;
                index++;
            }
        }
        enemies = newEnemies;
    }
}
