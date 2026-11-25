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

public class PlantIdentificationActivity extends AppCompatActivity {
    private ActivityResultLauncher<String> imagePickerLauncher;
    private Interpreter tflite;
    private Bitmap selectedBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            tflite = new Interpreter(loadModelFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_plant_identification);
        ImageView selectedPlantImage = findViewById(R.id.selectedPlantImage);
        Button chooseImageButton = findViewById(R.id.chooseImageButton);

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
}