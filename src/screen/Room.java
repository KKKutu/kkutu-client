package screen;

import static component.RoundedMenu.createRoundedPanel;

import component.RoundedTitle;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Room extends JFrame {

    private Socket socket;
    private DataOutputStream output;
    private DataInputStream input;
    private static final Color MENU_PANEL_COLOR = Color.decode("#F7F7F7");
    private static final Color LINE_COLOR = Color.decode("#E0DEDE");
    private static final Color ROOM_INFORMATION_PANEL_COLOR = Color.decode("#F7F7F7");
    private static final Color PEOPLE_PANEL_COLOR = Color.decode("#F7F7F7");
    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 600;

    private JPanel menuPanel;
    private JPanel roomInformationPanel;
    private JPanel peoplePanel;

    // 생성자에서 UI 설정
    public Room(Socket socket) {
        this.socket = socket;
        try {
            output = new DataOutputStream(socket.getOutputStream());
            input = new DataInputStream(socket.getInputStream());
            setScreen();
        } catch (IOException e){

        }

    }

    // 화면 기본 구성
    private void setWindow() {
        setTitle("Room");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(Color.decode("#F7F7F7"));
    }

    // UI 설정
    private void setScreen() {
        setWindow(); // 화면 기본 구성
        addLinePanel(); // 패널 구분선 그리기
        addPanels(); // 메뉴 패널, 방 정보 패널, 사람들 패널 추가

        // TODO : 방장이면 1, 일반 참가자면 2
        addManagerMenuContents(1); // 메뉴 패널의 내용 채우기

        // 방 정보 패널의 내용 채우기
        addRoomInformationContents();

        // 사람을 패널의 내용 채우기
        addPeople();

        setVisible(true); // 해당 프레임 보이게
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 화면 닫으면 프로그램 종료
    }

    // 패널
    private void addPanels() {
        menuPanel = createPanel(0, 0, WINDOW_WIDTH, 50, MENU_PANEL_COLOR);
        roomInformationPanel = createPanel(0, 52, WINDOW_WIDTH, 60, ROOM_INFORMATION_PANEL_COLOR);
        peoplePanel = createPanel(0, 112, WINDOW_WIDTH, WINDOW_HEIGHT - 112, PEOPLE_PANEL_COLOR);
    }

    // 메뉴 패널에 해당하는 콘텐츠 채우기
    private void addManagerMenuContents(int role) {

        // 메뉴 패널에 들어갈 컴포넌트들 초기화
        JPanel informationPanel = createRoundedPanel(0, 0, 50, 50, Color.decode("#E3E3E3"), 15); // 정보 - 방장, 참가자 모두 공통으로 사용
        JPanel managerStartGamePanel = createRoundedPanel(50, 0, 180, 50, Color.decode("#8DC0F2"), 15); // 시작 - 방장이 사용
        JPanel managerOutPanel = createRoundedPanel(230, 0, 180, 50, Color.decode("#FFADAD"), 15); // 나가기 - 방장이 사용
        JPanel participantOutPanel = createRoundedPanel(50, 0, 180, 50, Color.decode("#FFADAD"), 15); // 나가기 - 방장이 사용

        // 각 패널의 레이아웃 설정
        informationPanel.setLayout(null);
        managerStartGamePanel.setLayout(null);
        managerOutPanel.setLayout(null);
        participantOutPanel.setLayout(null);

        // informationPanel 에 마우스 리스너 추가
        informationPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 게임 소개, 개발자 정보 다이얼로그 보여주기
                System.out.println("게임 소개, 개발자 정보 다이얼로그 보여주기");
//                showInformationDialog();
            }
        });

        menuPanel.add(informationPanel);

        // 각 패널에 필요한 컨텐츠 추가
        addContentToPanel(informationPanel, "../image/readytogame/information.png", 50, 50);

        if (role == 1) { // 방장인 경우
            addCenteredTextToPanel(managerStartGamePanel, "시작", 20);
            addCenteredTextToPanel(managerOutPanel, "나가기", 20);

            managerStartGamePanel.addMouseListener(new MouseAdapter() {
                   @Override
                   public void mouseClicked(MouseEvent e) {
                       // TODO : 여기 기능 구현하기
                       System.out.println("방장 : 게임 시작하기");
                       // 상위 프레임 닫기
                       dispose();

                       // Room 클래스 실행
                       new Game(socket);
                   }
               }
            );

            managerOutPanel.addMouseListener(new MouseAdapter() {
                     @Override
                     public void mouseClicked(MouseEvent e) {
                         // TODO : 여기 기능 구현하기
                         System.out.println("방장 : 방 나가기");
                     }
                 }
            );

            menuPanel.add(managerStartGamePanel);
            menuPanel.add(managerOutPanel);
        }

        else { // 참가자인 경우
            addCenteredTextToPanel(participantOutPanel, "나가기", 20);

            participantOutPanel.addMouseListener(new MouseAdapter() {
                     @Override
                     public void mouseClicked(MouseEvent e) {
                         // TODO : 여기 기능 구현하기
                         System.out.println("참가자 : 방 나가기");
                     }
                 }
            );

            menuPanel.add(participantOutPanel);
        }


    }

    // 방 정보 내용
    private void addRoomInformationContents() {
        addRoomInformation();
    }

    // 방 목록 타이틀
    private void addRoomInformation() {

        // 둥근 회색 패널 추가
        Color greyPanelColor = Color.decode("#DDDDDD");
        int greyPanelWidth = 980;
        int greyPanelHeight = 40;
        int greyPanelX = 10;
        int greyPanelY = 10;
        JPanel greyPanel = RoundedTitle.createRoundedPanel(greyPanelX, greyPanelY, greyPanelWidth, greyPanelHeight, greyPanelColor, 15);
        greyPanel.setLayout(null);

        try {
            output.writeUTF("ACTION=RoomInfo&0");
            output.flush();

            String receivedData = input.readUTF();
            if (receivedData.contains("RoomInfo")) {
                String[] result = receivedData.split("&");

                String title = result[2];
                // 방 제목
                JLabel roomTitleTextLabel = new JLabel("[" + title + "]");
                roomTitleTextLabel.setBounds(12, 8, 200, 24); // 위치와 크기 설정 (이미지 옆)
                roomTitleTextLabel.setForeground(Color.BLACK);
                roomTitleTextLabel.setFont(new Font("Dialog", Font.PLAIN, 15));

                String info = result[3];
                // 기타 정보
                JLabel etcTextLabel = new JLabel("한국어    끝말잇기    " + info);
                etcTextLabel.setBounds(620, 8, 400, 24); // 위치와 크기 설정
                etcTextLabel.setForeground(Color.BLACK);
                etcTextLabel.setFont(new Font("Dialog", Font.PLAIN, 15));

                greyPanel.add(roomTitleTextLabel);
                greyPanel.add(etcTextLabel);
                roomInformationPanel.add(greyPanel);
            }

        } catch (IOException io){
            System.out.println(io.getMessage());
        }

    }

    // 사람들 패널
    private void addPeople() {
        // 사람들 추가
        addPersonPanel("가자미", 1, 0);    // 1: 방장
        addPersonPanel("가자미미", 2, 1);  // 2: 참가자
        addPersonPanel("가보자고", 2, 2);  // 2: 참가자
        addPersonPanel("네프끝내자", 2, 3); // 2: 참가자
    }

    private void addPersonPanel(String id, int isManager, int position) {
        int greyPanelX = 23 + (220 + 20) * position;
        int greyPanelY = 10;
        JPanel personPanel = createPersonPanel(greyPanelX, greyPanelY, id, isManager);
        peoplePanel.add(personPanel);
    }

    private JPanel createPersonPanel(int x, int y, String id, int isManager) {
        Color greyPanelColor = Color.decode("#E4E4E4");
        int greyPanelWidth = 220;
        int greyPanelHeight = 170;
        JPanel personPanel = RoundedTitle.createRoundedPanel(x, y, greyPanelWidth, greyPanelHeight, greyPanelColor, 15);
        personPanel.setLayout(null);

        // 이미지 레이블 추가
        JLabel imageLabel = createImageLabel("../image/profile/1.png", 20, 25, 80, 67);
        personPanel.add(imageLabel);

        // 텍스트 레이블 추가
        boolean isLeader = isManager == 1;
        JLabel textLabel = new JLabel(isLeader ? "방장" : "준비");
        textLabel.setForeground(isLeader ? Color.RED : Color.BLUE);
        textLabel.setFont(new Font("Dialog", Font.BOLD, 20));
        textLabel.setBounds(160, 20, 40, 24);
        personPanel.add(textLabel);

        // ID 레이블 추가
        JLabel idLabel = new JLabel(id);
        idLabel.setForeground(Color.BLACK);
        idLabel.setFont(new Font("Dialog", Font.PLAIN, 18));
        idLabel.setBounds(25, 127, 100, 24);
        personPanel.add(idLabel);

        return personPanel;
    }

    // TODO : 나중에 여기 하위 메소드 코드 리팩토링 시 util 패키지에 넣고 static 클래스로 만들기

    // 각각 패널 추가
    private JPanel createPanel(int x, int y, int width, int height, Color bgColor) {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(x, y, width, height);
        panel.setBackground(bgColor);
        add(panel);
        return panel;
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

    // 이미지 아이콘 로딩
    private ImageIcon loadIcon(String path) {
        return new ImageIcon(
                Objects.requireNonNull(getClass().getResource(path))
        );
    }

    // 선을 그리기 위한 패널 추가
    private void addLinePanel() {
        JPanel linePanel = new Room.LinePanel();
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
        }
    }

}
