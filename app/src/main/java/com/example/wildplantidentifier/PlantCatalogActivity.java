package com.example.wildplantidentifier;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;



public class PlantCatalogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_plant_catalog);
        Button backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(v -> {
            finish();
        });

        ListView plantListView = findViewById(R.id.plantListView);

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


        ArrayAdapter<Plant> adapter = new ArrayAdapter<Plant>(
                this,
                R.layout.plant_list_item,
                plants
        ) {
            @Override
            public View getView(
                    int position,
                    View convertView,
                    ViewGroup parent) {

                View view = convertView;

                if (view == null) {
                    view = getLayoutInflater().inflate(
                            R.layout.plant_list_item,
                            parent,
                            false
                    );
                }

                Plant plant = getItem(position);

                ImageView plantImage = view.findViewById(R.id.plantImage);
                TextView plantName = view.findViewById(R.id.plantName);

                plantName.setText(plant.getHebrewName());

                int imageResource = 0;

                if (plant.getImageName() != null
                        && !plant.getImageName().isEmpty()) {

                    imageResource = getResources().getIdentifier(
                            plant.getImageName(),
                            "drawable",
                            getPackageName()
                    );
                }

                if (imageResource != 0) {
                    plantImage.setImageResource(imageResource);
                    plantImage.setVisibility(View.VISIBLE);
                } else {
                    plantImage.setImageDrawable(null);
                    plantImage.setVisibility(View.INVISIBLE);
                }

                return view;
            }
        };

        plantListView.setAdapter(adapter);
        plantListView.setOnItemClickListener((parent, view, position, id) -> {

            Plant selectedPlant = plants.get(position);

            Intent intent = new Intent(
                    PlantCatalogActivity.this,
                    PlantDetailsActivity.class
            );

            intent.putExtra("hebrewName", selectedPlant.getHebrewName());
            intent.putExtra("scientificName", selectedPlant.getScientificName());
            intent.putExtra("description", selectedPlant.getDescription());
            intent.putExtra("habitat", selectedPlant.getHabitat());
            intent.putExtra("season", selectedPlant.getFloweringSeason());
            intent.putExtra("safety", selectedPlant.getSafetyInfo());
            intent.putExtra("imageName", selectedPlant.getImageName());

            startActivity(intent);
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}