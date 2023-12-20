package screen;

import component.RoundedButton;
import component.RoundedIdField;
import component.RoundedTitle;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.Socket;
import util.Audio;
import util.CustomFont;
import util.CustomImage;

import static component.RoundedMenu.createRoundedPanel;

public class ReadyToGame extends JFrame {

    // 통신에 필요
    private Socket socket;
    private DataOutputStream output;
    private DataInputStream input;

    // 스레드
    private UpdateThread updateThread;

    // 화면 구성에 필요
    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 600;
    private static final Color LINE_COLOR = Color.decode("#DCDCDC"); // 선 색
    private static final Color PANEL_COLOR = Color.decode("#F7F7F7"); // 패널 색

    private JPanel menuPanel;
    private JPanel peoplePanel;
    private JPanel addPeopleListPanel;
    private JPanel profilePanel;
    private JPanel roomsPanel;
    private JPanel roomLengthPanel;
    private JPanel roomListPanel;
    JScrollPane roomListScrollPane;

    private JPanel userListPanel; // 유저 리스트 패널

    // 오디오
    public Audio audio = new Audio();

    // 생성자에서 기본 설정
    public ReadyToGame(Socket socket) { // 소켓 받기
        audio.playAudio("lobby"); // 로비 음악 재생
        this.socket = socket; // 소켓 연결
        try {
            output = new DataOutputStream(socket.getOutputStream()); // 출력 스트림 생성
            input = new DataInputStream(socket.getInputStream()); // 입력 스트림 생성
            setScreen(); // UI 설정

            // UpdateThread 시작
            updateThread = new UpdateThread();
            updateThread.start();
        } catch (IOException io) { // I/O 에러 예외 처리
            System.out.println(io.getMessage());
        }
    }

    // 생성자에서 기본 설정
    public ReadyToGame(Socket socket, Thread thread) { // 소켓 받기
        thread.interrupt();
        audio.playAudio("lobby"); // 로비 음악 재생
        this.socket = socket; // 소켓 연결
        try {
            output = new DataOutputStream(socket.getOutputStream()); // 출력 스트림 생성
            input = new DataInputStream(socket.getInputStream()); // 입력 스트림 생성
            setScreen(); // UI 설정

            // UpdateThread 시작
            updateThread = new UpdateThread();
            updateThread.start();
        } catch (IOException io) { // I/O 에러 예외 처리
            System.out.println(io.getMessage());
        }
    }

    // 새로운 스레드 클래스 추가
    private class UpdateThread extends Thread {
        volatile boolean isRunning = true;
        volatile boolean btn = true;

