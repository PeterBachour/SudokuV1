package com.company;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;

public class Grid extends JPanel {

    int width = 500; //taille de l'ecran
    int height = 500; //taille de l'ecran
    private int counter = 0; //compteur pour savoir si la grille est rempli
    private int currentlySelectedCol;
    private int currentlySelectedRow;
    private Sudoku sudoku; //sudoku rempli au fur et a mesure
    private Sudoku initialSudoku; //Sudoku initial
    private int[][] error; //tableau des erreurs
    private int step = 0; //nombre de step fais par le joueur


    //initialisation de la grille
    public Grid(Sudoku sudoku, int counter) {
        addMouseListener(new SudokuMouseAdapter());
        currentlySelectedCol = -1;
        currentlySelectedRow = -1;
        error = new int[9][9];
        this.counter = counter;
        this.sudoku = sudoku;
        this.initialSudoku = sudoku;
    }

    //creation d'une nouvelle grille
    public void setNewGrid(int counter) {
        currentlySelectedCol = -1;
        currentlySelectedRow = -1;
        error = new int[9][9];
        this.counter = counter;
        step=0;
    }

    //affichage des lignes de la grille
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

    //  affichage de la grille avec les valeurs + le nombre de step
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
        if(step != 0)
            g.drawString("Steps: " + step, 0, height+30);
        else
            g.drawString("Steps: - ", 0, height+30);

    }

    // return l'endroit ou le joueur a clicker
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

    // affiche la valeur au milieu de la cellule de la grille
    private void drawCenteredString(Graphics g, String value, Rectangle rect, Font font, Color color) {
        FontMetrics metrics = g.getFontMetrics(font);
        int x = rect.x + (rect.width - metrics.stringWidth(value)) / 2;
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g.setColor(color);
        g.setFont(font);
        g.drawString(value, x, y);
    }

    // ajoute la nouvelle valeur a la grille
    public void message(int value){
        if(currentlySelectedCol != -1 && currentlySelectedRow != -1){
            if(error[currentlySelectedCol][currentlySelectedRow] == 0 )
                counter++;
            sudoku.setValue(currentlySelectedCol, currentlySelectedRow, value);
            error[currentlySelectedCol][currentlySelectedRow] = 0;
            step++;
            repaint();
            //si le nombre total de cellule rempli = 81, on check si le joueur a gagner
            if(counter == 81)
                checkCount();
        }
    }

    // return la valeur choisi par le joueur
    public class NumActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            message(Integer.parseInt(((JButton) e.getSource()).getText()));
        }
    }

    // montre les valeurs fausse de la grille
    public void checker(int[][] error){
        this.error = error;
        if(counter != 81)
            step++;
        repaint();
    }

    // retourne un hint au joueur
    public void getHint(){
        if(!sudoku.getInitialValue(currentlySelectedCol,currentlySelectedRow)  &&
                sudoku.getValue(currentlySelectedCol, currentlySelectedRow) == 0)
        {
            step++;
            if(error[currentlySelectedCol][currentlySelectedRow] == 0 )
                counter++;
            int val = sudoku.getSolvedValue(currentlySelectedCol, currentlySelectedRow);
            sudoku.setValue(currentlySelectedCol, currentlySelectedRow, val);
            error[currentlySelectedCol][currentlySelectedRow] = 0;
            repaint();
            if(counter == 81)
                checkCount();
        }
    }

    // vide la case
    public void clearCase(){
        if(!sudoku.getInitialValue(currentlySelectedCol,currentlySelectedRow)  && (
                sudoku.getValue(currentlySelectedCol, currentlySelectedRow) != 0)||
                error[currentlySelectedCol][currentlySelectedRow] != 0)  {
            step++;
            counter--;
            sudoku.setValue(currentlySelectedCol, currentlySelectedRow, 0);
            error[currentlySelectedCol][currentlySelectedRow] = 0;
            repaint();
        }
    }

    // check si le joueur a gagner
    public void checkCount(){
            if (sudoku.checkIfDone()) {
                String message = "<html>Congratulations!<br/>You resolved this grid in "
                        + step + " steps.</html>";
                String title = "Congratulations!";
                JFrame frame = new JFrame();
                JOptionPane.showMessageDialog(
                        frame,
                        new JLabel(message, null, JLabel.LEFT),
                        title, JOptionPane.INFORMATION_MESSAGE);
                setNewGrid(0);
            }
            else{
                checker(sudoku.checker());
            }
    }

}