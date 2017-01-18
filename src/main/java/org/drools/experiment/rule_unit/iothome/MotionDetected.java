package org.drools.experiment.rule_unit.iothome;


public class MotionDetected {
    private String location;

    public MotionDetected(String location) {
        this.location = location;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
}
