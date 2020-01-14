package de.kaes3kuch3n.raytracer;

import de.kaes3kuch3n.raytracer.display.ImagePanel;
import de.kaes3kuch3n.raytracer.display.Window;
import de.kaes3kuch3n.raytracer.utilities.scenefile.SceneFileLoader;

public class Main {
    private Scene scene;

    private void show(String sceneFilePath) {
        SceneFileLoader sceneFileLoader = new SceneFileLoader(sceneFilePath);

        scene = new Scene(sceneFileLoader.loadCamera(), sceneFileLoader.loadSkydome());
        scene.addCSGs(sceneFileLoader.loadCSGs());
        scene.addLights(sceneFileLoader.loadLights());

        Window window = new Window(800, 800);
        ImagePanel imagePanel = new ImagePanel(scene.renderImage(window.getSize()));
        window.setImage(imagePanel);
        window.addResizeListener(size -> imagePanel.updateImage(scene.renderImage(size)));
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
