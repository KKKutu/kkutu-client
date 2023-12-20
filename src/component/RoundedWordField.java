package component;

import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JTextField;

// 모서리가 둥근 텍스트 필드 - 게임 화면에서 사용
public class RoundedWordField extends JTextField {
    private static final long serialVersionUID = 1L;
    private Shape shape;
    private int radius;

    public RoundedWordField(int columns, int radius) {
        super(columns); // 열의 수 설정
        this.radius = radius;
        setOpaque(false); // 배경을 자체적으로 그림
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        g.setColor(getForeground());
        g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
    }

    @Override
    public boolean contains(int x, int y) {
        if (shape == null || !shape.getBounds().equals(getBounds())) {
            shape = new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, radius, radius);
        }
        return shape.contains(x, y);
    }
}

