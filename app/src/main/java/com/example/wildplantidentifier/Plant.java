
package com.example.wildplantidentifier;

public class Plant {

    private String hebrewName;
    private String scientificName;
    private String description;
    private String habitat;
    private String floweringSeason;
    private String safetyInfo;
    private String imageName;

    public Plant(String hebrewName, String scientificName,
                 String description, String habitat,
                 String floweringSeason, String safetyInfo,String imageName) {

        this.hebrewName = hebrewName;
        this.scientificName = scientificName;
        this.description = description;
        this.habitat = habitat;
        this.floweringSeason = floweringSeason;
        this.safetyInfo = safetyInfo;
        this.imageName = imageName;

    }

    public String getHebrewName() {
        return hebrewName;
    }

    public String getScientificName() {
        return scientificName;
    }

    public String getDescription() {
        return description;
    }

    public String getHabitat() {
        return habitat;
    }

    public String getFloweringSeason() {
        return floweringSeason;
    }

    public String getSafetyInfo() {
        return safetyInfo;
    }
    public String getImageName() {
        return imageName;
    }

    public void setHebrewName(String hebrewName) {
        this.hebrewName = hebrewName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setHabitat(String habitat) {
        this.habitat = habitat;
    }

    public void setFloweringSeason(String floweringSeason) {
        this.floweringSeason = floweringSeason;
    }

    public void setSafetyInfo(String safetyInfo) {
        this.safetyInfo = safetyInfo;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    @Override
    public String toString() {
        return "Plant{" +
                "hebrewName='" + hebrewName + '\'' +
                ", scientificName='" + scientificName + '\'' +
                ", description='" + description + '\'' +
                ", habitat='" + habitat + '\'' +
                ", floweringSeason='" + floweringSeason + '\'' +
                ", safetyInfo='" + safetyInfo + '\'' +
                ", imageName='" + imageName + '\'' +
                '}';
    }
}
