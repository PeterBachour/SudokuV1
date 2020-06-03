package com.company;

public class SudokuGenerator {
    int[][] sudoku;
    int[][] solvedSudoku;
    int[][] initialSudoku;
    int n = 9; // number of columns/rows.
    int K; // No. Of missing digits

    // Constructor
    SudokuGenerator(int level)
    {
        if(level == 1){
            K=1;
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

    public int getCount(){
        return 81 - K;
    }

    public int[][] getSudoku(){
        return sudoku;
    }
    public int[][] getSolvedSudoku(){
        return solvedSudoku;
    }
    public int[][] getInitialSudoku(){ return initialSudoku;}

    // Sudoku Generator
    public void fillValues()
    {
        // Fill the diagonal of 3 x 3 matrices
        fillDiagonal();

        // Fill remaining blocks
        fillRemaining(0, 3);

        // Remove Randomly K digits to make game
        removeKDigits();
    }

    // Fill the diagonal SRN number of SRN x SRN matrices
    void fillDiagonal()
    {
        for (int i = 0; i<n; i=i+3)
            // for diagonal box, start coordinates->i==j
            fillBox(i, i);
    }

    // Fill a 3 x 3 matrix.
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

    // A recursive function to fill remaining
    // matrix
    boolean fillRemaining(int i, int j)
    {
        //  System.out.println(i+" "+j);
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

    // Remove the K no. of digits to
    // complete game
    public void removeKDigits()
    {
        int count = K;
        while (count != 0)
        {
            int cellId = randomGenerator(n*n);

            // System.out.println(cellId);
            // extract coordinates i  and j
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

    // Random generator
    int randomGenerator(int num)
    {
        return (int) Math.floor((Math.random()*num+1));
    }

    // Check if safe to put in cell
    boolean CheckIfSafe(int i,int j,int num)
    {
        return (unUsedInRow(i, num) &&
                unUsedInCol(j, num) &&
                unUsedInBox(i-i%3, j-j%3, num));
    }

    // check in the row for existence
    boolean unUsedInRow(int i,int num)
    {
        for (int j = 0; j<n; j++)
            if (sudoku[i][j] == num)
                return false;
        return true;
    }

    // check in the row for existence
    boolean unUsedInCol(int j,int num)
    {
        for (int i = 0; i<n; i++)
            if (sudoku[i][j] == num)
                return false;
        return true;
    }

    // Returns false if given 3 x 3 block contains num.
    boolean unUsedInBox(int rowStart, int colStart, int num)
    {
        for (int i = 0; i<3; i++)
            for (int j = 0; j<3; j++)
                if (sudoku[rowStart+i][colStart+j]==num)
                    return false;

        return true;
    }

}
