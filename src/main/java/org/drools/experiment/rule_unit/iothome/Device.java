package org.drools.experiment.rule_unit.iothome;


public class Device {
    private String name;
    private String status;

    public Device(String name, String status) {
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        System.out.println("Device "+name+" is now "+status+" .");
        this.status = status;
    }
    
}
