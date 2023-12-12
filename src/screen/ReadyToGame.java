package screen;

import javax.swing.JFrame;
import java.awt.Color;

public class ReadyToGame extends JFrame {
    public ReadyToGame() {
        setTitle("Ready to Game");
        setSize(1000, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // 추가적인 UI 구성 요소가 필요하면 여기에 포함

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
