package com.company;

public class Sudoku {

    private int sudoku[][];
    private int solvedSudoku[][];
    private int initialSudoku[][];
    private int error[][];
    private int n = 9;
    private SudokuGenerator sg;
    private boolean isFixed;

    public Sudoku(int level) {
        generateSudoku(level);
    }

    public void generateSudoku(int level){
        sg = new SudokuGenerator(level);
        sudoku = sg.getSudoku();
        solvedSudoku = sg.getSolvedSudoku();
        initialSudoku = sg.getInitialSudoku();
        error = new int[9][9];

//        sudoku =  new  int[][] {
//                {3, 0, 6, 5, 0, 8, 4, 0, 0},
//                {5, 2, 0, 0, 0, 0, 0, 0, 0},
//                {0, 8, 7, 0, 0, 0, 0, 3, 1},
//                {0, 0, 3, 0, 1, 0, 0, 8, 0},
//                {9, 0, 0, 8, 6, 3, 0, 0, 5},
//                {0, 5, 0, 0, 9, 0, 6, 0, 0},
//                {1, 3, 0, 0, 0, 0, 2, 5, 0},
//                {0, 0, 0, 0, 0, 0, 0, 7, 4},
//                {0, 0, 5, 2, 0, 6, 3, 0, 0}
//        };
    }

    public boolean getInitialValue(int x, int y){
        return initialSudoku[x][y] != 0;
    }

    public void solve() {
        sudoku = solvedSudoku;
    }

    public int getValue(int x, int y){
        return sudoku[x][y];
    }

    public int getSolvedValue(int x, int y){
        return solvedSudoku[x][y];
    }

    public int[][] setValue(int x, int y, int value) {
        sudoku[x][y] = value;
        return sudoku;
    }

    public int[][] checker(){
        for(int i =0; i < 9 ; i++){
            for(int j =0; j < 9 ; j++) {
                if(sudoku[i][j] != 0){
                    if(sudoku[i][j] != solvedSudoku[i][j]){
                        error[i][j] = sudoku[i][j];
                        sudoku[i][j] = 0;
                    }
                }
            }
        }
        return error;
    }
    public boolean checkIfDone(){
        for(int i =0; i < 9 ; i++){
            for(int j =0; j < 9 ; j++) {
                if(sudoku[i][j] != solvedSudoku[i][j]){
                    return false;
                }
            }
        }
        return true;
    }


    public boolean isFixed() {
        return isFixed;
    }

    public void setFixed(boolean fixed) {
        isFixed = fixed;
    }
}