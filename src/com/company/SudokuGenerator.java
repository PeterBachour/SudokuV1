package com.company;

public class SudokuGenerator {
    int[][] sudoku;
    int[][] solvedSudoku;
    int[][] initialSudoku;
    int n = 9;
    int K; //le numero de cellule manquante - niveau

    // Constructeur
    SudokuGenerator(int level)
    {
        if(level == 1){
            K=34;
        }else if(level == 2){
            K=44;
        }else if(level == 3){
            K=54;
        }
        sudoku = new int[n][n];
        solvedSudoku = new int[n][n];
        initialSudoku = new int[n][n];
        fillValues();
    }

    // return le nombre de cellule rempli
    public int getCount(){
        return 81 - K;
    }

    // return le sudoku a resoudre
    public int[][] getSudoku(){
        return sudoku;
    }
    //return le sudoku resolu
    public int[][] getSolvedSudoku(){
        return solvedSudoku;
    }
    // return le sudoku initale
    public int[][] getInitialSudoku(){ return initialSudoku;}

    // Sudoku Generator
    public void fillValues()
    {
        // rempli les diagonal 3x3
        fillDiagonal();

        // rempli toute la grille
        fillRemaining(0, 3);

        // remove des cellule aleatoire
        removeKDigits();
    }

    void fillDiagonal()
    {
        for (int i = 0; i<n; i=i+3)
            fillBox(i, i);
    }

    void fillBox(int row,int col)
    {
        int num;
        for (int i=0; i<3; i++)
        {
            for (int j=0; j<3; j++)
            {
                do
                {
                    num = randomGenerator(n);
                }
                while (!unUsedInBox(row, col, num));

                sudoku[row+i][col+j] = num;
                solvedSudoku[row+i][col+j] = num;
                initialSudoku[row+i][col+j] = num;
            }
        }
    }

    //fonction recursive qui rempli la grille
    boolean fillRemaining(int i, int j)
    {
        if (j>=n && i<n-1)
        {
            i = i + 1;
            j = 0;
        }
        if (i>=n && j>=n)
            return true;

        if (i < 3)
        {
            if (j < 3)
                j = 3;
        }
        else if (i < n-3)
        {
            if (j==(int)(i/3)*3)
                j =  j + 3;
        }
        else
        {
            if (j == n-3)
            {
                i = i + 1;
                j = 0;
                if (i>=n)
                    return true;
            }
        }

        for (int num = 1; num<=n; num++)
        {
            if (CheckIfSafe(i, j, num))
            {
                sudoku[i][j] = num;
                solvedSudoku[i][j] = num;
                initialSudoku[i][j] = num;
                if (fillRemaining(i, j+1))
                    return true;

                sudoku[i][j] = 0;
                solvedSudoku[i][j] = 0;
                initialSudoku[i][j] = 0;
            }
        }
        return false;
    }


    public void removeKDigits()
    {
        int count = K;
        while (count != 0)
        {
            int cellId = randomGenerator(n*n);
            int i = (cellId/n);
            if(i == 9){
                i--;
            }
            int j = cellId%9;

            if (sudoku[i][j] != 0)
            {
                count--;
                sudoku[i][j] = 0;
                initialSudoku[i][j] = 0;
            }
        }
    }

    // generateur de numero aleatoire
    int randomGenerator(int num)
    {
        return (int) Math.floor((Math.random()*num+1));
    }

    // check si c;est possible de mettre une valeur dans cette cellule
    boolean CheckIfSafe(int i,int j,int num)
    {
        return (unUsedInRow(i, num) &&
                unUsedInCol(j, num) &&
                unUsedInBox(i-i%3, j-j%3, num));
    }

    // check si la valeur est dans la ligne
    boolean unUsedInRow(int i,int num)
    {
        for (int j = 0; j<n; j++)
            if (sudoku[i][j] == num)
                return false;
        return true;
    }

    // check si la valeur est dans la colonne
    boolean unUsedInCol(int j,int num)
    {
        for (int i = 0; i<n; i++)
            if (sudoku[i][j] == num)
                return false;
        return true;
    }

    // check si la valeur est dans la matrice 3x3
    boolean unUsedInBox(int rowStart, int colStart, int num)
    {
        for (int i = 0; i<3; i++)
            for (int j = 0; j<3; j++)
                if (sudoku[rowStart+i][colStart+j]==num)
                    return false;

        return true;
    }

}
