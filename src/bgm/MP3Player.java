//package bgm;
//
//import java.io.*;
//import java.net.URISyntaxException;
//import javax.sound.sampled.*;
//
//import com.sun.tools.javac.Main;
//import javazoom.jl.decoder.JavaLayerException;
//import javazoom.jl.player.Player;
//import javazoom.jl.player.Player;
//
//public class MP3Player extends Thread{
//    private Clip clip;
//    private boolean isLoop;
//
//    private File file;
//
//    private FileInputStream fis;
//    private Player player;
//    private BufferedInputStream bis;
//
//    public MP3Player() {
//
//        try {
//            String filename = "LobbyBGM_Winter_2023.mp3";
//            file = new File(MP3Player.class.getResource(filename).toURI());
//
//            fis = new FileInputStream(file);
//
//            bis = new BufferedInputStream(fis);
//
//            player = new Player(bis);
//
//        }  catch (JavaLayerException | FileNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (URISyntaxException e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//
//    public void close(){
//
//        isLoop = false;
//
//        player.close();
//
//        this.interrupt();  //음악을 종료하는 인터럽터
//
//    }
//
//    @Override
//    public void run(){
//
//        try{
//            do{
//                player.play();
//                fis = new FileInputStream(file);
//                bis = new BufferedInputStream(fis);
//                player = new Player(bis);
//
//            } while(isLoop);
//
//        } catch(Exception e){
//
//        }
//    }
//
//
//}
