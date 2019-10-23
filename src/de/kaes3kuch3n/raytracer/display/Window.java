package de.kaes3kuch3n.raytracer.display;

import javax.swing.*;
import java.awt.*;

public class Window {
    private JFrame window;

    public Window() {
        window = new JFrame("Ray Tracer");
        window.setSize(800, 800);
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
