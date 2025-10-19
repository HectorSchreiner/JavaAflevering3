import java.io.IOException;

public class Board {
    private int[][] table;

    public Board(int[][] table) {
        this.table = table;
    }

    public void fromFile(String filePath) throws IOException {
        try {
            
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public int[][] get() {
        return this.table;
    }
}