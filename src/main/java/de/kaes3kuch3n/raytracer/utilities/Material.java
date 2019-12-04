package de.kaes3kuch3n.raytracer.utilities;

import java.awt.*;

public class Material {
    private Vector3 albedo;
    private double roughness;
    private double metalness;
    private double reflectivity;

    public Material(Color albedo, double roughness, double metalness, double reflectivity) {
        this.albedo = new Vector3(
                Math.pow(albedo.getRed() / 255d, Consts.GAMMA),
                Math.pow(albedo.getGreen() / 255d, Consts.GAMMA),
                Math.pow(albedo.getBlue() / 255d, Consts.GAMMA));
        this.roughness = roughness;
        this.metalness = metalness;
        this.reflectivity = reflectivity;
    }

    public static Material CreateMetal(Color albedo, double metalness, double reflectivity) {
        return new Material(albedo, 0.001, metalness, reflectivity);
    }

    public static Material CreateRough(Color albedo, double roughness, double reflectivity) {
        return new Material(albedo, roughness, 0.001, reflectivity);
    }

    public void setRoughness(double roughness) {
        this.roughness = roughness;
    }

    public void setMetalness(double metalness) {
        this.metalness = metalness;
    }

    public Vector3 getColor() {
        return albedo;
    }

    public double getRoughness() {
        return roughness;
    }

    public double getMetalness() {
        return metalness;
    }

    public double getReflectivity() {
        return reflectivity;
    }

    public Vector3 getSpecularComponent(Vector3 normalVector, Vector3 toCameraVector, Vector3 toLightVector) {
        normalVector = normalVector.normalized();
        toCameraVector = toCameraVector.normalized();
        toLightVector = toLightVector.normalized();

        Vector3 h = Vector3.add(toCameraVector, toLightVector).divide(2).normalized();

        double d = getDistribution(normalVector, h);
        Vector3 f = getFresnel(normalVector, toCameraVector);
        double g = getGeometry(normalVector, toCameraVector, toLightVector);

        return new Vector3(d * f.x * g, d * f.y * g, d * f.z * g);
    }

    private double getDistribution(Vector3 n, Vector3 h) {
        double squareRoughness = roughness * roughness;
        return squareRoughness / (Math.PI * Math.pow(Math.pow(Vector3.dot(n, h), 2) * (squareRoughness - 1) + 1, 2));
    }

    private Vector3 getFresnel(Vector3 n, Vector3 v) {
        double f0r = getFresnelFactor(albedo.x);
        double f0g = getFresnelFactor(albedo.y);
        double f0b = getFresnelFactor(albedo.z);
        double r = f0r + (1 - f0r) * Math.pow(1 - Vector3.dot(n, v), 5);
        double g = f0g + (1 - f0g) * Math.pow(1 - Vector3.dot(n, v), 5);
        double b = f0b + (1 - f0b) * Math.pow(1 - Vector3.dot(n, v), 5);
        return new Vector3(r, g, b);
    }

    private double getFresnelFactor(double albedo) {
        return (1 - metalness) * 0.04 + metalness * albedo;
    }

    private double getGeometry(Vector3 n, Vector3 v, Vector3 l) {
        double nDotV = Vector3.dot(n, v);
        double nDotL = Vector3.dot(n, l);
        if (nDotL < 0)
            return 0;
        double rDiv2 = roughness / 2;
        return nDotV / (nDotV * (1 - rDiv2) + rDiv2) * nDotL / (nDotL * (1 - rDiv2) + rDiv2);
    }
}
