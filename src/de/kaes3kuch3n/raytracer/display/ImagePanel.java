package de.kaes3kuch3n.raytracer.display;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.function.Consumer;

public class ImagePanel extends JPanel {
    private Image image;
    private long lastRepaint;
    private static final int REPAINT_COOLDOWN = 50;

    /**
     * Create a new image panel
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
        lastRepaint = System.currentTimeMillis();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
    }

    /**
     * Add a imagePanel resize listener that is triggered every time the imagePanel's size changes
     *
     * @param resizeAction The action to execute whenever the imagePanel is resized.
     *                     Provides access to the new inner dimension of the imagePanel
     */
    public void addResizeListener(Consumer<Dimension> resizeAction) {
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                if (System.currentTimeMillis() - lastRepaint > REPAINT_COOLDOWN)
                    resizeAction.accept(e.getComponent().getSize());
            }
        });
    }
}

