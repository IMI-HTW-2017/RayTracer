package de.kaes3kuch3n.raytracer.display;

import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {
    private Image image;

    /**
     * Create a new image panel
     *
     * @param image The image the panel is supposed to display
     */
    public ImagePanel(Image image) {
        this.image = image;
        setPreferredSize(new Dimension(image.getWidth(this), image.getHeight(this)));
    }

    /**
     * Updates the image of the ImagePanel, using "repaint"
     *
     * @param image The new image for the ImagePanel
     */
    public void updateImage(Image image) {
        this.image = image;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
    }
}

