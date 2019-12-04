package de.kaes3kuch3n.raytracer;

import de.kaes3kuch3n.raytracer.display.ImagePanel;
import de.kaes3kuch3n.raytracer.display.Window;
import de.kaes3kuch3n.raytracer.objects.*;
import de.kaes3kuch3n.raytracer.utilities.CSGOperator;
import de.kaes3kuch3n.raytracer.utilities.Material;
import de.kaes3kuch3n.raytracer.utilities.Vector3;

import javax.swing.*;
import java.awt.*;

public class Main {
    private Scene scene;

    private void show() {
        Camera camera = new Camera(new Vector3(0d, 0, 3d), new Vector3(0d, 0, 2), 0);
        Window window = new Window(800, 800);
        scene = new Scene(camera);

        Material red = Material.CreateRough(new Color(255, 0, 0), 0.01, 0);
        Material green = Material.CreateRough(new Color(0, 255, 0), 0.01, 0.99);
        Material blue = Material.CreateMetal(new Color(52, 157, 184), 0.3, 0.9);
        Material white = Material.CreateMetal(new Color(255, 255, 255), 0.9, 0.99);

        Quadric sphere1 = new Sphere(1, red).translate(2.5, 0, 0);
        Quadric sphere2 = new Sphere(2, green).translate(-1.5, 0, 0);

        Quadric plane1 = new Plane(0,0,-1,white).translate(0,0, 5);
        Quadric plane2 = new Plane(0,0,1,white).translate(0,0, -5);


        scene.addCSGs(new CSG(plane1), new CSG(plane2), new CSG(sphere1));

        scene.addLights(
                new Light(new Vector3(0, 0, 3), new Color(255, 255, 255), 1f)
                //new Light(new Vector3(3, 0, 1), new Color(255, 255, 255), 1f)
                //new Light(new Vector3(0, 0, 15), new Color(255, 255, 255), 1f)
        );

        ImagePanel imagePanel = getRenderedImage(window.getSize());
        window.addResizeListener(size -> imagePanel.updateImage(scene.renderImage(size)));
        window.setImage(imagePanel);

        window.addSlider("RotateY", 0, 180, 0, e -> {
            Object eventSource = e.getSource();
            if (!(eventSource instanceof JSlider)) return;

            JSlider slider = (JSlider) eventSource;
            double value = slider.getValue() / 5d;
            sphere1.rotateY(value);
            sphere2.rotateY(value);


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
