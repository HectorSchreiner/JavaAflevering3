public class GameOfLifeMain {
    static int width = 100;
    static int heigth = 100;
    public static void main(String[] args) {
        createWindow();
    }

    static private void createWindow() {
        StdDraw.setCanvasSize(width, heigth);
    }
}