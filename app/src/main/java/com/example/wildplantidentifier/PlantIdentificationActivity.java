package com.example.wildplantidentifier;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import android.net.Uri;
import android.widget.ImageView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.TextView;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import android.graphics.Bitmap;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import android.Manifest;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationTokenSource;


public class PlantIdentificationActivity extends AppCompatActivity {
    private ActivityResultLauncher<Void> cameraLauncher;
    private ActivityResultLauncher<String> imagePickerLauncher;
    private Interpreter tflite;
    private Bitmap selectedBitmap;
    private FusedLocationProviderClient fusedLocationClient;

    private double plantLatitude;
    private double plantLongitude;

    private boolean hasPlantLocation = false;
    private ActivityResultLauncher<String[]> locationPermissionLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            tflite = new Interpreter(loadModelFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_plant_identification);
        fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(this);
        ImageView selectedPlantImage = findViewById(R.id.selectedPlantImage);
        Button chooseImageButton = findViewById(R.id.chooseImageButton);
        locationPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                permissions -> {
                    Boolean fineLocation = permissions.get(
                            Manifest.permission.ACCESS_FINE_LOCATION);

                    Boolean coarseLocation = permissions.get(
                            Manifest.permission.ACCESS_COARSE_LOCATION);

                    if (Boolean.TRUE.equals(fineLocation) ||
                            Boolean.TRUE.equals(coarseLocation)) {

                        getPlantLocation();

                    } else {
                        hasPlantLocation = false;

                        Toast.makeText(
                                this,
                                "לא ניתנה הרשאת מיקום",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );


// יצירת בוחר התמונות
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        selectedPlantImage.setImageURI(uri);

                        try {
                            java.io.InputStream inputStream =
                                    getContentResolver().openInputStream(uri);

                            selectedBitmap = BitmapFactory.decodeStream(inputStream);
                            if (selectedBitmap != null) {
                                identifyPlant(selectedBitmap);
                            }

                            if (inputStream != null) {
                                inputStream.close();
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

// פתיחת הגלריה בלחיצה
        chooseImageButton.setOnClickListener(v -> {
            imagePickerLauncher.launch("image/*");
        });
        Button backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(v -> {
            finish();
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicturePreview(),
                bitmap -> {
                    if (bitmap != null) {
                        selectedBitmap = bitmap;
                        selectedPlantImage.setImageBitmap(bitmap);
                        identifyPlant(bitmap);

                        // קבלת המיקום שבו צולמה התמונה
                        checkLocationPermission();
                    }
                }
        );
        Button takePhotoButton = findViewById(R.id.takePhotoButton);

        takePhotoButton.setOnClickListener(v -> {
            cameraLauncher.launch(null);
        });
    }
    private MappedByteBuffer loadModelFile() throws IOException {
        FileInputStream inputStream = new FileInputStream(
                getAssets().openFd("plant_model_4classes.tflite").getFileDescriptor()
        );

        FileChannel fileChannel = inputStream.getChannel();

        long startOffset = getAssets()
                .openFd("plant_model_4classes.tflite")
                .getStartOffset();

        long declaredLength = getAssets()
                .openFd("plant_model_4classes.tflite")
                .getLength();

        return fileChannel.map(
                FileChannel.MapMode.READ_ONLY,
                startOffset,
                declaredLength
        );
    }
    private Bitmap prepareImage(Bitmap original) {
        return Bitmap.createScaledBitmap(original, 224, 224, true);
    }

    private ByteBuffer convertImageToByteBuffer(Bitmap bitmap) {

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(
                1 * 224 * 224 * 3 * 4
        );

        byteBuffer.order(ByteOrder.nativeOrder());

        for (int y = 0; y < 224; y++) {
            for (int x = 0; x < 224; x++) {

                int pixel = bitmap.getPixel(x, y);

                float red = ((pixel >> 16) & 0xFF) / 127.5f - 1.0f;
                float green = ((pixel >> 8) & 0xFF) / 127.5f - 1.0f;
                float blue = (pixel & 0xFF) / 127.5f - 1.0f;

                byteBuffer.putFloat(red);
                byteBuffer.putFloat(green);
                byteBuffer.putFloat(blue);
            }
        }

        byteBuffer.rewind();

        return byteBuffer;
    }
    private void identifyPlant(Bitmap originalBitmap) {

        if (tflite == null || originalBitmap == null) {
            return;
        }

        Bitmap resizedBitmap = prepareImage(originalBitmap);

        ByteBuffer inputBuffer =
                convertImageToByteBuffer(resizedBitmap);

        float[][] output = new float[1][4];

        tflite.run(inputBuffer, output);

        String[] classNames = {
                "כלנית מצויה",
                "חרצית עטורה",
                "רקפת מצויה",
                "צמח לא מוכר"
        };

        int bestIndex = 0;
        float bestConfidence = output[0][0];

        for (int i = 1; i < 4; i++) {
            if (output[0][i] > bestConfidence) {
                bestConfidence = output[0][i];
                bestIndex = i;
            }
        }

        String plantName = classNames[bestIndex];
        // בדיקה אם הביטחון נמוך מ-50%
        if (bestConfidence < 0.50f) {
            clearPlantDetails();

            TextView resultText = findViewById(R.id.identificationResult);
            resultText.setText(
                    "הזיהוי אינו ודאי\n" +
                            "ביטחון: " + String.format(
                            java.util.Locale.US,
                            "%.2f%%",
                            bestConfidence * 100
                    ) +
                            "\nנסה לצלם תמונה ברורה יותר"
            );

            return;
        }

// בדיקה אם הצמח שייך למחלקת הצמחים הלא מוכרים
        if (bestIndex == 3) {
            clearPlantDetails();

            TextView resultText = findViewById(R.id.identificationResult);
            resultText.setText(
                    "לא זוהה צמח מוכר\n" +
                            "ביטחון: " + String.format(
                            java.util.Locale.US,
                            "%.2f%%",
                            bestConfidence * 100
                    )
            );

            return;
        }


        ArrayList<Plant> plants = PlantData.getPlants();

        Plant identifiedPlant = null;

        for (Plant plant : plants) {
            if (plant.getHebrewName().equals(plantName)) {
                identifiedPlant = plant;
                break;
            }
        }

        TextView resultText = findViewById(R.id.identificationResult);
        TextView scientificNameText = findViewById(R.id.scientificNameText);
        TextView confidenceText = findViewById(R.id.confidenceText);
        TextView descriptionText = findViewById(R.id.descriptionText);
        TextView habitatText = findViewById(R.id.habitatText);
        TextView seasonText = findViewById(R.id.seasonText);
        TextView safetyText = findViewById(R.id.safetyText);

        if (identifiedPlant != null) {

            resultText.setText(identifiedPlant.getHebrewName());

            scientificNameText.setText(
                    identifiedPlant.getScientificName()
            );

            confidenceText.setText(
                    "ביטחון זיהוי: " + String.format(
                            java.util.Locale.US,
                            "%.2f%%",
                            bestConfidence * 100
                    )
            );

            descriptionText.setText(
                    identifiedPlant.getDescription()
            );

            habitatText.setText(
                    identifiedPlant.getHabitat()
            );

            seasonText.setText(
                    identifiedPlant.getFloweringSeason()
            );

            safetyText.setText(
                    identifiedPlant.getSafetyInfo()
            );

        } else {

            resultText.setText("לא נמצא מידע על הצמח במאגר");
        }

    }private void clearPlantDetails() {
        TextView scientificNameText = findViewById(R.id.scientificNameText);
        TextView confidenceText = findViewById(R.id.confidenceText);
        TextView descriptionText = findViewById(R.id.descriptionText);
        TextView habitatText = findViewById(R.id.habitatText);
        TextView seasonText = findViewById(R.id.seasonText);
        TextView safetyText = findViewById(R.id.safetyText);

        scientificNameText.setText("");
        confidenceText.setText("");
        descriptionText.setText("");
        habitatText.setText("");
        seasonText.setText("");
        safetyText.setText("");
    }
    private void checkLocationPermission() {

        boolean fineLocation = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;

        boolean coarseLocation = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;

        if (fineLocation || coarseLocation) {
            getPlantLocation();
        } else {
            locationPermissionLauncher.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }
    }
    @SuppressWarnings("MissingPermission")
    private void getPlantLocation() {

        boolean fineLocation = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;

        boolean coarseLocation = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;

        if (!fineLocation && !coarseLocation) {
            return;
        }

        CancellationTokenSource cancellationTokenSource =
                new CancellationTokenSource();

        fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.getToken()
        ).addOnSuccessListener(location -> {

            if (location != null) {

                plantLatitude = location.getLatitude();
                plantLongitude = location.getLongitude();

                hasPlantLocation = true;

                Toast.makeText(
                        this,
                        "המיקום נשמר בהצלחה",
                        Toast.LENGTH_SHORT
                ).show();

            } else {

                hasPlantLocation = false;

                Toast.makeText(
                        this,
                        "לא הצלחנו לקבל מיקום. בדוק שה-GPS מופעל",
                        Toast.LENGTH_LONG
                ).show();
            }

        }).addOnFailureListener(e -> {

            hasPlantLocation = false;

            Toast.makeText(
                    this,
                    "אירעה שגיאה בקבלת המיקום",
                    Toast.LENGTH_SHORT
            ).show();

        });
    }


}
