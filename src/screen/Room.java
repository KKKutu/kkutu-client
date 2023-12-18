package screen;

import java.awt.Color;
import javax.swing.JFrame;

public class Room extends JFrame {

    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 600;

    // 생성자에서 UI 설정
    public Room() {
        setScreen();
    }

    // 화면 기본 구성
    private void setWindow() {
        setTitle("Room");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(Color.decode("#F7F7F7"));
    }

    // UI 설정
    private void setScreen() {
        setWindow(); // 화면 기본 구성
        setVisible(true); // 해당 프레임 보이게
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 화면 닫으면 프로그램 종료
    }
}
