
package com.example.wildplantidentifier;

import java.util.ArrayList;

public class PlantData {

    public static ArrayList<Plant> getPlants() {

        ArrayList<Plant> plants = new ArrayList<>();

        plants.add(new Plant(
                "כלנית מצויה",
                "Anemone coronaria",
                "צמח בר בעל פרחים צבעוניים",
                "שטחים פתוחים",
                "חורף ואביב",
                "אין להסתמך על זיהוי בתמונה כדי לקבוע אכילות",
                "anemone"
        ));

        plants.add(new Plant(
                "רקפת מצויה",
                "Cyclamen persicum",
                "צמח בעל פרחים הפונים כלפי מטה",
                "אזורים סלעיים ומוצלים",
                "חורף",
                "אין לאכול ללא זיהוי מוסמך",
                "cyclamen"
        ));

        plants.add(new Plant(
                "חרצית עטורה",
                "Glebionis coronaria",
                "צמח חד-שנתי בעל פרחים דמויי חרצית",
                "שטחים פתוחים",
                "חורף ואביב",
                "אין להסתמך על זיהוי בתמונה כדי לקבוע אכילות",
                "crown_daisy"
        ));

        return plants;
    }
}
