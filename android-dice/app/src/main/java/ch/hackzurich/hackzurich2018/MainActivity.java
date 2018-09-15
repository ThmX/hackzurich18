package ch.hackzurich.hackzurich2018;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class MainActivity extends Activity implements SensorEventListener {

    public class Dice {
        private int side;

        public Dice(int side) {
            this.side = side;
        }
    }

    public interface DiceService {
        @POST("hackzurich18_test1-endpoint_rotate_dice")
        Call<String> rotate(@Body Dice dice);
    }

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    private SideExtractor sideExtractor;
    private DiceService diceService;

    private int side;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sideExtractor = new SideExtractor();

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://europe-west1-thmx-ch.cloudfunctions.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        diceService = retrofit.create(DiceService.class);
    }

    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        side = 0;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            Log.d("xyz", "" + x + ", " + y + ", " + z);

            int side = sideExtractor.extract(x, y, z);
            Log.d("side", "" + side);

            if (side != this.side) {
                Log.i("dice", "Update from " + this.side + " to " + side);
                diceService.rotate(new Dice(side)).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.i("dice", "Response: " + response.message());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.i("dice", "Failure: " + t.getMessage());
                    }
                });
                this.side = side;
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
