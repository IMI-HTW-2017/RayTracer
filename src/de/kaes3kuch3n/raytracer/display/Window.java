package de.kaes3kuch3n.raytracer.display;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.function.Consumer;

public class Window {
    private JFrame window;

    public Window(int width, int height) {
        window = new JFrame("Ray Tracer");
        window.setSize(width, height);
        window.getContentPane().setSize(width, height);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setVisible(true);
    }

    public void setImage(ImagePanel imagePanel) {
        window.setContentPane(imagePanel);
        window.pack();
    }

    public Dimension getSize() {
        return window.getContentPane().getSize();
    }

    public void addResizeListener(Consumer<Dimension> resizeAction) {
        window.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                JFrame frame = (JFrame) e.getComponent();
                resizeAction.accept(frame.getContentPane().getSize());
            }
        });
    }
}
