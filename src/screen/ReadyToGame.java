package screen;

import static component.RoundedMenu.createRoundedPanel;

import component.RoundedTitle;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class ReadyToGame extends JFrame {

    private static final Color LINE_COLOR = Color.decode("#DCDCDC");
    private static final Color MENU_PANEL_COLOR = Color.decode("#F7F7F7");
    private static final Color PEOPLE_PANEL_COLOR = Color.decode("#F7F7F7");
    private static final Color PROFILE_PANEL_COLOR = Color.decode("#F7F7F7");
    private static final Color ROOMS_PANEL_COLOR = Color.decode("#F7F7F7");

    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 600;

    private JPanel menuPanel;
    private JPanel peoplePanel;
    private JPanel profilePanel;
    private JPanel roomsPanel;

    // 생성자에서 UI 설정
    public ReadyToGame() {
        setScreen();
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
//                showSettingDialog();
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
        JPanel greyPanel = RoundedTitle.createRoundedPanel(greyPanelX, greyPanelY, greyPanelWidth, greyPanelHeight, greyPanelColor, 15);
        greyPanel.setLayout(null);

        // 이미지 라벨 생성 및 추가
        JLabel imageLabel = createImageLabel("../image/readytogame/address.png", 10, 8, 24, 24);
        greyPanel.add(imageLabel);

        // 텍스트 레이블 추가
        JLabel textLabel = new JLabel("접속자 목록 [15명]"); // TODO : 여기 서버한테 받아와야 함
        textLabel.setBounds(40, 8, 200, 24); // 위치와 크기 설정 (이미지 옆)
        textLabel.setForeground(Color.BLACK);
        textLabel.setFont(new Font("Dialog", Font.PLAIN, 15));

        greyPanel.add(textLabel);
        peoplePanel.add(greyPanel);
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
        JPanel greyPanel = RoundedTitle.createRoundedPanel(greyPanelX, greyPanelY, greyPanelWidth, greyPanelHeight, greyPanelColor, 15);
        greyPanel.setLayout(null);

        // 이미지 라벨 생성 및 추가
        JLabel imageLabel = createImageLabel("../image/readytogame/list.png", 10, 8, 24, 24);
        greyPanel.add(imageLabel);

        // 텍스트 레이블 추가
        JLabel textLabel = new JLabel("방 목록 [34개]"); // TODO : 여기 서버한테 받아와야 함
        textLabel.setBounds(40, 8, 200, 24); // 위치와 크기 설정 (이미지 옆)
        textLabel.setForeground(Color.BLACK);
        textLabel.setFont(new Font("Dialog", Font.PLAIN, 15));

        greyPanel.add(textLabel);
        roomsPanel.add(greyPanel);
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

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.decode("#F7F7F7"));

        // TODO : 여기 서버한테 받아와야 함 (배열로 저장해서 출력하기)
        for (int i = 0; i < 15; i++) {
            JLabel label = new JLabel("Item " + (i + 1));
            label.setForeground(Color.BLACK);
            label.setFont(new Font("Dialog", Font.PLAIN, 15));
            contentPanel.add(label);
        }

        scrollPane.setViewportView(contentPanel);
        peoplePanel.add(scrollPane);
    }

    // 방 정보 스크롤팬 추가
    private void addRoomListScrollPane() {
        int panelWidth = 700;
        int panelHeight = 40;
        int panelX = 10;
        int panelY = 10;

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(panelX + 10, panelY + panelHeight + 10, panelWidth - 10, 440);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.decode("#F7F7F7"));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.decode("#F7F7F7"));

        String waitRoomImagePath = "../image/room/wait_room.png";
        String playRoomImagePath = "../image/room/play_room.png";

        // 20개의 각 이미지를 추가
        for (int i = 0; i < 20; i++) {
            JPanel rowPanel = new JPanel();
            rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
            rowPanel.setBackground(Color.decode("#F7F7F7"));

            // Wait room 이미지 추가
            JLabel waitRoomLabel = createImageLabel(waitRoomImagePath, 0, 0, -1, -1);
            waitRoomLabel.setBorder(new EmptyBorder(10, 5, 10, 5)); // 상, 좌, 하, 우 여백
            rowPanel.add(waitRoomLabel);

            // Play room 이미지 추가
            JLabel playRoomLabel = createImageLabel(playRoomImagePath, 0, 0, -1, -1);
            playRoomLabel.setBorder(new EmptyBorder(10, 5, 10, 5)); // 상, 좌, 하, 우 여백
            rowPanel.add(playRoomLabel);

            contentPanel.add(rowPanel);
        }

        scrollPane.setViewportView(contentPanel);
        roomsPanel.add(scrollPane);
    }

    // 아이디 보여주기
    private void addId() {
        String userName = "가자미가자미가자미"; // TODO : 서버에서 받아오거나 여기서(클라이언트 프로프램에서) 사용자 정보를 저장하는 클래스에서 가져오기
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

    // 프로필 이미지 보여주기
    private void addProfileImg() {
        String imagePath = "../image/profile/1.png";
        JLabel idLabel = (JLabel) profilePanel.getComponent(profilePanel.getComponentCount() - 1); // 마지막에 추가된 컴포넌트(여기서는 idLabel)를 가져옴

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

}