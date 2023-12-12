package screen;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ReadyToGame extends JFrame {

    private static final Color BACKGROUND_COLOR = Color.decode("#F7F7F7");
    private static final Color LINE_COLOR = Color.decode("#DCDCDC");

    private JPanel menuPanel;
    private JPanel peoplePanel;
    private JPanel profilePanel;
    private JPanel roomsPanel;

    public ReadyToGame() {
        setTitle("Ready to Game");
        setSize(1000, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // 네 개의 패널
        menuPanel = addPanel(0, 0, 1000, 50, Color.YELLOW);
        peoplePanel = addPanel(0, 52, 276, 309, Color.BLACK);
        profilePanel = addPanel(0, 363, 276, 237, Color.RED);
        roomsPanel = addPanel(278, 52, 724, 546, Color.BLUE);

        // 선 그리기
        addLinePanel();

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // 패널 추가
    private JPanel addPanel(int x, int y, int width, int height, Color bgColor) {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(x, y, width, height);
        panel.setBackground(bgColor);
        add(panel);
        return panel;
    }

    // 선을 그리기 위한 패널 추가
    private void addLinePanel() {
        JPanel linePanel = new LinePanel();
        linePanel.setBounds(0, 0, 1000, 600);
        linePanel.setOpaque(false);
        add(linePanel);
    }

    // 선 그리기
    private static class LinePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(LINE_COLOR);
            g.fillRect(0, 50, 1000, 2);
            g.fillRect(10, 361, 256, 2);
            g.fillRect(276, 59, 2, 530);
        }
    }

}