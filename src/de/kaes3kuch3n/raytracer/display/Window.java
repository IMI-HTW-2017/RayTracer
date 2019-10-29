package de.kaes3kuch3n.raytracer.display;

import javax.swing.*;
import java.awt.*;

public class Window {
    private JFrame window;

    /**
     * Create a new window instance with a fixed inner width and height
     * @param width  The width of the window's content pane
     * @param height The height of the window's content pane
     */
    public Window(int width, int height) {
        window = new JFrame("Ray Tracer");
        window.setSize(width, height);
        window.getContentPane().setSize(width, height);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setVisible(true);
    }

    /**
     * Set an image for the window to display
     * @param imagePanel The image panel containing the image to show
     */
    public void setImage(ImagePanel imagePanel) {
        window.setContentPane(imagePanel);
        window.pack();
    }

    /**
     * Get the current inner size of the window
     * @return The window's inner size
     */
    public Dimension getSize() {
        return window.getContentPane().getSize();
    }

}
