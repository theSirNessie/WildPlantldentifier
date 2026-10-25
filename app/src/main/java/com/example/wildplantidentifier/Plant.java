
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

    public static void main(String[] args) {
        Plant anemone = new Plant(
                "כלנית מצויה",
                "Anemone coronaria",
                "צמח בר בעל פרחים צבעוניים",
                "שטחים פתוחים",
                "חורף ואביב",
                "אין להסיק על אכילות לפי זיהוי בתמונה",
                "Anemone"
        );
    }
}
