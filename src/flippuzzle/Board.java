package flippuzzle;

import javafx.scene.control.Alert;
import javafx.scene.shape.Rectangle;

// Class for my gameboard.
public class Board {
    // An array of tiles for easy access via row and column indices.
    private Rectangle[][] tiles;
    private int numFlipped;
    private int count;
    
    public Board(int rows, int cols) {
        this.tiles = new Rectangle[rows][cols];
        this.numFlipped = 0;
        this.count = 0;
    }
    
    public Rectangle[][] getTiles() {
        return this.tiles;
    }
    
    public void incrementNumFlipped() {
        this.numFlipped++;
    }
    
    public void decrementNumFlipped() {
        this.numFlipped--;
    }
    
    public int getNumFlipped() {
        return this.numFlipped;
    }
    
    public void resetNumFlipped(){
        this.numFlipped = 0;
    }
    
    public void incrementCount() {
        this.count++;
    }
    
    public void resetCount() {
        this.count = 0;
    }
    
    public int getCount() {
        return this.count;
    }
    
    // Flips tile[row][col] over; displays a win message if the player wins the
    // game after this move.
    public void flip(Rectangle[][] tiles, int row, int col) {
        // Flip over the tile being clicked.
        try
        {
            if (tiles[row][col].getOpacity() == 0) {
                tiles[row][col].setOpacity(100);
                decrementNumFlipped();
            }                
            
            else {
                tiles[row][col].setOpacity(0);
                incrementNumFlipped();
            }
        }    
        catch (ArrayIndexOutOfBoundsException e)
        {
            return;
        }
    }
}
