package com.gpv0001.virgil;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.services.commons.utils.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class MainActivity extends FragmentActivity {
    public static class DetailsFragment extends android.support.v4.app.Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = (ViewGroup) inflater.inflate(R.layout.details_fragment, container, false);
            return view;
        }
    }

    public class RoadMarker {
        public int hazardIndex;
        public String hazard;
        public int severityIndex;
        public String severity;

        public LatLng position;
        public RoadMarker(LatLng pos) {
            position = pos;
        }
    }

    public class HazardMarker {
        public int hazardTypeIndex;
        public String hazardType;
        public int severityIndex;
        public String severity;
        public String details;

        public LatLng position;
        public HazardMarker(LatLng pos) {
            position = pos;
        }
    }

    public class AidMarker {
        public String location;
        public String description;
        public String services;

        public LatLng position;
        public AidMarker(LatLng pos) {
            position = pos;
        }
    }

    public class NeedMarker {
        public int affectedIndex;
        public String affected;
        public int severityIndex;
        public String severity;

        public LatLng position;
        public NeedMarker(LatLng pos) {
            position = pos;
        }
    }

    public static class RoadFragment extends android.support.v4.app.Fragment {
        private Spinner m_roadHazard;
        private Spinner m_severity;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = (ViewGroup) inflater.inflate(R.layout.road_fragment, container, false);
            m_roadHazard = (Spinner) view.findViewById(R.id.road_hazard_spinner);
            m_severity = (Spinner) view.findViewById(R.id.road_severity_spinner);

            return view;
        }

        public void setHazardIndex(int index) {
            m_roadHazard.setSelection(index);
        }

        public int getHazardIndex() {
            return m_roadHazard.getSelectedItemPosition();
        }

        public String getHazard() {
            return (String)m_roadHazard.getSelectedItem();
        }

        public void setSeverityIndex(int index) {
            m_severity.setSelection(index);
        }

        public int getSeverityIndex() {
            return m_severity.getSelectedItemPosition();
        }

        public String getSeverity() {
            return (String)m_severity.getSelectedItem();
        }

        public void populateWithMarker(RoadMarker rm) {
            m_roadHazard.setSelection(rm.hazardIndex);
            m_severity.setSelection(rm.severityIndex);
        }

        public void populateMarker(RoadMarker rm) {
            rm.hazard = getHazard();
            rm.hazardIndex = getHazardIndex();
            rm.severity = getSeverity();
            rm.severityIndex = getSeverityIndex();
        }
    }

    public static class HazardFragment extends android.support.v4.app.Fragment {
        private Spinner m_type;
        private Spinner m_severity;
        private EditText m_details;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = (ViewGroup) inflater.inflate(R.layout.hazard_fragment, container, false);
            m_type = (Spinner)view.findViewById(R.id.hazard_spinner);
            m_severity = (Spinner)view.findViewById(R.id.severity_spinner);
            m_details = (EditText)view.findViewById(R.id.details);
            return view;
        }

        public void setHazardTypeIndex(int index) {
            m_type.setSelection(index);
        }

        public int getHazardTypeIndex() {
            return m_type.getSelectedItemPosition();
        }

        public String getHazardType() {
            return (String)m_type.getSelectedItem();
        }

        public void setSeverityIndex(int index) {
            m_severity.setSelection(index);
        }

        public int getSeverityIndex() {
            return m_severity.getSelectedItemPosition();
        }

        public String getSeverity() {
            return (String)m_severity.getSelectedItem();
        }

        public void setDetails(String details) {
            m_details.setText(details);
        }

        public String getDetails() {
            return m_details.getText().toString();
        }

        public void populateWithMarker(HazardMarker rm) {
            m_type.setSelection(rm.hazardTypeIndex);
            m_severity.setSelection(rm.severityIndex);
            m_details.setText(rm.details);
        }

        public void populateMarker(HazardMarker rm) {
            rm.details = getDetails();
            rm.hazardType = getHazardType();
            rm.hazardTypeIndex = getHazardTypeIndex();
            rm.severity = getSeverity();
            rm.severityIndex = getSeverityIndex();
        }
    }

    public static class AidFragment extends android.support.v4.app.Fragment {
        private EditText m_location;
        private EditText m_description;
        private EditText m_services;

        public void populateWithMarker(AidMarker rm) {
            m_location.setText(rm.location);
            m_description.setText(rm.description);
            m_services.setText(rm.services);
        }

        public void populateMarker(AidMarker rm) {
            rm.location = getLocation();
            rm.description = getDescription();
            rm.services = getServices();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = (ViewGroup) inflater.inflate(R.layout.aid_fragment, container, false);
            m_location = (EditText) view.findViewById(R.id.txt_location);
            m_description = (EditText) view.findViewById(R.id.txt_description);
            m_services = (EditText) view.findViewById(R.id.txt_services);

            return view;
        }

        public void setLocation(String location) {
            m_location.setText(location);
        }
        public String getLocation() {
            return m_location.getText().toString();
        }

        public void setDescription(String description) {
            m_description.setText(description);
        }

        public String getDescription() {
            return m_description.getText().toString();
        }

        public void setServices(String services) {
            m_services.setText(services);
        }

        public String getServices() {
            return m_services.getText().toString();
        }
    }

    public static class NeedFragment extends android.support.v4.app.Fragment {
        private Spinner m_numAffected;
        private Spinner m_severity;

        public void populateWithMarker(NeedMarker rm) {
            m_numAffected.setSelection(rm.affectedIndex);
            m_severity.setSelection(rm.severityIndex);
        }

        public void populateMarker(NeedMarker rm) {
            rm.affectedIndex = getNumAffectedIndex();
            rm.affected = getNumAffected();
            rm.severity = getSeverity();
            rm.severityIndex = getSeverityIndex();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = (ViewGroup) inflater.inflate(R.layout.need_fragment, container, false);
            m_numAffected = (Spinner) view.findViewById(R.id.need_spinner);
            m_severity = (Spinner) view.findViewById(R.id.need_severity_spinner);

            return view;
        }

        public void setNumAffected(int index) {
            m_numAffected.setSelection(index);
        }

        public int getNumAffectedIndex() {
            return m_numAffected.getSelectedItemPosition();
        }

        public String getNumAffected() {
            return (String)m_numAffected.getSelectedItem();
        }

        public void setSeverity(int index) {
            m_severity.setSelection(index);
        }

        public int getSeverityIndex() {
            return m_severity.getSelectedItemPosition();
        }

        public String getSeverity() {
            return (String)m_severity.getSelectedItem();
        }
    }

    private MapView mapView;
    private MapEventListener mapEventListener;
    private DetailsFragment m_detailsFragment;

    private RoadFragment m_roadFragment;
    private HazardFragment m_hazardFragment;
    private AidFragment m_aidFragment;
    private NeedFragment m_needFragment;

    private LatLng m_mostRecentPoint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        MapboxAccountManager.start(this, getString(R.string.access_token));

        // This contains the MapView in XML and needs to be called after the account manager
        setContentView(R.layout.activity_main);

        m_detailsFragment = (DetailsFragment) getSupportFragmentManager().findFragmentById( R.id.fragment_details );
        m_roadFragment= (RoadFragment) getSupportFragmentManager().findFragmentById( R.id.fragment_road );
        m_hazardFragment= (HazardFragment) getSupportFragmentManager().findFragmentById( R.id.fragment_hazard );
        m_aidFragment= (AidFragment) getSupportFragmentManager().findFragmentById( R.id.fragment_aid );
        m_needFragment= (NeedFragment) getSupportFragmentManager().findFragmentById( R.id.fragment_need );

        getSupportFragmentManager().beginTransaction()
                .hide(m_detailsFragment)
                .hide(m_roadFragment)
                .hide(m_hazardFragment)
                .hide(m_aidFragment)
                .hide(m_needFragment)
                .commit();

        final InfoView infoView = new InfoView(this.getApplicationContext(), null);
        infoView.setVisibility(View.VISIBLE);

        final InfoWindowAdapter iva = new InfoWindowAdapter(infoView);

        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        final MainActivity t = this;
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                mapEventListener = new MapEventListener(mapboxMap, t);
                mapboxMap.setInfoWindowAdapter(iva);

                mapboxMap.setOnMapClickListener(mapEventListener);
                mapboxMap.setOnMapLongClickListener(mapEventListener);

                CameraPosition cam = new CameraPosition.Builder().target(new LatLng(34.73, -86.58)).zoom(12).build();
                mapboxMap.setCameraPosition(cam);

                addDataFromAssets("Hospitals", R.drawable.ic_aid, mapboxMap);
                addDataFromAssets("UrgentCare", R.drawable.ic_aid, mapboxMap);