        @Override
        public void run() {
            while (!Thread.interrupted() && isRunning) {
                try {

                    // 서버에 업데이트 요청
                    // UI 업데이트를 Swing 스레드에서 실행
                    if(!isRunning)
                        break;

                    synchronized (input) {
                        if (input.available() > 0 && btn) { // 데이터가 도착했는지 확인
                            String inputLine = input.readUTF();  // 서버에서 데이터 받기

                            if (inputLine.contains("ACTION")) {
                                System.out.println("Thread :" + inputLine);
                                // 메시지 분석
                                String[] messageParts = inputLine.split("&");
                                String action = messageParts[0].split("=")[1];

                                // 형식 "ACTION=SIGN_UP&USERNAME=newUser&PASSWORD=password123"
                                switch (action) {
                                    case "UpdateUserList": { // 접속자 리스트 업데이트
                                        SwingUtilities.invokeLater(() -> updateUI(messageParts));
                                        System.out.println("UpdateUserList : " + inputLine);
                                        break;
                                    }
                                    case "UpdateRoomList": { // 접속자 리스트 업데이트
                                        int roomLength = Integer.parseInt(messageParts[1]);
                                        SwingUtilities.invokeLater(() -> updateRoomList(messageParts, roomLength));
                                        System.out.println("UpdateRoomList : " + inputLine);
                                        break;
                                    }
                                    case "RoomInfo": {
                                        System.out.println("RoomInfo : " + inputLine);
                                        break;
                                    }
                                }
                            }
                            System.out.println("test InputLine : " + inputLine);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        // 스레드 종료
        public void setStopThread(){
            System.out.println("스레드 종료시키기");
            isRunning = false;
            btn = false;
            updateThread.interrupt();
        }

        // UI 업데이트 메서드
        private void updateUI(String[] nameList) {

            // 사용자 목록을 출력하는 JLabel 업데이트
            userListPanel.removeAll();
            userListPanel.setLayout(new BoxLayout(userListPanel, BoxLayout.Y_AXIS));
            userListPanel.setBackground(Color.decode("#F7F7F7"));

            // 각각의 이름을 분할
            for (int i = 2; i < nameList.length; i++) {
                JLabel label = new JLabel(nameList[i]);
                label.setForeground(Color.BLACK);
                label.setFont(CustomFont.getPlainFont(15));
                userListPanel.add(label);
            }

            System.out.println("리스트 다시 그리기");

            // 패널 다시 그리기
            userListPanel.revalidate();
            userListPanel.repaint();

            // 접속자 수 다시 그리기
            addPeopleListPanel.removeAll();

            // 이미지 라벨 생성 및 추가
            JLabel imageLabel = CustomImage.createImageLabel("../image/readytogame/address.png", 10, 8, 24, 24);
            addPeopleListPanel.add(imageLabel);

            JLabel textLabel = new JLabel("접속자 목록 [" + nameList[1] + "]");
            textLabel.setBounds(40, 8, 200, 24); // x 위치, y 위치, 넓이, 길이
            textLabel.setForeground(Color.BLACK); // 글씨 색
            textLabel.setFont(CustomFont.getPlainFont(15)); // 폰트 설정
            addPeopleListPanel.add(textLabel); // 부착

            // 다시 그리기
            addPeopleListPanel.revalidate();
            addPeopleListPanel.repaint();

            peoplePanel.add(addPeopleListPanel); // 부착
        }

        private void updateRoomList(String[] result, int roomLength) {

            // 방 개수 업데이트 하기
            roomLengthPanel.removeAll();

            // 텍스트 레이블 추가
            JLabel textLabel = new JLabel("방 목록 ["+ roomLength + "]");
            textLabel.setBounds(40, 8, 200, 24); // x 위치, y 위치, 넓이, 길이
            textLabel.setForeground(Color.BLACK); // 글씨 색
            textLabel.setFont(CustomFont.getPlainFont(15)); // 폰트 설정

            roomLengthPanel.add(textLabel); // 부착

            // 다시 그리기
            roomLengthPanel.revalidate();
            roomLengthPanel.repaint();

            roomsPanel.add(roomLengthPanel); // 부착

            // 방 리스트 업데이트하기
            // result 형식 : ACTION=UpdateRoomList&1&0&test&2&3&30초
            // 순서 roomId / title / playerNum / round / time

            roomListPanel.removeAll();

            roomListPanel = new JPanel();
            roomListPanel.setLayout(new BoxLayout(roomListPanel, BoxLayout.Y_AXIS));
            roomListPanel.setBackground(Color.decode("#F7F7F7"));

            // 방 리스트 업데이트하기
            // result 형식 : ACTION=UpdateRoomList&1&0&test&2&3&30초
            // 순서 roomId / title / playerNum / round / time
            // 20개의 각 이미지를 추가

            for (int i = 2; i < result.length; i++) {

                String[] roomInfo = result[i].split(",");
                String roomId = roomInfo[0];
                String title = roomInfo[1];
                String playerNum = roomInfo[2] + " / "  + roomInfo[3];
                String roundAndTime = "라운드 " + roomInfo[4] + " / " + roomInfo[5] + "초";
                String isPlaying = roomInfo[6];

                JPanel rowPanel = new JPanel();
                rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
                rowPanel.setBackground(Color.decode("#F7F7F7"));

                String imgPath;

                // Wait room 이미지 추가
                if(isPlaying.equals("false"))
                    imgPath = "../image/room/wait_room.png";
                else
                    imgPath = "../image/room/play_room.png";

                // Wait room 이미지 추가
                JLabel waitRoomLabel = CustomImage.createImageLabel(imgPath, 0, 0, -1, -1);
                waitRoomLabel.setBorder(new EmptyBorder(10, 5, 10, 5)); // 상, 좌, 하, 우 여백
                rowPanel.add(waitRoomLabel);

                JLabel titleText = new JLabel(title);
                titleText.setBounds(39, 32, 150, 20); // x 위치, y 위치, 넓이, 길이
                titleText.setFont(CustomFont.getPlainFont(18)); // 폰트 설정
                waitRoomLabel.add(titleText); // 부착

                JLabel numText = new JLabel(playerNum);
                numText.setBounds(272, 34, 100, 20); // x 위치, y 위치, 넓이, 길이
                numText.setFont(CustomFont.getPlainFont(10)); // 폰트 설정
                waitRoomLabel.add(numText); // 부착

                JLabel roundText = new JLabel(roundAndTime);
                roundText.setBounds(39, 76, 100, 20); // x 위치, y 위치, 넓이, 길이
                roundText.setFont(CustomFont.getPlainFont(14)); // 폰트 설정
                waitRoomLabel.add(roundText); // 부착

                rowPanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        System.out.println("Clicked Room ID: " + roomId);
                        dispose(); // 상위 프레임 닫기

                        synchronized (updateThread){ // UpdateThread 스레드 종료
                            updateThread.setStopThread();
                        }

                        // 방 생성
                        new Room(socket, Long.parseLong(roomId), audio, updateThread);
                    }
                });

                roomListPanel.add(rowPanel); // 부착
            }

            roomListScrollPane.setViewportView(roomListPanel);

            // 다시 그리기
            roomListPanel.revalidate();
            roomListPanel.repaint();

            roomsPanel.add(roomListScrollPane); // 부착

        }
    }

    // UI 설정
    public void setScreen() {
        setWindow(); // 화면 기본 구성
        addPanels(); // 네 개의 패널 부착
        addLinePanel(); // 패널 구분선 그리기
        addMenuContents(); // 메뉴 패널의 내용 채우기
        addPeopleContents(); // 접속자 패널의 내용 채우기
        addProfileContents(); // 내 프로필 패널의 내용 채우기
        addRoomsContents(); // 방 정보 패널의 내용 채우기
        setVisible(true); // 해당 프레임 보이게
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 화면 닫으면 프로그램 종료
    }

    // 화면 기본 구성
    private void setWindow() {
        setTitle("로비"); // 프레임의 제목 설정
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT); // 프레임 크기 설정
        setResizable(false); // 사용자가 화면 크기 변경 불가
        setLocationRelativeTo(null); // 해당 프레임을 화면의 중앙에 위치
        setLayout(null); // 컴포넌트들의 절대 위치 설정을 위해 레이아웃 매니저 비활성화
        getContentPane().setBackground(Color.decode("#F7F7F7")); // 프레임의 배경색
    }

