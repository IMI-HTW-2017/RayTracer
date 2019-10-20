package de.kaes3kuch3n.raytracer;

import de.kaes3kuch3n.raytracer.objects.RCObject;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    private List<RCObject> rcObjects = new ArrayList<>();
    private Camera camera;

    public Scene(Camera camera, List<RCObject> rcObjects){
        this(camera);
        this.rcObjects.addAll(rcObjects);
    }

    public Scene(Camera camera){
        this.camera = camera;
    }

    public void addRCObject(RCObject rcObject){
        this.rcObjects.add(rcObject);
    }

    public Camera getCamera() {
        return camera;
    }
}