//                addDataFromAssets("NursingHomes", R.drawable.ic_people, mapboxMap);
//                addDataFromAssets("SchoolsInMadisonCounty", R.drawable.ic_people, mapboxMap);
//                addDataFromAssets("TrailerParks", R.drawable.ic_people, mapboxMap);


                new DrawGeoJson(mapboxMap).execute();
            }
        });
    }

    // fileName, just the name no type
    public void addDataFromAssets(String fileName, int iconIndex, MapboxMap mapboxmap) {
        Log.e("Resource: ", "" + getResources().getIdentifier(fileName, "raw", getPackageName()));

        try {
            // Open and parse CSV data files
            InputStream ins = getResources().getAssets().open(fileName + ".csv");//getResources().openRawResource(getResources().getIdentifier("Hospitals", "raw", getPackageName()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(ins));

            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                List<String> csv = CSVReader.parseLine(csvLine);
                // Create an Icon object for the marker to use
                IconFactory iconFactory = IconFactory.getInstance(MainActivity.this);
                Drawable iconDrawable = ContextCompat.getDrawable(MainActivity.this, iconIndex);
                Icon icon = iconFactory.fromDrawable(iconDrawable);

                // Add the custom icon marker to the map
                mapboxmap.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.parseDouble(csv.get(1)), Double.parseDouble(csv.get(0))))
                        .title(csv.get(2))
                        .snippet(csv.get(3))
                        .icon(icon));
            }
        } catch (IOException e) {
            System.out.println("Error reading from data file!");
        }
    }

    public void showAddDialog(LatLng pos) {
        m_mostRecentPoint = pos;

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                R.anim.slide_in_right, 0, 0, R.anim.slide_out_right
        ).show( m_detailsFragment ).addToBackStack( "animation" ).commit();
    }

    public void showRoadDialog(View view) {
        getSupportFragmentManager()
                .beginTransaction()
                .hide(m_detailsFragment)
                .setCustomAnimations(
                        R.anim.slide_in_right, 0, 0, R.anim.slide_out_right
                ).show( m_roadFragment ).addToBackStack( "animation" ).commit();
    }

    public void showHazardDialog(View view) {
        getSupportFragmentManager()
                .beginTransaction()
                .hide(m_detailsFragment)
                .setCustomAnimations(
                        R.anim.slide_in_right, 0, 0, R.anim.slide_out_right
                ).show( m_hazardFragment ).addToBackStack( "animation" ).commit();
    }

    public void showAidDialog(View view) {
        getSupportFragmentManager()
                .beginTransaction()
                .hide(m_detailsFragment)
                .setCustomAnimations(
                        R.anim.slide_in_right, 0, 0, R.anim.slide_out_right
                ).show( m_aidFragment ).addToBackStack( "animation" ).commit();
    }

    public void showNeedDialog(View view) {
        getSupportFragmentManager()
                .beginTransaction()
                .hide(m_detailsFragment)
                .setCustomAnimations(
                        R.anim.slide_in_right, 0, 0, R.anim.slide_out_right
                ).show( m_needFragment ).addToBackStack( "animation" ).commit();
    }

    public void onSaveRoadClick(View view) {
        RoadMarker roadMarker = new RoadMarker(m_mostRecentPoint);
        m_roadFragment.populateMarker(roadMarker);

        final RoadMarker bs = roadMarker;
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                IconFactory iconFactory = IconFactory.getInstance(MainActivity.this);
                Drawable iconDrawable = null;
                switch (bs.severityIndex) {
                    case 0:
                        iconDrawable = ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_road_hazard_high);
                        break;
                    case 1:
                        iconDrawable = ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_road_hazard_medium);
                        break;
                    case 2:
                        iconDrawable = ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_road_hazard_low);
                        break;
                }
                Icon icon = iconFactory.fromDrawable(iconDrawable);

                mapboxMap.addMarker(
                        new MarkerOptions()
                            .title(bs.hazard)
                            .position(bs.position)
                            .icon(icon));
            }
        });

        getSupportFragmentManager()
                .beginTransaction()
                .hide(m_roadFragment)
                .commit();
    }

    public void onSaveHazardClick(View view) {
        HazardMarker haz = new HazardMarker(m_mostRecentPoint);
        m_hazardFragment.populateMarker(haz);

        final HazardMarker bs = haz;
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                IconFactory iconFactory = IconFactory.getInstance(MainActivity.this);
                Drawable iconDrawable = null;
                switch (bs.severityIndex) {
                    case 0:
                        iconDrawable = ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_hazard_high);
                        break;
                    case 1:
                        iconDrawable = ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_hazard_medium);
                        break;
                    case 2:
                        iconDrawable = ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_hazard_low);
                        break;
                }
                Icon icon = iconFactory.fromDrawable(iconDrawable);

                mapboxMap.addMarker(
                        new MarkerOptions()
                                .title(bs.hazardType)
                                .position(bs.position)
                                .icon(icon));
            }
        });

        getSupportFragmentManager()
                .beginTransaction()
                .hide(m_hazardFragment)
                .commit();
    }

    public void onSaveAidClick(View view) {
        AidMarker marker = new AidMarker(m_mostRecentPoint);
        m_aidFragment.populateMarker(marker);

        final AidMarker bs = marker;
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                IconFactory iconFactory = IconFactory.getInstance(MainActivity.this);
                Drawable iconDrawable = ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_aid);
                Icon icon = iconFactory.fromDrawable(iconDrawable);

                mapboxMap.addMarker(
                        new MarkerOptions()
                                .title(bs.description)
                                .position(bs.position)
                                .icon(icon));
            }
        });

        getSupportFragmentManager()
                .beginTransaction()
                .hide(m_aidFragment)
                .commit();
    }

    public void onSaveNeedClick(View view) {
        NeedMarker marker = new NeedMarker(m_mostRecentPoint);
        m_needFragment.populateMarker(marker);

        final NeedMarker bs = marker;
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                IconFactory iconFactory = IconFactory.getInstance(MainActivity.this);
                Drawable iconDrawable = null;
                switch (bs.severityIndex) {
                    case 0:
                        iconDrawable = ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_need_high);
                        break;
                    case 1:
                        iconDrawable = ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_need_medium);
                        break;
                    case 2:
                        iconDrawable = ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_need_low);
                        break;
                }
                Icon icon = iconFactory.fromDrawable(iconDrawable);

                mapboxMap.addMarker(
                        new MarkerOptions()
                                .title(bs.affected)
                                .position(bs.position)
                                .icon(icon));
            }
        });

        getSupportFragmentManager()
                .beginTransaction()
                .hide(m_needFragment)
                .commit();
    }

    public void onDismissClick(View view) {
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction().commit();
    }

    // Add the mapView lifecycle to the activity's lifecycle methods
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    public class InfoWindowAdapter implements MapboxMap.InfoWindowAdapter {
        private InfoView infoView;

        public InfoWindowAdapter(InfoView view) {
            infoView = view;
        }

        @Nullable
        public View getInfoWindow(@NonNull Marker marker) {
            return (View)infoView;
        }
    }

    public class MapEventListener implements MapboxMap.OnMapClickListener, MapboxMap.OnMapLongClickListener {
        private MapboxMap map;
        private MainActivity mainActivity;

        public MapEventListener(MapboxMap map, MainActivity activity) {
            this.map = map;
            mainActivity = activity;
        }
        public void onMapClick(LatLng pos) {
//            MarkerOptions markerOptions = new MarkerOptions();
//            markerOptions.setTitle("asdf");
//            markerOptions.setPosition(pos);
//
//            map.addMarker(markerOptions);
        }

        public void onMapLongClick(LatLng pos) {
            mainActivity.showAddDialog(pos);
        }
    }

    private class DrawGeoJson extends AsyncTask<Void, Void, List<LatLng>> {
        private MapboxMap mapboxMap;
        public DrawGeoJson(MapboxMap map) {
            super();
            mapboxMap = map;
        }

        @Override
        protected List<LatLng> doInBackground(Void... voids) {

            ArrayList<LatLng> points = new ArrayList<>();

            try {
                // Load GeoJSON file
                InputStream inputStream = getAssets().open("CensusTractsMadison.geojson");
                BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
                StringBuilder sb = new StringBuilder();
                int cp;
                while ((cp = rd.read()) != -1) {
                    sb.append((char) cp);
                }

                inputStream.close();

                // Parse JSON
                JSONObject json = new JSONObject(sb.toString());
                JSONArray features = json.getJSONArray("features");
                JSONObject feature = features.getJSONObject(0);
                JSONObject geometry = feature.getJSONObject("geometry");
                if (geometry != null) {
                    String type = geometry.getString("type");

                    // Our GeoJSON only has one feature: a line string
                    if (!TextUtils.isEmpty(type) && type.equalsIgnoreCase("LineString")) {

                        // Get the Coordinates
                        JSONArray coords = geometry.getJSONArray("coordinates");
                        for (int lc = 0; lc < coords.length(); lc++) {
                            JSONArray coord = coords.getJSONArray(lc);
                            LatLng latLng = new LatLng(coord.getDouble(1), coord.getDouble(0));
                            points.add(latLng);
                        }
                    }
                }
            } catch (Exception exception) {
                Log.e("GeoJSON: ", exception.toString());
            }

            return points;
        }

        @Override
        protected void onPostExecute(List<LatLng> points) {
            super.onPostExecute(points);

            if (points.size() > 0) {

                // Draw polyline on map
                mapboxMap.addPolyline(new PolylineOptions()
                        .addAll(points)
                        .color(Color.parseColor("#3bb2d0"))
                        .width(2));
            }
        }
    }
}
