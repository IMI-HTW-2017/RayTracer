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
        Camera camera = new Camera(new Vector3(0d, 0, 5d), new Vector3(0d, 0, 4), 0);
        Window window = new Window(800, 800);
        scene = new Scene(camera);

        Material red = Material.CreateRough(new Color(255, 0, 0), 0.01);
        Material green = Material.CreateRough(new Color(0, 255, 0), 0.7);
        Material blue = Material.CreateMetal(new Color(52, 157, 184), 0.3);

        Quadric sphere1 = new Sphere(2, red).translate(-2, 0, 0);
        Quadric sphere2 = new Sphere(2, red).translate(2, 0, 0);
        Quadric sphere3 = new Sphere(2, blue).translate(0, 0, 0);

        Quadric right = new Plane(1,0,0, blue).translate(1,0, 0);
        Quadric left = new Plane(-1,0,0, blue).translate(-1,0, 0);
        Quadric top = new Plane(0,1,0, blue).translate(0,1, 0);
        Quadric bottom = new Plane(0,-1,0, blue).translate(0,-1, 0);
        Quadric front = new Plane(0,0,1, blue).translate(0,0, 1);
        Quadric back = new Plane(0,0,-1, blue).translate(0,0, -1);

        CSG csg1 = new CSG(right, left, CSGOperator.SUBTRACT);
        CSG csg2 = new CSG(top, bottom, CSGOperator.SUBTRACT);
        CSG csg3 = new CSG(front, back, CSGOperator.SUBTRACT);
        CSG csg4 = new CSG(csg1, csg2, CSGOperator.INTERSECT);
        CSG csg5 = new CSG(csg3, csg4, CSGOperator.INTERSECT);

        scene.addCSGs(csg5);

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
            right.rotateY(value);
            left.rotateY(value);
            top.rotateY(value);
            bottom.rotateY(value);
            front.rotateY(value);
            top.rotateY(value);


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
