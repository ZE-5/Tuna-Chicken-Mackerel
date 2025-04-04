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

            if (entity instanceof Enemy) { //Handling enemies
                Enemy enemy = (Enemy) entity;
                Rectangle2D enemyBoundingBox = enemy.getBoundingBox();
                Rectangle2D enemyAttackBoundingBox = enemy.getAttackBoundingBox();

                if (playerAttackBoundingBox != null && playerAttackBoundingBox.intersects(enemyBoundingBox))
                    enemy.damaged(player.getDamage());

                if (enemyAttackBoundingBox != null && enemyAttackBoundingBox.intersects(playerBoundingBox))
                    player.damaged(enemy.getDamage());
            }
            
            else if (entity.getBoundingBox().intersects(playerBoundingBox)) { //Non-enemy entities
                
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

                //Wall
                else if (entity instanceof Wall) {
                    Wall wall = (Wall) entity;
                    
                    if (wall.getTopLine().intersects(playerBoundingBox))
                        player.setY(wall.getY() - player.getHeight());
                    
                    else if (wall.getBottomLine().intersects(playerBoundingBox))
                        player.setY(wall.getY() + wall.getHeight());
                    
                    else if (wall.getLeftLine().intersects(playerBoundingBox))
                        player.setX(wall.getX() - player.getWidth());
                    
                    else if (wall.getRightLine().intersects(playerBoundingBox))
                        player.setX(wall.getX() + wall.getWidth());
                }
            }
        }
    }
}
