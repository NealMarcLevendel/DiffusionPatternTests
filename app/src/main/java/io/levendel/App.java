package io.levendel;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import io.levendel.utilities.Color;
import io.levendel.utilities.ColorMap;
import io.levendel.utilities.Point;
import io.levendel.utilities.ColorMapOperations;

public class App {
    static final Point ORIGIN = new Point(0, 0);
    static final Point BOUND = new Point(512,512);

    static ColorMap canvas = new ColorMap();
    static ColorMap changes = new ColorMap();
    public static void main(String[] args) {
        // Setup
        for (int x = ORIGIN.x; x < BOUND.x; x++) {
            for (int y = ORIGIN.y; y < BOUND.y; y++) {
                int rgb = choose(255, 0);
                canvas.setColor(new Point(x, y), new Color(255,rgb,rgb,rgb));
            }
        }

        showImage(ColorMapOperations.to_BufferedImage(canvas, ORIGIN, BOUND));
        startSimulation();
    }
    public static void startSimulation(){
        Thread simulationThread = new Thread(() -> {

            while (true) {

                changes = canvas.copy();

                for (int x = ORIGIN.x; x < BOUND.x; x++) {
                    for (int y = ORIGIN.y; y < BOUND.y; y++) {
                        Point curr = new Point(x, y);
                        int rgb = canvas.getColor(curr).red & 0xFF;

                        if (rgb == 0) blackToWhite(curr);
                    }
                }

                //canvas = changes.copy();
                //updateImage(ColorMapOperations.to_BufferedImage(canvas, ORIGIN, BOUND));

                //changes = canvas.copy();
                
                for (int x = ORIGIN.x; x < BOUND.x; x++) {
                    for (int y = ORIGIN.y; y < BOUND.y; y++) {
                        Point curr = new Point(x, y);
                        int rgb = canvas.getColor(curr).red & 0xFF;

                        if (rgb == 255) whiteToBlack(curr);
                    }
                }

                canvas = changes.copy();
                updateImage(ColorMapOperations.to_BufferedImage(canvas, ORIGIN, BOUND));
                try {
                    Thread.sleep(1L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });

        simulationThread.start();
    }
    public static void blackToWhite(Point curr) {
        boolean left = false;
        boolean right = false;
        boolean top = false;
        boolean bottom = false;

        boolean top_left = false;
        boolean top_right = false;
        boolean bottom_left = false;
        boolean bottom_right = false;

        if (curr.x-1 >= ORIGIN.x &&
            (canvas.getColor(new Point(curr.x-1, curr.y)).red & 0xFF) == 255) left = true;
        if (curr.x+1 < BOUND.x &&
            (canvas.getColor(new Point(curr.x+1, curr.y)).red & 0xFF) == 255) right = true;
        if (curr.y+1 < BOUND.y &&
            (canvas.getColor(new Point(curr.x, curr.y+1)).red & 0xFF) == 255) top = true;
        if (curr.y-1 >= ORIGIN.y &&
            (canvas.getColor(new Point(curr.x, curr.y-1)).red & 0xFF) == 255) bottom = true;

        if (curr.x-1 >= ORIGIN.x && curr.y+1 < BOUND.y &&
            (canvas.getColor(new Point(curr.x-1, curr.y+1)).red & 0xFF) == 255) top_left = true;
        if (curr.x+1 < BOUND.x && curr.y+1 < BOUND.y &&
            (canvas.getColor(new Point(curr.x+1, curr.y+1)).red & 0xFF) == 255) top_right = true;
        if (curr.x-1 >= ORIGIN.x && curr.y-1 >= ORIGIN.y &&
            (canvas.getColor(new Point(curr.x-1, curr.y-1)).red & 0xFF) == 255) top_left = true;
        if (curr.x+1 < BOUND.x && curr.y-1 >= ORIGIN.y &&
            (canvas.getColor(new Point(curr.x+1, curr.y-1)).red & 0xFF) == 255) top_right = true;

        int numSidesWhite = 0;

        if (left) numSidesWhite++;
        if (right) numSidesWhite++;
        if (top) numSidesWhite++;
        if (bottom) numSidesWhite++;

        if (top_left) numSidesWhite++;
        if (top_right) numSidesWhite++;
        if (bottom_left) numSidesWhite++;
        if (bottom_right) numSidesWhite++;

        if (numSidesWhite == 6) { //==3
            changes.setColor(curr, new Color(255,255,255,255));
            //System.out.print("Set");
        }
    }
    public static void whiteToBlack(Point curr) {
        boolean left = false;
        boolean right = false;
        boolean top = false;
        boolean bottom = false;

        boolean top_left = false;
        boolean top_right = false;
        boolean bottom_left = false;
        boolean bottom_right = false;

        if (curr.x-1 >= ORIGIN.x &&
            (canvas.getColor(new Point(curr.x-1, curr.y)).red & 0xFF) == 0) left = true;
        if (curr.x+1 < BOUND.x &&
            (canvas.getColor(new Point(curr.x+1, curr.y)).red & 0xFF) == 0) right = true;
        if (curr.y+1 < BOUND.y &&
            (canvas.getColor(new Point(curr.x, curr.y+1)).red & 0xFF) == 0) top = true;
        if (curr.y-1 >= ORIGIN.y &&
            (canvas.getColor(new Point(curr.x, curr.y-1)).red & 0xFF) == 0) bottom = true;

        if (curr.x-1 >= ORIGIN.x && curr.y+1 < BOUND.y &&
            (canvas.getColor(new Point(curr.x-1, curr.y+1)).red & 0xFF) == 0) top_left = true;
        if (curr.x+1 < BOUND.x && curr.y+1 < BOUND.y &&
            (canvas.getColor(new Point(curr.x+1, curr.y+1)).red & 0xFF) == 0) top_right = true;
        if (curr.x-1 >= ORIGIN.x && curr.y-1 >= ORIGIN.y &&
            (canvas.getColor(new Point(curr.x-1, curr.y-1)).red & 0xFF) == 0) top_left = true;
        if (curr.x+1 < BOUND.x && curr.y-1 >= ORIGIN.y &&
            (canvas.getColor(new Point(curr.x+1, curr.y-1)).red & 0xFF) == 0) top_right = true;

        int numSidesBlack = 0;

        if (left) numSidesBlack++;
        if (right) numSidesBlack++;
        if (top) numSidesBlack++;
        if (bottom) numSidesBlack++;


        if (top_left) numSidesBlack++;
        if (top_right) numSidesBlack++;
        if (bottom_left) numSidesBlack++;
        if (bottom_right) numSidesBlack++;

        if (numSidesBlack > 4) { // >2
            changes.setColor(curr, new Color(255,0,0,0));
            //System.out.print("Set");
        }
    }

    public static int choose(int a, int b) {
        Random random = new Random();
        boolean choice = random.nextBoolean();

        if (choice) return a;
        else return b;
    }


    static JLabel label;

    public static void showImage(BufferedImage image) {
        JFrame frame = new JFrame("Image");

        label = new JLabel();
        frame.add(label);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        updateImage(image);
    }

    public static void updateImage(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        Image scaled = image.getScaledInstance(
            width, height, Image.SCALE_DEFAULT
        );

        label.setIcon(new ImageIcon(scaled));
        label.revalidate();
        label.repaint();
    }
}
