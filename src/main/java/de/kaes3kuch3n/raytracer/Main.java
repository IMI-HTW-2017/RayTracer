package de.kaes3kuch3n.raytracer;

import de.kaes3kuch3n.raytracer.display.ImagePanel;
import de.kaes3kuch3n.raytracer.display.Window;
import de.kaes3kuch3n.raytracer.objects.*;
import de.kaes3kuch3n.raytracer.utilities.Consts;
import de.kaes3kuch3n.raytracer.utilities.Material;
import de.kaes3kuch3n.raytracer.utilities.Vector3;

import javax.swing.*;
import java.awt.*;

public class Main {
    private Scene scene;

    private void show() {
        Camera camera = new Camera(new Vector3(0, 0, 5), new Vector3(0, 0, 4), 0);
        Window window = new Window(800, 800);
        scene = new Scene(camera);

        Material red = Material.CreateRough(new Color(255, 0, 0), 0.01, 0);
        Material green = Material.CreateRough(new Color(0, 255, 0), 0.01, 0);
        Material blue = Material.CreateMetal(new Color(52, 157, 184), 0.3, 0.9);
        Material white = Material.CreateRough(new Color(255, 255, 255), 0.1, 0.4);
        Material white2 = Material.CreateRough(new Color(255, 255, 255), 0.9, 0.1);
        Material transparent = Material.CreateTransparent(new Color(184, 250, 255), 0.1, 0.3, 0.7, Consts.Refraction.WATER);

        //Cube
//        Quadric left = new Plane(1, 0, 0, transparent).translate(1,0,0);
//        Quadric right = new Plane(-1, 0, 0, transparent).translate(-1,0,0);
//        Quadric top = new Plane(0, 1, 0, transparent).translate(0,1,0);
//        Quadric bottom = new Plane(0, 1, 0, blue).translate(0,-5,0).rotateX(-30);
//        Quadric front = new Plane(0, 0, 1, transparent).translate(0,0,1);
//        Quadric back = new Plane(0, 0, 1, transparent).translate(0,0,-10);

//        CSG csg1 = new CSG(top, bottom, CSGOperator.SUBTRACT);
//        CSG csg2 = new CSG(left, right, CSGOperator.SUBTRACT);
//        CSG csg3 = new CSG(front, back, CSGOperator.SUBTRACT);
//        CSG csg4 = new CSG(csg1, csg2, CSGOperator.INTERSECT);
//        CSG csg5 = new CSG(csg3, csg4, CSGOperator.INTERSECT);

        Quadric bottom = new Plane(0, 1, 0, 1, transparent).translate(0, -4, 0).rotateX(0);
        Quadric sphere1 = new Sphere(1, red).translate(0, -8, -3);
        Quadric sphere2 = new Sphere(50, red).translate(0, -18, -50);
        Quadric sphere3 = new Sphere(0.5, red).translate(-1.5, -2, 0);
        Quadric sphere5 = new Sphere(50, red).translate(20, 6, -50);


        scene.addCSGs(new CSG(sphere1), new CSG(sphere2), new CSG(sphere3), new CSG(bottom), new CSG(sphere5));

        scene.addLights(
                new Light(new Vector3(0, 3, 5), new Color(255, 255, 255), 1f)
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
