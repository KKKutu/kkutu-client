package component;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import javax.swing.JPanel;

public class RoundedMenu {

    // 둥근 모서리 패널 생성 메서드
    public static JPanel createRoundedPanel(int x, int y, int width, int height, Color color, int cornerRadius) {
        JPanel roundedPanel = new RoundedPanel(cornerRadius, color);
        roundedPanel.setBounds(x, y, width, height);
        roundedPanel.setOpaque(false);
        return roundedPanel;
    }

    // 둥근 모서리를 가진 JPanel 커스텀 클래스
    private static class RoundedPanel extends JPanel {
        private final Color backgroundColor;
        private final int cornerRadius;

        public RoundedPanel(int cornerRadius, Color bgColor) {
            this.cornerRadius = cornerRadius;
            this.backgroundColor = bgColor;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(backgroundColor);

            // 클리핑 경로 생성
            Path2D.Float path = new Path2D.Float();
            path.moveTo(0, cornerRadius);
            path.quadTo(0, 0, cornerRadius, 0);
            path.lineTo(getWidth() - cornerRadius, 0);
            path.quadTo(getWidth(), 0, getWidth(), cornerRadius);
            path.lineTo(getWidth(), getHeight());
            path.lineTo(0, getHeight());
            path.closePath();

            g2.clip(path);
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
    }

}
