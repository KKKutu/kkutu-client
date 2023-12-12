package screen;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class Loading extends JFrame {

    private JLabel countdownLabel;

    public Loading() {
        setScreen();
        startCountdown(countdownLabel);
    }

    // 로딩 화면을 위한 카운트다운 시작
    private void startCountdown(JLabel countdownLabel) {

        Timer timer = new Timer(1000, new ActionListener() {
            int timeLeft = 4;

            @Override
            public void actionPerformed(ActionEvent e) {
                timeLeft--;
                if (timeLeft >= 1) {
                    countdownLabel.setText("로딩중... " + timeLeft);
                    countdownLabel.repaint(); // 레이블을 다시 그리기
                } else {
                    ((Timer) e.getSource()).stop();
                    openReadyToGameScreen();
                }
            }
        });
        timer.setRepeats(true);
        timer.start();
    }

    // ReadyToGame 화면 열기
    private void openReadyToGameScreen() {
        new ReadyToGame();
        dispose(); // Loading 화면 닫기
    }

    // 배경
    public JLabel createBackground() {
        return createImageLabel("../image/loading/loading.png", 0, 0, 1000, 600); // 적절한 너비와 높이 지정
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

    // 카운트 다운 라벨
    public JLabel createCountdownLabel() {
        countdownLabel = new JLabel("로딩중... 3", SwingConstants.CENTER);
        countdownLabel.setFont(new Font("Dialog", Font.BOLD, 20));

        // 배경색을 노란색으로 설정
        countdownLabel.setBackground(Color.WHITE);
        countdownLabel.setOpaque(true); // 배경색이 보이도록 설정

        // 위치와 크기를 동시에 설정
        countdownLabel.setBounds(449, 345, 113, 30); // 위치: 444, 360, 너비: 100, 높이: 50
        return countdownLabel;
    }

    public void setScreen() {
        setTitle("Loading..");
        setSize(1000, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);
        add(createBackground()); // 배경
        add(createCountdownLabel());

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}