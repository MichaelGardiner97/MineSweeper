package Lab10_MineSweeper_;
import util.StdDraw;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by Michael Gardiner on 5/1/16.
 */

public class Grid {

    private Square[][] g;
    private int W, H;
    private int numMines;
    public boolean quit;

    public Grid(int Win, int Hin, int numM){

        this.quit = false;
        this.W = Win;
        this.H = Hin;
        StdDraw.setXscale(0, this.W);
        StdDraw.setYscale(0, this.H);
        this.numMines = numM;
        this.g = new Square[this.W][this.H];

        for (int r = 0; r < this.W; r++) {
            for (int c = 0; c < this.H; c++) {
                this.g[r][c] = new Square(r,c);
            }
        }
        setMineNum();
        countSurrounding();
    }
    public void runMouseCheck(){
        boolean pressed = false;
        boolean clicked = false;
        while (!pressed && !clicked){
            if (StdDraw.isKeyPressed(KeyEvent.VK_F)){
                double x = StdDraw.mouseX();
                double y = StdDraw.mouseY();
                int X = (int)(x);
                int Y = (int)(y);
                this.g[X][Y].toggleFlag();
                this.refresh();
                clicked = true;
                try {
                    Thread.sleep(200);
                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            } else if (!StdDraw.isKeyPressed(KeyEvent.VK_F)){
                clicked = false;
            }
            if (StdDraw.mousePressed()){
                double x = StdDraw.mouseX();
                double y = StdDraw.mouseY();
                int X = (int)(x);
                int Y = (int)(y);
                reveal(this.g[X][Y]);
                if(maybeBoom(this.g[X][Y])){
                    break;
                }
                if(Win()){
                    break;
                }
                pressed = true;
                this.refresh();
            } else if (!StdDraw.mousePressed()){
                pressed = false;
                if(Win()){
                    break;
                }
            }
        }
    }

    public boolean Win(){
        if (countFlags() + countRevealed() == this.W*this.H){
            StdDraw.setPenColor(StdDraw.GRAY);
            StdDraw.filledRectangle(this.W/2, this.H/2, this.W/2+200, this.H/2+200);
            StdDraw.setPenColor(StdDraw.CYAN);
            Font newFont = new Font("Times", Font.BOLD, this.W*3);
            StdDraw.setFont(newFont);
            StdDraw.text(this.W/2, this.H/2, "YOU WIN!!!");
            this.quit = true;
            return true;
        } else {
            return false;
        }
    }
    public int countFlags(){
        int flags = 0;
        for (int c = 0; c < this.W; c++) {
            for (int r = 0; r < this.H; r++) {
                if(this.g[c][r].flagged){
                    flags++;
                }
            }
        }
        return flags;
    }
    public int countRevealed(){
        int revealed = 0;
        for (int c = 0; c < this.W; c++) {
            for (int r = 0; r < this.H; r++) {
                if(this.g[c][r].revealed){
                    revealed++;
                }
            }
        }
        return revealed;
    }
    public void countSurrounding(){
        for (int c = 0; c < this.W; c++){
            for (int r = 0; r < this.H; r++){
                int num = 0;
                if (this.g[c][r].numSurrounding == -1){
                    continue;
                }
                /**
                 * Goes through all the neighboring squares and adds 1 to the total for every bomb
                  */
                if (c - 1 >= 0 && c + 1 <= this.W-1 && r - 1 >= 0 && r + 1 <= this.H-1){
                    if (this.g[c-1][r].numSurrounding == -1){num+=1;}
                    if (this.g[c-1][r+1].numSurrounding == -1){num+=1;}
                    if (this.g[c-1][r-1].numSurrounding == -1){num+=1;}
                    if (this.g[c][r+1].numSurrounding == -1){num+=1;}
                    if (this.g[c][r-1].numSurrounding == -1){num+=1;}
                    if (this.g[c+1][r+1].numSurrounding == -1){num+=1;}
                    if (this.g[c+1][r].numSurrounding == -1){num+=1;}
                    if (this.g[c+1][r-1].numSurrounding == -1){num+=1;}
                    this.g[c][r].numSurrounding = num;
                } else if (c == 0 && r - 1 >= 0 && r + 1 <= this.H-1){
                    if (this.g[c][r-1].numSurrounding == -1){num+=1;}
                    if (this.g[c][r+1].numSurrounding == -1){num+=1;}
                    if (this.g[c+1][r].numSurrounding == -1){num+=1;}
                    if (this.g[c+1][r+1].numSurrounding == -1){num+=1;}
                    if (this.g[c+1][r-1].numSurrounding == -1){num+=1;}
                    this.g[c][r].numSurrounding = num;
                } else if (c == this.W-1 && r - 1 >= 0 && r + 1 <= this.H-1){
                    if (this.g[c][r-1].numSurrounding == -1){num+=1;}
                    if (this.g[c][r+1].numSurrounding == -1){num+=1;}
                    if (this.g[c-1][r].numSurrounding == -1){num+=1;}
                    if (this.g[c-1][r+1].numSurrounding == -1){num+=1;}
                    if (this.g[c-1][r-1].numSurrounding == -1){num+=1;}
                    this.g[c][r].numSurrounding = num;
                } else if (r == 0 && c - 1 >= 0 && c + 1 <= this.H-1){
                    if (this.g[c-1][r].numSurrounding == -1){num+=1;}
                    if (this.g[c+1][r].numSurrounding == -1){num+=1;}
                    if (this.g[c][r+1].numSurrounding == -1){num+=1;}
                    if (this.g[c-1][r+1].numSurrounding == -1){num+=1;}
                    if (this.g[c+1][r+1].numSurrounding == -1){num+=1;}
                    this.g[c][r].numSurrounding = num;
                } else if (r == this.H-1 && c - 1 >= 0 && c + 1 <= this.H-1) {
                    if (this.g[c-1][r].numSurrounding == -1){num+=1;}
                    if (this.g[c+1][r].numSurrounding == -1){num+=1;}
                    if (this.g[c][r-1].numSurrounding == -1){num+=1;}
                    if (this.g[c-1][r-1].numSurrounding == -1){num+=1;}
                    if (this.g[c+1][r-1].numSurrounding == -1){num+=1;}
                    this.g[c][r].numSurrounding = num;
                } else if (c == 0 && r == 0){
                    if (this.g[c+1][r+1].numSurrounding == -1){num+=1;}
                    if (this.g[c+1][r].numSurrounding == -1){num+=1;}
                    if (this.g[c][r+1].numSurrounding == -1){num+=1;}
                    this.g[c][r].numSurrounding = num;
                } else if (c == this.W-1 && r == 0){
                    if (this.g[c-1][r+1].numSurrounding == -1){num+=1;}
                    if (this.g[c-1][r].numSurrounding == -1){num+=1;}
                    if (this.g[c][r+1].numSurrounding == -1){num+=1;}
                    this.g[c][r].numSurrounding = num;
                } else if (c == 0 && r == this.H-1){
                    if (this.g[c+1][r].numSurrounding == -1){num+=1;}
                    if (this.g[c+1][r-1].numSurrounding == -1){num+=1;}
                    if (this.g[c][r-1].numSurrounding == -1){num+=1;}
                    this.g[c][r].numSurrounding = num;
                } else if (c == this.W-1 && r == this.H-1){
                    if (this.g[c-1][r].numSurrounding == -1){num+=1;}
                    if (this.g[c-1][r-1].numSurrounding == -1){num+=1;}
                    if (this.g[c][r-1].numSurrounding == -1){num+=1;}
                    this.g[c][r].numSurrounding = num;
                }
            }
        }
    }
    public void setMineNum(){
        int totalMines = 0;
        while (totalMines < this.numMines){
            int x = (int)(Math.random()*W);
            int y = (int)(Math.random()*H);
            if (this.g[x][y].numSurrounding != -1){
                this.g[x][y].numSurrounding = -1;
                totalMines++;
            }
        }
    }
    public void refresh(){
        for (int c = 0; c < this.W; c++) {
            for (int r = 0; r < this.H; r++) {
                this.g[c][r].draw();
            }
        }
    }
    public boolean maybeBoom(Square a){
        boolean boom = false;
        if (a.numSurrounding == -1){
            for (int c = 0; c < this.W; c++) {
                for (int r = 0; r < this.H; r++) {
                    reveal(this.g[c][r]);
                }
            }
            gameOver();
            boom = true;
        }
        return boom;
    }
    public void gameOver(){
        StdDraw.setPenColor(StdDraw.GRAY);
        StdDraw.filledRectangle(this.W/2, this.H/2, this.W/2+200, this.H/2+200);
        StdDraw.setPenColor(StdDraw.BLACK);
        Font newFont = new Font("Times", Font.BOLD, this.W*3);
        StdDraw.setFont(newFont);
        StdDraw.text(this.W/2, this.H/2, "GAME OVER");
        this.quit = true;
    }
    public void reveal(Square x){
        if (x.numSurrounding == 0 && !x.revealed) {
            x.setRevealed();
            int c = x.c;
            int r = x.r;
            if (c - 1 >= 0 && c + 1 <= this.W - 1 && r - 1 >= 0 && r + 1 <= this.H - 1) {
                reveal(this.g[c + 1][r + 1]);
                reveal(this.g[c + 1][r]);
                reveal(this.g[c + 1][r - 1]);
                reveal(this.g[c][r + 1]);
                reveal(this.g[c][r - 1]);
                reveal(this.g[c - 1][r + 1]);
                reveal(this.g[c - 1][r]);
                reveal(this.g[c - 1][r - 1]);
            } else if (c == 0 && r - 1 >= 0 && r + 1 <= this.H - 1) {
                reveal(this.g[c + 1][r + 1]);
                reveal(this.g[c + 1][r]);
                reveal(this.g[c + 1][r - 1]);
                reveal(this.g[c][r + 1]);
                reveal(this.g[c][r - 1]);
            } else if (c == this.W - 1 && r - 1 >= 0 && r + 1 <= this.H - 1) {
                reveal(this.g[c - 1][r + 1]);
                reveal(this.g[c - 1][r]);
                reveal(this.g[c - 1][r - 1]);
                reveal(this.g[c][r + 1]);
                reveal(this.g[c][r - 1]);
            } else if (r == 0 && c - 1 >= 0 && c + 1 <= this.H - 1) {
                reveal(this.g[c + 1][r]);
                reveal(this.g[c - 1][r]);
                reveal(this.g[c + 1][r + 1]);
                reveal(this.g[c][r + 1]);
                reveal(this.g[c - 1][r + 1]);
            } else if (r == this.H - 1 && c - 1 >= 0 && c + 1 <= this.H - 1) {
                reveal(this.g[c + 1][r]);
                reveal(this.g[c - 1][r]);
                reveal(this.g[c + 1][r - 1]);
                reveal(this.g[c][r - 1]);
                reveal(this.g[c - 1][r - 1]);
            } else if (c == 0 && r == 0) {
                reveal(this.g[c][r + 1]);
                reveal(this.g[c + 1][r]);
                reveal(this.g[c + 1][r + 1]);
            } else if (c == this.W - 1 && r == 0) {
                reveal(this.g[c][r + 1]);
                reveal(this.g[c - 1][r + 1]);
                reveal(this.g[c - 1][r]);
            } else if (c == 0 && r == this.H - 1) {
                reveal(this.g[c + 1][r]);
                reveal(this.g[c + 1][r - 1]);
                reveal(this.g[c][r - 1]);
            } else if (c == this.W - 1 && r == this.H - 1) {
                reveal(this.g[c][r - 1]);
                reveal(this.g[c - 1][r - 1]);
                reveal(this.g[c - 1][r]);
            }
        }else if (x.numSurrounding > 0 || x.revealed) {
            x.setRevealed();
            return;
        }
    }
}
