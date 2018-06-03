package flippuzzle;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class FlipPuzzleController implements Initializable{
    
    @FXML
    private GridPane grid;
    @FXML
    private Button resetButton;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        displayWelcomeMessage();
        Board board = createBoard();
        initComponents(board);
        configureReset(board);
    }    
    
    // This is the first ui that the user encounters!
    public void displayWelcomeMessage() {
        Alert welcomeMessage = new Alert(AlertType.INFORMATION);
        welcomeMessage.setTitle("Welcome!");
        welcomeMessage.setHeaderText("Welcome.");
        welcomeMessage.setContentText("Enjoy your game! :D");
        
        // We'll return from this method after the user is ready to play and
        // presses the OK button.
        welcomeMessage.showAndWait();
    }
    
    // Creates and returns a board with the number of columns and rows defined in 
    // the Constants class.
    public Board createBoard() {
        return new Board(Constants.NUMROWS, Constants.NUMCOLS);
    }
    
    // Initialize the board components and configure them to respond to user actions.
    public void initComponents(Board board) {
        // Just a convenient indicator of whether the current tile is odd or even.
        int oddTile = 0;
        
        // This is supposed to be for debugging purposes only, I'll eventually
        // need to find a more proper way to create the grid lines.
        grid.setGridLinesVisible(true); 
        
        for (int row = 0; row < Constants.NUMROWS; row++)
        {
            for (int col = 0; col < Constants.NUMCOLS; col++)
            {                
                // To create a checkered pattern! Alternates colors as we go
                // through each tile in the grid. Only works if each row has an
                // odd number of tiles though.
                Color color = (oddTile == 0 ? Color.BURLYWOOD : Color.BLACK);               
                Rectangle tile = fillCurrentTile(row, col, color);
                
                // Store the current tile in our board's nifty tiles array.
                board.getTiles()[row][col] = tile;
                
                // Apparently local variables referenced from lambda expressions have to be final,
                // so I just created some temporary final variables and stored the values I needed
                // in them. Kinda hacky but it works and has O(1) space complexity, so I guess it
                // shouldn't hurt!
                final Rectangle[][] tiles = board.getTiles();
                final int _row = row;
                final int _col = col;
                
                // Configure the logic to flip tiles as intended when clicked.
                tile.setOnMouseClicked(event -> {
                    if (event.getButton() == MouseButton.PRIMARY)
                    {
                        // flip clicked tile and tiles to the top, right, bottom and left of it.
                        board.flip(tiles, _row, _col);
                        board.flip(tiles, _row - 1, _col);
                        board.flip(tiles, _row, _col + 1);
                        board.flip(tiles, _row + 1, _col);
                        board.flip(tiles, _row, _col - 1);
                        
                        board.incrementCount();
                        
                        if (won(board))
                            displayWinMessage(board);
                    }                        
                });                
                
                // Toggles the state of the next tile.
                // If the current tile is even, the next will be odd and vice versa.
                oddTile ^= 1;
            }
        }
    }
    
    // Creates a new tile, positioning it at location (row, col) of the grid.
    public Rectangle fillCurrentTile(int row, int col, Color color) {
        Rectangle tile = new Rectangle();
        
        tile.setFill(color);
        tile.setWidth(120);
        tile.setHeight(120);
        
        GridPane.setRowIndex(tile, row);
        GridPane.setColumnIndex(tile, col);
        
        // Add the freshly created tile to our grid.
        grid.getChildren().add(tile);
        
        return tile;
    }   
    
    // Configure the reset button to restore the grid to its original state.
    public void configureReset(Board board) {
        resetButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override 
            public void handle(ActionEvent e) {
                // Turn over all tiles to their original state (such that no part
                // of the underlying image is exposed).
                for (int row = 0; row < Constants.NUMROWS; row++)
                    for (int col = 0; col < Constants.NUMCOLS; col++)
                        board.getTiles()[row][col].setOpacity(100);
                
                board.resetNumFlipped();
                board.resetCount();
            }
        });
    }
    
    // Checks the board's state to determine if the player has won the game.
    public boolean won (Board board) {
        // If all tiles are flipped over, the player's won!
        return board.getNumFlipped() == Constants.NUMTILES;
    }
    
    // Congratulate the winner.
    public void displayWinMessage(Board board) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Congratulations!");
        alert.setHeaderText("Congratulations!");
        alert.setContentText("Wuff! You won in " + board.getCount() + " moves!");
        
        alert.showAndWait();
        System.exit(0);
    }
}