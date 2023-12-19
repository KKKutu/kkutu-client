package screen;

import component.RoundedWordField;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class Game extends JFrame {

    private Socket socket;
    private JLabel countdownLabel;
    private JLabel timerLabel;
    private RoundedWordField inputField;
    private JLabel chainLabel; // 몇 번 이어갔는지 표시하는 라벨
    private int chainValue = 0; // 현재 몇 번 이어갔는지 값

    private boolean isReadyStatus = true;

    // TODO : 소켓으로 정보 갖고오기
    private int roundNum = 3; // 몇 번의 라운드를 할 지
    // TODO : 소켓으로 정보 갖고오기
    private int oneRoundTime = 30; // 한 라운드 당 시간 (30초 또는 60초)

    private int currentRound = 0; // 현재 라운드

    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 600;

    public Game(Socket socket) {
        this.socket = socket;
        setScreen();
        startCountdown(countdownLabel);
    }

    // UI 설정
    public void setScreen() {
        setWindow(); // 화면 기본 구성
        addPanels(); // 패널들 부착
        setVisible(true); // 해당 프레임 보이게
        inputField.requestFocusInWindow(); // 포커스
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 화면 닫으면 프로그램 종료
    }

    // 화면 기본 구성
    private void setWindow() {
        setTitle("Game");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);
    }

    // 패널들 부착
    private void addPanels() {

        add(createFrog()); // 개구리 이미지

        chainLabel = createScoreLabel(); // 점수 레이블 생성
        add(chainLabel); // 점수 레이블 추가

        add(createChain()); // 체인 이미지

        inputField = createInputField();
        add(inputField);// 입력 필드

        add(createCountdownLabel()); // 카운트 다운 텍스트 라벨
        timerLabel = createTimerImg(); // 타이머 이미지 라벨 생성
        add(timerLabel); // 타이머 이미지 라벨 추가

        add(createTimerImg()); // 타이머 이미지
        add(createBackground()); // 배경 (마지막에 넣기)
    }

    // 점수 레이블 생성
    private JLabel createScoreLabel() {
        JLabel label = new JLabel("0", SwingConstants.CENTER);
        label.setFont(new Font("Dialog", Font.BOLD, 30));
        label.setForeground(Color.WHITE);
        label.setBounds(808, 115, 50, 50); // 위치와 크기 설정
        return label;
    }

    // 입력 필드
    private RoundedWordField createInputField() {
        RoundedWordField field = new RoundedWordField(1, 10);
        field.setBounds(38, 500, 924, 37);
        field.setBorder(BorderFactory.createLineBorder(Color.decode("#A0A0A0"), 2));
        field.setFont(new Font("Dialog", Font.BOLD, 15));

        // ActionListener 추가
        field.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isReadyStatus) { // isReadyStatus가 false일 때만 동작
                    String inputText = field.getText(); // 입력 필드의 텍스트 가져오기
                    if(!inputText.isEmpty()) { // 비어있지 않은 경우에만!!

                        // System.out.println 대신에
                        // TODO : 있는 단어인지 검증 - 있는 단어라면 글자 수 세기, 유저의 점수 올리기, chain 1 증가
                        System.out.println(inputText); // 콘솔에 텍스트 출력
                        chainValue++; // 점수 증가
                        chainLabel.setText(String.valueOf(chainValue)); // 레이블 업데이트

                        field.setText(""); // 입력 필드 초기화
                        field.requestFocusInWindow(); // 입력 필드에 다시 포커스 설정
                    }

                }
            }
        });

        return field;
    }

    // 게임 카운트 다운
    private void startCountdown(JLabel countdownLabel) {
        Timer timer = new Timer(1000, new ActionListener() {
            int timeLeft = 3; // 초기 카운트다운 3초

            @Override
            public void actionPerformed(ActionEvent e) {

                // UI 업데이트 메소드 호출
                updateTimerAppearance(timeLeft);

                if (isReadyStatus) { // 준비 상태
                    if (timeLeft >= 0) {
                        countdownLabel.setText(Integer.toString(timeLeft));
                    } else {
                        isReadyStatus = false;
                        timeLeft = oneRoundTime; // 게임 시간 설정 (30 또는 60초)
                        currentRound++;
                    }
                } else { // 준비 상태가 아닐 때
                    if (timeLeft >= 0) {
                        countdownLabel.setText(Integer.toString(timeLeft));
                    } else {
                        if (currentRound < roundNum) {
                            startNewRound(); // 새 라운드 시작
                            System.out.println(currentRound + "번째 라운드 종료"); // 라운드 종료 메시지 출력
                            isReadyStatus = true;
                            timeLeft = 4; // 다음 라운드 준비 시간
                        } else {
                            ((Timer) e.getSource()).stop();
                            System.out.println("===게임 종료===");
                            System.out.println(currentRound + "번째 라운드 종료");
                        }
                    }
                }
                timeLeft--;
                countdownLabel.repaint(); // 레이블을 다시 그리기
            }
        });
        timer.setRepeats(true);
        timer.start();
    }

    // 게임 끝
    private void GameOver() {
        System.out.println("게임 끝!");
//        new ReadyToGame(socket);
//        dispose(); // Loading 화면 닫기
    }

    // 라운드 시작 시 호출되는 메서드
    private void startNewRound() {
        chainValue = 0; // 점수 초기화
        chainLabel.setText("0"); // 레이블 초기화
        // 라운드 시작 로직...
    }

    // 타이머 외관 업데이트
    private void updateTimerAppearance(int timeLeft) {
        if (isReadyStatus) {
            countdownLabel.setForeground(Color.WHITE);
            changeTimerImage("ready.png"); // 준비 상태 이미지
        } else {
            if (timeLeft <= 10) {
                countdownLabel.setForeground(new Color(255, 19, 19)); // 빨간색
                changeTimerImage("danger.png"); // 위험 상태 이미지
            } else if (timeLeft <= 15) {
                countdownLabel.setForeground(new Color(255, 199, 0)); // 노란색
                changeTimerImage("normal.png"); // 보통 상태 이미지
            } else if (timeLeft <= 60) {
                countdownLabel.setForeground(new Color(0, 255, 26)); // 초록색
                changeTimerImage("safe.png"); // 안전 상태 이미지
            }
        }
    }


    // 타이머 이미지 변경
    private void changeTimerImage(String imageName) {
        ImageIcon icon = loadIcon("../image/game/" + imageName);
        timerLabel.setIcon(icon); // 타이머 라벨의 아이콘 변경
        timerLabel.repaint();
    }

    // 타이머
    public JLabel createTimerImg() {
        JLabel label = new JLabel();
        label.setBounds(132, 75, 86, 98);
        label.setHorizontalAlignment(JLabel.CENTER);
        return label;
    }

    // 배경
    public JLabel createBackground() {
        return createImageLabel("../image/game/bg.png", 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    // 개구리
    public JLabel createFrog() {
        return createImageLabel("../image/game/frog.png", 306, 59, 388, 119);
    }

    // 체인
    public JLabel createChain() {
        return createImageLabel("../image/game/chain.png", 782, 73, 100, 100);
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

    // 카운트 다운 라벨
    public JLabel createCountdownLabel() {
        countdownLabel = new JLabel("", SwingConstants.CENTER);
        countdownLabel.setFont(new Font("Dialog", Font.BOLD, 30));
        countdownLabel.setForeground(Color.WHITE);

        // 위치와 크기를 동시에 설정
        countdownLabel.setBounds(151, 107, 50, 50);
        return countdownLabel;
    }

}