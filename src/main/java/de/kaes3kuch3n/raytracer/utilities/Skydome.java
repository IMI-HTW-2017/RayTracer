package de.kaes3kuch3n.raytracer.utilities;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Skydome {
    private BufferedImage texture;

    public Skydome(String texturePath) {
        try {
            texture = ImageIO.read(new File(texturePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Color getColor(double x, double y) {
        int argb = texture.getRGB((int) (x * (texture.getWidth() - 1)), (int) (y * (texture.getHeight() - 1)));
        return new Color(argb);
    }
}
