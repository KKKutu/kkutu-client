package screen;

import component.RoundedButton;
import component.RoundedIdField;
import component.RoundedPwField;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import util.CustomFont;
import util.CustomImage;

public class Start extends JFrame {

    // 통신에 필요
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private static final int serverPort = 9999;
    private static final String serverAddress = "localhost";

    // 화면 구성에 필요
    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 600;
    private static final Color BUTTON_BG_COLOR = Color.decode("#FFFFFF");

    // 생성자에서 기본 설정
    public Start() {
        try {
            socket = new Socket(serverAddress, serverPort); // 소켓 생성
            output = new DataOutputStream(socket.getOutputStream()); // 출력 스트림 생성
            input = new DataInputStream(socket.getInputStream()); // 입력 스트림 생성
            output.writeUTF("접속 시도"); // 서버에 메시지 전송
            setScreen(); // UI 설정
            output.flush(); // 출력 스트림의 데이터 모두 전송 - 데이터 전송 보장
        } catch (IOException e) { // I/O 에러 예외 처리
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
        setTitle("게임 시작 화면"); // 프레임의 제목 설정
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT); // 프레임 크기 설정
        setResizable(false); // 사용자가 화면 크기 변경 불가
        setLocationRelativeTo(null); // 해당 프레임을 화면의 중앙에 위치
        setLayout(null); // 컴포넌트들의 절대 위치 설정을 위해 레이아웃 매니저 비활성화
        getContentPane().setBackground(Color.WHITE); // 프레임의 배경색
    }

    // 필요한 요소들 부착
    private void addComponents() {
        add(createLogo()); // 상위 끄투 로고
        add(createKkutuBg()); // 가운데 끄투 배경 이미지
        add(createLoginBtn()); // 로그인 버튼
        add(createSignupBtn()); // 회원가입 버튼
    }

    // 상위 끄투 코리아 로고
    public JLabel createLogo() {
        return CustomImage.createImageLabel("../image/start/logo.png", 42, 46, 214, 44); // 적절한 넓이, 높이 지정
    }

    // 중간 끄투 배경 이미지
    public JLabel createKkutuBg() {
        return CustomImage.createImageLabel("../image/start/main_background.png", 0, 143, 1000, 313); // 적절한 넓이, 높이 지정
    }

    // 로그인, 회원가입 버튼의 공통적인 요소들
    private JButton createButton(String text, int x, int y, int width, int height) {
        JButton button = new RoundedButton(text, 50); // 모서리가 50 둥근 버튼 만들기
        button.setBounds(x, y, width, height); // x 위치, y 위치, 넓이, 길이
        button.setBackground(BUTTON_BG_COLOR); // 버튼 배경색
        button.setFont(CustomFont.getBoldFont(20)); // 폰트 설정
        return button;
    }

    // 로그인 버튼
    public JButton createLoginBtn() {
        JButton loginBtn = createButton("로그인", 280, 480, 200, 50);
        loginBtn.addActionListener(e -> showLoginDialog()); // 클릭 시 로그인 다이얼로그 띄우기
        return loginBtn;
    }

    // 회원가입 버튼
    public JButton createSignupBtn() {
        JButton signupBtn = createButton("회원가입", 520, 480, 200, 50);
        signupBtn.addActionListener(e -> showSignupDialog()); // 클릭 시 회원가입 다이얼로그 띄우기
        return signupBtn;
    }

