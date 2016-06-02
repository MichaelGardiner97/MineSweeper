package Lab10_MineSweeper_;
import util.StdDraw;
import java.util.Scanner;

/**
 * Created by Michael Gardiner on 5/1/16.
 */

public class MineSweeper {

    private Grid board;

    /**
     * Creates board game with Width, Height, and Bomb numbers
     * @param Win - width
     * @param Hin - height
     * @param Mine - mine number
     */
    public MineSweeper(int Win, int Hin, int Mine){
        this.board = new Grid(Win, Hin, Mine);
    }

    /**
     * Lets the user pick between a pre-made board and a custom board
     * @param min - Minimum number of mines, and minimum height and width
     * @param max - Maximum number of mines, and maximum height and width
     * @param scan - User input
     * @return - The option chosen by the user
     */
    public static int getMenuOption(int min, int max, Scanner scan){
        if (min >= max){
            throw new IllegalArgumentException("min cannot be greater than max");
        }
        int output  = -1;
        while (true){
            System.out.printf("Enter a value between %d and %d: ", min, max);
            try {
                output = Integer.parseInt(scan.next());
            } catch (NumberFormatException nfe){
                System.out.println("Not a valid integer.");
                continue;
            }
            if (output >= min && output <= max)
                return output;
            System.out.println("Invalid Menu Option");
        }
    }

    /**
     * Run the program
     */
    public void run(){
        while (!this.board.quit){
            this.board.runMouseCheck();
        }
    }

    public static void main(String[] args) {
        int W = 0;
        int H = 0;
        int mines = 0;
        Scanner scan = new Scanner(System.in);
        System.out.println("Welcome!\nPlease choose a pre-made board or make your own: (1 or 2)");
        int choose = getMenuOption(1,2,scan);
        if (choose == 1){
            System.out.println("Difficulty level: 1. Beginner 2. Intermediate 3. Expert");
            int level = getMenuOption(1,3,scan);
            if (level == 1){
                W = 9;
                H = 9;
                mines = 10;
                StdDraw.setCanvasSize(300, 300);
            } else if (level == 2){
                W = 16;
                H = 16;
                mines = 40;
                StdDraw.setCanvasSize(425, 425);
            } else if (level == 3){
                W = 30;
                H = 16;
                mines = 99;
                StdDraw.setCanvasSize(700, 425);
            }
        } else if (choose == 2){
            System.out.println("What would you like the width, height, and number of mines to be?");
            System.out.println("Width:");
            W = getMenuOption(9, 30, scan);
            System.out.println("Height:");
            H = getMenuOption(9, 30, scan);
            System.out.println("Mines:");
            mines = getMenuOption(10, 300, scan);
            int x = (20*W)+100 ;
            int y = (20*H)+100;
            StdDraw.setCanvasSize(x,y);
        }
        MineSweeper game = new MineSweeper(W, H, mines);
        game.run();
    }
}