    // 네 개의 패널
    private void addPanels() {
        menuPanel = createPanel(0, 0, WINDOW_WIDTH, 50, PANEL_COLOR);
        peoplePanel = createPanel(0, 52, 276, 309, PANEL_COLOR);
        profilePanel = createPanel(0, 363, 276, 237, PANEL_COLOR);
        roomsPanel = createPanel(278, 52, 724, 546, PANEL_COLOR);
    }

    // 각각 패널 추가
    private JPanel createPanel(int x, int y, int width, int height, Color bgColor) {
        JPanel panel = new JPanel(); // 패널 생성
        panel.setLayout(null); // 컴포넌트들의 절대 위치 설정을 위해 레이아웃 매니저 비활성화
        panel.setBounds(x, y, width, height); // x 위치, y 위치, 넓이, 길이
        panel.setBackground(bgColor); // 패널의 배경색
        add(panel); // 부착
        return panel;
    }

    // 메뉴 패널에 해당하는 콘텐츠 채우기
    private void addMenuContents() {

        // 메뉴 패널에 들어갈 컴포넌트들 초기화
        JPanel informationPanel = createRoundedPanel(0, 0, 50, 50, Color.decode("#BBBBBB"), 15);

        JPanel makeRoomPanel = createRoundedPanel(50, 0, 237, 50, Color.decode("#8DC0F2"), 15);

        // 컴포넌트들의 절대 위치 설정을 위해 레이아웃 매니저 비활성화
        informationPanel.setLayout(null);
        makeRoomPanel.setLayout(null);

        // 각 패널에 필요한 컨텐츠 추가
        addContentToPanel(informationPanel, "../image/readytogame/information.png", 50, 50);
        addCenteredTextToPanel(makeRoomPanel, "방 만들기", 20);

        // 메뉴 패널에 컴포넌트들 추가
        menuPanel.add(informationPanel);
        menuPanel.add(makeRoomPanel);

        // informationPanel 에 마우스 리스너 추가
        informationPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 게임 소개, 개발자 정보 다이얼로그 보여주기
                showInformationDialog();
            }
        });

        // informationPanel 에 마우스 리스너 추가
        makeRoomPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("방 생성됨");
                makeRoomDialog();
            }
        });
    }

    // 접속자 패널에 해당하는 콘텐츠 내용 채우기
    private void addPeopleContents() {
        addPeopleListTitle();
        addPeopleListScrollPane();
    }

    // 방 정보 패널에 해당하는 콘텐츠 내용 채우기
    private void addRoomsContents() {
        addRoomListTitle();
        addRoomListScrollPane();
    }

    // 내 프로필 패널에 해당하는 콘텐츠 내용 채우기
    private void addProfileContents() {
        addProfileTitle();
        addId();
        addProfileImg();
    }

    // 접속자 목록 타이틀
    private void addPeopleListTitle() {
        Color greyPanelColor = Color.decode("#DDDDDD");
        int greyPanelWidth = 256;
        int greyPanelHeight = 40;
        int greyPanelX = 10;
        int greyPanelY = 10;
        addPeopleListPanel = RoundedTitle.createRoundedPanel(greyPanelX, greyPanelY, greyPanelWidth, greyPanelHeight, greyPanelColor, 15);
        addPeopleListPanel.setLayout(null);

        // 이미지 라벨 생성 및 추가
        JLabel imageLabel = CustomImage.createImageLabel("../image/readytogame/address.png", 10, 8, 24, 24);
        addPeopleListPanel.add(imageLabel);

        // 유저 수 가져오기
        String userLength;

        try {
            output.writeUTF("ACTION=UserLength"); // 서버에 메시지 전송
            output.flush(); // 출력 스트림의 데이터 모두 전송 - 데이터 전송 보장

            // List<String> 객체 수신
            String receivedData = input.readUTF(); // 서버에서 데이터 받기

            if(receivedData.contains("UserLength")){

                // 각각의 이름을 분할
                userLength = receivedData.split("&")[1];

                // 텍스트 라벨 추가
                JLabel textLabel = new JLabel("접속자 목록 [" + userLength + "]");
                textLabel.setBounds(40, 8, 200, 24); // x 위치, y 위치, 넓이, 길이
                textLabel.setForeground(Color.BLACK); // 글씨색
                textLabel.setFont(CustomFont.getPlainFont(15)); // 폰트 설정

                addPeopleListPanel.add(textLabel); // 부착
                peoplePanel.add(addPeopleListPanel); // 부착
            }

        } catch (IOException io) { // I/O 에러 예외 처리
            System.out.println(io.getMessage());
        }

    }

    // 내 프로필 타이틀
    private void addProfileTitle() {
        Color greyPanelColor = Color.decode("#DDDDDD");
        int greyPanelWidth = 256;
        int greyPanelHeight = 40;
        int greyPanelX = 10;
        int greyPanelY = 10;
        JPanel greyPanel = RoundedTitle.createRoundedPanel(greyPanelX, greyPanelY, greyPanelWidth, greyPanelHeight, greyPanelColor, 15);
        greyPanel.setLayout(null); // 컴포넌트들의 절대 위치 설정을 위해 레이아웃 매니저 비활성화

        // 이미지 라벨
        JLabel imageLabel = CustomImage.createImageLabel("../image/readytogame/user.png", 10, 8, 24, 24);
        greyPanel.add(imageLabel); // 부착

        // 텍스트 레이블
        JLabel textLabel = new JLabel("내 정보");
        textLabel.setBounds(40, 8, 200, 24); // x 위치, y 위치, 넓이, 길이
        textLabel.setForeground(Color.BLACK); // 글씨색
        textLabel.setFont(CustomFont.getPlainFont(15)); // 폰트 설정

        greyPanel.add(textLabel); // 부착
        profilePanel.add(greyPanel); // 부착
    }

    // 방 목록 타이틀
    private void addRoomListTitle() {
        Color greyPanelColor = Color.decode("#DDDDDD");
        int greyPanelWidth = 700;
        int greyPanelHeight = 40;
        int greyPanelX = 10;
        int greyPanelY = 10;
        roomLengthPanel = RoundedTitle.createRoundedPanel(greyPanelX, greyPanelY, greyPanelWidth, greyPanelHeight, greyPanelColor, 15);
        roomLengthPanel.setLayout(null); // 컴포넌트들의 절대 위치 설정을 위해 레이아웃 매니저 비활성화

        // 이미지 라벨
        JLabel imageLabel = CustomImage.createImageLabel("../image/readytogame/list.png", 10, 8, 24, 24);
        roomLengthPanel.add(imageLabel); // 부착

        try {
            output.writeUTF("ACTION=RoomLength"); // 서버에 메시지 전송
            output.flush(); // 출력 스트림의 데이터 모두 전송 - 데이터 전송 보장

            synchronized (input){

                String receivedData = input.readUTF(); // 서버에서 데이터 받기

                if(receivedData.contains("RoomLength")){
                    int roomLength = Integer.parseInt(receivedData.split("&")[1]);

                    // 텍스트 라벨  추가
                    JLabel textLabel = new JLabel("방 목록 ["+ roomLength + "]");
                    textLabel.setBounds(40, 8, 200, 24); // x 위치, y 위치, 넓이, 길이
                    textLabel.setForeground(Color.BLACK); // 글씨색
                    textLabel.setFont(CustomFont.getPlainFont(15)); // 폰트 설정

                    roomLengthPanel.add(textLabel); // 부착
                    roomsPanel.add(roomLengthPanel); // 부착
                }
            }

        } catch (IOException io) { // I/O 에러 예외 처리
            System.out.println(io.getMessage());
        }

    }

    // 접속자 리스트 스크롤팬 추가
    private void addPeopleListScrollPane() {
        int panelWidth = 256;
        int panelHeight = 40;
        int panelX = 10;
        int panelY = 10;

        JScrollPane scrollPane = new JScrollPane(); // 스크롤팬 생성
        scrollPane.setBounds(panelX + 10, panelY + panelHeight + 10, panelWidth - 10, 225); // x 위치, y 위치, 넓이, 길이
        scrollPane.setBorder(null); // 컴포넌트들의 절대 위치 설정을 위해 레이아웃 매니저 비활성화
        scrollPane.getViewport().setBackground(Color.decode("#F7F7F7")); // 배경색

        userListPanel = new JPanel();
        userListPanel.setLayout(new BoxLayout(userListPanel, BoxLayout.Y_AXIS));
        userListPanel.setBackground(Color.decode("#F7F7F7"));


        // 서버에게 리스트 요청
        try {
            output.writeUTF("ACTION=FindAll"); // 서버에 메시지 전송
            output.flush(); // 출력 스트림의 데이터 모두 전송 - 데이터 전송 보장

            // List<String> 객체 수신
            System.out.println("보내기 성공");

            String receivedList = input.readUTF(); // 서버에서 데이터 받기
            System.out.println("첫번째 이름 리스트" + receivedList);

            // 각각의 이름을 분할
            String[] nameList = receivedList.split("&");
            for (int i = 1; i < nameList.length; i++) {
                JLabel label = new JLabel(nameList[i]);

                label.setForeground(Color.BLACK); // 글씨색
                label.setFont(CustomFont.getPlainFont(15)); // 폰트 설정
                userListPanel.add(label); // 부착
            }
        } catch (IOException io) { // I/O 에러 예외 처리
            System.out.println(io.getMessage());
        }

        scrollPane.setViewportView(userListPanel);
        peoplePanel.add(scrollPane); // 부착
    }

    // 방 정보 스크롤팬 추가
    private void addRoomListScrollPane() {
        int panelWidth = 700;
        int panelHeight = 40;
        int panelX = 10;
        int panelY = 10;

        roomListScrollPane = new JScrollPane(); // 스크롤팬 생성
        roomListScrollPane.setBounds(panelX + 10, panelY + panelHeight + 10, panelWidth - 10, 440); // x 위치, y 위치, 넓이, 길이
        roomListScrollPane.setBorder(null); // 선 안보이게
        roomListScrollPane.getViewport().setBackground(Color.decode("#F7F7F7")); // 배경색

        roomListPanel = new JPanel();
        roomListPanel.setLayout(new BoxLayout(roomListPanel, BoxLayout.Y_AXIS));
        roomListPanel.setBackground(Color.decode("#F7F7F7"));

        try {
            output.writeUTF("ACTION=RoomList&"); // 서버에 메시지 전송
            output.flush(); // 출력 스트림의 데이터 모두 전송 - 데이터 전송 보장

            String receivedData = input.readUTF(); // 서버에서 데이터 받기
            System.out.println(receivedData);
            if(receivedData.contains("RoomList")){

                // 방 리스트 업데이트하기
                // result 형식 : ACTION=UpdateRoomList&1&0&test&2&3&30초
                // 순서 roomId / title / playerNum / round / time
                // 20개의 각 이미지를 추가

                String[] result = receivedData.split("&");

                for (int i = 2; i < result.length; i++) {

                    String[] roomInfo = result[i].split(",");
                    String roomId = roomInfo[0];
                    String title = roomInfo[1];
                    String playerNum = roomInfo[2] + " / "  + roomInfo[3];
                    String roundAndTime = "라운드 " + roomInfo[4] + " / " + roomInfo[5] + "초";
                    String isPlaying = roomInfo[6];

                    JPanel rowPanel = new JPanel();
                    rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
                    rowPanel.setBackground(Color.decode("#F7F7F7"));


                    String imgPath;
                    // Wait room 이미지 추가
                    if(isPlaying.equals("false")) imgPath = "../image/room/wait_room.png";
                    else imgPath = "../image/room/play_room.png";

                    JLabel waitRoomLabel = CustomImage.createImageLabel(imgPath, 0, 0, -1, -1);
                    waitRoomLabel.setBorder(new EmptyBorder(10, 5, 10, 5)); // 상, 좌, 하, 우 여백
                    rowPanel.add(waitRoomLabel);

                    JLabel titleText = new JLabel(title);
                    titleText.setBounds(39, 32, 150, 20); // x 위치, y 위치, 넓이, 길이
                    titleText.setFont(CustomFont.getPlainFont(18));
                    waitRoomLabel.add(titleText);

                    JLabel numText = new JLabel(playerNum);
                    numText.setBounds(272, 34, 100, 20); // x 위치, y 위치, 넓이, 길이
                    numText.setFont(CustomFont.getPlainFont(10));
                    waitRoomLabel.add(numText);

                    JLabel roundText = new JLabel(roundAndTime);
                    roundText.setBounds(39, 76, 100, 20); // x 위치, y 위치, 넓이, 길이
                    roundText.setFont(CustomFont.getPlainFont(14));
                    waitRoomLabel.add(roundText);

                    rowPanel.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            System.out.println("Clicked Room ID: " + roomId);
                            // 상위 프레임 닫기
                            dispose();

                            // Room 클래스 실행
                            new Room(socket, Long.parseLong(roomId), audio, updateThread);

                        }
                    });
                    roomListPanel.add(rowPanel);
                }

                roomListScrollPane.setViewportView(roomListPanel);
                roomsPanel.add(roomListScrollPane);
            }
        } catch (IOException io) { // I/O 에러 예외 처리
            System.out.println(io.getMessage());
        }

    }

    // 아이디 보여주기
    private void addId() {
        // 서버에게 유저 이름 요청
        try{
            output.writeUTF("ACTION=UserName"); // 서버에 메시지 전송
            output.flush(); // 출력 스트림의 데이터 모두 전송 - 데이터 전송 보장

            String receivedData = input.readUTF(); // 서버에서 데이터 받기
            System.out.println("유저 이름 가져와지는거 확인" + receivedData);
            if(receivedData.contains("UserName")){

                String userName = receivedData.split("&")[1];
                System.out.println("유저이름 : " + userName);
                int fontSize = 15;
                JLabel idLabel = createTextLabel(userName, fontSize);

                // 라벨의 x 위치를 중앙 정렬을 위해 계산
                int labelWidth = idLabel.getPreferredSize().width;
                int xPosition = (profilePanel.getWidth() - labelWidth) / 2;

                // 프로필 제목 아래에 라벨 위치 설정
                int yPosition = 70;

                idLabel.setBounds(xPosition, yPosition, labelWidth, idLabel.getPreferredSize().height); // x 위치, y 위치, 넓이, 길이
                profilePanel.add(idLabel);
            }


        } catch (IOException io) { // I/O 에러 예외 처리
            System.out.println(io.getMessage());
        }

    }

    // 프로필 이미지 보여주기
    private void addProfileImg() {
        String imagePath = "../image/profile/1.png";

        // 이미지의 x 위치와 y 위치 계산
        int xPosition = (profilePanel.getWidth() - 80) / 2;
        int yPosition = 100; // 이름 라벨 아래 30픽셀

        JLabel imgLabel = CustomImage.createImageLabel(imagePath, xPosition, yPosition, 80, 68);
        profilePanel.add(imgLabel);
    }

    // 선을 그리기 위한 패널 추가
    private void addLinePanel() {
        JPanel linePanel = new LinePanel();
        linePanel.setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT); // x 위치, y 위치, 넓이, 길이
        linePanel.setOpaque(false);
        add(linePanel);
    }

    // 선 그리기
    private static class LinePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(LINE_COLOR);
            g.fillRect(0, 50, 1000, 2);
            g.fillRect(10, 361, 256, 2);
            g.fillRect(276, 59, 2, 530);
        }
    }

    // 패널에 이미지 추가 (가운데 정렬)
    private void addContentToPanel(JPanel panel, String imagePath, int imgWidth, int imgHeight) {
        JLabel imgLabel = CustomImage.createImageLabel(imagePath, (panel.getWidth() - imgWidth) / 2, (panel.getHeight() - imgHeight) / 2, imgWidth, imgHeight);
        panel.add(imgLabel);
    }

    // 패널에 텍스트 추가 (가운데 정렬)
    private void addCenteredTextToPanel(JPanel panel, String text, int fontSize) {
        JLabel textLabel = createTextLabel(text, fontSize);
        textLabel.setSize(panel.getWidth(), panel.getHeight());
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setVerticalAlignment(JLabel.CENTER);
        panel.add(textLabel);
    }

    // 텍스트 라벨 생성 (크기 조정 없음)
    private JLabel createTextLabel(String text, int fontSize) {
        JLabel label = new JLabel(text);
        label.setFont(CustomFont.getPlainFont(fontSize));
        return label;
    }

    private void makeRoomDialog() {
        JDialog dialog = new JDialog(this, "방 만들기", true); // true로 설정하여 모달로 만듦
        dialog.setLayout(null);
        dialog.setTitle("방 만들기");
        dialog.setSize(300, 250); // 다이얼로그 크기 설정
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null); // 화면 중앙에 위치

        // 방 제목 텍스트 라벨
        JLabel roomTitleText = new JLabel("방 제목");
        roomTitleText.setBounds(29, 26, 100, 20); // x 위치, y 위치, 넓이, 길이
        roomTitleText.setFont(CustomFont.getPlainFont(12)); // 폰트 설정

        // 플레이어 수 텍스트 라벨
        JLabel playerNumText = new JLabel("플레이어 수");
        playerNumText.setBounds(18, 61, 100, 20); // x 위치, y 위치, 넓이, 길이
        playerNumText.setFont(CustomFont.getPlainFont(12)); // 폰트 설정

        // 라운드 수 텍스트 라벨
        JLabel roundNumText = new JLabel("라운드 수");
        roundNumText.setBounds(24, 96, 100, 20); // x 위치, y 위치, 넓이, 길이
        roundNumText.setFont(CustomFont.getPlainFont(12)); // 폰트 설정

        // 라운드 시간 텍스트 라벨
        JLabel roundTimeText = new JLabel("라운드 시간");
        roundTimeText.setBounds(18, 131, 100, 20); // x 위치, y 위치, 넓이, 길이
        roundTimeText.setFont(CustomFont.getPlainFont(12)); // 폰트 설정

        // 방 제목 입력 필드
        RoundedIdField inputRoomTitle = new RoundedIdField(20, 10);
        inputRoomTitle.setBounds(97, 21, 185, 25); // x 위치, y 위치, 넓이, 길이
        dialog.add(inputRoomTitle); // 부착

        // 플레이어 수 선택 박스
        String[] playerNumChoices = {"2", "3", "4"}; // 플레이어 수 옵션 (2, 3, 4)
        JComboBox<String> playerNumSelectBox = new JComboBox<>(playerNumChoices);
        playerNumSelectBox.setBounds(93, 60, 190, 30); // x 위치, y 위치, 넓이, 길이
        playerNumSelectBox.setSelectedItem("2"); // 디폴트 값 설정
        dialog.add(playerNumSelectBox); // 부착

        // 라운드 수 선택 박스
        String[] roundNumChoices = {"2", "3", "4"}; // 라운드 수 옵션 (2, 3, 4)
        JComboBox<String> roundNumSelectBox = new JComboBox<>(roundNumChoices);
        roundNumSelectBox.setBounds(93, 95, 190, 30); // x 위치, y 위치, 넓이, 길이
        roundNumSelectBox.setSelectedItem("3"); // 디폴트 값 설정
        dialog.add(roundNumSelectBox); // 부착

        // 라운드 시간 선택 박스
        String[] roundTimeChoices = {"30초", "60초"};
        JComboBox<String> roundTimeSelectBox = new JComboBox<>(roundTimeChoices);
        roundTimeSelectBox.setBounds(93, 130, 190, 30); // x 위치, y 위치, 넓이, 길이

        // 확인 버튼
        RoundedButton confirmBtn = new RoundedButton("확인", 30); // 두 번째 매개변수는 round 정도
        confirmBtn.setBounds(110, 175, 80, 28); // x 좌표, y 좌표, 너비, 높이
        confirmBtn.setBackground(Color.decode("#EEEEEE")); // 버튼 배경색
        confirmBtn.setFont(CustomFont.getPlainFont(12)); // 폰트 설정

        // 구성 요소 다이얼로그에 추가
        dialog.add(roomTitleText); // 방 제목
        dialog.add(playerNumText); // 플레이어 수
        dialog.add(roundNumText); // 라운드 수

        // 라운드 시간
        dialog.add(roundTimeText);
        dialog.add(roundTimeSelectBox);

        // 확인 버튼에 대한 ActionListener 추가
        confirmBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 입력받은 값들을 콘솔에 출력
                String selectedRoomTitle = inputRoomTitle.getText();
                String selectedPlayerNum = (String) playerNumSelectBox.getSelectedItem();
                String selectedRoundNum = (String) roundNumSelectBox.getSelectedItem();
                String selectedRoundTime = (String) roundTimeSelectBox.getSelectedItem();

                System.out.println("방 제목: " + selectedRoomTitle);
                System.out.println("플레이어 수: " + selectedPlayerNum);
                System.out.println("라운드 수: " + selectedRoundNum);
                System.out.println("라운드 시간: " + selectedRoundTime);

                try {
                    System.out.println("updateThread=interrupt");

                    updateThread.setStopThread();

                    output.writeUTF("ACTION=CreateRoom&" + selectedRoomTitle  + "&" + selectedPlayerNum  + "&" + selectedRoundNum  + "&" // 서버에 메시지 전송
                            + selectedRoundTime); // 서버에 메시지 전송
                    output.flush(); // 출력 스트림의 데이터 모두 전송 - 데이터 전송 보장

                    synchronized (input) {
                        String receivedData = input.readUTF(); // 서버에서 데이터 받기

                        if (receivedData.contains("CreateRoom")) {
                            String roomId = receivedData.split("&")[1];

                            dialog.dispose(); // 다이얼로그 닫기
                            dispose(); // 상위 프레임 닫기

                            // Room 클래스 실행
                            new Room(socket, Long.parseLong(roomId), audio, updateThread);
                        }
                    }

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        // 확인 버튼
        dialog.add(confirmBtn); // 부착
        dialog.setVisible(true); // 다이얼로그 보이기
    }

    // 정보 보기 클릭 시 다이얼로그
    private void showInformationDialog() {
        JDialog dialog = new JDialog(ReadyToGame.this, "개발자 정보 보기", true); // true로 설정하여 모달로 만듦
        dialog.setLayout(null); // 컴포넌트들의 절대 위치 설정을 위해 레이아웃 매니저 비활성화
        dialog.setTitle("개발자 정보 보기"); // 다이얼로그 이름
        dialog.setSize(300, 220); // 다이얼로그 크기 설정
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null); // 화면 중앙에 위치

        // 강민서 라벨
        JLabel label1 = new JLabel("강민서");
        label1.setFont(CustomFont.getPlainFont(12)); // 폰트 설정
        label1.setBounds(185, 35, 100, 30); // x, y, 넓이, 높이
        dialog.add(label1); // 부착

        // 김태하 라벨
        JLabel label2 = new JLabel("김태하");
        label2.setFont(CustomFont.getPlainFont(12)); // 폰트 설정
        label2.setBounds(185, 119, 100, 30); // x, y, 넓이, 높이
        dialog.add(label2); // 부착

        // 강민서 이미지
        JLabel minseoImg = CustomImage.createImageLabel("../image/profile/minseo.png", 90, 16, 70, 70); // 적절한 넓이, 높이 지정
        dialog.add(minseoImg);

        // 김태하 이미지
        JLabel taeImg = CustomImage.createImageLabel("../image/profile/taeha.png", 90, 100, 70, 70); // 적절한 넓이, 높이 지정
        dialog.add(taeImg); // 부착

        dialog.setVisible(true); // 다이얼로그 보이기
    }

}