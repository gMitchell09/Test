package com.gpv0001.virgil;

import android.app.Fragment;
import android.content.Context;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.gpv0001.virgil.R;
import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.InfoWindow;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

public class MainActivity extends FragmentActivity {
    public static class DetailsFragment extends android.support.v4.app.Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = (ViewGroup) inflater.inflate(R.layout.details_fragment, container, false);
            return view;
        }
    }

    public static class RoadFragment extends android.support.v4.app.Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = (ViewGroup) inflater.inflate(R.layout.road_fragment, container, false);
            return view;
        }
    }

    public static class HazardFragment extends android.support.v4.app.Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = (ViewGroup) inflater.inflate(R.layout.hazard_fragment, container, false);
            return view;
        }
    }

    public static class AidFragment extends android.support.v4.app.Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = (ViewGroup) inflater.inflate(R.layout.aid_fragment, container, false);
            return view;
        }
    }

    public static class NeedFragment extends android.support.v4.app.Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = (ViewGroup) inflater.inflate(R.layout.need_fragment, container, false);
            return view;
        }
    }

    private MapView mapView;
    private MapEventListener mapEventListener;
    private DetailsFragment m_detailsFragment;

    private RoadFragment m_roadFragment;
    private HazardFragment m_hazardFragment;
    private AidFragment m_aidFragment;
    private NeedFragment m_needFragment;

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
            }
        });
    }

    public void showAddDialog() {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                R.anim.slide_in_right, 0, 0, R.anim.slide_out_right
        ).add( R.id.activity_main, new DetailsFragment() ).addToBackStack( "animation" ).commit();
    }

    public void showRoadDialog(View view) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in_right, 0, 0, R.anim.slide_out_right
                ).add( R.id.activity_main, new RoadFragment() ).addToBackStack( "animation" ).commit();
    }

    public void showHazardDialog(View view) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in_right, 0, 0, R.anim.slide_out_right
                ).add( R.id.activity_main, new HazardFragment() ).addToBackStack( "animation" ).commit();
    }

    public void showAidDialog(View view) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in_right, 0, 0, R.anim.slide_out_right
                ).add( R.id.activity_main, new AidFragment() ).addToBackStack( "animation" ).commit();
    }

    public void showNeedDialog(View view) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in_right, 0, 0, R.anim.slide_out_right
                ).add( R.id.activity_main, new NeedFragment() ).addToBackStack( "animation" ).commit();
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
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.setTitle("asdf");
            markerOptions.setPosition(pos);

            map.addMarker(markerOptions);
        }

        public void onMapLongClick(LatLng pos) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.setTitle("fdsa");
            markerOptions.setPosition(pos);

            map.addMarker(markerOptions);

            mainActivity.showAddDialog();
        }
    }
}
