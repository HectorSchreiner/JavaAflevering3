import java.io.IOException;

public class Board {
    private int[][] table;

    Board(int[][] table) {
        this.table = table;
    }

    Board(int width, int height) {
        int value = 0;
        this.table = new int[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                this.table[i][j] = value;
            }
        }
    }

    public void fromFile(String filePath) throws IOException {
        // todo !
    }

    public int getHeight() {
        return table.length;
    }

    public int getWidth() {
        if (table.length == 0) {
            return 0; 
        }
    return table[0].length; 
}

    public int[][] getTable() {
        return this.table;
    }

    public Field getField(int x, int y) {
        int value = this.getTable()[x][y];
        return new Field(x, y, value);
    }

    public void setField(Field field){
        try {
            this.table[field.y()][field.x()] = field.value();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public Field getNeighbor(Field origin, Direction neighbor) {
        int x = origin.x();
        int y = origin.y();

        switch (neighbor) {
            case UP:
                if (y > 0) {
                    return getField(x, y-1);
                } else {
                    return getField(x, getHeight());
                }
        
            case DOWN:
                if (y < getHeight()) {
                    return getField(x, y+1);
                } else {
                    return getField(x, 0);
                }
                
            case LEFT:
                if (x > 0) {
                    return getField(x-1, y);
                } else {
                    return getField(getWidth(), y);
                }

            case RIGHT:
                if (x < getWidth()) {
                    return getField(x+1, y);
                } else {
                    return getField(0, y);
                }
        }
        return origin;
    }
}

record Field(int x, int y, int value) { 
    public boolean isAlive() {
        if (value == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isDead() {
        if (value == 0) {
            return true;
        } else {
            return false;
        }
    }
}

enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT
}