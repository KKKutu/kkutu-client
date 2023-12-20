package screen;

//import bgm.MP3Player;
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
import java.util.Objects;

import static component.RoundedMenu.createRoundedPanel;


public class ReadyToGame extends JFrame {

    private Socket socket;
    private DataOutputStream output;
    private DataInputStream input;
    private UpdateThread updateThread;

    private static final Color LINE_COLOR = Color.decode("#DCDCDC");
    private static final Color MENU_PANEL_COLOR = Color.decode("#F7F7F7");
    private static final Color PEOPLE_PANEL_COLOR = Color.decode("#F7F7F7");
    private static final Color PROFILE_PANEL_COLOR = Color.decode("#F7F7F7");
    private static final Color ROOMS_PANEL_COLOR = Color.decode("#F7F7F7");

    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 600;

    private JPanel menuPanel;
    private JPanel peoplePanel;
    private JPanel addPeopleListPanel;
    private JPanel profilePanel;
    private JPanel roomsPanel;
    private JPanel roomLengthPanel;
    private JPanel roomListPanel;
    JScrollPane roomListScrollPane;

    private JPanel userListPanel; // 유저 리스트 패널


    // 생성자에서 UI 설정
    public ReadyToGame(Socket socket) {
        this.socket = socket;
        try {
            output = new DataOutputStream(socket.getOutputStream());
            input = new DataInputStream(socket.getInputStream());

            // 배경음악 틀기
//            SwingUtilities.invokeLater(new Runnable() {
//                @Override
//                public void run() {
//                    new Thread(new MP3Player()).start();
//                }
//            });


            // UpdateThread 시작
            setScreen();

            updateThread = new UpdateThread();
            updateThread.start();


        }catch (IOException e){

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
                            String inputLine = input.readUTF();
                            // 데이터 처리 로직

                            if (inputLine.contains("ACTION")) {
                                System.out.println("Thread :" + inputLine);
                                // 메시지 분석
                                String[] messageParts = inputLine.split("&");
                                String action = messageParts[0].split("=")[1];

                                // 형식 "ACTION=SIGN_UP&USERNAME=newUser&PASSWORD=password123"
                                switch (action) {
                                    case "UpdateUserList": {
                                        // 접속자 리스트 업데이트
                                        SwingUtilities.invokeLater(() -> updateUI(messageParts));

                                        System.out.println("UpdateUserList : " + inputLine);
                                        break;
                                    }
                                    case "UpdateRoomList": {
                                        // 접속자 리스트 업데이트
                                        int roomLength = Integer.parseInt(messageParts[1]);
                                        SwingUtilities.invokeLater(() -> updateRoomList(messageParts, roomLength));
    //                                updateRoomList(messageParts, roomLength);
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
                            // 일정 간격으로 업데이트를 확인하기 위해 스레드 일시 중지
    //                    Thread.sleep(200);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        public void setStopThread(){
            System.out.println("스레드 종료시키기");
            isRunning = false;
            btn = false;
            updateThread.interrupt();
        }

        // UI 업데이트 메서드
        private void updateUI(String[] nameList) {
            // 여기에 UI 업데이트 로직 추가
            // 예를 들어, 기존의 사용자 목록을 지우고 새로운 목록으로 대체하는 등의 로직을 작성
            // ...

            // 사용자 목록을 출력하는 JLabel 업데이트
            userListPanel.removeAll();

            userListPanel.setLayout(new BoxLayout(userListPanel, BoxLayout.Y_AXIS));
            userListPanel.setBackground(Color.decode("#F7F7F7"));


            // 각각의 이름을 분할
            for (int i = 2; i < nameList.length; i++) {
                JLabel label = new JLabel(nameList[i]);

                label.setForeground(Color.BLACK);
                label.setFont(new Font("Dialog", Font.PLAIN, 15));
                userListPanel.add(label);
            }

            System.out.println("리스트 다시 그리기");
            // 패널 다시 그리기
            userListPanel.revalidate();
            userListPanel.repaint();

            // 접속자 수 다시 그리기
            addPeopleListPanel.removeAll();
            // 이미지 라벨 생성 및 추가
            JLabel imageLabel = createImageLabel("../image/readytogame/address.png", 10, 8, 24, 24);
            addPeopleListPanel.add(imageLabel);

            JLabel textLabel = new JLabel("접속자 목록 [" + nameList[1] + "]");
            textLabel.setBounds(40, 8, 200, 24); // 위치와 크기 설정 (이미지 옆)
            textLabel.setForeground(Color.BLACK);
            textLabel.setFont(new Font("Dialog", Font.PLAIN, 15));

            addPeopleListPanel.add(textLabel);
            addPeopleListPanel.revalidate();
            addPeopleListPanel.repaint();
            peoplePanel.add(addPeopleListPanel);
        }

        private void updateRoomList(String[] result, int roomLength) {

            // 방 개수 업데이트 하기
            roomLengthPanel.removeAll();

            // 텍스트 레이블 추가
            JLabel textLabel = new JLabel("방 목록 ["+ roomLength + "]");
            textLabel.setBounds(40, 8, 200, 24); // 위치와 크기 설정 (이미지 옆)
            textLabel.setForeground(Color.BLACK);
            textLabel.setFont(new Font("Dialog", Font.PLAIN, 15));

            roomLengthPanel.add(textLabel);
            roomLengthPanel.revalidate();
            roomLengthPanel.repaint();
            roomsPanel.add(roomLengthPanel);


            // 방 리스트 업데이트하기
            // result 형식 : ACTION=UpdateRoomList&1&0&test&2&3&30초
            // 순서 roomId / title / playerNum / round / time

            roomListPanel.removeAll();

            roomListPanel = new JPanel();
            roomListPanel.setLayout(new BoxLayout(roomListPanel, BoxLayout.Y_AXIS));
            roomListPanel.setBackground(Color.decode("#F7F7F7"));

            String waitRoomImagePath = "../image/room/wait_room.png";
            String playRoomImagePath = "../image/room/play_room.png";

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
                JLabel waitRoomLabel = createImageLabel(imgPath, 0, 0, -1, -1);
                waitRoomLabel.setBorder(new EmptyBorder(10, 5, 10, 5)); // 상, 좌, 하, 우 여백
                rowPanel.add(waitRoomLabel);

                JLabel titleText = new JLabel(title);
                titleText.setBounds(39, 32, 150, 20);
                titleText.setFont(new Font("Dialog", Font.PLAIN, 18));
                waitRoomLabel.add(titleText);

                JLabel numText = new JLabel(playerNum);
                numText.setBounds(272, 34, 100, 20);
                numText.setFont(new Font("Dialog", Font.PLAIN, 10));
                waitRoomLabel.add(numText);

                JLabel roundText = new JLabel(roundAndTime);
                roundText.setBounds(39, 76, 100, 20);
                roundText.setFont(new Font("Dialog", Font.PLAIN, 14));
                waitRoomLabel.add(roundText);
                // Play room 이미지 추가
//                JLabel playRoomLabel = createImageLabel(playRoomImagePath, 0, 0, -1, -1);
//                playRoomLabel.setBorder(new EmptyBorder(10, 5, 10, 5)); // 상, 좌, 하, 우 여백
//                rowPanel.add(playRoomLabel);

                rowPanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {

                        System.out.println("Clicked Room ID: " + roomId);
                        // 상위 프레임 닫기
                        dispose();
                        // UpdateThread 스레드 종료
                        setStopThread();

                        new Room(socket, Long.parseLong(roomId));

                    }
                });

                roomListPanel.add(rowPanel);
            }

            roomListScrollPane.setViewportView(roomListPanel);
            roomListPanel.revalidate();
            roomListPanel.repaint();
            roomsPanel.add(roomListScrollPane);

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
        setTitle("Ready to Game");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(Color.decode("#F7F7F7"));
    }

    // 네 개의 패널
    private void addPanels() {
        menuPanel = createPanel(0, 0, WINDOW_WIDTH, 50, MENU_PANEL_COLOR);
        peoplePanel = createPanel(0, 52, 276, 309, PEOPLE_PANEL_COLOR);
        profilePanel = createPanel(0, 363, 276, 237, PROFILE_PANEL_COLOR);
        roomsPanel = createPanel(278, 52, 724, 546, ROOMS_PANEL_COLOR);
    }

    // 각각 패널 추가
    private JPanel createPanel(int x, int y, int width, int height, Color bgColor) {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(x, y, width, height);
        panel.setBackground(bgColor);
        add(panel);
        return panel;
    }

    // 메뉴 패널에 해당하는 콘텐츠 채우기
    private void addMenuContents() {

        // 메뉴 패널에 들어갈 컴포넌트들 초기화
        JPanel informationPanel = createRoundedPanel(0, 0, 50, 50, Color.decode("#BBBBBB"), 15);
        JPanel settingPanel = createRoundedPanel(50, 0, 50, 50, Color.decode("#CCCCCC"), 15);
        JPanel changeProfilePanel = createRoundedPanel(100, 0, 163, 50, Color.decode("#E3E3E3"), 15);
        JPanel makeRoomPanel = createRoundedPanel(263, 0, 237, 50, Color.decode("#8DC0F2"), 15);

        // 각 패널의 레이아웃 설정
        informationPanel.setLayout(null);
        settingPanel.setLayout(null);
        changeProfilePanel.setLayout(null);
        makeRoomPanel.setLayout(null);

        // 각 패널에 필요한 컨텐츠 추가
        addContentToPanel(informationPanel, "../image/readytogame/information.png", 50, 50);
        addContentToPanel(settingPanel, "../image/readytogame/setting.png", 50, 50);
        addCenteredTextToPanel(changeProfilePanel, "프로필 변경", 20);
        addCenteredTextToPanel(makeRoomPanel, "방 만들기", 20);

        // 메뉴 패널에 컴포넌트들 추가
        menuPanel.add(informationPanel);
        menuPanel.add(settingPanel);
        menuPanel.add(changeProfilePanel);
        menuPanel.add(makeRoomPanel);

        // informationPanel 에 마우스 리스너 추가
        informationPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 게임 소개, 개발자 정보 다이얼로그 보여주기
                System.out.println("게임 소개, 개발자 정보 다이얼로그 보여주기");
//                showInformationDialog();
            }
        });

        // settingPanel 에 마우스 리스너 추가
        settingPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("게임 환경 설정 다이얼로그 보여주기");
                showSettingDialog();
            }
        });

        // changeProfilePanel 에 마우스 리스너 추가
        changeProfilePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showProfileDialog();
            }
        });

        // informationPanel 에 마우스 리스너 추가
        makeRoomPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 다음 화면으로 넘어가기
                System.out.println("다음 화면으로 넘어갑니다");
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
        JLabel imageLabel = createImageLabel("../image/readytogame/address.png", 10, 8, 24, 24);
        addPeopleListPanel.add(imageLabel);

        String userLength;
        // 유저 수 가져오기
        try{
            output.writeUTF("ACTION=UserLength");
            output.flush();
            // List<String> 객체 수신
            String receivedData = input.readUTF();

            if(receivedData.contains("UserLength")){
                // 각각의 이름을 분할
                userLength = receivedData.split("&")[1];
                // 텍스트 레이블 추가
                JLabel textLabel = new JLabel("접속자 목록 [" + userLength + "]");
                textLabel.setBounds(40, 8, 200, 24); // 위치와 크기 설정 (이미지 옆)
                textLabel.setForeground(Color.BLACK);
                textLabel.setFont(new Font("Dialog", Font.PLAIN, 15));

                addPeopleListPanel.add(textLabel);
                peoplePanel.add(addPeopleListPanel);
            }

        } catch (IOException io){
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
        greyPanel.setLayout(null);

        // 이미지 라벨 생성 및 추가
        JLabel imageLabel = createImageLabel("../image/readytogame/user.png", 10, 8, 24, 24);
        greyPanel.add(imageLabel);

        // 텍스트 레이블 추가
        JLabel textLabel = new JLabel("내 정보");
        textLabel.setBounds(40, 8, 200, 24); // 위치와 크기 설정 (이미지 옆)
        textLabel.setForeground(Color.BLACK);
        textLabel.setFont(new Font("Dialog", Font.PLAIN, 15));

        greyPanel.add(textLabel);
        profilePanel.add(greyPanel);
    }

    // 방 목록 타이틀
    private void addRoomListTitle() {
        Color greyPanelColor = Color.decode("#DDDDDD");
        int greyPanelWidth = 700;
        int greyPanelHeight = 40;
        int greyPanelX = 10;
        int greyPanelY = 10;
        roomLengthPanel = RoundedTitle.createRoundedPanel(greyPanelX, greyPanelY, greyPanelWidth, greyPanelHeight, greyPanelColor, 15);
        roomLengthPanel.setLayout(null);

        // 이미지 라벨 생성 및 추가
        JLabel imageLabel = createImageLabel("../image/readytogame/list.png", 10, 8, 24, 24);
        roomLengthPanel.add(imageLabel);

        try {
            output.writeUTF("ACTION=RoomLength");
            output.flush();

            String receivedData = input.readUTF();
            if(receivedData.contains("RoomLength")){
                int roomLength = Integer.parseInt(receivedData.split("&")[1]);
                // 텍스트 레이블 추가
                JLabel textLabel = new JLabel("방 목록 ["+ roomLength + "]");
                textLabel.setBounds(40, 8, 200, 24); // 위치와 크기 설정 (이미지 옆)
                textLabel.setForeground(Color.BLACK);
                textLabel.setFont(new Font("Dialog", Font.PLAIN, 15));

                roomLengthPanel.add(textLabel);
                roomsPanel.add(roomLengthPanel);
            }
        } catch (IOException io){
            System.out.println(io.getMessage());
        }

    }

    // 접속자 리스트 스크롤팬 추가
    private void addPeopleListScrollPane() {
        int panelWidth = 256;
        int panelHeight = 40;
        int panelX = 10;
        int panelY = 10;

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(panelX + 10, panelY + panelHeight + 10, panelWidth - 10, 225);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.decode("#F7F7F7"));

        userListPanel = new JPanel();
        userListPanel.setLayout(new BoxLayout(userListPanel, BoxLayout.Y_AXIS));
        userListPanel.setBackground(Color.decode("#F7F7F7"));


        // 서버에게 리스트 요청
        try{
            output.writeUTF("ACTION=FindAll");
            output.flush();
            // List<String> 객체 수신
            System.out.println("보내기 성공");
            String receivedList = input.readUTF();
            System.out.println("첫번째 이름 리스트" + receivedList);
            // 각각의 이름을 분할
            String[] nameList = receivedList.split("&");
            for (int i = 1; i < nameList.length; i++) {
                JLabel label = new JLabel(nameList[i]);

                label.setForeground(Color.BLACK);
                label.setFont(new Font("Dialog", Font.PLAIN, 15));
                userListPanel.add(label);
            }
        } catch (IOException io){
            System.out.println(io.getMessage());
        }


        scrollPane.setViewportView(userListPanel);
        peoplePanel.add(scrollPane);
    }

    // 방 정보 스크롤팬 추가
    private void addRoomListScrollPane() {
        int panelWidth = 700;
        int panelHeight = 40;
        int panelX = 10;
        int panelY = 10;

        roomListScrollPane = new JScrollPane();
        roomListScrollPane.setBounds(panelX + 10, panelY + panelHeight + 10, panelWidth - 10, 440);
        roomListScrollPane.setBorder(null);
        roomListScrollPane.getViewport().setBackground(Color.decode("#F7F7F7"));

        roomListPanel = new JPanel();
        roomListPanel.setLayout(new BoxLayout(roomListPanel, BoxLayout.Y_AXIS));
        roomListPanel.setBackground(Color.decode("#F7F7F7"));


        String waitRoomImagePath = "../image/room/wait_room.png";
        String playRoomImagePath = "../image/room/play_room.png";

        try {
            output.writeUTF("ACTION=RoomList&");
            output.flush();

            String receivedData = input.readUTF();
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
                    if(isPlaying.equals("false"))
                        imgPath = "../image/room/wait_room.png";
                    else
                        imgPath = "../image/room/play_room.png";

                    JLabel waitRoomLabel = createImageLabel(imgPath, 0, 0, -1, -1);
                    waitRoomLabel.setBorder(new EmptyBorder(10, 5, 10, 5)); // 상, 좌, 하, 우 여백
                    rowPanel.add(waitRoomLabel);

                    JLabel titleText = new JLabel(title);
                    titleText.setBounds(39, 32, 150, 20);
                    titleText.setFont(new Font("Dialog", Font.PLAIN, 18));
                    waitRoomLabel.add(titleText);

                    JLabel numText = new JLabel(playerNum);
                    numText.setBounds(272, 34, 100, 20);
                    numText.setFont(new Font("Dialog", Font.PLAIN, 10));
                    waitRoomLabel.add(numText);

                    JLabel roundText = new JLabel(roundAndTime);
                    roundText.setBounds(39, 76, 100, 20);
                    roundText.setFont(new Font("Dialog", Font.PLAIN, 14));
                    waitRoomLabel.add(roundText);

                    // Play room 이미지 추가
//                    JLabel playRoomLabel = createImageLabel(playRoomImagePath, 0, 0, -1, -1);
//                    playRoomLabel.setBorder(new EmptyBorder(10, 5, 10, 5)); // 상, 좌, 하, 우 여백
//                    rowPanel.add(playRoomLabel);

                    rowPanel.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            System.out.println("Clicked Room ID: " + roomId);
                            // 상위 프레임 닫기
                            dispose();

                            // Room 클래스 실행
                            new Room(socket, Long.parseLong(roomId));

                        }
                    });
                    roomListPanel.add(rowPanel);
                }

                roomListScrollPane.setViewportView(roomListPanel);
                roomsPanel.add(roomListScrollPane);
            }
        } catch (IOException io){
            System.out.println(io.getMessage());
        }

    }

    // 아이디 보여주기
    private void addId() {
        // 서버에게 유저 이름 요청
        try{
            output.writeUTF("ACTION=UserName");
            output.flush();

            String receivedData = input.readUTF();
            if(receivedData.contains("UserName")){

                String userName = receivedData.split("&")[1];
                int fontSize = 15;
                JLabel idLabel = createTextLabel(userName, fontSize);

                // 라벨의 x 위치를 중앙 정렬을 위해 계산
                int labelWidth = idLabel.getPreferredSize().width;
                int xPosition = (profilePanel.getWidth() - labelWidth) / 2;

                // 프로필 제목 아래에 라벨 위치 설정
                int yPosition = 70;

                idLabel.setBounds(xPosition, yPosition, labelWidth, idLabel.getPreferredSize().height);
                profilePanel.add(idLabel);
            }


        } catch (IOException io){
            System.out.println(io.getMessage());
        }

    }

    // 프로필 이미지 보여주기
    private void addProfileImg() {
        String imagePath = "../image/profile/1.png";
        JLabel idLabel = (JLabel) profilePanel.getComponent(profilePanel.getComponentCount()-1); // 마지막에 추가된 컴포넌트(여기서는 idLabel)를 가져옴


        // 이미지의 x 위치와 y 위치 계산
        ImageIcon tempIcon = loadIcon(imagePath);
        int width = tempIcon.getIconWidth();
        int height = tempIcon.getIconHeight();
        int xPosition = (profilePanel.getWidth() - width) / 2;
        int yPosition = idLabel.getY() + idLabel.getHeight() + 20; // 이름 라벨 아래 30픽셀

        JLabel imgLabel = createImageLabel(imagePath, xPosition, yPosition, width, height);
        profilePanel.add(imgLabel);


    }

    // 선을 그리기 위한 패널 추가
    private void addLinePanel() {
        JPanel linePanel = new LinePanel();
        linePanel.setBounds(0, 0, 1000, 600);
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

    // 이미지 아이콘 로딩
    private ImageIcon loadIcon(String path) {
        return new ImageIcon(
                Objects.requireNonNull(getClass().getResource(path))
        );
    }

    // 이미지를 라벨로 (위치 조정)
    private JLabel createImageLabel(String imagePath, int x, int y, int width, int height) {
        JLabel imgLabel = new JLabel();
        ImageIcon icon = loadIcon(imagePath);
        imgLabel.setIcon(icon);
        imgLabel.setBounds(x, y, width, height);
        imgLabel.setLayout(null);  // 레이아웃 매니저 설정
        return imgLabel;
    }

    // 패널에 이미지 추가 (가운데 정렬)
    private void addContentToPanel(JPanel panel, String imagePath, int imgWidth, int imgHeight) {
        JLabel imgLabel = createImageLabel(imagePath, (panel.getWidth() - imgWidth) / 2, (panel.getHeight() - imgHeight) / 2, imgWidth, imgHeight);
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
        label.setFont(new Font("Dialog", Font.PLAIN, fontSize));
        return label;
    }

    // 설정 다이얼로그 표시 메서드
    private void showSettingDialog() {
        JDialog dialog = new JDialog(this, "설정", true); // true로 설정하여 모달로 만듦
        dialog.setLayout(null);
        dialog.setTitle("설정");
        dialog.setSize(300, 220); // 다이얼로그 크기 설정
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null); // 화면 중앙에 위치

        // 텍스트 라벨 생성 및 설정
        JLabel backgroundText = new JLabel("배경음악 조절");
        backgroundText.setBounds(20, 26, 100, 20);
        backgroundText.setFont(new Font("Dialog", Font.PLAIN, 12));

        JLabel effectText = new JLabel("효과음 조절");
        effectText.setBounds(26, 61, 100, 20);
        effectText.setFont(new Font("Dialog", Font.PLAIN, 12));

        JLabel backgroundSelectText = new JLabel("배경음악 선택");
        backgroundSelectText.setBounds(21, 96, 100, 20);
        backgroundSelectText.setFont(new Font("Dialog", Font.PLAIN, 12));

        // 슬라이더 추가
        JSlider backgroundSlider = new JSlider(0, 100, 50); // TODO : 서버로부터 저장된 값 받아오기
        backgroundSlider.setBounds(120, 26, 150, 20);

        JSlider effectSlider = new JSlider(0, 100, 50); // TODO : 서버로부터 저장된 값 받아오기
        effectSlider.setBounds(120, 61, 150, 20);

        // 셀렉트 박스 추가 // TODO : 서버로부터 저장된 값 받아오기
        String[] musicChoices = {"기본", "크리스마스"};
        JComboBox<String> musicSelectBox = new JComboBox<>(musicChoices);
        musicSelectBox.setBounds(120, 98, 150, 20);

        // 저장 버튼
        RoundedButton saveBtn = new RoundedButton("저장", 30); // 두 번째 매개변수는 round 정도
        saveBtn.setBounds(110, 145, 80, 28); // x 좌표, y 좌표, 너비, 높이
        saveBtn.setBackground(Color.decode("#EEEEEE")); // 버튼 배경 색
        saveBtn.setFont(new Font("Dialog", Font.PLAIN, 12)); // 폰트 지정

        // 구성 요소 다이얼로그에 추가
        dialog.add(backgroundText);
        dialog.add(effectText);
        dialog.add(backgroundSelectText);
        dialog.add(backgroundSlider);
        dialog.add(effectSlider);
        dialog.add(musicSelectBox);
        dialog.add(saveBtn);

        // 액션 리스너 추가
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 슬라이더와 콤보박스의 값을 출력
                int backgroundMusicValue = backgroundSlider.getValue();
                int effectSoundValue = effectSlider.getValue();
                String selectedMusic = (String) musicSelectBox.getSelectedItem();

                System.out.println("배경음악 조절: " + backgroundMusicValue);
                System.out.println("효과음 조절: " + effectSoundValue);
                System.out.println("선택된 배경음악: " + selectedMusic);

                // 다이얼로그 닫기
                dialog.dispose();
            }
        });

        dialog.setVisible(true); // 다이얼로그 보이기
    }

    // 프로필 변경 다이얼로그 표시 메서드
    private void showProfileDialog() {
        JDialog dialog = new JDialog(this, "프로필 변경", true); // true로 설정하여 모달로 만듦
        dialog.setLayout(null);
        dialog.setTitle("프로필 변경");
        dialog.setSize(600, 350); // 다이얼로그 크기 설정
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null); // 화면 중앙에 위치
        dialog.setVisible(true); // 다이얼로그 보이기
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
        roomTitleText.setBounds(29, 26, 100, 20);
        roomTitleText.setFont(new Font("Dialog", Font.PLAIN, 12));

        // 플레이어 수 텍스트 라벨
        JLabel playerNumText = new JLabel("플레이어 수");
        playerNumText.setBounds(18, 61, 100, 20);
        playerNumText.setFont(new Font("Dialog", Font.PLAIN, 12));

        // 라운드 수 텍스트 라벨
        JLabel roundNumText = new JLabel("라운드 수");
        roundNumText.setBounds(24, 96, 100, 20);
        roundNumText.setFont(new Font("Dialog", Font.PLAIN, 12));

        // 라운드 시간 텍스트 라벨
        JLabel roundTimeText = new JLabel("라운드 시간");
        roundTimeText.setBounds(18, 131, 100, 20);
        roundTimeText.setFont(new Font("Dialog", Font.PLAIN, 12));

        // 방 제목 입력 필드
        RoundedIdField inputRoomTitle = new RoundedIdField(20, 10);
        inputRoomTitle.setBounds(97, 21, 185, 25);
        dialog.add(inputRoomTitle);

        // 플레이어 수 선택 박스
        String[] playerNumChoices = {"2", "3", "4"}; // 플레이어 수 옵션 (2, 3, 4)
        JComboBox<String> playerNumSelectBox = new JComboBox<>(playerNumChoices);
        playerNumSelectBox.setBounds(93, 60, 190, 30);
        playerNumSelectBox.setSelectedItem("2"); // 디폴트 값 설정
        dialog.add(playerNumSelectBox);

        // 라운드 수 선택 박스
        String[] roundNumChoices = {"2", "3", "4"}; // 라운드 수 옵션 (2, 3, 4)
        JComboBox<String> roundNumSelectBox = new JComboBox<>(roundNumChoices);
        roundNumSelectBox.setBounds(93, 95, 190, 30);
        roundNumSelectBox.setSelectedItem("3"); // 디폴트 값 설정
        dialog.add(roundNumSelectBox);

        // 라운드 시간 선택 박스
        String[] roundTimeChoices = {"30초", "60초"};
        JComboBox<String> roundTimeSelectBox = new JComboBox<>(roundTimeChoices);
        roundTimeSelectBox.setBounds(93, 130, 190, 30);

        // 확인 버튼
        RoundedButton confirmBtn = new RoundedButton("확인", 30); // 두 번째 매개변수는 round 정도
        confirmBtn.setBounds(110, 175, 80, 28); // x 좌표, y 좌표, 너비, 높이
        confirmBtn.setBackground(Color.decode("#EEEEEE")); // 버튼 배경 색
        confirmBtn.setFont(new Font("Dialog", Font.PLAIN, 12)); // 폰트 지정

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

                    output.writeUTF("ACTION=CreateRoom&" + selectedRoomTitle  + "&" + selectedPlayerNum  + "&" + selectedRoundNum  + "&"
                            + selectedRoundTime);
                    output.flush();

                    synchronized (input) {
                        String receivedData = input.readUTF();
                        // 데이터 처리 로직

                        if (receivedData.contains("CreateRoom")) {
                            String roomId = receivedData.split("&")[1];
                            // 다이얼로그 닫기
                            dialog.dispose();

                            // 상위 프레임 닫기
                            dispose();

                            // Room 클래스 실행
                            new Room(socket, Long.parseLong(roomId));
                        }
                    }

//                    new Room(socket, 0L);



                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }


            }
        });

        // 확인 버튼
        dialog.add(confirmBtn);

        dialog.setVisible(true); // 다이얼로그 보이기
    }

}