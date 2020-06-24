package sugarrain.oss.danbi_oss5;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView clock;
    TextView location;
    ImageButton refresh;
    RecyclerView recyclerView;
    ExtendedFloatingActionButton add_memo;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clock = findViewById(R.id.clock);
        location = findViewById(R.id.tv_location);
        refresh = findViewById(R.id.btn_refresh);
        recyclerView = findViewById(R.id.recyclerView);
        add_memo = findViewById(R.id.btn_add_memo);

        dbHelper = new DBHelper(this, DBHelper.dbName, null, DBHelper.dbVersion);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            }
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.interrupted()) {
                    try {
                        Thread.sleep(1000); //1초 간격으로 실행
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Calendar c = Calendar.getInstance();
                                SimpleDateFormat sdf = new SimpleDateFormat("a hh:mm:ss");
                                clock.setText(sdf.format(c.getTime()));
                            }
                        });
                    } catch (InterruptedException e) {
                        // error
                    }
                }
            }
        }).start();

        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final Geocoder geocoder = new Geocoder(MainActivity.this);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
                    } else {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
                    }
                } else {
                    Criteria criteria = new Criteria();
                    criteria.setAccuracy(Criteria.ACCURACY_LOW);
                    criteria.setPowerRequirement(Criteria.POWER_LOW);
                    lm.requestSingleUpdate(criteria, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location l) {
                            try {
                                List<Address> address = geocoder.getFromLocation(l.getLatitude(), l.getLongitude(), 10);
                                if (address.size() == 0) {
                                    location.setText("해당되는 주소 정보 없음");
                                } else {
                                    location.setText(address.get(0).getAddressLine(0));
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onProviderDisabled(String provider) {
                        }

                        @Override
                        public void onProviderEnabled(String provider) {
                        }

                        @Override
                        public void onStatusChanged(String provider, int status,
                                                    Bundle extras) {
                        }
                    }, null);

                }
            }
        });

        List<MemoData> datas = dbHelper.getAllData();
        MemoAdapter adapter = new MemoAdapter(datas);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));




    }

}