package de.kaes3kuch3n.raytracer.utilities;

public class Ray {
    private Vector3 position;
    private Vector3 direction;

    public Ray(Vector3 position, Vector3 direction){
        this.position = position;
        this.direction = direction;
    }

    public int getRayHits(){
        double a = Math.pow(direction.x, 2) + Math.pow(direction.y, 2) + Math.pow(direction.z, 2);
        return 0;
    }
}

