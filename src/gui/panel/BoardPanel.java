    package gui.panel;

    import java.awt.Color;
    import java.awt.Font;
    import java.awt.GridLayout;
    import java.awt.event.MouseAdapter;
    import java.awt.event.MouseEvent;
    import javax.swing.JPanel;
    import javax.swing.BorderFactory;
    import javax.swing.JLabel;
    import javax.swing.border.Border;
    import gui.ICommon;
    import gui.ITrans;
    import javax.swing.ImageIcon;
    import logic.Board;
    import logic.Square;

    public class BoardPanel extends JPanel implements ICommon {

        private static final long serialVersionUID = -6403941308246651773L;
        private Label[][] lbSquare;
        private ITrans listener;
        private int numSquareClosed;
        private ImageIcon flagIcon;
        private ImageIcon bombIcon;
        private ImageIcon[] numberIcons;

        public BoardPanel() {
            initComp();
            addComp();
            addEvent();
        }

        @Override
        public void initComp() {
            setLayout(new GridLayout(Board.NUM_ROWS, Board.NUM_COLUMNS));
            flagIcon = loadImageIcon("/img/flag.jpg");
            bombIcon = loadImageIcon("/img/bomb.jpg");
            numberIcons = new ImageIcon[8];
            for (int i = 1; i <= 5; i++) {
                numberIcons[i - 1] = loadImageIcon("/img/num" + i + ".jpg");
            }
        }

        private ImageIcon loadImageIcon(String path) {
            java.net.URL imgURL = getClass().getResource(path);
            if (imgURL != null) {
                return new ImageIcon(imgURL);
            } else {
                System.err.println("Couldn't find file: " + path);
                return null;
            }
        }

        @Override
        public void addComp() {
            Border border = BorderFactory.createLineBorder(Color.gray, 1);
            lbSquare = new Label[Board.NUM_ROWS][Board.NUM_COLUMNS];
            for (int i = 0; i < lbSquare.length; i++) {
                for (int j = 0; j < lbSquare[0].length; j++) {
                    lbSquare[i][j] = new Label();
                    lbSquare[i][j].setOpaque(true);
                    lbSquare[i][j].setBackground(new Color(242, 242, 242));
                    lbSquare[i][j].setBorder(border);
                    lbSquare[i][j].setHorizontalAlignment(JLabel.CENTER);
                    lbSquare[i][j].setVerticalAlignment(JLabel.CENTER);
                    add(lbSquare[i][j]);
                }
            }
        }

        @Override
        public void addEvent() {
            for (int i = 0; i < lbSquare.length; i++) {
                for (int j = 0; j < lbSquare[0].length; j++) {
                    lbSquare[i][j].x = i;
                    lbSquare[i][j].y = j;
                    lbSquare[i][j].addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseReleased(MouseEvent e) {
                            Label label = (Label) e.getComponent();
                            if (e.getButton() == MouseEvent.BUTTON1) {
                                listener.play(label.x, label.y);
                            } else if (e.getButton() == MouseEvent.BUTTON3) {
                                listener.target(label.x, label.y);
                            }
                        }
                    });
                }
            }
        }

        public void addListener(ITrans event) {
            listener = event;
        }

        public void updateBoard() {
            numSquareClosed = 0;
            Square[][] listSquare = listener.getListSquare();
            for (int i = 0; i < listSquare.length; i++) {
                for (int j = 0; j < listSquare[0].length; j++) {
                    if (!listSquare[i][j].isOpen()) {
                        lbSquare[i][j].setBackground(new Color(242, 242, 242));
                        lbSquare[i][j].setForeground(Color.black);
                        numSquareClosed++;
                        if (!listSquare[i][j].isTarget()) {
                            lbSquare[i][j].setText("");
                            lbSquare[i][j].setIcon(null); // clear icon
                        } else {
                            lbSquare[i][j].setText(""); // clear text
                            lbSquare[i][j].setIcon(flagIcon); // set flag image
                        }
                    } else {
                        if (listSquare[i][j].isHasMine()) {
                            lbSquare[i][j].setText(""); // clear text
                            lbSquare[i][j].setIcon(bombIcon); // set bomb image
                        } else {
                            lbSquare[i][j].setIcon(null); // clear icon
                            int numMineAround = listSquare[i][j].getNumMineAround();
                            if (numMineAround == 0) {
                                lbSquare[i][j].setText("");
                            } else {
                                lbSquare[i][j].setText(""); // clear text
                                lbSquare[i][j].setIcon(numberIcons[numMineAround - 1]); // set number image
                            }
                        }
                        lbSquare[i][j].setBackground(Color.white);
                    }
                }
            }
        }

        private class Label extends JLabel {

            private static final long serialVersionUID = -2732246908959275513L;
            int x, y;
        }


        public void mouseReleased(MouseEvent e) {
            Label label = (Label) e.getComponent();
            if (e.getButton() == MouseEvent.BUTTON1) {
                Square[][] listSquare = listener.getListSquare();
                if (!listSquare[label.x][label.y].isTarget()) {
                    listener.play(label.x, label.y);
                }
            } else if (e.getButton() == MouseEvent.BUTTON3) {
                listener.target(label.x, label.y);
            }
        }

        public int getNumSquareClosed() {
            return numSquareClosed;
        }
    }
