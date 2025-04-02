import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.Vector;

public class CollisionManager {
    private Vector<GameEntity> gameEntities;
    private Player player;

    public CollisionManager(Vector<GameEntity> gameEntities, Player player) {
        this.gameEntities = gameEntities;
        this.player = player;
    }

    public void checkCollisions() {
        Rectangle2D playerBoundingBox = player.getBoundingBox();
        Rectangle2D playerAttackBoundingBox = player.getAttackBoundingBox();
        // for (int i = 0; i < gameEntities.size(); i++) {
        // for (GameEntity entity : gameEntities) {
        Iterator<GameEntity> iterator = gameEntities.iterator();
        while (iterator.hasNext()) {
            GameEntity entity = iterator.next();

            if (entity instanceof Enemy) {
                Enemy enemy = (Enemy) entity;
                Rectangle2D enemyBoundingBox = enemy.getBoundingBox();
                Rectangle2D enemyAttackBoundingBox = enemy.getAttackBoundingBox();

                if (playerAttackBoundingBox != null && playerAttackBoundingBox.intersects(enemyBoundingBox))
                    enemy.damaged(player.getDamage());

                if (enemyAttackBoundingBox != null && enemyAttackBoundingBox.intersects(playerBoundingBox))
                    player.damaged(enemy.getDamage());
            }
            
            else if (entity.getBoundingBox().intersects(playerBoundingBox)) {
                // handle collision here

                //Health
                if (entity instanceof HealthPickup) {
                    player.heal();
                    iterator.remove();
                }

                //Strength                
                else if (entity instanceof StrengthPickup) {
                    player.applyBonusDamage();
                    iterator.remove();
                }

                // //Enemy
                // else if (entity instanceof Enemy && player.isAttacking() ) {
                //     ((Enemy) entity).gotHit(player.getDamage());
                // }
            }
        }
    }
}
