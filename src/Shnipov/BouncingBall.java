package Shnipov;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class BouncingBall implements Runnable {
    private static final int MAX_RADIUS = 40;
    private static final int MIN_RADIUS = 5;
    private static final int MAX_SPEED = 15;

    private Field field;
    private int radius;
    private Color color;
    private boolean check;

    private double x;
    private double y;

    private int speed;
    private double speedX;
    private double speedY;

    public BouncingBall(Field field) {

        this.field = field;
        radius = new Double(Math.random() * (MAX_RADIUS - MIN_RADIUS)).intValue() + MIN_RADIUS;


        speed = new Double(Math.round(5 * MAX_SPEED / radius)).intValue();
        if (speed > MAX_SPEED) {
            speed = MAX_SPEED;
        }
        // Начальное направление случайно, угол в пределах от 0 до 2PI
        double angle = Math.random() * 2 * Math.PI;
        if (angle >=0 && angle <= Math.PI/2)
            check = true;
        else check = false;

        speedX = 3 * Math.cos(angle);
        speedY = 3 * Math.sin(angle);

        color = new Color((float) Math.random(), (float) Math.random(), (float) Math.random());


        x = Math.random() * (field.getSize().getWidth() - 2 * radius) + radius;
        y = Math.random() * (field.getSize().getHeight() - 2 * radius) + radius;

        Thread thisThread = new Thread(this);

        thisThread.start();
    }

    public void paint(Graphics2D canvas) {
        canvas.setColor(color);
        canvas.setPaint(color);
        Ellipse2D.Double ball = new Ellipse2D.Double(x-radius, y-radius, 2*radius, 2*radius);

        canvas.draw(ball);
        canvas.fill(ball);
    }

    public void run() {
        try {
            while (true) {
                // Синхронизация потоков делается на самом объекте поля // Если движение разрешено - управление будет возвращено // В противном случае - активный поток заснѐт
                field.canMove(this);
                if (x + speedX <= radius) {
                    speedX = -speedX;
                    x = radius;
                } else if (x + speedX >= field.getWidth() - radius) {
                    speedX = -speedX;
                    x = new Double(field.getWidth() - radius).intValue();
                } else if (y + speedY <= radius) {
                    speedY = -speedY;
                    y = radius;
                } else if (y + speedY >= field.getHeight() - radius) {
                    speedY = -speedY;
                    y = new Double(field.getHeight() - radius).intValue();
                } else {
                    x += speedX;
                    y += speedY;
                }
                // Засыпаем на X миллисекунд, где X определяется исходя из
                // скорости: Скорость = 1 (медленно), засыпаем на 15мс. // Скорость = 15 (быстро), засыпаем на 1 мс.
                Thread.sleep(16 - speed);
            }
        } catch (InterruptedException ex) {
        // Если нас прервали, то ничего не делаем и просто выходим
        }
    }
    public boolean getCheck(){
        double angle = Math.asin(speedX/ speedY);
        if (angle >=0 && angle <= Math.PI/2)
            check = true;
        else check = false;
        return check;
    }
}
