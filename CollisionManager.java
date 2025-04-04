import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.Vector;

public class CollisionManager {
    private Vector<GameEntity> gameEntities;
    private Player player;

    public CollisionManager(Player player, Vector<GameEntity> gameEntities) {
        this.gameEntities = gameEntities;
        this.player = player;
    }

    public void checkCollisions(boolean[] keys) {
        Rectangle2D playerBoundingBox = player.getBoundingBox();
        Rectangle2D playerAttackBoundingBox = player.getAttackBoundingBox();


        Vector<Projectile> projectiles = new Vector<Projectile>();
        for (GameEntity entity : gameEntities) {
            if (!(entity instanceof Projectile))
                continue;

            Projectile projectile = (Projectile) entity;
            if (projectile instanceof EnemyProjectile && playerBoundingBox.intersects(projectile.getBoundingBox())){ //if projectile hits player
                player.damaged(projectile.getDamage());
                projectile.delete();
            }
            else
                projectiles.add(projectile);
        }


        Iterator<Projectile> projectileIterator;
        Iterator<GameEntity> entityIterator = gameEntities.iterator();
        while (entityIterator.hasNext()) {
            GameEntity entity = entityIterator.next();

            if (entity instanceof Enemy) { //Handling enemies
                Enemy enemy = (Enemy) entity;
                Rectangle2D enemyBoundingBox = enemy.getBoundingBox();
                Rectangle2D enemyAttackBoundingBox = enemy.getAttackBoundingBox();

                if (playerAttackBoundingBox != null && playerAttackBoundingBox.intersects(enemyBoundingBox))
                    enemy.damaged(player.getDamage());

                if (enemyAttackBoundingBox != null && enemyAttackBoundingBox.intersects(playerBoundingBox))
                    player.damaged(enemy.getDamage());

                //Check if player's projectile hits enemy
                projectileIterator = projectiles.iterator();
                while (projectileIterator.hasNext()) {
                    Projectile projectile = projectileIterator.next();
                    if (projectile instanceof PlayerProjectile && !enemy.isDead() && projectile.getBoundingBox().intersects(enemyBoundingBox)) {
                        enemy.damaged(projectile.getDamage());
                        projectile.delete();
                        projectileIterator.remove();
                    }
                }
            }

            else if (entity instanceof Wall) { //Handling walls
                Wall wall = (Wall) entity;

                //Wall collision
                if (wall.getBoundingBox().intersects(playerBoundingBox)) {
                    if (keys[3] && wall.getTopLine().intersects(playerBoundingBox))
                        player.setY(wall.getY() - player.getHeight() - 1);
                    
                    else if (keys[0] && wall.getBottomLine().intersects(playerBoundingBox))
                        player.setY(wall.getY() + wall.getHeight());
                    
                    else if (keys[1] && wall.getLeftLine().intersects(playerBoundingBox))
                        player.setX(wall.getX() - player.getWidth() - 1);
                    
                    else if (keys[2] && wall.getRightLine().intersects(playerBoundingBox))
                        player.setX(wall.getX() + wall.getWidth());
                }                        
                    
                //Check if projectile hits a wall
                projectileIterator = projectiles.iterator();
                while (projectileIterator.hasNext()) {
                    Projectile projectile = projectileIterator.next();
                    if (wall.getBoundingBox().intersects(projectile.getBoundingBox())) {
                        projectile.delete();
                        projectileIterator.remove();
                    }
                }
            }
            
            else if (entity.getBoundingBox().intersects(playerBoundingBox)) { //Pickups
                
                //Health
                if (entity instanceof HealthPickup) {
                    player.heal();
                    entityIterator.remove();
                }

                //Strength                
                else if (entity instanceof StrengthPickup) {
                    player.applyBonusDamage();
                    entityIterator.remove();
                }
            }
        }
    }
}
