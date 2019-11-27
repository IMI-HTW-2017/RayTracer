package de.kaes3kuch3n.raytracer;

import de.kaes3kuch3n.raytracer.utilities.Vector3;

public class Camera {
    private Vector3 position;
    private Vector3 focusPoint;
    private double roll;

    public Camera(Vector3 position, Vector3 focusPoint, double roll) {
        this.position = position;
        this.focusPoint = focusPoint;
        this.roll = roll;
    }

    public Vector3 getPosition() {
        return this.position;
    }

    public ImagePlane getImagePlane() {
        Vector3 direction = Vector3.subtract(focusPoint, position);
        Vector3 top = new Vector3(Math.sin(roll), Math.cos(roll), 0); // y-axis for roll = 0

        Vector3 right = Vector3.cross(direction, top).normalized();
        Vector3 up = Vector3.cross(right, direction).normalized();

        return new ImagePlane(focusPoint, right, up);
    }

    public static class ImagePlane {
        public Vector3 focusPoint;
        public Vector3 rightVector;
        public Vector3 upVector;

        public ImagePlane(Vector3 focusPoint, Vector3 rightVector, Vector3 upVector) {
            this.focusPoint = focusPoint;
            this.rightVector = rightVector;
            this.upVector = upVector;
        }
    }
}
