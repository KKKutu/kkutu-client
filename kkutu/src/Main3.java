//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.Objects;
//
//public class Main3 extends JFrame{
//    public Main3() {
//        setGUI();
//    }
//
//    public JLabel createBackGround() {//그리는 함수
//        JLabel imgLabel = new JLabel();
//
//        ImageIcon icon = new ImageIcon(
//                Objects.requireNonNull(Main3.class.getResource("./image/main.png"))
//        );
//
//        Image img = icon.getImage();
//        Image updateImg = img.getScaledInstance(1200, 800, Image.SCALE_SMOOTH);
//        ImageIcon updateIcon = new ImageIcon(updateImg);
//
//        imgLabel.setIcon(updateIcon);
//
//        imgLabel.setBounds(0, 0, 1200, 800);
//        imgLabel.setHorizontalAlignment(JLabel.CENTER);
//
//        return imgLabel;
//    }
//
//    public JButton createStartButton() {//그리는 함수
//
//
//        ImageIcon icon = new ImageIcon(
//                Objects.requireNonNull(Main3.class.getResource("./image/loginBtn.png"))
//        );
//
//
//        Image img = icon.getImage();
//        Image updateImg = img.getScaledInstance(220, 182, Image.SCALE_SMOOTH);
//        ImageIcon updateIcon = new ImageIcon(updateImg);
//
//        JButton btn = new JButton(updateIcon);
//
//        btn.setBorderPainted(false); // 버튼 테두리 설정해제
////        btn.setPreferredSize(new Dimension(300, 50)); // 버튼 크기 지정
//        btn.setBounds(825,318, 220, 182);
//
//        btn.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//            }
//        });
//
//        return btn;
//    }
//
//    public void setGUI() {
//
//        setTitle("KKutu");
//        setSize(1000,600);
//        setResizable(false);
//        setLocationRelativeTo(null);
//        setLayout(null);
//
//        add(createStartButton());
//        add(createBackGround());
//
//        setVisible(true);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    }
//
//    public static void main(String[] args){
//        new Main3();
//    }
//}
