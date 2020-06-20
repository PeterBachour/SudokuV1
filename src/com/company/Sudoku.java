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

    // initialisation de la grille avec le niveau choisi
    public void generateSudoku(int level){
        sg = new SudokuGenerator(level);
        counter = sg.getCount();
        sudoku = sg.getSudoku();
        solvedSudoku = sg.getSolvedSudoku();
        initialSudoku = sg.getInitialSudoku();
        error = new int[9][9];
    }

    //retourne la valeur initiale du sudoku
    public boolean getInitialValue(int x, int y){
        return initialSudoku[x][y] != 0;
    }

    // resouds la grille
    public void solve() {
        sudoku = solvedSudoku;
    }

    //retourne le nombre de valeur rempli dans la grille
    public int getCount(){
        return counter;
    }

    // retourne la valeur d'une cellule choisi
    public int getValue(int x, int y){
        return sudoku[x][y];
    }

    // retourne la valeur resolu d'une cellule choisi
    public int getSolvedValue(int x, int y){
        return solvedSudoku[x][y];
    }

    // set une valeur a une cellule choisi
    public int[][] setValue(int x, int y, int value) {
        sudoku[x][y] = value;
        return sudoku;
    }

    // return les erreurs de la grille
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

    //check si la grille est identique a la grille resolu
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