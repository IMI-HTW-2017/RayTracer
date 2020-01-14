package de.kaes3kuch3n.raytracer.utilities.scenefile;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import de.kaes3kuch3n.raytracer.Camera;
import de.kaes3kuch3n.raytracer.objects.*;
import de.kaes3kuch3n.raytracer.utilities.Material;
import de.kaes3kuch3n.raytracer.utilities.Skydome;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

public class SceneFileLoader {
    private SceneFile sceneFile;

    public SceneFileLoader(String filePath) {
        try {
            sceneFile = new Gson().fromJson(loadFileAsString(filePath), SceneFile.class);
        } catch (JsonSyntaxException | SceneFileLoadingError e) {
            throw new SceneFileLoadingError(e.getMessage());
        }
    }

    public CSG[] loadCSGs() {
        CSG[] csgs = new CSG[sceneFile.objects.length];

        for (int i = 0; i < sceneFile.objects.length; i++) {
            csgs[i] = loadObject(sceneFile.objects[i]);
        }

        return csgs;
    }

    public Light[] loadLights() {
        Light[] lights = new Light[sceneFile.lights.length];

        for (int i = 0; i < sceneFile.lights.length; i++) {
            SceneLight light = sceneFile.lights[i];
            Color color = new Color(light.color.red, light.color.green, light.color.blue);
            lights[i] = new Light(light.position, light.radius, color, light.intensity);
        }

        return lights;
    }

    public Camera loadCamera() {
        if (sceneFile.camera == null)
            throw new SceneFileLoadingError("No camera defined in scene file");
        SceneCamera camera = sceneFile.camera;
        return new Camera(camera.position, camera.focusPoint, camera.roll);
    }

    public Skydome loadSkydome() {
        if (sceneFile.skydomeFilePath == null || sceneFile.skydomeFilePath.isEmpty()) {
            throw new SceneFileLoadingError("No skydome file path defined in scene file");
        }
        try {
            return new Skydome(sceneFile.skydomeFilePath);
        } catch (IOException e) {
            throw new SceneFileLoadingError("Invalid skydome file path");
        }
    }

    private CSG loadObject(SceneObject object) {
        if (object.isCSG() == object.isQuadric())
            throw new SceneFileLoadingError("Invalid object configuration");
        // If object is CSG, load children recursively and combine them using the correct operator
        if (object.isCSG())
            return new CSG(loadObject(object.first), loadObject(object.second), object.operator);

        // No CSG, load quadric with material
        Quadric quadric;
        JsonMaterial jsonMaterial = object.material;
        Color color = new Color(jsonMaterial.albedo.red, jsonMaterial.albedo.green, jsonMaterial.albedo.blue);
        Material material = new Material(color, jsonMaterial.roughness, jsonMaterial.metalness, jsonMaterial.reflectivity,
                jsonMaterial.transparency, jsonMaterial.refractionIndex);
        // Create quadric based on object type
        switch (object.type) {
            case SPHERE:
                quadric = new Sphere(1, material);
                break;
            case PLANE:
                quadric = new Plane(0, 0, 1, 1, material);
                break;
            case CYLINDER:
                quadric = new Cylinder(1, 1, 0, 1, material);
                break;
            default:
                throw new SceneFileLoadingError("Unrecognized object type: " + object.type);
        }
        // Apply transformations
        quadric.rotateX(object.rotation.x).rotateY(object.rotation.y).rotateZ(object.rotation.z)
                .scale(object.scale.x, object.scale.y, object.scale.z)
                .translate(object.position.x, object.position.y, object.position.z);

        return new CSG(quadric);
    }

    private String loadFileAsString(String filePath) throws SceneFileLoadingError {
        System.out.println(Path.of(filePath).toAbsolutePath());
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String nextLine;
            while ((nextLine = reader.readLine()) != null) {
                builder.append(nextLine);
            }
            return builder.toString();
        } catch (FileNotFoundException e) {
            throw new SceneFileLoadingError("Couldn't find scene file: " + e.getMessage());
        } catch (IOException e) {
            throw new SceneFileLoadingError(e.getMessage());
        }
    }
}
