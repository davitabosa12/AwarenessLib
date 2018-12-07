package br.ufc.great.awarenesslib;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.SnapshotClient;
import com.google.android.gms.awareness.snapshot.LocationResponse;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnSuccessListener;

import awarenesshelper.LocationFence;
import awarenesshelper.LocationMethod;

public class LocationMessageActivity extends AppCompatActivity {

    Place gotPlace;
    Button searchPlace, registerFence;
    double lat, lon;
    double pickLat, pickLon;
    String pickPlaceName;
    EditText edtPlaceName, edtReminder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_message);

        setTitle("PlaceReminder");

        searchPlace = findViewById(R.id.btn_search);
        registerFence = findViewById(R.id.btn_ok);
        edtPlaceName = findViewById(R.id.edt_found_place);
        edtReminder = findViewById(R.id.edt_reminder);

        SnapshotClient s = Awareness.getSnapshotClient(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        s.getLocation().addOnSuccessListener(new OnSuccessListener<LocationResponse>() {
            @Override
            public void onSuccess(LocationResponse locationResponse) {
                Location l = locationResponse.getLocation();
                lat = l.getLatitude();
                lon = l.getLongitude();
                addButtonFuncionality();
            }
        });
    }
    private void register(double lat, double lon, String message){
        if(message == null || message.isEmpty()){
            //nope
            return;
        }

    }

     /*private LocationMethod fromSpinner(){
       Spinner spinner = findViewById(R.id.spinner);
        String selected = (String)spinner.getSelectedItem();
        if(selected.equals("Entering")){
            return LocationMethod.LOCATION_ENTERING;
        } else if(selected.equals("Exiting")){
            return LocationMethod.LOCATION_EXITING;
        } else {
            return LocationMethod.LOCATION_IN;
        }
    }*/

    private void addButtonFuncionality(){
        searchPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(getParent()),1);
                } catch (GooglePlayServicesRepairableException e) {
                    Toast.makeText(getApplicationContext(),"GMS not available.",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"GMS not available.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                pickLat = place.getLatLng().latitude;
                pickLon = place.getLatLng().longitude;
                pickPlaceName = place.getName().toString();
                edtPlaceName.setText(pickPlaceName);
            }
        }

    }
}
