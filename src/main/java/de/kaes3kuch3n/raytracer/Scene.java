package de.kaes3kuch3n.raytracer;

import de.kaes3kuch3n.raytracer.objects.CSG;
import de.kaes3kuch3n.raytracer.objects.Light;
import de.kaes3kuch3n.raytracer.utilities.Consts;
import de.kaes3kuch3n.raytracer.utilities.Material;
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

                //Debug
                //if(x == imageSize.width / 2 && y == imageSize.height / 2)
                //    System.out.println();

                Ray ray = new Ray(camera.getPosition(), Vector3.subtract(new Vector3(planePosX, planePosY, planePosZ), camera.getPosition()));
                Ray.Hit closetHit = null;
                for (CSG csg : csg) {
                    Ray.Hit rayHit = csg.getFirstRayHit(ray);

                    //Current csg not hit
                    if (rayHit == null)
                        continue;

                    if (closetHit == null || closetHit.distance > rayHit.distance)
                        closetHit = rayHit;
                }
                //No sphere hit
                if (closetHit == null)
                    continue;
                image.setRGB(x, y, calculateColor(closetHit));
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
    private int calculateColor(Ray.Hit rayHit) {
        double r = 0;
        double g = 0;
        double b = 0;

        for (Light light : lights) {
            Vector3 cameraDirection = Vector3.subtract(camera.getPosition(), rayHit.position).normalized();
            Vector3 lightDirection = Vector3.subtract(light.getPosition(), rayHit.position).normalized();

            //Invert normalVector if the surface is inverted
            Vector3 normalVector;
            if (!rayHit.invertedNormal)
                normalVector = rayHit.quadric.getNormalVector(rayHit.position).normalized();
            else
                normalVector = rayHit.quadric.getNormalVector(rayHit.position).normalized().inverted();

            Material quadricMaterial = rayHit.quadric.getMaterial();
            Vector3 specularComponent = quadricMaterial.getSpecularComponent(normalVector, cameraDirection, lightDirection);
            double metalness = quadricMaterial.getMetalness();
            double lightDot = Vector3.dot(normalVector, lightDirection);

            double diffuseComponentRed = (1 - specularComponent.x) * (1 - metalness);
            double diffuseComponentGreen = (1 - specularComponent.y) * (1 - metalness);
            double diffuseComponentBlue = (1 - specularComponent.z) * (1 - metalness);

            r += light.getIntensity() * lightDot * light.getColor().x *
                    (diffuseComponentRed * quadricMaterial.getColor().x + specularComponent.x);
            g += light.getIntensity() * lightDot * light.getColor().y *
                    (diffuseComponentGreen * quadricMaterial.getColor().y + specularComponent.y);
            b += light.getIntensity() * lightDot * light.getColor().z *
                    (diffuseComponentBlue * quadricMaterial.getColor().z + specularComponent.z);
        }

        r = Math.pow(r, 1 / Consts.GAMMA) * 255;
        g = Math.pow(g, 1 / Consts.GAMMA) * 255;
        b = Math.pow(b, 1 / Consts.GAMMA) * 255;

        return new Color((int) r, (int) g, (int) b).getRGB();
    }

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

}
