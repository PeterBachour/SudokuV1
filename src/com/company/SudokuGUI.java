package com.company;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;

import static java.awt.Color.*;
import static javax.swing.JOptionPane.*;

public class SudokuGUI extends JFrame {

    private Grid grid;
    private JRadioButton easy, medium, hard;
    private JButton generate, solve, check, hints, clear;
    private JPanel buttonSelectionPanel;
    private int level = 1;
    private Sudoku sudoku;

    public SudokuGUI () {
        sudoku = new Sudoku(level);
        initWindow();

    }

    //Initialize window <p>
    public void initWindow () {
        setTitle("Sudoku by Peter Bachour");
        setSize(727, 600);
        setResizable(false);
        setLocationRelativeTo(null);

        drawGrid();
        drawPanel();
        drawButtons();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    //Draw the Grid
    private void drawGrid(){
        this.grid = new Grid(sudoku);
        this.add(grid, BorderLayout.CENTER);
    }

    //Draw the Panel
    public void drawPanel(){
        this.clear = new JButton("Clear Case");
        this.clear.setForeground(BLACK);
        this.clear.setFont(new Font ("Comic Sans Ms", Font.BOLD, 15));
        this.clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                clearCase();
            }
        });


        this.hints = new JButton("Hints");
        this.hints.setForeground(BLACK);
        this.hints.setFont(new Font ("Comic Sans Ms", Font.BOLD, 15));
        this.hints.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                getHint();
            }
        });

        this.generate = new JButton("Generate");
        this.generate.setForeground(BLACK);
        this.generate.setFont(new Font ("Comic Sans Ms", Font.BOLD, 15));
        this.generate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                new DialogListener("Are you sure you want to start a new game?", "New Game?", 1);
            }
        });

        this.check = new JButton ("Check");
        this.check.setForeground(new Color(46080));
        this.check.setFont(new Font ("Comic Sans Ms", Font.BOLD, 15));
        this.check.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                checker();
            }
        });

        this.solve = new JButton("Solve");
        this.solve.setForeground(RED);
        this.solve.setFont(new Font ("Comic Sans Ms", Font.BOLD, 15));
        this.solve.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                new DialogListener("Are you sure you want to solve this game?", "Solve Game?", 2);
            }
        });

        this.easy = new JRadioButton("Easy");
        this.easy.setFont(new Font ("Comic Sans Ms", Font.PLAIN, 15));
        this.easy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                level = 1;
            }
        });


        this.medium = new JRadioButton("Intermediate");
        this.medium.setFont(new Font ("Comic Sans Ms", Font.PLAIN, 15));
        this.medium.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                level = 2;
            }
        });


        this.hard = new JRadioButton("Hard");
        this.hard.setFont(new Font ("Comic Sans Ms", Font.PLAIN, 15));
        this.hard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                level = 3;
            }
        });

        JPanel buttons = new JPanel();

        //to select only 1 RadioButton.
        ButtonGroup bg = new ButtonGroup();
        bg.add(easy);
        bg.add(medium);
        bg.add(hard);
        this.easy.setSelected(true);

        buttons.setBorder(new EmptyBorder(10, 10, 10, 10));
        buttons.setLayout(new GridLayout(10, 1, 0, 0));
        buttons.add(this.clear);
        buttons.add(this.hints);
        buttons.add(this.check);
        buttons.add(this.solve);
        buttons.add(this.generate);
        buttons.add(this.easy);
        buttons.add(this.medium);
        buttons.add(this.hard);

        add(buttons, BorderLayout.WEST);
    }

    //Draw the buttons from 1 to 9
    public void drawButtons(){
        buttonSelectionPanel = new JPanel();

        String[] values = {"1","2","3","4","5","6","7","8","9"};

        for(String value : values) {
            JButton b = new JButton(value);
            b.setPreferredSize(new Dimension(50,50));
            b.setForeground(BLACK);
            b.setFont(new Font ("Comic Sans Ms", Font.BOLD, 20));
            b.addActionListener(grid.new NumActionListener());
            buttonSelectionPanel.add(b);
        }
        buttonSelectionPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        buttonSelectionPanel.setLayout(new GridLayout(10, 1, 0, 0));

        add(this.buttonSelectionPanel, BorderLayout.EAST);

    }

    //Show a DialogBox
    private class DialogListener {
        public DialogListener(String text, String title, int id){
            JPanel panel = new JPanel();
            panel.setSize(new Dimension(250, 150));
            panel.setLayout(null);
            JLabel label = new JLabel(text);
            label.setVerticalAlignment(SwingConstants.BOTTOM);
            label.setBounds(20, 20, 300, 30);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(label);
            UIManager.put("OptionPane.minimumSize", new Dimension(400, 150));
            int res = showConfirmDialog(null, panel, title,
                    YES_NO_OPTION,
                    PLAIN_MESSAGE);
            if(res == 0) {
                if(id == 1){
                    sudoku.generateSudoku(level);
                    grid.setNewGrid();
                    repaint();
                }
                else if(id == 2){
                    sudoku.solve();
                    grid.setNewGrid();
                    repaint();
                }
            }
        }
    }

    public void checker(){
        if(sudoku.checkIfDone()){
            String message = "<html>Congratulations!<br/>You resolved this grid in "
                    + grid.getStep() + " steps.</html>";
            String title = "Congratulations!";
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(
                    frame,
                    new JLabel(message, null, JLabel.LEFT),
                    title, JOptionPane.INFORMATION_MESSAGE);

        } else
            grid.checker(sudoku.checker());

    }

    public void getHint(){
        grid.getHint();
    }

    public void clearCase() {
        grid.clearCase();
    }
}