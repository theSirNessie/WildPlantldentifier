package com.example.wildplantidentifier;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PlantDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_plant_details);

        String hebrewName = getIntent().getStringExtra("hebrewName");
        String scientificName = getIntent().getStringExtra("scientificName");
        String description = getIntent().getStringExtra("description");
        String habitat = getIntent().getStringExtra("habitat");
        String season = getIntent().getStringExtra("season");
        String safety = getIntent().getStringExtra("safety");
        String imageName = getIntent().getStringExtra("imageName");

// חיבור הרכיבים מהעיצוב לקוד
        ImageView plantImage = findViewById(R.id.detailPlantImage);
        TextView plantName = findViewById(R.id.detailPlantName);
        TextView scientificNameText = findViewById(R.id.detailScientificName);
        TextView descriptionText = findViewById(R.id.detailDescription);
        TextView habitatText = findViewById(R.id.detailHabitat);
        TextView seasonText = findViewById(R.id.detailSeason);
        TextView safetyText = findViewById(R.id.detailSafety);

// הצגת המידע
        plantName.setText(hebrewName);
        scientificNameText.setText(scientificName);
        descriptionText.setText("תיאור: " + description);
        habitatText.setText("אזור גידול: " + habitat);
        seasonText.setText("עונת פריחה: " + season);
        safetyText.setText("מידע בטיחותי: " + safety);

// טעינת תמונת הצמח
        if (imageName != null && !imageName.isEmpty()) {
            int imageResource = getResources().getIdentifier(
                    imageName,
                    "drawable",
                    getPackageName()
            );

            if (imageResource != 0) {
                plantImage.setImageResource(imageResource);
            }
        }


    }
}