package main;

import entity.Player;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    // SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16 tile
    final int scale = 3;

    public final int tileSize = originalTileSize * scale; // 48x48 tile
    public final int maxScreenCol = 16;
    public final int maxScreenrow = 12;
    public final int screenWidth = tileSize * maxScreenCol; // 768 pixel (48x16)
    public final int screenHeight = tileSize * maxScreenrow;// 576 pixel (48x12)


    // FPS
    int FPS = 60;

    TileManager tileM = new TileManager(this);
    KeyHandler KeyH = new KeyHandler();
    Thread gameThread;
    Player player = new Player(this, KeyH);


    // set player's default position
    int playerX = 100;
    int playerY = 100;
    int pLayerSpeed = 4;


    public GamePanel() {

     this.setPreferredSize(new Dimension(screenWidth, screenHeight));
     this.setBackground(Color.WHITE);
     this.setDoubleBuffered(true);
     this.addKeyListener(KeyH);
     this.setFocusable(true);
    }

    public void startGameThread() {

        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        double drawInterval = 1000000000/FPS; // 0.01666 seconds
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {

            update();

            repaint();


            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime/1000000;

                if(remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);

                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
    public void update() {

        player.update();
    }
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        // 1st will be 1st layer
        tileM.Draw(g2);
        player.draw(g2);

        g2.dispose();
    }
}
