package screen;

import component.RoundedButton;
import component.RoundedIdField;
import component.RoundedPwField;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Start extends JFrame {

    public Start() {
        setScreen();
    }

    public JLabel createLogo() { // 끄투 로고

        JLabel imgLabel = new JLabel();

        ImageIcon icon = new ImageIcon(
                Objects.requireNonNull(Start.class.getResource("../image/start/logo.png"))
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
                Objects.requireNonNull(Start.class.getResource("../image/start/main_background.png"))
        );

        Image img = icon.getImage();
        ImageIcon imageIcon = new ImageIcon(img);

        imgLabel.setIcon(imageIcon);

        imgLabel.setBounds(0, 143, imageIcon.getIconWidth(), imageIcon.getIconHeight());
        imgLabel.setHorizontalAlignment(JLabel.CENTER);

        return imgLabel;
    }

    public JButton loginBtn() { // 로그인 버튼

        RoundedButton loginBtn = new RoundedButton("로그인", 50); // 두 번째 매개변수는 round 정도
        loginBtn.setBounds(280, 480, 200, 50); // x 좌표, y 좌표, 너비, 높이
        loginBtn.setBackground(Color.decode("#FFFFFF")); // 버튼 배경 색
        loginBtn.setFont(new Font("Dialog", Font.BOLD, 20)); // 폰트 지정

        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new JDialog(Start.this, "로그인", true); // true로 설정하여 모달로 만듦
                dialog.setLayout(null);
                dialog.setTitle("로그인");
                dialog.setLayout(null); // 절대 위치 지정을 위해 레이아웃을 null로 설정
                dialog.setSize(300, 180); // 다이얼로그 크기 설정
                dialog.setResizable(false);
                dialog.setLocationRelativeTo(null); // 화면 중앙에 위치

                // 아이디 라벨 생성 및 추가
                JLabel idLabel = new JLabel("아이디");
                idLabel.setFont(new Font("Dialog", Font.PLAIN, 12)); // 폰트 크기를 12로 설정
                idLabel.setBounds(24, 21, 100, 30); // x, y, 너비, 높이
                dialog.add(idLabel);

                // 아이디 입력 필드
                RoundedIdField inputId = new RoundedIdField(20, 10);
                inputId.setBounds(74, 21, 208, 25);
                dialog.add(inputId);

                // 비밀번호 라벨 생성 및 추가
                JLabel passwordLabel = new JLabel("비밀번호");
                passwordLabel.setFont(new Font("Dialog", Font.PLAIN, 12)); // 폰트 크기를 12로 설정
                passwordLabel.setBounds(18, 56, 100, 30); // x, y, 너비, 높이
                dialog.add(passwordLabel);

                // 비밀번호 필드
                RoundedPwField inputPw = new RoundedPwField(20, 10);
                inputPw.setBounds(74, 56, 208, 25);
                dialog.add(inputPw);

                // 로그인 버튼
                RoundedButton loginBtn = new RoundedButton("로그인", 30); // 두 번째 매개변수는 round 정도
                loginBtn.setBounds(110, 96, 80, 28); // x 좌표, y 좌표, 너비, 높이
                loginBtn.setBackground(Color.decode("#EEEEEE")); // 버튼 배경 색
                loginBtn.setFont(new Font("Dialog", Font.PLAIN, 12)); // 폰트 지정

                // TODO : 서버에 데이터 전송, 검증 필요
                ActionListener confirmAction = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String id = inputId.getText();
                        String pw = new String(inputPw.getPassword());
                        System.out.println("ID: " + id + ", Password: " + pw); // 콘솔에 출력
                        dialog.dispose(); // 다이얼로그 닫기
                    }
                };
                loginBtn.addActionListener(confirmAction);
                inputPw.addActionListener(confirmAction); // 엔터 키에도 동일한 액션 리스너 적용

                dialog.add(loginBtn); // Add confirm button to dialog

                dialog.setVisible(true); // 다이얼로그 보이기
            }
        });

        return loginBtn;
    }

    public JButton signupBtn() { // 회원가입 버튼

        RoundedButton signupBtn = new RoundedButton("회원가입", 50); // 두 번째 매개변수는 round 정도
        signupBtn.setBounds(520, 480, 200, 50); // x 좌표, y 좌표, 너비, 높이
        signupBtn.setBackground(Color.decode("#FFFFFF")); // 버튼 배경 색
        signupBtn.setFont(new Font("Dialog", Font.BOLD, 20)); // 폰트 지정

        signupBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new JDialog(Start.this, "회원가입", true); // true로 설정하여 모달로 만듦
                dialog.setLayout(null);
                dialog.setTitle("회원가입");
                dialog.setSize(300, 180); // 다이얼로그 크기 설정
                dialog.setResizable(false);
                dialog.setLocationRelativeTo(null); // 화면 중앙에 위치

                // 아이디 라벨 생성 및 추가
                JLabel idLabel = new JLabel("아이디");
                idLabel.setFont(new Font("Dialog", Font.PLAIN, 12)); // 폰트 크기를 12로 설정
                idLabel.setBounds(24, 21, 100, 30); // x, y, 너비, 높이
                dialog.add(idLabel);

                // 아이디 입력 필드
                RoundedIdField inputId = new RoundedIdField(20, 10);
                inputId.setBounds(74, 21, 129, 25);
                dialog.add(inputId);

                // TODO : 서버에서 검증 필요
                // 아이디 중복확인 버튼
                RoundedButton checkIdBtn = new RoundedButton("중복확인", 15);
                checkIdBtn.setBounds(208, 21, 74, 25); // x 좌표, y 좌표, 너비, 높이
                checkIdBtn.setBackground(Color.decode("#EEEEEE")); // 버튼 배경 색
                checkIdBtn.setFont(new Font("Dialog", Font.PLAIN, 8)); // 폰트 지정
                dialog.add(checkIdBtn);

                // 비밀번호 라벨 생성 및 추가
                JLabel passwordLabel = new JLabel("비밀번호");
                passwordLabel.setFont(new Font("Dialog", Font.PLAIN, 12)); // 폰트 크기를 12로 설정
                passwordLabel.setBounds(18, 56, 100, 30); // x, y, 너비, 높이
                dialog.add(passwordLabel);

                // 비밀번호 필드
                RoundedPwField inputPw = new RoundedPwField(20, 10);
                inputPw.setBounds(74, 56, 208, 25);
                dialog.add(inputPw);

                // 회원가입 버튼
                RoundedButton signupBtn = new RoundedButton("회원가입", 30); // 두 번째 매개변수는 round 정도
                signupBtn.setBounds(110, 96, 93, 28); // x 좌표, y 좌표, 너비, 높이
                signupBtn.setBackground(Color.decode("#EEEEEE")); // 버튼 배경 색
                signupBtn.setFont(new Font("Dialog", Font.PLAIN, 12)); // 폰트 지정

                // TODO : 서버에 데이터 전송, 검증 필요
                ActionListener confirmAction = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String id = inputId.getText();
                        String pw = new String(inputPw.getPassword());
                        System.out.println("ID: " + id + ", Password: " + pw); // 콘솔에 출력
                        dialog.dispose(); // 다이얼로그 닫기
                    }
                };
                signupBtn.addActionListener(confirmAction);
                inputPw.addActionListener(confirmAction); // 엔터 키에도 동일한 액션 리스너 적용

                dialog.add(signupBtn); // Add signupBtn button to dialog

                dialog.setVisible(true); // 다이얼로그 보이기
            }
        });

        return signupBtn;
    }

    public void setScreen() {

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
}
