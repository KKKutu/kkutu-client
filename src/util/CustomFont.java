package util;

import java.awt.Font;

// 전역으로 사용할 폰트 정의
public class CustomFont {

    // 폰트의 기본 설정
    private static final String FONT_NAME = "Dialog";

    // BOLD 는 고정, 폰트 크기는 매개변수로 받음
    public static Font getBoldFont(int size) {
        return new Font(FONT_NAME, Font.BOLD, size);
    }

    // PLAIN 는 고정, 폰트 크기는 매개변수로 받음
    public static Font getPlainFont(int size) {
        return new Font(FONT_NAME, Font.PLAIN, size);
    }

}
