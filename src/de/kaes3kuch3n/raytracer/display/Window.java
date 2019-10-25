package de.kaes3kuch3n.raytracer.display;

import javax.swing.*;
import java.awt.*;

public class Window {
    private JFrame window;

    public Window(int width, int height) {
        window = new JFrame("Ray Tracer");
        window.setSize(width, height);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setVisible(true);
    }

    public void setImage(ImagePanel imagePanel) {
        window.setContentPane(imagePanel);
        window.pack();
    }

    public Dimension getSize() {
        return window.getSize();
    }
}