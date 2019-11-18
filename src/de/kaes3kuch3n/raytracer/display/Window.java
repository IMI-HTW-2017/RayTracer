package de.kaes3kuch3n.raytracer.display;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.function.Consumer;

public class Window {
    private JFrame window;
    private Timer resizeTimer;
    private Dimension previousSize;
    private JPanel sliderPanel;

    /**
     * Create a new window instance with a fixed inner width and height
     * @param width  The width of the window's content pane
     * @param height The height of the window's content pane
     */
    public Window(int width, int height) {
        sliderPanel = new JPanel();
        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.PAGE_AXIS));

        window = new JFrame("Ray Tracer");
        window.setSize(width, height);
        window.getContentPane().setSize(width, height);
        window.add(sliderPanel, BorderLayout.SOUTH);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setVisible(true);

        previousSize = new Dimension(0, 0);
    }

    /**
     * Set an image for the window to display
     * @param imagePanel The image panel containing the image to show
     */
    public void setImage(ImagePanel imagePanel) {
        window.getContentPane().add(imagePanel, BorderLayout.CENTER);
        window.pack();
    }

    public void addSlider(String name, int min, int max, int value, ChangeListener changeListener) {
        JLabel label = new JLabel(name);
        JSlider slider = new JSlider(min, max, value);
        slider.addChangeListener(changeListener);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        panel.add(label);
        panel.add(slider);
        sliderPanel.add(panel);
        window.pack();
    }

    /**
     * Get the current inner size of the window
     * @return The window's inner size
     */
    public Dimension getSize() {
        return window.getContentPane().getSize();
    }

    /**
     * Add a window resize listener that is triggered every time the window's size changes
     * @param resizeAction The action to execute whenever the window is resized.
     *                     Provides access to the new inner dimension of the window
     */
    public void addResizeListener(Consumer<Dimension> resizeAction) {
        window.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                // Create a resize timer if it doesn't exist (delay prevents event from being spammed)
                if (resizeTimer == null)
                    resizeTimer = new Timer(100, actionEvent -> {
                        JFrame frame = (JFrame) e.getComponent();
                        // Check if size of window did actually change
                        Dimension size = frame.getContentPane().getSize();
                        if (size.width == previousSize.width && size.height == previousSize.height) {
                            return;
                        }
                        // Size did change, execute resize action
                        resizeAction.accept(size);
                        previousSize = size;
                    });
                else {
                    resizeTimer.restart();
                }
            }
        });
    }
}
