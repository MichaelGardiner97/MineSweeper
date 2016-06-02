package Lab10_MineSweeper_;
import util.StdDraw;

/**
 * Created by Michael Gardiner on 5/1/16.
 */

public class Square {

    public boolean flagged;
    public boolean revealed;
    public int numSurrounding;
    public int r;
    public int c;

    /**
     * Each square has a boolean flagged value, boolean revealed value for if it's clicked, and a number of neighboring bombs
     * @param cin - column
     * @param rin - row
     */
    public Square(int cin, int rin){
        this.flagged = false;
        this.revealed = false;
        this.numSurrounding = 0;
        this.c = cin;
        this.r = rin;
        draw();
    }

    public void toggleFlag(){this.flagged = !this.flagged;}
    public void setRevealed(){this.revealed = true;}

    public void draw(){
        if (!revealed && !flagged){
            StdDraw.setPenColor(StdDraw.GRAY);
            StdDraw.filledSquare(this.c + 0.5, this.r + 0.5, 0.45);
        } else if (revealed && !flagged){
            StdDraw.setPenColor(StdDraw.GREEN);
            StdDraw.filledSquare(this.c + 0.5, this.r + 0.5, 0.45);
            if (numSurrounding == 0){
                numSurrounding = 0;
            } else if (numSurrounding == -1){
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.filledSquare(this.c + 0.5, this.r + 0.5, 0.45);
            } else {
                String number = Integer.toString(this.numSurrounding);
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.setFont();
                StdDraw.text(this.c + 0.5, this.r + 0.5, number);
            }
        } else if (flagged){
            StdDraw.setPenColor(StdDraw.YELLOW);
            StdDraw.filledSquare(this.c + 0.5, this.r + 0.5, 0.45);
        }
    }
}