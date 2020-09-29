package com.realestate.dto;

public class HouseDTO extends FacilityDTO {

    private Integer numberOfStoreys;
    private boolean hasBackyard;
    private boolean hasGarden;

    public HouseDTO() {
    }

    public Integer getNumberOfStoreys() {
        return numberOfStoreys;
    }

    public void setNumberOfStoreys(Integer numberOfStoreys) {
        this.numberOfStoreys = numberOfStoreys;
    }

    public boolean isHasBackyard() {
        return hasBackyard;
    }

    public void setHasBackyard(boolean hasBackyard) {
        this.hasBackyard = hasBackyard;
    }

    public boolean isHasGarden() {
        return hasGarden;
    }

    public void setHasGarden(boolean hasGarden) {
        this.hasGarden = hasGarden;
    }
}
