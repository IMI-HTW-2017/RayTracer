package de.kaes3kuch3n.raytracer;

import de.kaes3kuch3n.raytracer.objects.Light;
import de.kaes3kuch3n.raytracer.objects.Sphere;
import de.kaes3kuch3n.raytracer.utilities.Ray;
import de.kaes3kuch3n.raytracer.utilities.Vector3;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Scene {
    private List<Sphere> spheres = new ArrayList<>();
    private List<Light> lights = new ArrayList<>();
    private Camera camera;
    private Dimension imageSize;

    public Scene(Camera camera, Dimension imageSize) {
        this.camera = camera;
        this.imageSize = imageSize;
    }

    public void addSpheres(Sphere... spheres) {
        this.spheres.addAll(Arrays.asList(spheres));
    }

    public void addLights(Light... lights) {
        this.lights.addAll(Arrays.asList(lights));
    }

    public Image renderImage() {
        BufferedImage image = new BufferedImage(imageSize.width, imageSize.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = image.createGraphics();
        graphics2D.setPaint(Color.BLACK);
        graphics2D.fillRect(0, 0, image.getWidth(), image.getHeight());

        // Get image plane and use it for calculating the ray directions
        Camera.ImagePlane plane = camera.getImagePlane();
        Vector3 topLeft = Vector3.subtract(plane.focusPoint, Vector3.add(plane.rightVector, plane.upVector.inverted()));

        double stepSizeX = 2.0 / imageSize.width;
        double stepSizeY = 2.0 / imageSize.height;

        double planePosX, planePosY, planePosZ;

        for (int y = 0; y < imageSize.height; y++) {
            for (int x = 0; x < imageSize.width; x++) {

                Vector3 stepVectorX = plane.rightVector.multiply(stepSizeX * x);
                Vector3 stepVectorY = plane.upVector.inverted().multiply(stepSizeY * y);

                planePosX = topLeft.x + stepVectorX.x + stepVectorY.x;
                planePosY = topLeft.y + stepVectorX.y + stepVectorY.y;
                planePosZ = topLeft.z + stepVectorX.z + stepVectorY.z;

                ArrayList<RayHitResult> rayHitResults = new ArrayList<>();
                for (Sphere sphere : spheres) {
                    Ray ray = new Ray(camera.getPosition(), new Vector3(planePosX, planePosY, planePosZ));
                    Ray.Hit rayHit = sphere.getRayHit(ray);

                    //Nothing hit? -> Black
                    if (rayHit == null) {
                        continue;
                    }
                    rayHitResults.add(new RayHitResult(rayHit, sphere));

                }

                if (rayHitResults.isEmpty())
                    continue;

                RayHitResult result = Collections.min(rayHitResults);
                image.setRGB(x, y, calculateColor(result.sphere, result.rayHit));
            }
        }
        return image;
    }

    private int calculateColor(Sphere sphere, Ray.Hit rayHit) {
        int r = 0;
        int g = 0;
        int b = 0;
        for (Light light : lights) {
            boolean skipFlag = false;
            //Light with normal vector
            Vector3 lightDirection = Vector3.subtract(light.getPosition(), rayHit.position).normalize();
            Ray rayToLight = new Ray(rayHit.position, lightDirection);
            for (Sphere otherSphere : spheres) {
                if (otherSphere == sphere)
                    continue;
                Ray.Hit otherRayHit = otherSphere.getRayHit(rayToLight);
                if (otherRayHit != null) {
                    skipFlag = true;
                    break;
                }
            }
            if (skipFlag)
                continue;
            Vector3 normalVector = Vector3.subtract(rayHit.position, sphere.getPosition()).normalize();
            double lightCos = Vector3.dot(lightDirection, normalVector);
            if (lightCos < 0)
                lightCos = 0;

            //Some global lighting
            int globalLight = 0;
            r += (lightCos * light.getColor().getRed() * light.getIntensity()) * sphere.getColorRatio().x + globalLight;
            g += (lightCos * light.getColor().getGreen() * light.getIntensity()) * sphere.getColorRatio().y + globalLight;
            b += (lightCos * light.getColor().getBlue() * light.getIntensity()) * sphere.getColorRatio().z + globalLight;
            r = Math.min(r, 255);
            g = Math.min(g, 255);
            b = Math.min(b, 255);
        }
        return new Color(r, g, b).getRGB();
    }

    private static class RayHitResult implements Comparable {
        private Ray.Hit rayHit;
        private Sphere sphere;

        public RayHitResult(Ray.Hit rayHit, Sphere sphere) {
            this.rayHit = rayHit;
            this.sphere = sphere;
        }

        @Override
        public int compareTo(Object o) {
            if (!(o instanceof RayHitResult))
                throw new ClassCastException();
            return Double.compare(this.rayHit.distance, ((RayHitResult) o).rayHit.distance);
        }
    }

}
