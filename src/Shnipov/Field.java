package Shnipov;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Field extends JPanel {
    private boolean paused;
    private boolean myPause;
    private ArrayList<BouncingBall> balls = new ArrayList<BouncingBall>(15);

    //таймер прорисовки
    private Timer repaintTimer = new Timer(10, new ActionListener() {
        public void actionPerformed(ActionEvent ev) {
            repaint();
        }
    });

    public Field() {
        setBackground(Color.WHITE);
        repaintTimer.start();
    }
    public void addBall(){

        balls.add(new BouncingBall(this));
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D canvas = (Graphics2D) g;
        for (BouncingBall ball: balls) {
            ball.paint(canvas);
        }
    }

    public synchronized void canMove(BouncingBall ball) throws InterruptedException {
        if (paused) {
            wait();
        }
        if (myPause) {
            if (ball.getCheck()) {
                wait();
            }
        }

    }
    public synchronized void pause(){
        myPause = false;
        paused = true;
    }
    public synchronized void resume() {
        paused = false;
        myPause = false;
        notifyAll();
    }
    public synchronized void mypause(){
        myPause = true;
    }

}
