package gui.panel;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.Timer;
import gui.ICommon;
import gui.ITrans;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JOptionPane;
import logic.Board;
import dominpg.MainMenu; // Import MainMenu

public class ControlPanel extends JPanel implements ICommon {

    private static final long serialVersionUID = 5219120377989554161L;
    public static final boolean STT_WIN = true;
    public static final boolean STT_LOSE = false;
    private JLabel lbNumSquareClosed;
    private JLabel lbNotify;
    private JLabel lbTimer;
    private JButton btnRestart;
    private ITrans listener;
    private Timer timer;
    private int seconds;
    private ArrayList<Integer> score = new ArrayList<>();

    public ControlPanel() {
        initComp();
        addComp();
        addEvent();
    }

    @Override
    public void initComp() {
        setLayout(null);
    }

    @Override
    public void addComp() {
        Font font = new Font("VNI", Font.PLAIN, 20);

        lbNumSquareClosed = new JLabel();
        lbNumSquareClosed.setFont(font);
        lbNumSquareClosed.setText("Number square closed: " + Board.NUM_ROWS * Board.NUM_COLUMNS);
        lbNumSquareClosed.setBounds(10, 10, 250, 40);
        add(lbNumSquareClosed);

        lbNotify = new JLabel();
        lbNotify.setFont(font);
        lbNotify.setBounds(270, 10, 200, 40);
        add(lbNotify);

        lbTimer = new JLabel();
        lbTimer.setFont(font);
        lbTimer.setBounds(550, 10, 200, 40);
        add(lbTimer);

        btnRestart = new JButton();
        btnRestart.setFont(font);
        btnRestart.setText("Play again");
        btnRestart.setBounds(1000, 10, 140, 40);
        add(btnRestart);
    }

    @Override
    public void addEvent() {
        btnRestart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.restart();
                lbNumSquareClosed.setText("Number square closed: " + Board.NUM_ROWS * Board.NUM_COLUMNS);
                lbNotify.setText("");
                seconds = 0;
                startTimer();
            }
        });
        startTimer();
    }

    private void startTimer() {
        if (timer != null) {
            timer.stop();
        }
        timer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seconds++;
                lbTimer.setText("Time: " + seconds + "s");
            }
        });
        timer.start();
    }

    public void addListener(ITrans event) {
        listener = event;
    }

    public void updateStatus(int numSquareClosed) {
        lbNumSquareClosed.setText("Number square closed: " + numSquareClosed); //Number Square Closed
        if (numSquareClosed == Board.NUM_MINES) {
            lbNotify.setText("You Win");
            lbNotify.setForeground(Color.blue);
            timer.stop();
            showLeaderboard();
        } else if (numSquareClosed == 0) {
            lbNotify.setText("You Lose");
            lbNotify.setForeground(Color.red);
            timer.stop();
            showLossDialog();
        }
    }

    private void showLeaderboard() {
        score.add(seconds);
        Collections.sort(score);
        String message = "You win in " + seconds + " seconds\n";
        message += "Rankings:\n";
        for (int i = 0; i < score.size(); i++) {
            message += "Top " + (i + 1) + ": " + score.get(i) + " seconds\n";
        }
        int option = JOptionPane.showOptionDialog(this, message, "Ranking",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                new Object[]{"Play again", "Back To Menu"}, "Play again");

        if (option == 0) {
            listener.restart();
            lbNumSquareClosed.setText("Number square closed: " + Board.NUM_ROWS * Board.NUM_COLUMNS);
            lbNotify.setText("");
            seconds = 0;
            startTimer();
        } else if (option == 1) {
            openMainMenu();
        }
    }

    private void showLossDialog() {
    int option = JOptionPane.showOptionDialog(null, 
            "You lose! Select one of the options below.",
            "You lose", 
            JOptionPane.DEFAULT_OPTION, 
            JOptionPane.INFORMATION_MESSAGE, 
            null,
            new Object[]{"Play again", "Back To Menu"}, 
            "Play again");

    if (option == 0) {
        listener.restart();
        lbNumSquareClosed.setText("Number square closed: " + Board.NUM_ROWS * Board.NUM_COLUMNS);
        lbNotify.setText("");
        seconds = 0;
        startTimer();
    } else if (option == 1) {
        openMainMenu();
    }
}

    private void openMainMenu() {
        MainMenu mainMenu = new MainMenu();
        mainMenu.setVisible(true);
        // Close the current window
        getTopLevelAncestor().setVisible(false);
    }
}
