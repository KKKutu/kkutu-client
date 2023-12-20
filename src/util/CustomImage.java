package util;

import java.util.Objects;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

// 전역으로 사용할 이미지와 관련된 함수 정의
public class CustomImage {

    // 지정된 이미지 파일을 사용하여 라벨을 생성하고 구성
    public static JLabel createImageLabel(String imagePath, int x, int y, int width, int height) {
        JLabel imgLabel = new JLabel();
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(CustomImage.class.getResource(imagePath)));
        imgLabel.setIcon(icon); // 라벨에 아이콘 설정
        imgLabel.setBounds(x, y, width, height); // 라벨의 위치와 크기 설정
        imgLabel.setHorizontalAlignment(JLabel.CENTER); // 라벨 내의 이미지 정렬 설정
        return imgLabel; // 설정된 라벨 반환
    }

}
