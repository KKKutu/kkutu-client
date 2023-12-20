package screen;

import component.RoundedPersonPanel;
import component.RoundedPersonPanel.RoundedPanel;
import component.RoundedWordField;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import util.Audio;

public class Game extends JFrame {

    private Socket socket;
    private DataOutputStream output;
    private DataInputStream input;
    private Long roomId;
    private Audio audio;
    private JLabel countdownLabel;
    private JLabel timerLabel;
    private RoundedWordField inputField;
    private JLabel chainLabel; // 몇 번 이어갔는지 표시하는 라벨
    private int chainValue = 0; // 현재 몇 번 이어갔는지 값
    private int currentPerson = 0; // 현재 차례인 사람의 인덱스 (0부터 시작)
    private JPanel[] personPanels; // 사람 패널들을 저장할 배열
    private JLabel[] scoreLabels; // 점수 라벨들을 저장할 배열
    private String selectedWord; // 뽑힌 단어를 저장하는 변수
    private String lastWord = ""; // 마지막으로 입력된 단어 저장
    private JLabel wordLabel; // 사용자가 입력한 단어를 보여주는 라벨

    private boolean isReadyStatus = true;

    // TODO : 소켓으로 정보 갖고오기
    private int roundNum = 3; // 몇 번의 라운드를 할 지
    // TODO : 소켓으로 정보 갖고오기
    private int oneRoundTime = 30; // 한 라운드 당 시간 (30초 또는 60초)

    private int currentRound = 0; // 현재 라운드

    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 600;

    private String[] threeLengthWordArr = {"개나리", "강남역", "보조개", "수영장", "오리발", "어린이"};
    private String[] fiveLengthWordArr = {"아이스크림", "크리스마스", "아메리카노", "오케스트라", "코인노래방"};
    private String[] selectedWordArray; // 선택된 단어를 글자별로 저장하는 배열

    public Game(Socket socket, Audio audio, Thread prevThread, Long roomId) {
        prevThread.interrupt();
        this.roomId = roomId;
        this.socket = socket;
        this.audio = audio;

        try {
            output = new DataOutputStream(socket.getOutputStream());
            input = new DataInputStream(socket.getInputStream());
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }

        // 로비 음악 끝
        audio.stopAudio("lobby");
        audio.closeAudio("lobby");

        // 랜덤 단어 선택 및 분리
        selectAndSplitWord();

        setScreen();
        startCountdown(countdownLabel);
    }

    private void selectAndSplitWord() {
        String[] wordArr = roundNum == 3 ? threeLengthWordArr : fiveLengthWordArr;
        selectedWord = wordArr[(int) (Math.random() * wordArr.length)];
        System.out.println("뽑힌 단어는? " + selectedWord);

        selectedWordArray = selectedWord.split("");
        lastWord = selectedWordArray[0];
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

        // 뽑힌 단어 표시
        JLabel wordLabel = createWord();
        add(wordLabel);
        add(showInputText()); // 사용자가 입력한 단어 보여주기
        add(createFrog()); // 개구리 이미지

        chainLabel = createChainLabel(); // 점수 라벨 생성
        add(chainLabel); // 체인 라벨 추가

        add(createChain()); // 체인 이미지

        inputField = createInputField();
        add(inputField);// 입력 필드

        add(createCountdownLabel()); // 카운트 다운 텍스트 라벨
        timerLabel = createTimerImg(); // 타이머 이미지 라벨 생성
        add(timerLabel); // 타이머 이미지 라벨 추가

        add(createTimerImg()); // 타이머 이미지

        try{
            output.writeUTF("ACTION=InGameUserLength&" + roomId);
            output.flush();

            String receivedData = input.readUTF();
            int length = Integer.parseInt(receivedData.split("&")[1]);
            // 사용자 패널 추가
            add(personPanel(length));
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }


        add(createBackground()); // 배경 (마지막에 넣기)
    }


    // 체인 라벨 생성
    private JLabel createChainLabel() {
        JLabel label = new JLabel("0", SwingConstants.CENTER);
        label.setFont(new Font("Dialog", Font.BOLD, 30));
        label.setForeground(Color.WHITE);
        label.setBounds(808, 115, 50, 50); // 위치와 크기 설정
        return label;
    }

    // 사용자가 입력한 단어를 보여주는 라벨
    private JLabel showInputText() {
        wordLabel = new JLabel("", SwingConstants.CENTER);
        wordLabel.setFont(new Font("Dialog", Font.BOLD, 17));
        wordLabel.setForeground(Color.WHITE);
        wordLabel.setBounds(0, 130, WINDOW_WIDTH, 30); // 프레임 전체 너비를 사용하도록 설정
        wordLabel.setHorizontalAlignment(SwingConstants.CENTER); // 가운데 정렬
        return wordLabel;
    }

    // 사용자들 추가
    private JPanel personPanel(int personNum) {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 250, 1000, 223);
        panel.setOpaque(false); // 투명 배경 설정

        personPanels = new JPanel[personNum]; // 패널 배열 초기화
        scoreLabels = new JLabel[personNum]; // 점수 라벨 배열 초기화

