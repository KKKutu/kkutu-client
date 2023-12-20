package util;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Audio {

    private Clip lobbyClip;
    private Clip gameStartClip;
    private Clip roundStartClip;
    private Clip gameNormalClip;
    private Clip gameFastClip;
    private Clip successClip;
    private Clip failClip;

    // 생성자
    public Audio() {
        lobbyClip = getClip("src/audio/lobby.wav");
        gameStartClip = getClip("src/audio/game_start.wav");
        roundStartClip = getClip("src/audio/round_start.wav");
        gameNormalClip = getClip("src/audio/game_normal.wav");
        gameFastClip = getClip("src/audio/game_fast.wav");
        successClip = getClip("src/audio/success.wav");
        failClip = getClip("src/audio/fail.wav");
    }

    // 오디오 클립 가져오기
    private Clip getClip(String filePath) {

        Clip clip = null;

        try {
            clip = AudioSystem.getClip();
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            clip.open(audioStream);
        }
        catch (LineUnavailableException e) { e.printStackTrace(); }
        catch (UnsupportedAudioFileException e) { e.printStackTrace(); }
        catch (IOException e) { e.printStackTrace(); }

        return clip;
    }

    // 오디오 재생
    public void playAudio(String name) {

        switch(name) {
            case "lobby":
                lobbyClip.start();
                lobbyClip.loop(Clip.LOOP_CONTINUOUSLY);  // 클립을 무한 반복 재생
                break;
            case "gameStart":
//                gameStartClip.setFramePosition(0);
                gameStartClip.start();
                waitForClipToEnd(gameStartClip);
                break;
            case "roundStart":
                roundStartClip.start();
                break;
            case "gameNormal":
                gameNormalClip.start();
                break;
            case "gameFast":
                gameFastClip.start();
                break;
            case "success":
                successClip.setFramePosition(0);
                successClip.start();
                break;
            case "fail":
                failClip.setFramePosition(0);
                failClip.start();
                break;
        }

    }

    // 오디오 정지
    public void stopAudio(String name) {
        switch(name) {
            case "lobby":
                lobbyClip.stop();
                break;
            case "gameStart":
                gameStartClip.stop();
                break;
            case "roundStart":
                roundStartClip.stop();
                break;
            case "gameNormal":
                gameNormalClip.stop();
                break;
            case "gameFast":
                gameFastClip.stop();
                break;
            case "success":
                successClip.stop();
                break;
            case "fail":
                failClip.stop();
                break;
        }
    }

    // 오디오 종료
    public void closeAudio(String name) {
        switch(name) {
            case "lobby":
                lobbyClip.close();
                break;
            case "gameStart":
                gameStartClip.close();
                break;
            case "roundStart":
                roundStartClip.close();
                break;
            case "gameNormal":
                gameNormalClip.close();
                break;
            case "gameFast":
                gameFastClip.close();
                break;
            case "success":
                successClip.close();
                break;
            case "fail":
                failClip.close();
                break;

        }
    }

    // 클립이 끝날 때까지 기다리는 메소드
    private void waitForClipToEnd(Clip clip) {
        while (clip.getMicrosecondPosition() < clip.getMicrosecondLength()) {
            try {
                Thread.sleep(20); // 100 밀리초마다 체크
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}