public class Pipe {
    public int x, y, width, height;
    public boolean passes = false;
    public boolean isTop;

    public Pipe(int x, int y, int width, int height, boolean isTop){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isTop = isTop;
    }
}
