package com.company;

public class Sudoku {

    private int sudoku[][];
    private int solvedSudoku[][];
    private int initialSudoku[][];
    private int error[][];
    private int n = 9;
    private int counter = 0;
    private SudokuGenerator sg;

    public Sudoku(int level) {
        generateSudoku(level);
    }

    public void generateSudoku(int level){
        sg = new SudokuGenerator(level);
        counter = sg.getCount();
        sudoku = sg.getSudoku();
        solvedSudoku = sg.getSolvedSudoku();
        initialSudoku = sg.getInitialSudoku();
        error = new int[9][9];
    }

    public boolean getInitialValue(int x, int y){
        return initialSudoku[x][y] != 0;
    }

    public void solve() {
        sudoku = solvedSudoku;
    }

    public int getCount(){
        return counter;
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
}