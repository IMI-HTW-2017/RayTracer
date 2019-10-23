package de.kaes3kuch3n.raytracer.display;

import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {
    private Image image;

    public ImagePanel(Image image) {
        this.image = image;
        setPreferredSize(new Dimension(image.getWidth(this), image.getHeight(this)));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(image, 0, 0, null);
    }
}
