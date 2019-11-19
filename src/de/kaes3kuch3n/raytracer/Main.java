package de.kaes3kuch3n.raytracer;

import de.kaes3kuch3n.raytracer.display.ImagePanel;
import de.kaes3kuch3n.raytracer.display.Window;
import de.kaes3kuch3n.raytracer.objects.CSG;
import de.kaes3kuch3n.raytracer.objects.Light;
import de.kaes3kuch3n.raytracer.objects.Quadric;
import de.kaes3kuch3n.raytracer.objects.Sphere;
import de.kaes3kuch3n.raytracer.utilities.Material;
import de.kaes3kuch3n.raytracer.utilities.Operator;
import de.kaes3kuch3n.raytracer.utilities.Vector3;

import javax.swing.*;
import java.awt.*;

public class Main {
    private Scene scene;

    private void show() {
        Camera camera = new Camera(new Vector3(0d, 0, 4d), new Vector3(0d, 0, 3), 0);
        Window window = new Window(800, 800);
        scene = new Scene(camera);

        Material red = Material.CreateRough(new Color(255, 0, 0), 0.01);
        Material green = Material.CreateRough(new Color(0, 255, 0), 0.7);
        Material blue = Material.CreateMetal(new Color(0, 0, 255), 0.3);

        Quadric sphere1 = new Sphere(1, red).translate(0.3, 0, 0);
        Quadric sphere2 = new Sphere(1, blue).translate(-0.3, 0, 0);
        Quadric sphere3 = new Sphere(1, green).translate(0, -0.6, 0);

        CSG csg1 = new CSG(sphere1, sphere2, Operator.COMBINE);
        CSG csg2 = new CSG(sphere3, csg1, Operator.COMBINE);

        scene.addCSGs(csg2);


        scene.addLights(
                new Light(new Vector3(3, 0, 3), new Color(255, 255, 255), 1f)
                //new Light(new Vector3(3, 0, 1), new Color(255, 255, 255), 1f)
                //new Light(new Vector3(0, 0, 15), new Color(255, 255, 255), 1f)
        );

        ImagePanel imagePanel = getRenderedImage(window.getSize());
        window.addResizeListener(size -> imagePanel.updateImage(scene.renderImage(size)));
        window.setImage(imagePanel);

        window.addSlider("Roughness", 0, 100, (int) (blue.getRoughness() * 100), e -> {
            Object eventSource = e.getSource();
            if (!(eventSource instanceof JSlider)) return;

            JSlider slider = (JSlider) eventSource;
            blue.setRoughness(slider.getValue() / 100.0);
            imagePanel.updateImage(scene.renderImage(window.getSize()));
        });

        window.addSlider("Metalness", 0, 100, (int) (blue.getMetalness() * 100), e -> {
            Object eventSource = e.getSource();
            if (!(eventSource instanceof JSlider)) return;

            JSlider slider = (JSlider) eventSource;
            blue.setMetalness(slider.getValue() / 100.0);
            imagePanel.updateImage(scene.renderImage(window.getSize()));
        });
    }

    private ImagePanel getRenderedImage(Dimension size) {
        return new ImagePanel(scene.renderImage(size));
    }

    public static void main(String[] args) {
        new Main().show();
    }
}
