package io.sunshower.arcus;


import org.junit.Test;

import java.util.Objects;
import java.util.Scanner;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.fail;

public class TicTacToeTest {


    @Test
    public void ensureTicTacToeBoardPrintsCorrectlyWhenNoTokensArePlaced() {
        final TicTacToeBoard board = new TicTacToeBoard();
        board.addToken(0, 1, 1);
        final ExhaustiveSearch search = new ExhaustiveSearch(board);
        try {
            search.search();
        } catch(IllegalStateException ex) {
            fail("wow.  Totes didn't find an empty spot");
        }
        
        assertThat(search.x, is(0));
        assertThat(search.y, is(0));
    }
    
    
    @Test
    public void ensureGameLoopWorks() {
        
        final TicTacToeBoard board = new TicTacToeBoard();
        final Scanner scaner = new Scanner(System.in);
        final ExhaustiveSearch adversary = new ExhaustiveSearch(board);
        
        for(;;) {
            play(board, adversary, scaner);
        }
    }

    private boolean play(TicTacToeBoard board, ExhaustiveSearch adversary, Scanner scanner) {
        System.out.println("Plz play with me");
        
        try {
            int  x     = scanner.nextInt();
            int  y     = scanner.nextInt();
            char token = extract(scanner.next());
            
            return true;
        } catch(GameException ex) {
            System.out.format("Your machine overloard reports that: %s.  Wanna try again?\n", ex.getMessage());
            return false;
        }
    }

    private char extract(String next) {
        switch(next.trim()) {
            case "X" : {
                return 1;
            }
            case "Y" : 
                return 2;
            default: 
                throw new GameException(String.format("you're feeding me nonsense.  What means '%s'", next));
        }
    }
    
    static class GameException extends RuntimeException {

        public GameException(String format) {
            super(format);
        }
    }


    static class ExhaustiveSearch {
        
        int x, y;
        final TicTacToeBoard board;
        ExhaustiveSearch(final TicTacToeBoard board) {
            Objects.requireNonNull(board);
            this.board = board;
        }
        
        void search() {
            for(int i = 0; i < board.tokens.length; i++) {
                for(int j = 0; j < board.tokens.length; j++) {
                    if(board.tokens[i][j] == 0) {
                        x = i;
                        y = j;
                        return;
                    }
                    
                }
            }
            throw new IllegalStateException("No empty places, homeskillet");
        }
    }

    static class TicTacToeBoard {

        int[][] tokens = new int[3][3];
        
        
        boolean isFull() {
            for(int i = 0; i < tokens.length; i++) {
                for(int j = 0; j < tokens.length; j++) {
                    if(tokens[i][j] == 0) {
                        return false;
                    }
                }
            }
            return true;
        }

        boolean addToken(int x, int y, int token) {
//            if(checkToken(x, y))  {
//                return false;
//            }
            tokens[x][y] = token;
            return true;
        }
        
        
        private boolean checkToken(int x, int y) {
            return x < tokens.length && y < tokens[x].length && tokens[x][y] == 0;
        }

        public String toString() {
            StringBuilder b = new StringBuilder();
            for (int i = 0; i < tokens.length; i++) {
                int[] row = tokens[i];
                for (int j = 0; j < row.length; j++) {
                    b.append(getPlace(i, j));
                    if(j < 2)  {
                        b.append("|");
                    }
                }
                b.append("\n");
            }
            return b.toString();
        }

        char getPlace(int x, int y) {
            int value = tokens[x][y];
            switch(value) {
                case 0:
                    return '-';
                case 1:
                    return 'X';
                case 2: 
                    return 'O';
            }
            throw new IllegalStateException("Jacked up token, homeskillet");
        }


    }

}