package de.kaes3kuch3n.raytracer;

import de.kaes3kuch3n.raytracer.utilities.Vector3;

public class Camera {
    private Vector3 position;
    private Vector3 focusPoint;

    public Camera(Vector3 position, Vector3 focusPoint) {
        this.position = position;
        this.focusPoint = focusPoint;
    }


    public Vector3 getPosition() {
        return this.position;
    }
}
