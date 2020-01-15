package de.kaes3kuch3n.raytracer;

import de.kaes3kuch3n.raytracer.display.ImagePanel;
import de.kaes3kuch3n.raytracer.display.Window;
import de.kaes3kuch3n.raytracer.utilities.scenefile.SceneFileLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Main {

    private void show(String sceneFilePath) {
        SceneFileLoader sceneFileLoader = new SceneFileLoader(sceneFilePath);

        Scene scene = new Scene(sceneFileLoader.loadCamera(), sceneFileLoader.loadSkydome());
        scene.addCSGs(sceneFileLoader.loadCSGs());
        scene.addLights(sceneFileLoader.loadLights());

        Dimension windowSize = new Dimension(800, 800);
        Window window = new Window(windowSize);
        Renderer sceneRenderer = new Renderer(scene);
        ImagePanel imagePanel = new ImagePanel(new BufferedImage(windowSize.width, windowSize.height, BufferedImage.TYPE_INT_ARGB));
        window.setImage(imagePanel);
        sceneRenderer.renderImage(windowSize, imagePanel);
        window.addResizeListener(size -> sceneRenderer.renderImage(size, imagePanel));
    }

    public static void main(String[] args) {
        String sceneFilePath = args[0];
        if (sceneFilePath == null) {
            System.err.println("Usage: java -jar RayTracer.jar <scene file path>");
            System.exit(0);
        }
        new Main().show(sceneFilePath);
    }
}
