package de.kaes3kuch3n.raytracer.objects;

import de.kaes3kuch3n.raytracer.utilities.Vector3;

public class RCObject {
    Vector3 position;

    RCObject(Vector3 position){
        this.position = position;
    }

    public Vector3 getPosition(){
        return this.position;
    }


    
}
