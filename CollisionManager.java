import java.awt.geom.Rectangle2D;
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
        for (int i = 0; i < gameEntities.size(); i++) {
            if (gameEntities.get(i).getBoundingBox().intersects(playerBoundingBox)) {
                // handle collision here

                //Health
                if (gameEntities.get(i) instanceof HealthPickup) {
                    player.heal();
                    gameEntities.remove(i);
                }

                //Strength                
                else if (gameEntities.get(i) instanceof StrengthPickup) {
                    player.applyBonusDamage();
                    gameEntities.remove(i);
                }

                //Enemy
                else if (gameEntities.get(i) instanceof Enemy && player.isAttacking()) {
                    ((Enemy) gameEntities.get(i)).gotHit(player.getDamage());
                }
            }
        }
    }
}
