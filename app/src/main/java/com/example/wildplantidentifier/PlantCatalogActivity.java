package com.example.wildplantidentifier;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;


public class PlantCatalogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_plant_catalog);

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
                ""
        ));

        plants.add(new Plant(
                "חרצית עטורה",
                "Glebionis coronaria",
                "צמח חד-שנתי בעל פרחים דמויי חרצית",
                "שטחים פתוחים",
                "חורף ואביב",
                "אין להסתמך על זיהוי בתמונה כדי לקבוע אכילות",
                ""
        ));

        ArrayAdapter<Plant> adapter = new ArrayAdapter<Plant>(
                this,
                android.R.layout.simple_list_item_1,
                plants
        ) {
            @Override
            public android.view.View getView(
                    int position,
                    android.view.View convertView,
                    android.view.ViewGroup parent) {

                android.view.View view = super.getView(
                        position, convertView, parent);

                android.widget.TextView textView =
                        (android.widget.TextView) view;

                Plant plant = getItem(position);
                textView.setText(plant.getHebrewName());

                return view;
            }
        };

        plantListView.setAdapter(adapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}