        try {
            output.writeUTF("ACTION=InGameUserList&" + roomId);
            output.flush();

            String receivedData = input.readUTF();
            String[] users = receivedData.split("&");

            for (int i = 1; i < users.length; i++) {
                String name = users[i].split(",")[1];
                // 둥근 모서리 패널 생성
                JPanel person = RoundedPersonPanel.createRoundedPanel(38 + (i-1) * (162 + 34), 0, 162, 223, Color.decode("#D8D8D8"), 10);
                person.setLayout(null);

                // 이미지 라벨의 위치 계산
                int imageLabelSize = 150;
                int imageLabelX = (person.getWidth() - imageLabelSize) / 2;
                int imageLabelY = (person.getHeight() - imageLabelSize) / 2 - 40; // 약간 위쪽으로 조정

                // 이미지 라벨 추가
                JLabel imageLabel = createImageLabel("../image/profile/1.png", imageLabelX, imageLabelY, imageLabelSize, imageLabelSize);
                person.add(imageLabel);

                // ID 라벨 추가
                JLabel idLabel = new JLabel(name, SwingConstants.CENTER);
                idLabel.setFont(new Font("Dialog", Font.BOLD, 15));
                idLabel.setForeground(Color.BLACK);
                int idLabelWidth = 100;
                int idLabelX = (person.getWidth() - idLabelWidth) / 2;
                idLabel.setBounds(idLabelX, 132, idLabelWidth, 20);
                person.add(idLabel);

                // 점수 라벨 추가
                JLabel scoreLabel = new JLabel("00000");
                scoreLabel.setFont(new Font("Dialog", Font.BOLD, 40));
                scoreLabel.setForeground(Color.BLACK);
                scoreLabel.setBounds(16, 161, 162, 40);
                person.add(scoreLabel);

                panel.add(person); // 생성된 패널을 메인 패널에 추가
                personPanels[i-1] = person; // 여기에 추가: 패널을 배열에 저장
                scoreLabels[i-1] = scoreLabel; // 점수 라벨을 배열에 저장
            }


        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
        updatePanelUI(); // 초기 UI 업데이트
        return panel;
    }

    // 패널 UI 업데이트 메서드
    private void updatePanelUI() {
        for (int i = 0; i < personPanels.length; i++) {
            if (i == currentPerson) {
                // 현재 차례인 사람의 패널 스타일 변경
                ((RoundedPanel)personPanels[i]).setStyle(Color.decode("#CEFFDB"), Color.decode("#008C25"), 3);
            } else {
                // 다른 사람의 패널은 기본 스타일로
                ((RoundedPanel)personPanels[i]).setStyle(Color.decode("#D8D8D8"), Color.BLACK, 1);
            }
        }
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
                if (!isReadyStatus) {
                    String inputText = field.getText().trim();
                    // 입력된 단어가 유효하고, 단순히 시작 글자만 입력된 것이 아닌 경우
                    if (!inputText.isEmpty() && isValidWord(inputText) && !inputText.equals(lastWord)) {
                        System.out.println(inputText); // 입력받은 단어 출력
                        chainValue++;
                        chainLabel.setText(String.valueOf(chainValue));

                        int newScore = Integer.parseInt(scoreLabels[currentPerson].getText()) + inputText.length() * 5;
                        scoreLabels[currentPerson].setText(String.format("%05d", newScore));

                        // 단어 라벨 업데이트
                        updateWordLabel(inputText);
                        audio.playAudio("success");

                        // 다음 차례 설정
                        lastWord = inputText.substring(inputText.length() - 1);
                        currentPerson = (currentPerson + 1) % personPanels.length;
                        updatePanelUI();
                    } else {
                        // 유효하지 않은 단어에 대한 처리
                        System.out.println("실패"); // 콘솔에 실패 메시지 출력
                        audio.playAudio("fail"); // 실패 효과음 재생
                    }
                    // 입력 필드 초기화
                    field.setText("");
                    field.requestFocusInWindow();
                }
            }

            // 단어 라벨 위치 업데이트
            private void updateWordLabel(String text) {
                wordLabel.setText(text);
                FontMetrics fm = wordLabel.getFontMetrics(wordLabel.getFont());
                int textWidth = fm.stringWidth(text);
                int labelX = (WINDOW_WIDTH - textWidth) / 2;
                wordLabel.setBounds(labelX, 130, textWidth, 30);
            }

            // 끝말잇기 유효성 검사
            private boolean isValidWord(String word) {
                return word.startsWith(lastWord);
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
                    if (timeLeft > 0) { // 3 2 1 0
                        audio.playAudio("gameStart");
                        audio.playAudio("roundStart");
                        countdownLabel.setText(Integer.toString(timeLeft));
                    } else {
                        isReadyStatus = false;
                        timeLeft = oneRoundTime; // 게임 시간 설정 (30 또는 60초)
                        currentRound++;
                    }
                } else { // 준비 상태가 아닐 때
                    if (timeLeft >= 0) {
                        audio.playAudio("gameNormal");

                        if(timeLeft <= 15) {
                            audio.stopAudio("gameNormal");
                            audio.closeAudio("gameNormal");
                            audio.playAudio("gameFast");
                        }

                        countdownLabel.setText(Integer.toString(timeLeft));
                    } else {
                        if (currentRound < roundNum) {
                            startNewRound(); // 새 라운드 시작
                            audio.stopAudio("gameFast");
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

    // 게임 끝 - 아직은 사용 X
    private void GameOver() {
        System.out.println("게임 끝!");
//        new ReadyToGame(socket);
//        dispose(); // Loading 화면 닫기
    }

    // 뽑힌 단어를 보여주는 라벨 생성
    private JLabel createWord() {
        JLabel wordLabel = new JLabel(selectedWord, SwingConstants.CENTER);
        wordLabel.setFont(new Font("Dialog", Font.BOLD, 20));
        wordLabel.setForeground(Color.WHITE);
        wordLabel.setBounds(448, 68, 100, 30);
        return wordLabel;
    }

    // 라운드 시작 시 호출되는 메서드
    private void startNewRound() {
        chainValue = 0;
        chainLabel.setText("0");
        lastWord = selectedWordArray[currentRound]; // 새로운 시작 글자 설정
        System.out.println(lastWord + "로 시작하는 단어로 게임 시작");
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