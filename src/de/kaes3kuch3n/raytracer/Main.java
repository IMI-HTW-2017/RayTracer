package de.kaes3kuch3n.raytracer;

import de.kaes3kuch3n.raytracer.utilities.Ray;
import de.kaes3kuch3n.raytracer.utilities.Vector3;

public class Main {
    private View view;
    private Camera camera;
    public static void main(String[] args) {
        calculatePlane(new View(), new Camera(new Vector3(0d,0d,0d), new Vector3(0d,0d, -1d)));
    }

    public static void calculatePlane(View view, Camera camera){
        double stepSizeY = 2.0 / view.getHeight();
        double stepSizeX = 2.0 / view.getWidth();
        for (int y = 0; y < view.getHeight(); y++) {
            for (int x = 0; x < view.getWidth(); x++) {
                int pixelPos = x + y * view.getWidth();

                double planePosX = (stepSizeX * x) - 1;
                double planePosY = (stepSizeY * y) - 1;
                new Ray(camera.getPosition(), new Vector3(planePosX, planePosY, -1));


            }
        }
    }
}
