package component;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JButton;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

// 모서리가 둥근 버튼 - 로그인, 회원가입, 확인 버튼에서 사용
public class RoundedButton extends JButton {
    private int cornerRadius;

    public RoundedButton(String label, int cornerRadius) {
        super(label);
        this.cornerRadius = cornerRadius;
        setOpaque(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
    }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 배경색 채우기
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius));

            // 모서리 선 그리기
            g2.setColor(Color.decode("#AAAAAA")); // 모서리 색상 설정
            g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius));

            super.paintComponent(g);
        }
}

