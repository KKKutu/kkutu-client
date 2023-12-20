package component;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Path2D;
import javax.swing.JPanel;

// 모든 모서리가 둥근 패널 - 게임 화면에서 사용
public class RoundedPersonPanel {

    // 둥근 모서리 패널 생성 메서드
    public static JPanel createRoundedPanel(int x, int y, int width, int height, Color color, int cornerRadius) {
        JPanel roundedPanel = new RoundedPanel(cornerRadius, color);
        roundedPanel.setBounds(x, y, width, height);
        roundedPanel.setOpaque(false);
        return roundedPanel;
    }

    // 둥근 모서리를 가진 JPanel 커스텀 클래스
    public static class RoundedPanel extends JPanel {
        private Color backgroundColor;
        private final int cornerRadius;
        private Stroke borderStroke; // 테두리 스트로크
        private Color borderColor; // 테두리 색상

        public RoundedPanel(int cornerRadius, Color bgColor) {
            this.cornerRadius = cornerRadius;
            this.backgroundColor = bgColor;
            this.borderStroke = new BasicStroke(1); // 기본 스트로크 설정
            this.borderColor = Color.BLACK; // 기본 테두리 색상 설정
        }

        // 여기에 setStyle 메서드 추가
        public void setStyle(Color bgColor, Color borderColor, int borderWidth) {
            this.backgroundColor = bgColor;
            this.borderStroke = new BasicStroke(borderWidth);
            this.borderColor = borderColor;
            repaint(); // 패널 다시 그리기
        }


        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 둥근 사각형을 그리기 위한 클리핑 경로 생성
            Path2D.Float path = new Path2D.Float();
            int w = getWidth() - 1; // 스트로크 너비 고려
            int h = getHeight() - 1; // 스트로크 너비 고려

            path.moveTo(cornerRadius, 1);
            path.lineTo(w - cornerRadius, 1);
            path.quadTo(w, 1, w, cornerRadius);
            path.lineTo(w, h - cornerRadius);
            path.quadTo(w, h, w - cornerRadius, h);
            path.lineTo(cornerRadius, h);
            path.quadTo(1, h, 1, h - cornerRadius);
            path.lineTo(1, cornerRadius);
            path.quadTo(1, 1, cornerRadius, 1);
            path.closePath();

            g2.setColor(backgroundColor);
            g2.fill(path);

            // 스트로크 설정 및 테두리 그리기 (기존 코드 수정)
            Stroke oldStroke = g2.getStroke();
            g2.setStroke(borderStroke); // 변경된 스트로크 사용
            g2.setColor(borderColor); // 변경된 색상 사용
            g2.draw(path);
            g2.setStroke(oldStroke); // 원래 스트로크로 복원
        }

    }

}
