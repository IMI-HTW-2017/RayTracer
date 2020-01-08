package de.kaes3kuch3n.raytracer.utilities.scenefile;

import de.kaes3kuch3n.raytracer.utilities.CSGOperator;
import de.kaes3kuch3n.raytracer.utilities.Vector3;

public class SceneObject {
    // CSG fields
    public SceneObject first;
    public SceneObject second;
    public CSGOperator operator;

    // Object fields
    public ObjectType type;
    public Vector3 position;
    public Vector3 rotation;
    public Vector3 scale;
    public JsonMaterial material;

    public boolean isQuadric() {
        return type != null && position != null && rotation != null &&
                scale != null && material != null && material.albedo != null;
    }

    public boolean isCSG() {
        return first != null && second != null && operator != null;
    }
}
