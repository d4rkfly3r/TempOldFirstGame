package net.mrblockplacer.FirstGame;

import net.mrblockplacer.FirstGame.graphics.Screen;
import net.mrblockplacer.FirstGame.input.Controller;
import net.mrblockplacer.FirstGame.input.InputHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Display extends Canvas implements Runnable {
    public static final int WIDTH = (int) (Toolkit.getDefaultToolkit().getScreenSize().width * .666);
    public static final int HEIGHT = (int) (Toolkit.getDefaultToolkit().getScreenSize().height * .666);
    public static final String TITLE = "Joshua's Game Pre-Alpha 0.0.1.2";
    private static final long serialVersionUID = 1L;
    public static boolean F7 = false;
    public static boolean abcde = false;
    private Thread thread;
    private boolean running = false;
    private Screen screen;
    private BufferedImage img;
    private Game game;
    private int[] pixels;
    private InputHandler input;
    private int OldX = 0;
    private int NewX = 0;
    private int fps = 0;

    public Display() {
        Dimension size = new Dimension(WIDTH, HEIGHT);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        screen = new Screen(WIDTH, HEIGHT);
        game = new Game();
        img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();

        input = new InputHandler();
        addKeyListener(input);
        addFocusListener(input);
        addMouseListener(input);
        addMouseMotionListener(input);
    }

    /**
     * THIS IS THE MAIN METHOD
     **/
    public static void main(String[] args) {
        Display game = new Display();
        JFrame frame = new JFrame();
        BufferedImage cursorBlank = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor blank = Toolkit.getDefaultToolkit().createCustomCursor(cursorBlank, new Point(0, 0), "blank");
        frame.getContentPane().setCursor(blank);
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setTitle(TITLE);
        frame.setResizable(false);
        frame.setVisible(true);

        System.out.println("Running...");

        game.start();
    }

    /**
     * THIS IS THE START METHOD
     **/
    public synchronized void start() {
        if (running)
            return;
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    /**
     * THIS IS THE STOP METHOD::::THIS IS ONLY FOR APPLETS
     **/
    public synchronized void stop() {
        if (!running)
            return;
        running = false;
        try {
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * THIS IS THE RUN METHOD::::THIS IS FOR THREADS
     **/
    public void run() {
        int frames = 0;
        double unprocessedSeconds = 0;
        long previousTime = System.nanoTime();
        double secondsPerTick = 1 / 60.0;
        int tickCount = 0;
        boolean ticked = false;

        while (running) {
            long currentTime = System.nanoTime();
            long passedTime = currentTime - previousTime;
            previousTime = currentTime;
            unprocessedSeconds += passedTime / 1000000000.0;
            requestFocus();

            while (unprocessedSeconds > secondsPerTick) {
                tick();
                unprocessedSeconds -= secondsPerTick;
                ticked = true;
                tickCount++;
                if (tickCount % 60 == 0) {
                    // System.out.println(frames + " fps");
                    fps = frames;
                    previousTime += 1000.0;
                    frames = 0;
                }
            }
            if (ticked) {
                render();
                frames++;
            }
            render();
            frames++;
            NewX = InputHandler.MouseX;

            if (NewX == OldX) {
                Controller.turnLeft = false;
                Controller.turnRight = false;
            }
            if (NewX > OldX) {
                Controller.turnRight = true;

            }
            if (NewX < OldX) {
                Controller.turnLeft = true;

            }
            OldX = NewX;
        }
    }

    private void tick() {
        game.tick(input.key);
    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        screen.render(game);

        for (int i = 0; i < WIDTH * HEIGHT; i++) {
            pixels[i] = screen.pixels[i];
        }

        Graphics g = bs.getDrawGraphics();
        g.drawImage(img, 0, 0, WIDTH + 10, HEIGHT + 10, null);

        g.setFont(new Font("Veranda", 0, 45));
        g.setColor(Color.YELLOW);
        g.drawString("FPS: " + fps, WIDTH - 175, 50);

        g.dispose();
        bs.show();

    }
}

/**
 * Made by JOSHUA F
 **/
