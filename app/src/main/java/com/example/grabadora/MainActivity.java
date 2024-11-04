package com.example.grabadora;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private MediaRecorder grabacion;
    private String archivoSalida = null;
    private Button btn_recorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_recorder = findViewById(R.id.btn_rec);
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1000);
        }
    }

    public void Recorder(View view) {
        if (grabacion == null) {
            archivoSalida = getExternalFilesDir(null).getAbsolutePath() + "/Grabacion.mp3";
            grabacion = new MediaRecorder();
            grabacion.setAudioSource(MediaRecorder.AudioSource.MIC);
            grabacion.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            grabacion.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            grabacion.setOutputFile(archivoSalida);

            try {
                grabacion.prepare();
                grabacion.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

            btn_recorder.setBackgroundResource(R.drawable.rec);
            Toast.makeText(getApplicationContext(), "Grabando...", Toast.LENGTH_SHORT).show();
        } else if (grabacion != null) {
            grabacion.stop();
            grabacion.release();
            grabacion = null;
            btn_recorder.setBackgroundResource(R.drawable.stop);
            Toast.makeText(getApplicationContext(), "Grabación finalizada", Toast.LENGTH_SHORT).show();
        }
    }

    public void reproducir(View view) {
        MediaPlayer mp = new MediaPlayer();
        try {
            mp.setDataSource(archivoSalida);
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mp.start();
        Toast.makeText(getApplicationContext(), "Reproduciendo...", Toast.LENGTH_SHORT).show();
    }
}
