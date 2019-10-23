package de.kaes3kuch3n.raytracer;

import javax.swing.*;
import java.awt.*;

public class View extends JFrame {
    public View() {
        setup();
    }

    private void setup() {
        setSize(800, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void setImage(Image image) {
        setContentPane(new ViewPanel(image));
        pack();
    }

    private static class ViewPanel extends JPanel {
        private Image image;

        ViewPanel(Image image) {
            this.image = image;
            setPreferredSize(new Dimension(image.getWidth(this), image.getHeight(this)));
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            g.drawImage(image, 0, 0, null);
        }
    }
}
