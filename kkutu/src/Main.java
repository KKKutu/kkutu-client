import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Main extends JFrame{
    public Main() {
        setGUI();
    }

    public JLabel createBackGround() {//그리는 함수
        JLabel imgLabel = new JLabel();

        ImageIcon icon = new ImageIcon(
                Objects.requireNonNull(Main.class.getResource("./image/main.png"))
        );

        Image img = icon.getImage();
        Image updateImg = img.getScaledInstance(1200, 800, Image.SCALE_SMOOTH);
        ImageIcon updateIcon = new ImageIcon(updateImg);

        imgLabel.setIcon(updateIcon);

        imgLabel.setBounds(0, 0, 1200, 800);
        imgLabel.setHorizontalAlignment(JLabel.CENTER);

        return imgLabel;
    }

    public void setGUI() {

        setTitle("KKutu");
        setSize(1200,800);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);

        add(createBackGround());

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args){
        new Main();
    }
}
