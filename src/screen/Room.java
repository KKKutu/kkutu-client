package screen;

import static component.RoundedMenu.createRoundedPanel;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Room extends JFrame {

    private static final Color MENU_PANEL_COLOR = Color.decode("#000000");
    private static final Color INFORMATION_PANEL_COLOR = Color.decode("#DCDCDC");

    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 600;

    private JPanel menuPanel;
    private JPanel informationPanel;

    // 생성자에서 UI 설정
    public Room() {
        setScreen();
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
        addPanels();
        addMenuContents(); // 메뉴 패널의 내용 채우기
        setVisible(true); // 해당 프레임 보이게
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 화면 닫으면 프로그램 종료
    }

    // 패널
    private void addPanels() {
        menuPanel = createPanel(0, 0, WINDOW_WIDTH, 50, MENU_PANEL_COLOR);
        informationPanel = createPanel(0, 52, 276, 309, INFORMATION_PANEL_COLOR); // 여기 수정해야됨
    }

    // 메뉴 패널에 해당하는 콘텐츠 채우기
    private void addMenuContents() {

        // 메뉴 패널에 들어갈 컴포넌트들 초기화
        JPanel startGamePanel = createRoundedPanel(0, 0, 180, 50, Color.decode("#73D07A"), 15); // 시작
        JPanel outPanel = createRoundedPanel(180, 0, 180, 50, Color.decode("#FFADAD"), 15); // 나가기

        // 각 패널의 레이아웃 설정
        startGamePanel.setLayout(null);
        outPanel.setLayout(null);

        // 각 패널에 필요한 컨텐츠 추가
        addCenteredTextToPanel(startGamePanel, "시작", 20);
        addCenteredTextToPanel(outPanel, "나가기", 20);

        // 메뉴 패널에 컴포넌트들 추가
        menuPanel.add(startGamePanel);
        menuPanel.add(outPanel);
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

}