    // 로그인 버튼 클릭 시 다이얼로그
    private void showLoginDialog() {
        JDialog dialog = new JDialog(Start.this, "로그인", true); // true로 설정하여 모달로 만듦
        dialog.setLayout(null); // 컴포넌트들의 절대 위치 설정을 위해 레이아웃 매니저 비활성화
        dialog.setTitle("로그인"); // 다이얼로그 이름
        dialog.setLayout(null); // 절대 위치 지정을 위해 레이아웃을 null로 설정
        dialog.setSize(300, 180); // 다이얼로그 크기 설정
        dialog.setResizable(false); // 사용자가 화면 크기 변경 불가
        dialog.setLocationRelativeTo(null); // 해당 프레임을 화면의 중앙에 위치

        // 아이디 라벨
        JLabel idLabel = new JLabel("아이디");
        idLabel.setFont(CustomFont.getPlainFont(12)); // 폰트 설정
        idLabel.setBounds(24, 21, 100, 30); // x, y, 넓이, 높이
        dialog.add(idLabel); // 부착

        // 아이디 입력 필드
        RoundedIdField inputId = new RoundedIdField(20, 10); // 모서리가 10 둥근 입력 필드 만들기
        inputId.setBounds(74, 21, 208, 25); // x, y, 넓이, 높이
        dialog.add(inputId); // 부착

        // 비밀번호 라벨
        JLabel passwordLabel = new JLabel("비밀번호");
        passwordLabel.setFont(CustomFont.getPlainFont(12)); // 폰트 설정
        passwordLabel.setBounds(18, 56, 100, 30); // x, y, 넓이, 높이
        dialog.add(passwordLabel); // 부착

        // 비밀번호 입력 필드
        RoundedPwField inputPw = new RoundedPwField(20, 10); // 모서리가 10 둥근 입력 필드 만들기
        inputPw.setBounds(74, 56, 208, 25); // x, y, 넓이, 높이
        dialog.add(inputPw);

        // 로그인 버튼
        RoundedButton loginBtn = new RoundedButton("로그인", 30); // 모서리가 30 둥근 버튼 만들기
        loginBtn.setBounds(110, 96, 80, 28); // x 좌표, y 좌표, 넓이, 높이
        loginBtn.setBackground(Color.decode("#EEEEEE")); // 버튼 배경색
        loginBtn.setFont(CustomFont.getPlainFont(12)); // 폰트 설정

        // 액션 리스너
        ActionListener confirmAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = inputId.getText(); // 사용자로부터 입력받은 id 값 가져오기
                String pw = new String(inputPw.getPassword()); // 사용자로부터 입력받은 pw 값 가져오기
                System.out.println("ID: " + id + ", Password: " + pw); // 콘솔에 출력

                try {
                    output.writeUTF("ACTION=SignIn&USERNAME="+id+"&PASSWORD="+pw);
                    output.flush();

                    String signUpRes = input.readUTF();

                    if(signUpRes.contains("SignIn")){
                        if(signUpRes.contains("YES")){ // 로그인이 성공하는 경우
                            System.out.println("로그인 성공");
                            dialog.dispose(); // 다이얼로그 닫기
                            Start.this.setVisible(false); // 현재 Start 프레임을 숨기기
                            Start.this.dispose(); // 현재 Start 프레임을 닫기
                            new Loading(socket); // Loading 화면 띄우기

                        } else { // 로그인이 실패하는 경우
                            inputId.setText("");
                            inputPw.setText("");
                            System.out.println("로그인 실패");
                        }
                    }

                } catch (IOException io){ // I/O 에러 예외 처리
                    System.out.println(io.getMessage());
                }

            }
        };
        loginBtn.addActionListener(confirmAction); // 로그인 버튼에 액션 리스너 등록
        inputPw.addActionListener(confirmAction); // 엔터 키에도 동일한 액션 리스너 등록

        dialog.add(loginBtn); // 부착
        dialog.setVisible(true); // 다이얼로그 보이기
    }

    // 회원가입 버튼 클릭 시 다이얼로그
    private void showSignupDialog() {
        JDialog dialog = new JDialog(Start.this, "회원가입", true); // true로 설정하여 모달로 만듦
        dialog.setLayout(null); // 컴포넌트들의 절대 위치 설정을 위해 레이아웃 매니저 비활성화
        dialog.setTitle("회원가입"); // 다이얼로그 이름
        dialog.setSize(300, 180); // 다이얼로그 크기 설정
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null); // 화면 중앙에 위치

        // 아이디 라벨
        JLabel idLabel = new JLabel("아이디");
        idLabel.setFont(CustomFont.getPlainFont(12)); // 폰트 설정
        idLabel.setBounds(24, 21, 100, 30); // x, y, 넓이, 높이
        dialog.add(idLabel); // 부착

        // 아이디 입력 필드
        RoundedIdField inputId = new RoundedIdField(20, 10); // 모서리가 10 둥근 입력 필드 만들기
        inputId.setBounds(74, 21, 129, 25); // x, y, 넓이, 높이
        dialog.add(inputId); // 부착

        // 아이디 중복확인 버튼
        RoundedButton checkIdBtn = new RoundedButton("중복확인", 15); // 모서리가 15 둥근 버튼 만들기
        checkIdBtn.setBounds(208, 21, 74, 25); // x 좌표, y 좌표, 넓이, 높이
        checkIdBtn.setBackground(Color.decode("#EEEEEE")); // 버튼 배경색
        checkIdBtn.setFont(CustomFont.getPlainFont(8)); // 폰트 설정

        // 액션 리스너
        checkIdBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = inputId.getText(); // 사용자로부터 입력받은 id 값 가져오기
                System.out.println("ID: " + id); // 콘솔에 출력
                try {
                    output.writeUTF("ACTION=FindName&USERNAME="+id);
                    output.flush();
                    String receivedData = input.readUTF();
                    String result = receivedData.split("&")[1].split("=")[1];

                    // NO인 경우 중복되지 않음
                    if(receivedData.contains("FindName")){

                        if(result.equals("NO")){
                            System.out.println("중복 X");
                        } else {
                            inputId.setText("");
                            System.out.println("중복 O");
                        }
                    }

                } catch (IOException io){ // I/O 에러 예외 처리
                    System.out.println(io.getMessage());
                }
            }
        });

        dialog.add(checkIdBtn); // 부착

        // 비밀번호 라벨 생성 및 추가
        JLabel passwordLabel = new JLabel("비밀번호");
        passwordLabel.setFont(CustomFont.getPlainFont(12)); // 폰트 설정
        passwordLabel.setBounds(18, 56, 100, 30); // x, y, 넓이, 높이
        dialog.add(passwordLabel); // 부착

        // 비밀번호 필드
        RoundedPwField inputPw = new RoundedPwField(20, 10); // 모서리가 10 둥근 입력 필드 만들기
        inputPw.setBounds(74, 56, 208, 25); // x, y, 넓이, 높이
        dialog.add(inputPw); // 부착

        // 회원가입 버튼
        RoundedButton signupBtn = new RoundedButton("회원가입", 30); // 모서리가 30 둥근 버튼 만들기
        signupBtn.setBounds(110, 96, 93, 28); // x 좌표, y 좌표, 넓이, 높이
        signupBtn.setBackground(Color.decode("#EEEEEE")); // 버튼 배경색
        signupBtn.setFont(CustomFont.getPlainFont(12)); // 폰트 설정

        // 액션 리스너
        ActionListener confirmAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = inputId.getText(); // 사용자로부터 입력받은 id 값 가져오기
                String pw = new String(inputPw.getPassword()); // 사용자로부터 입력받은 pw 값 가져오기
                System.out.println("ID: " + id + ", Password: " + pw); // 콘솔에 출력
                try {
                    output.writeUTF("ACTION=SignUp&USERNAME="+id+"&PASSWORD="+pw);
                    output.flush();

                    String signUpRes = input.readUTF();

                    if(signUpRes.contains("SignUp")){
                        if(signUpRes.contains("OK")){ // 회원가입이 성공하는 경우
                            dialog.dispose(); // 다이얼로그 닫기
                            System.out.println("회원가입 성공");
                        } else { // 회원가입이 실패하는 경우
                            inputId.setText("");
                            inputPw.setText("");
                            System.out.println("회원가입 실패");
                        }
                    }

                } catch (IOException io){ // I/O 에러 예외 처리
                    System.out.println(io.getMessage());
                }

            }
        };

        signupBtn.addActionListener(confirmAction); // 회원가입 버튼에 액션 리스너 등록
        inputPw.addActionListener(confirmAction); // 엔터 키에도 동일한 액션 리스너 적용

        dialog.add(signupBtn); // 부착
        dialog.setVisible(true); // 다이얼로그 보이기
    }

}
