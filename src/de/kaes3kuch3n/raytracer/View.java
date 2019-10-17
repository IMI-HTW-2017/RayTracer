package de.kaes3kuch3n.raytracer;

import javax.swing.*;
import java.awt.*;

public class View extends JFrame {
    private Image image;
    public View() {
        setup();
    }

    private void setup() {
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(image, 0, 0, null);
    }
}
