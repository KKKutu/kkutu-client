package screen;

import static component.RoundedMenu.createRoundedPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ReadyToGame extends JFrame {

    private static final Color LINE_COLOR = Color.decode("#DCDCDC");
    private static final Color MENU_PANEL_COLOR = Color.decode("#F7F7F7");
    private static final Color PEOPLE_PANEL_COLOR = Color.BLACK;
    private static final Color PROFILE_PANEL_COLOR = Color.RED;
    private static final Color ROOMS_PANEL_COLOR = Color.GRAY;

    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 600;

    private JPanel menuPanel;
    private JPanel peoplePanel;
    private JPanel profilePanel;
    private JPanel roomsPanel;

    public ReadyToGame() {
        setScreen();
    }

    public void setScreen() {
        setWindow(); // 화면 기본 구성
        addPanels(); // 네 개의 패널 부착
        addLinePanel(); // 패널 구분선 그리기
        addMenuContents(); // 메뉴 패널의 내용 채우기

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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