package de.kaes3kuch3n.raytracer.utilities;

import java.awt.*;

public class Material {
    private Vector3 albedo;
    private double roughness;
    private double metalness;
    private double reflectivity;
    private double transparency;
    private double refractionIndex;

    public Material(Color albedo, double roughness, double metalness, double reflectivity, double transparency, double refractionIndex) {
        this.albedo = new Vector3(
                Math.pow(albedo.getRed() / 255d, Consts.GAMMA),
                Math.pow(albedo.getGreen() / 255d, Consts.GAMMA),
                Math.pow(albedo.getBlue() / 255d, Consts.GAMMA));
        this.roughness = roughness;
        this.metalness = metalness;
        this.reflectivity = reflectivity;
        this.transparency = transparency;
        this.refractionIndex = refractionIndex;
    }


    public Material(Material material) {
        this(new Color((float) material.albedo.x, (float) material.albedo.y, (float) material.albedo.z), material.roughness, material.metalness, material.reflectivity, material.transparency, material.refractionIndex);
    }

    public static Material CreateMetal(Color albedo, double metalness, double reflectivity) {
        return new Material(albedo, 0.001, metalness, reflectivity, 0, 0);
    }

    public static Material CreateRough(Color albedo, double roughness, double reflectivity) {
        return new Material(albedo, roughness, 0.001, reflectivity, 0, 0);
    }

    public static Material CreateTransparent(Color albedo, double roughness, double reflectivity, double transparency, double refractionIndex) {
        return new Material(albedo, roughness, 0.001, reflectivity, transparency, refractionIndex);
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
        Vector3 f = new Vector3(
                getFresnel(normalVector, toCameraVector, getFresnelFactor(albedo.x)),
                getFresnel(normalVector, toCameraVector, getFresnelFactor(albedo.y)),
                getFresnel(normalVector, toCameraVector, getFresnelFactor(albedo.z))
        );
        double g = getGeometry(normalVector, toCameraVector, toLightVector);

        return new Vector3(d * f.x * g, d * f.y * g, d * f.z * g);
    }

    public double getFresnel(Vector3 n, Vector3 v, double f0) {
        double nDotV = Vector3.dot(n, v);
        if (nDotV < 0)
            return 1;
        return f0 + (1 - f0) * Math.pow(1 - nDotV, 5);
    }

    private double getDistribution(Vector3 n, Vector3 h) {
        double squareRoughness = roughness * roughness;
        return squareRoughness / (Math.PI * Math.pow(Math.pow(Vector3.dot(n, h), 2) * (squareRoughness - 1) + 1, 2));
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

    public double getTransparency() {
        return transparency;
    }

    public double getRefractionIndex() {
        return refractionIndex;
    }

    public void ChangeReflectivity(double factor) {
        reflectivity *= factor;
    }

    public void ChangeTransparency(double factor) {
        transparency *= factor;
    }

    public void ApplyFresnel(double fresnel) {
        double factor = fresnel * 2d;
        reflectivity = Math.min(reflectivity * factor, 1);
        transparency = Math.min(transparency * (2 - factor), 1);
    }
}
