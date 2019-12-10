package de.kaes3kuch3n.raytracer;

import de.kaes3kuch3n.raytracer.objects.CSG;
import de.kaes3kuch3n.raytracer.objects.Light;
import de.kaes3kuch3n.raytracer.utilities.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Scene {
    private List<CSG> csgs = new ArrayList<>();
    private List<Light> lights = new ArrayList<>();
    private Camera camera;

    public Scene(Camera camera) {
        this.camera = camera;
    }

    public void addCSGs(CSG... csgs) {
        this.csgs.addAll(Arrays.asList(csgs));
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

                //Debug
                if(x == imageSize.width / 2 + 10 && y == imageSize.height / 2)
                    System.out.println();

                Ray ray = new Ray(camera.getPosition(), Vector3.subtract(new Vector3(planePosX, planePosY, planePosZ), camera.getPosition()));
                Tuple<CSG, Ray.Hit> tuple = getClosestCSG(ray);
                if (tuple == null)
                    continue;
                image.setRGB(x, y, calculateColor(tuple.getFirst(), tuple.getSecond()));
            }
        }
        return image;
    }

    /**
     * Calculates the color of a pixel using the provided quadric and rayhit. Uses all lights in the scene.
     *
     * @param rayHit The rayhit of the quadric and ray
     * @return The color of the pixel (RGB int)
     */
    private int calculateColor(CSG currentCSG, Ray.Hit rayHit) {
        Material mat = rayHit.quadric.getMaterial();
        Vector3 color = calculateColorRecursive(currentCSG, rayHit, 1, mat.getReflectivity(), 1, mat.getTransparency(), Consts.Refraction.AIR);

        int r = (int) (Math.pow(color.x, 1 / Consts.GAMMA) * 255);
        int g = (int) (Math.pow(color.y, 1 / Consts.GAMMA) * 255);
        int b = (int) (Math.pow(color.z, 1 / Consts.GAMMA) * 255);

        r = Math.max(0, Math.min(r, 255));
        g = Math.max(0, Math.min(g, 255));
        b = Math.max(0, Math.min(b, 255));

        return new Color(r, g, b).getRGB();
    }

    private Vector3 calculateColorRecursive(CSG currentCSG, Ray.Hit rayHit, int reflectionStep, double reflectionWeight, int refractionStep, double refractionWeight, double previousRefractionIndex) {

        // Recursion limit reached
        if (reflectionStep > Consts.Reflection.MAX_STEPS || refractionStep > Consts.Refraction.MAX_STEPS)
            return new Vector3(0, 0, 0);

        double r = 0;
        double g = 0;
        double b = 0;

        Vector3 normalVector = rayHit.quadric.getNormalVector(rayHit.position).normalized();
        //Invert normalVector if the surface is inverted
        if (rayHit.invertedNormal)
            normalVector = normalVector.inverted();

        Vector3 rayDirection = rayHit.ray.getDirection().normalized();
        // Add small value to prevent hitting itself
        Vector3 rayOrigin = Vector3.add(rayHit.position, normalVector.multiply(Consts.SMALL_VALUE));

        Material quadricMaterial = rayHit.quadric.getMaterial();

        for (Light light : lights) {

            Vector3 lightDirection = Vector3.subtract(light.getPosition(), rayHit.position).normalized();
            double shadowFactor = getShadowFactor(currentCSG, new Ray(rayOrigin, lightDirection));

            Vector3 specularComponent = quadricMaterial.getSpecularComponent(normalVector, rayDirection.inverted(), lightDirection);
            double metalness = quadricMaterial.getMetalness();
            double lightDot = Math.max(0, Vector3.dot(normalVector, lightDirection));


            double diffuseComponentRed = (1 - specularComponent.x) * (1 - metalness);
            double diffuseComponentGreen = (1 - specularComponent.y) * (1 - metalness);
            double diffuseComponentBlue = (1 - specularComponent.z) * (1 - metalness);

            r += light.getIntensity() * lightDot * light.getColor().x *
                    (diffuseComponentRed * quadricMaterial.getColor().x + specularComponent.x) * (1 - shadowFactor);
            g += light.getIntensity() * lightDot * light.getColor().y *
                    (diffuseComponentGreen * quadricMaterial.getColor().y + specularComponent.y) * (1 - shadowFactor);
            b += light.getIntensity() * lightDot * light.getColor().z *
                    (diffuseComponentBlue * quadricMaterial.getColor().z + specularComponent.z) * (1 - shadowFactor);
        }
        Vector3 color = new Vector3(r, g, b);
        Vector3 reflectionColor = new Vector3(0, 0, 0);
        Vector3 refractionColor = new Vector3(0, 0, 0);
        // REFLECTION
        if (quadricMaterial.getReflectivity() != 0 && reflectionWeight > Consts.Reflection.WEIGHT_MIN) {
            Vector3 newDirection = Vector3.subtract(rayDirection, normalVector.multiply(2 * Vector3.dot(normalVector, rayDirection)));
            Ray ray = new Ray(rayOrigin, newDirection);
            Tuple<CSG, Ray.Hit> tuple = getClosestCSG(ray);
            if(tuple == null)
                reflectionColor = new Vector3(color.x * (1 - reflectionWeight), color.y * (1 - reflectionWeight), color.z * (1 - reflectionWeight));
            else
                reflectionColor = calculateColorRecursive(tuple.getFirst(), tuple.getSecond(),
                        reflectionStep + 1, reflectionWeight * tuple.getSecond().quadric.getMaterial().getReflectivity(),
                        refractionStep, refractionWeight, previousRefractionIndex);
        }
        // REFRACTION
        if (quadricMaterial.getTransparency() != 0 && refractionWeight > Consts.Refraction.WEIGHT_MIN) {
            double i = previousRefractionIndex / quadricMaterial.getRefractionIndex();
            double cosAngle = Vector3.dot(rayDirection.inverted(), normalVector);
            double root = Math.sqrt(1 - i * i * (1 - cosAngle * cosAngle));
            Vector3 refractionDirection = Vector3.add(rayDirection.multiply(i), normalVector.multiply(i * cosAngle - root));
            rayOrigin = Vector3.add(rayHit.position, normalVector.inverted().multiply(Consts.SMALL_VALUE));
            Tuple<CSG, Ray.Hit> tuple = getClosestCSG(new Ray(rayOrigin, refractionDirection));
            if(tuple == null)
                refractionColor = new Vector3(color.x * (1 - refractionWeight), color.y * (1 - refractionWeight), color.z * (1 - refractionWeight));
            else {
                refractionColor = calculateColorRecursive(tuple.getFirst(), tuple.getSecond(), reflectionStep, reflectionWeight,
                        refractionStep + 1, refractionWeight * tuple.getSecond().quadric.getMaterial().getTransparency(), quadricMaterial.getRefractionIndex());
            }
        }
        return new Vector3(
                color.x * (1 - refractionWeight - reflectionWeight) + reflectionColor.x * reflectionWeight + refractionColor.x * refractionWeight,
                color.y * (1 - refractionWeight - reflectionWeight) + reflectionColor.y * reflectionWeight + refractionColor.y * refractionWeight,
                color.z * (1 - refractionWeight - reflectionWeight) + reflectionColor.z * reflectionWeight + refractionColor.z * refractionWeight
        );
    }

    private Tuple<CSG, Ray.Hit> getClosestCSG(Ray ray) {
        Ray.Hit closetHit = null;
        CSG closestCSG = null;
        for (CSG csg : csgs) {
            Ray.Hit rayHit = csg.getFirstRayHit(ray);
            // csg not hit?
            if (rayHit == null)
                continue;

            if (closetHit == null || closetHit.distance > rayHit.distance) {
                closetHit = rayHit;
                closestCSG = csg;
            }
        }
        if(closetHit == null)
            return null;
        return new Tuple<>(closestCSG, closetHit);
    }

    private double getShadowFactor(CSG currentCSG, Ray rayToLight) {
        //Check if there is a csg between the current csg and the light source
        for (CSG otherCSG : csgs) {
            //Skip if we are looking at the same csg
            if (otherCSG == currentCSG)
                continue;
            Ray.Hit otherRayHit = otherCSG.getFirstRayHit(rayToLight);
            //Something hit?
            if (otherRayHit != null)
                return 1 - otherRayHit.quadric.getMaterial().getTransparency();
        }
        return 0;
    }

    private Vector3 getRefractionRecursive(Ray ray, Vector3 color, double transparency, int step, double weight, double currentRefractionIndex) {
        return null;
    }
}
