package de.kaes3kuch3n.raytracer;

import de.kaes3kuch3n.raytracer.display.ImagePanel;
import de.kaes3kuch3n.raytracer.utilities.Vector3;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.*;

public class Renderer {
    private final Scene scene;

    public Renderer(Scene scene) {

        this.scene = scene;
    }

    public void renderImage(Dimension imageSize, ImagePanel imagePanel) {
        BufferedImage image = new BufferedImage(imageSize.width, imageSize.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = image.createGraphics();
        graphics2D.setPaint(Color.BLACK);
        graphics2D.fillRect(0, 0, image.getWidth(), image.getHeight());

        // Calculate aspect ratio
        double widthRatio;
        double heightRatio;
        if (imageSize.width > imageSize.height) {
            widthRatio = 1;
            heightRatio = (double) imageSize.height / imageSize.width;
        } else {
            widthRatio = (double) imageSize.width / imageSize.height;
            heightRatio = 1;
        }

        //Starting position is the top-left corner
        Camera.ImagePlane imagePlane = scene.getImagePlane();
        Vector3 topLeft = Vector3.subtract(imagePlane.focusPoint, Vector3.add(imagePlane.rightVector.multiply(widthRatio), imagePlane.upVector.inverted().multiply(heightRatio)));
        double stepSizeX = 2.0 * widthRatio / imageSize.width;
        double stepSizeY = 2.0 * heightRatio / imageSize.height;

        ExecutorService executor = Executors.newCachedThreadPool();
        CompletionService<PixelRenderer.Result> completionService = new ExecutorCompletionService<>(executor);

        for (int x = 0; x < imageSize.width; x++) {
            for (int y = 0; y < imageSize.height; y++) {
                completionService.submit(new PixelRenderer(scene, x, y, stepSizeX, stepSizeY, topLeft));
            }
        }

        int renderedPixels = 0;
        boolean error = false;

        while (renderedPixels < imageSize.width * imageSize.height && !error) {
            Future<PixelRenderer.Result> future = completionService.poll();
            try {
                PixelRenderer.Result result = future.get();
                image.setRGB(result.x, result.y, result.color.getRGB());
                imagePanel.updateImage(image);
                renderedPixels++;
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                error = true;
            }
        }

        executor.shutdown();
    }
}
