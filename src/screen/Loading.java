package screen;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import util.CustomFont;
import util.CustomImage;

public class Loading extends JFrame {

    // 통신에 필요
    private Socket socket;

    // 화면 구성에 필요
    private JLabel countdownLabel;
    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 600;

    // 생성자에서 기본 설정
    public Loading(Socket socket) {
        this.socket = socket; // 소켓 연결
        setScreen(); // UI 설정
        startCountdown(countdownLabel); // 3초 카운트 다운 시작
    }

    // UI 설정
    public void setScreen() {
        setWindow(); // 화면 기본 구성
        addPanels(); // 두 개의 패널 부착
        setVisible(true); // 해당 프레임 보이게
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 화면 닫으면 프로그램 종료
    }

    // 화면 기본 구성
    private void setWindow() {
        setTitle("로딩중"); // 프레임의 제목 설정
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT); // 프레임 크기 설정
        setResizable(false); // 사용자가 화면 크기 변경 불가
        setLocationRelativeTo(null); // 해당 프레임을 화면의 중앙에 위치
        setLayout(null); // 컴포넌트들의 절대 위치 설정을 위해 레이아웃 매니저 비활성화
    }

    // 두 개의 패널 부착
    private void addPanels() {
        add(createBackground()); // 배경
        add(createCountdownLabel()); // 카운트 다운 텍스트 라벨
    }

    // 로딩 화면을 위한 카운트다운 시작
    private void startCountdown(JLabel countdownLabel) {

        // 1초 간격으로 이벤트를 발생시키는 타이머 생성
        Timer timer = new Timer(1000, new ActionListener() {
            int timeLeft = 4; // 카운트다운 시간

            @Override
            public void actionPerformed(ActionEvent e) {
                timeLeft--; // 매 초마다 시간 감소
                if (timeLeft >= 1) {
                    countdownLabel.setText("로딩중... " + timeLeft); // 남은 시간을 라벨에 표시
                    countdownLabel.repaint(); // 라벨 다시 그리기
                } else {
                    ((Timer) e.getSource()).stop(); // 카운트다운이 끝나면 타이머 정지
                    openReadyToGameScreen(); // 3초 지나면 ReadyToGame 화면 열기
                }
            }
        });
        timer.setRepeats(true); // 타이머가 반복되도록 설정
        timer.start(); // 타이머 시작
    }

    // ReadyToGame 화면 열기
    private void openReadyToGameScreen() {
        new ReadyToGame(socket); // socket 전달
        dispose(); // Loading 화면 닫기
    }

    // 카운트 다운 라벨
    public JLabel createCountdownLabel() {
        countdownLabel = new JLabel("로딩중... 3", SwingConstants.CENTER);
        countdownLabel.setFont(CustomFont.getBoldFont(20)); // 폰트 설정
        countdownLabel.setBackground(Color.WHITE); // 배경색
        countdownLabel.setOpaque(true); // 보이게
        countdownLabel.setBounds(449, 345, 113, 30); // x 위치, y 위치, 넓이, 길이
        return countdownLabel;
    }

    // 배경
    public JLabel createBackground() {
        return CustomImage.createImageLabel("../image/loading/loading.png", 0, 0, 1000, 600); // 적절한 너비와 높이 지정
    }

}