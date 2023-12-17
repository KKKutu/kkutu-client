package screen;

import component.RoundedButton;
import component.RoundedIdField;
import component.RoundedPwField;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Start extends JFrame {

    private static final String serverAddress = "localhost";
    private static final int serverPort = 9999;
    private DataOutputStream output;
    private DataInputStream input;

    private Socket socket;
    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 600;
    private static final Color BUTTON_BG_COLOR = Color.decode("#FFFFFF");
    private static final Font BUTTON_FONT = new Font("Dialog", Font.BOLD, 20);

    // 생성자에서 UI 설정
    public Start() {
        try {
            socket = new Socket(serverAddress, serverPort);
            output = new DataOutputStream(socket.getOutputStream());
            input = new DataInputStream(socket.getInputStream());
            output.writeUTF("접속 시도");
            setScreen();
//            new Thread(this::Start).start();

            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    // UI 설정
    public void setScreen() {
        setWindow(); // 화면 기본 구성
        addComponents(); // 필요한 요소들 부착
        setVisible(true); // 해당 프레임 보이게
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 화면 닫으면 프로그램 종료
    }

    // 화면 기본 구성
    private void setWindow() {
        setTitle("Ready to Game");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);
    }

    // 필요한 요소들 부착
    private void addComponents() {
        add(createLogo()); // 상위 끄투 로고
        add(createKkutuBg()); // 가운데 끄투 배경 이미지
        add(createLoginBtn()); // 로그인 버튼
        add(createSignupBtn()); // 회원가입 버튼
    }

    // 이미지 아이콘 로딩
    private ImageIcon loadIcon(String path) {
        return new ImageIcon(
                Objects.requireNonNull(getClass().getResource(path))
        );
    }

    // 이미지를 라벨로
    private JLabel createImageLabel(String imagePath, int x, int y, int width, int height) {
        JLabel imgLabel = new JLabel();
        ImageIcon icon = loadIcon(imagePath);
        imgLabel.setIcon(icon);
        imgLabel.setBounds(x, y, width, height);
        imgLabel.setHorizontalAlignment(JLabel.CENTER);
        return imgLabel;
    }

    // 상위 끄투 코리아 로고
    public JLabel createLogo() {
        return createImageLabel("../image/start/logo.png", 42, 46, 214, 44); // 적절한 너비와 높이 지정
    }

    // 중간 끄투 배경 이미지
    public JLabel createKkutuBg() {
        return createImageLabel("../image/start/main_background.png", 0, 143, 1000, 313); // 적절한 너비와 높이 지정
    }

    // 로그인, 회원가입 버튼에 공통적인 요소들
    private JButton createButton(String text, int x, int y, int width, int height) {
        JButton button = new RoundedButton(text, 50);
        button.setBounds(x, y, width, height);
        button.setBackground(BUTTON_BG_COLOR);
        button.setFont(BUTTON_FONT);
        return button;
    }

    // 로그인 버튼
    public JButton createLoginBtn() {
        JButton loginBtn = createButton("로그인", 280, 480, 200, 50);
        loginBtn.addActionListener(e -> showLoginDialog());
        return loginBtn;
    }

    // 회원가입 버튼
    public JButton createSignupBtn() {
        JButton signupBtn = createButton("회원가입", 520, 480, 200, 50);
        signupBtn.addActionListener(e -> showSignupDialog());
        return signupBtn;
    }

    // 로그인 버튼 클릭 시 다이얼로그
    private void showLoginDialog() {
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

                // TODO : 로그인이 성공된 상황에서만 수행되어야 할 작업
                dialog.dispose(); // 다이얼로그 닫기
                Start.this.setVisible(false); // 현재 Start 프레임을 숨기기
                Start.this.dispose(); // 현재 Start 프레임을 닫기
                new Loading(socket); // Loading 화면 띄우기
            }
        };
        loginBtn.addActionListener(confirmAction);
        inputPw.addActionListener(confirmAction); // 엔터 키에도 동일한 액션 리스너 적용

        dialog.add(loginBtn);
        dialog.setVisible(true); // 다이얼로그 보이기
    }

    // 회원가입 버튼 클릭 시 다이얼로그
    private void showSignupDialog() {
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
        checkIdBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = inputId.getText();
                System.out.println("ID: " + id); // 콘솔에 출력
                try {
                    output.writeUTF("ACTION=FindName&USERNAME="+id);
                    output.flush();
                    String result = input.readUTF().split("&")[1].split("=")[1];

                    // NO인 경우 중복되지 않음
                    if(result.equals("NO")){
                        System.out.println("중복 X");
                    } else {
                        inputId.setText("");
                        System.out.println("중복 O");
                    }
                } catch (IOException io){
                    System.out.println("Error");
                }
            }
        });
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
                try {
                    output.writeUTF("ACTION=SignUp&USERNAME="+id+"&PASSWORD="+pw);
                    output.flush();
                    String result = input.readUTF().split("&")[1].split("=")[1];
                    System.out.println(result);
                    if(result.equals("OK")){
                        dialog.dispose(); // 다이얼로그 닫기
                        System.out.println("회원가입 성공");
                    } else {
                        inputId.setText("");
                        inputPw.setText("");
                        System.out.println("회원가입 실패");
                    }
                } catch (IOException io){
                    System.out.println("Error");
                }

            }
        };
        signupBtn.addActionListener(confirmAction);
        inputPw.addActionListener(confirmAction); // 엔터 키에도 동일한 액션 리스너 적용

        dialog.add(signupBtn);
        dialog.setVisible(true); // 다이얼로그 보이기
    }

}
