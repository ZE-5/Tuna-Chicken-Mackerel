public final class EnemyProjectile extends Projectile{
    public EnemyProjectile(int x1, int y1, int x2, int y2, int width, int height, int damage, int speed, int timeOut) {
        super(x1, y1, x2, y2, width, height, damage, speed, timeOut);
    }
    
    public EnemyProjectile(int x, int y, int width, int height, int damage, int speed, int timeOut) {
        super(x, y, x + 1, y, width, height, damage, speed, timeOut);
    }
}
