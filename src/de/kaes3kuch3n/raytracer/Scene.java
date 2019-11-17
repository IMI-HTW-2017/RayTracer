package de.kaes3kuch3n.raytracer;

import de.kaes3kuch3n.raytracer.objects.CSG;
import de.kaes3kuch3n.raytracer.objects.Light;
import de.kaes3kuch3n.raytracer.utilities.Operator;
import de.kaes3kuch3n.raytracer.utilities.Ray;
import de.kaes3kuch3n.raytracer.utilities.Vector3;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Scene {
    private List<CSG> csg = new ArrayList<>();
    private List<Light> lights = new ArrayList<>();
    private Camera camera;

    public Scene(Camera camera) {
        this.camera = camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public void addCSGs(CSG... csgs) {
        this.csg.addAll(Arrays.asList(csgs));
    }

    public void addLights(Light... lights) {
        this.lights.addAll(Arrays.asList(lights));
    }

    public Image renderImage(Dimension imageSize) {
        BufferedImage image = new BufferedImage(imageSize.width, imageSize.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = image.createGraphics();
        graphics2D.setPaint(Color.BLACK);
        graphics2D.fillRect(0, 0, image.getWidth(), image.getHeight());

        // Get image plane and use it for calculating the ray directions
        Camera.ImagePlane plane = camera.getImagePlane();

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
        Vector3 topLeft = Vector3.subtract(plane.focusPoint, Vector3.add(plane.rightVector.multiply(widthRatio), plane.upVector.inverted().multiply(heightRatio)));
        double stepSizeX = 2.0 * widthRatio / imageSize.width;
        double stepSizeY = 2.0 * heightRatio / imageSize.height;

        double planePosX, planePosY, planePosZ;

        for (int y = 0; y < imageSize.height; y++) {
            for (int x = 0; x < imageSize.width; x++) {

                Vector3 stepVectorX = plane.rightVector.multiply(stepSizeX * x);
                Vector3 stepVectorY = plane.upVector.inverted().multiply(stepSizeY * y);

                planePosX = topLeft.x + stepVectorX.x + stepVectorY.x;
                planePosY = topLeft.y + stepVectorX.y + stepVectorY.y;
                planePosZ = topLeft.z + stepVectorX.z + stepVectorY.z;


                Ray ray = new Ray(camera.getPosition(), Vector3.subtract(new Vector3(planePosX, planePosY, planePosZ), camera.getPosition()));
                RayHitResult minDistanceHit = null;
                for (CSG csg : csg) {
                    Ray.Hit rayHit = csg.getFirstRayHit(ray);

                    //Current quadric not hit
                    if (rayHit == null)
                        continue;
                    if (minDistanceHit == null || minDistanceHit.compareTo(rayHit) > 0)
                        minDistanceHit = new RayHitResult(rayHit, csg);
                }
                //No sphere hit
                if (minDistanceHit == null)
                    continue;
                //image.setRGB(x, y, new Color(255, 0, 0).getRGB());
                image.setRGB(x, y, calculateColor(minDistanceHit.csg, minDistanceHit.rayHit));
            }
        }
        return image;
    }

    /**
     * Calculates the color of a pixel using the provided quadric and rayhit. Uses all lights in the scene.
     *
     * @param csg The csg that was hit
     * @param rayHit The rayhit of the quadric and ray
     * @return The color of the pixel (RGB int)
     */
    private int calculateColor(CSG csg, Ray.Hit rayHit) {
        int r = 0;
        int g = 0;
        int b = 0;
        for (Light light : lights) {
            boolean skipFlag = false;
            //Light with normal vector
            Vector3 lightDirection = Vector3.subtract(light.getPosition(), rayHit.position).normalized();
            Ray rayToLight = new Ray(rayHit.position, lightDirection);

            /*
            // ----- Shadows ----- //

            //Check if there is a quadric between the current quadric and the light source
            for (Quadric otherQuadric : quadrics) {
                //Skip if we are looking at the same quadric
                if (otherQuadric == quadric)
                    continue;
                Ray.Hit otherRayHit = otherQuadric.getRayhit(rayToLight);
                //Something hit? Skip coloring
                if (otherRayHit != null) {
                    skipFlag = true;
                    break;
                }
            }
            if (skipFlag)
                continue;
            // ---------- //
             */
            Vector3 normalVector;
            if(!rayHit.invertedNormal)
                 normalVector = rayHit.quadric.getNormalVector(rayHit.position);
            else
                normalVector = rayHit.quadric.getNormalVector(rayHit.position).inverted();

            double lightCos = Vector3.dot(lightDirection, normalVector);
            if (lightCos < 0)
                lightCos = 0;

            r += (lightCos * light.getColor().getRed() * light.getIntensity()) * rayHit.quadric.getColorRatio().x;
            g += (lightCos * light.getColor().getGreen() * light.getIntensity()) * rayHit.quadric.getColorRatio().y;
            b += (lightCos * light.getColor().getBlue() * light.getIntensity()) * rayHit.quadric.getColorRatio().z;
            r = Math.min(r, 255);
            g = Math.min(g, 255);
            b = Math.min(b, 255);
        }
        return new Color(r, g, b).getRGB();
    }

    /**
     * Small class for saving ray hits and comparing them by their distance
     */
    private static class RayHitResult implements Comparable {
        private Ray.Hit rayHit;
        private CSG csg;

        private RayHitResult(Ray.Hit rayHit, CSG csg) {
            this.rayHit = rayHit;
            this.csg = csg;
        }

        @Override
        public int compareTo(Object o) {
            if (!(o instanceof RayHitResult))
                throw new ClassCastException();
            return Double.compare(this.rayHit.distance, ((RayHitResult) o).rayHit.distance);
        }
    }

}
