package com.company;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;

public class Grid extends JPanel {

    int width = 500;
    int height = 500;
    private int currentlySelectedCol;
    private int currentlySelectedRow;
    private Sudoku sudoku;
    private Sudoku initialSudoku;
    private int[][] error;
    private int step = 0;


    public Grid(Sudoku sudoku) {
        addMouseListener(new SudokuMouseAdapter());
        currentlySelectedCol = -1;
        currentlySelectedRow = -1;
        error = new int[9][9];
        this.sudoku = sudoku;
        this.initialSudoku = sudoku;
    }

    public void setNewGrid() {
        currentlySelectedCol = -1;
        currentlySelectedRow = -1;
        error = new int[9][9];
        step=0;
    }

        private void drawGrid(Graphics2D g2){
        g2.setBackground(Color.WHITE);
        g2.translate(0,12);
        g2.setStroke(new BasicStroke(3));
        g2.drawLine(1, 0, 1, height);
        g2.drawLine(0, 0, width, 0);
        g2.setStroke(new BasicStroke(1));

        for (int i = 1; i < 10; i++) {
            if (i % 3 == 0) {
                g2.setStroke(new BasicStroke(3));
                g2.drawLine(i * width/9, 0, i * width/9, (height));
                g2.drawLine(0, i * height/9, width, i * (height)/9);
                g2.setStroke(new BasicStroke(1));
            } else {
                g2.drawLine(i * width/9, 0, i * width/9, (height));
                g2.drawLine(0, i * (height)/9, width, i * (height)/9);
            }
        }
    }

    //  Paints the 9x9 grid.
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);

        drawGrid(g2);

        for(int i = 0;i<9;i++){
            for(int j=0;j<9;j++){
                if(error[i][j] != 0)
                    drawCenteredString(g2, String.valueOf(error[i][j]),
                            new Rectangle(i * width/9,j * height/9,width/9,height/9),
                            new Font("Comic Sans Ms", Font.PLAIN, 30), Color.RED );
                if(sudoku.getValue(i,j) != 0 && error[i][j] == 0)
                    if(initialSudoku.getInitialValue(i ,j))
                        drawCenteredString(g2, String.valueOf(sudoku.getValue(i, j)),
                            new Rectangle(i * width/9,j * height/9,width/9,height/9),
                            new Font("Comic Sans Ms", Font.BOLD, 30), Color.BLACK );
                    else
                        drawCenteredString(g2, String.valueOf(sudoku.getValue(i, j)),
                                new Rectangle(i * width/9,j * height/9,width/9,height/9),
                                new Font("Comic Sans Ms", Font.PLAIN, 30), Color.BLACK );
            }
        }
        if(currentlySelectedCol != -1 && currentlySelectedRow != -1) {
            g2.setColor(new Color(0.1f,0.4f,1.0f,0.2f));
            g2.fillRect(currentlySelectedCol * width/9,currentlySelectedRow * height/9,width/9,height/9);
        }

        g.setColor(Color.BLACK);
        g.setFont(new Font("Comic Sans Ms", Font.BOLD, 25));
        g.drawString("Steps: " + step, 0, height+30);
    }

    // returns mouse location.
    private class SudokuMouseAdapter extends MouseInputAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getButton() == MouseEvent.BUTTON1) {
                int slotWidth = width/9;
                int slotHeight = height/9;
                currentlySelectedRow = e.getY() / slotHeight;
                currentlySelectedCol = e.getX() / slotWidth;
                if(currentlySelectedRow == 9){
                    currentlySelectedRow--;
                }
                if(currentlySelectedCol == 9){
                    currentlySelectedCol--;
                }
                if(!sudoku.getInitialValue(currentlySelectedCol,currentlySelectedRow))
                    e.getComponent().repaint();
            }
        }
    }

    // Draws value in the center of the cell.
    private void drawCenteredString(Graphics g, String value, Rectangle rect, Font font, Color color) {
        FontMetrics metrics = g.getFontMetrics(font);
        int x = rect.x + (rect.width - metrics.stringWidth(value)) / 2;
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g.setColor(color);
        g.setFont(font);
        g.drawString(value, x, y);
    }

    public void message(String value){
        if(currentlySelectedCol != -1 && currentlySelectedRow != -1){
            sudoku.setValue(currentlySelectedCol, currentlySelectedRow, Integer.parseInt(value));
            error[currentlySelectedCol][currentlySelectedRow] = 0;
            step++;
            repaint();
        }
    }

    // Returns the value chosen from the buttons 1-9.
    public class NumActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            message(((JButton) e.getSource()).getText());
        }
    }

    public void checker(int[][] error){
        this.error = error;
        step++;
        repaint();
    }

    public int getStep(){
        return step;
    }

    public void getHint(){
        step++;
        int val = sudoku.getSolvedValue(currentlySelectedCol, currentlySelectedRow);
        sudoku.setValue(currentlySelectedCol, currentlySelectedRow, val);
        repaint();
    }

    public void clearCase(){
        step++;
        sudoku.setValue(currentlySelectedCol, currentlySelectedRow, 0);
        error[currentlySelectedCol][currentlySelectedRow] = 0;
        repaint();

    }
}