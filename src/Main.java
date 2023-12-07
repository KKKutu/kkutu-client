import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Main extends JFrame {

    public Main() {
        setGUI();
    }

    public JLabel createLogo() { // 끄투 로고

        JLabel imgLabel = new JLabel();

        ImageIcon icon = new ImageIcon(
                Objects.requireNonNull(Main.class.getResource("./image/start/logo.png"))
        );

        Image img = icon.getImage();
        ImageIcon imageIcon = new ImageIcon(img);

        imgLabel.setIcon(imageIcon);

        imgLabel.setBounds(42, 46, imageIcon.getIconWidth(), imageIcon.getIconHeight());
        imgLabel.setHorizontalAlignment(JLabel.CENTER);

        return imgLabel;
    }

    public JLabel createKkutuBg() { // 가운데 이미지

        JLabel imgLabel = new JLabel();

        ImageIcon icon = new ImageIcon(
                Objects.requireNonNull(Main.class.getResource("./image/start/main_background.png"))
        );

        Image img = icon.getImage();
        ImageIcon imageIcon = new ImageIcon(img);

        imgLabel.setIcon(imageIcon);

        imgLabel.setBounds(0, 143, imageIcon.getIconWidth(), imageIcon.getIconHeight());
        imgLabel.setHorizontalAlignment(JLabel.CENTER);

        return imgLabel;
    }

    public JButton loginBtn() { // 로그인 버튼
        ImageIcon loginBtnIcon = new ImageIcon(
                Objects.requireNonNull(Main.class.getResource("./image/start/login_btn.png"))
        );

        JButton loginBtn = new JButton(loginBtnIcon);
        loginBtn.setBounds(280, 480, 200, 50); // x 좌표, y 좌표, 너비, 높이
        loginBtn = setBtnDefault(loginBtn);

        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new JDialog();
                dialog.setTitle("로그인 다이얼로그");
                dialog.setSize(300, 140); // 다이얼로그 크기 설정
                dialog.setLocationRelativeTo(null); // 화면 중앙에 위치
                dialog.setModal(true); // 모달 다이얼로그로 설정

                // 다이얼로그에 추가할 컴포넌트는 여기에 구성

                dialog.setVisible(true); // 다이얼로그 보이기
            }
        });

        return loginBtn;
    }

    public JButton signupBtn() { // 회원가입 버튼
        ImageIcon signupBtnIcon = new ImageIcon(
                Objects.requireNonNull(Main.class.getResource("./image/start/signup_btn.png"))
        );

        JButton signupBtn = new JButton(signupBtnIcon);
        signupBtn.setBounds(520, 480, 200, 50); // x 좌표, y 좌표, 너비, 높이
        signupBtn = setBtnDefault(signupBtn);

        signupBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new JDialog();
                dialog.setTitle("회원가입 다이얼로그");
                dialog.setSize(300, 140); // 다이얼로그 크기 설정
                dialog.setLocationRelativeTo(null); // 화면 중앙에 위치
                dialog.setModal(true); // 모달 다이얼로그로 설정

                // 다이얼로그에 추가할 컴포넌트는 여기에 구성

                dialog.setVisible(true); // 다이얼로그 보이기
            }
        });

        return signupBtn;
    }

    // 버튼 공통 설정
    private JButton setBtnDefault(JButton btn) {
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        return btn;
    }

    public void setGUI() {

        setTitle("KKutu");
        setSize(1000,600);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);

        getContentPane().setBackground(Color.WHITE);

        // 이미지
        add(createLogo()); // 로고 이미지
        add(createKkutuBg()); // 끄투 이미지

        // 버튼
        add(loginBtn()); // 로그인 버튼
        add(signupBtn()); // 회원가입 버튼

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args){
        new Main();
    }
}
