package screen;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Objects;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ReadyToGame extends JFrame {

    private static final Color BACKGROUND_COLOR = Color.decode("#F7F7F7");
    private static final Color LINE_COLOR = Color.decode("#DCDCDC");

    private JPanel menuPanel;
    private JPanel peoplePanel;
    private JPanel profilePanel;
    private JPanel roomsPanel;

    public ReadyToGame() {
        setScreen();
    }

    public void setScreen() {
        setTitle("Ready to Game");
        setSize(1000, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // 네 개의 패널
        menuPanel = addPanel(0, 0, 1000, 50, Color.decode("#F7F7F7"));
        peoplePanel = addPanel(0, 52, 276, 309, Color.BLACK);
        profilePanel = addPanel(0, 363, 276, 237, Color.RED);
        roomsPanel = addPanel(278, 52, 724, 546, Color.BLUE);

        // 메뉴 부착
        // 이미지 라벨 추가
        JLabel informationLabel = createImageLabel("../image/readytogame/information.png", 0, 0, 50, 50);
        menuPanel.add(informationLabel);

        JLabel settingLabel = createImageLabel("../image/readytogame/setting.png", 50, 0, 50, 50);
        menuPanel.add(settingLabel);

        JLabel changeProfileLabel = createImageLabel("../image/readytogame/change_profile.png", 100, 0, 163, 50);
        menuPanel.add(changeProfileLabel);

        JLabel makeRoomLabel = createImageLabel("../image/readytogame/make_room.png", 263, 0, 163, 50);
        menuPanel.add(makeRoomLabel);

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

    // 이미지 아이콘 로딩
    private ImageIcon loadIcon(String path) {
        return new ImageIcon(
                Objects.requireNonNull(getClass().getResource(path))
        );
    }

    // 이미지를 라벨로
    private JLabel createImageLabel(String imagePath, int x, int y, int width, int height) {
        JLabel imgLabel = new JLabel();
        ImageIcon icon = loadIcon(imagePath);
        imgLabel.setIcon(icon);
        imgLabel.setBounds(x, y, width, height);
        imgLabel.setHorizontalAlignment(JLabel.CENTER);
        return imgLabel;
    }

}