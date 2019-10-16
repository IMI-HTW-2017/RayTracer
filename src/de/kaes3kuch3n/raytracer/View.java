package de.kaes3kuch3n.raytracer;

import javax.swing.*;

public class View extends JFrame {
    public View() {
        setup();
    }

    private void setup() {
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
}
