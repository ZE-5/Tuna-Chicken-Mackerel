public abstract class Pickup extends GameEntity{
    Pickup(int x, int y){
        super(x, y, 20, 20);
    }

    Pickup(int x, int y, int width, int height){
        super(x, y, width, height);
    }
}
