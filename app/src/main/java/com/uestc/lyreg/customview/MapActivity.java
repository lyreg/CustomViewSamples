package com.uestc.lyreg.customview;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.cloud.CloudItem;
import com.amap.api.services.cloud.CloudItemDetail;
import com.amap.api.services.cloud.CloudResult;
import com.amap.api.services.cloud.CloudSearch;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapActivity extends CheckPermissionsActivity
        implements LocationSource, AMapLocationListener,
        AMap.OnMapLoadedListener, CloudSearch.OnCloudSearchListener,
        AMap.OnMarkerClickListener{

    private final List<LatLng> DefaultMarkers = new ArrayList<LatLng>() {{
        add(new LatLng(30.745559, 103.924581));
        add(new LatLng(30.743666, 103.927792)); //综合体育馆
        add(new LatLng(30.743482, 103.923994)); //红旗超市
    }};

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.mapview)
    MapView mMapView;

    private AMap mAmap;
    private AMapLocationClientOption locationClientOption;
    private AMapLocationClient locationClient;
    private OnLocationChangedListener mListener;
    private CloudSearch mCloudSearch;
    private CloudSearch.Query mQuery;

    private final String mTableID = "577334db7bbf193264690e4b";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);

        setupViews(savedInstanceState);
        setupWindowAnimations();
    }

    private void setupViews(Bundle savedInstanceState) {
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        mMapView.onCreate(savedInstanceState);

        if(mAmap == null) {
            mAmap = mMapView.getMap();
            setupMap();
        }
    }

    private void setupWindowAnimations() {
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.RIGHT);
        slide.setDuration(400);

        getWindow().setEnterTransition(slide);
        getWindow().setReturnTransition(slide);
    }

    private void setupMap() {
        mAmap.setOnMapLoadedListener(this);
        mAmap.setOnMarkerClickListener(this);

        mAmap.setLocationSource(this);
        mAmap.getUiSettings().setMyLocationButtonEnabled(true);
        mAmap.setMyLocationEnabled(true);

        mAmap.getUiSettings().setScaleControlsEnabled(true);

//        addmarkersToMap();

    }

    private void searchCloud(LatLonPoint point) {
        if(mCloudSearch == null) {
            mCloudSearch = new CloudSearch(this);
            mCloudSearch.setOnCloudSearchListener(this);
        }

        CloudSearch.SearchBound bound = new CloudSearch.SearchBound(point, 4000);

        try {
            mQuery = new CloudSearch.Query(mTableID, "", bound);
        } catch (AMapException e) {
            e.printStackTrace();
        }

        mCloudSearch.searchCloudAsyn(mQuery);
    }

    private void addmarkersToMap() {
        for(LatLng marker : DefaultMarkers) {
            Log.e("addmarkersToMap", marker.toString());
            mAmap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                    .position(marker).draggable(false));
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
        deactivate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
//        deactivate();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if(mListener != null && aMapLocation != null) {
            if(aMapLocation.getErrorCode() == 0) {
                Log.e("onLocationChanged", "address => " + aMapLocation.getAddress());
                mAmap.moveCamera(CameraUpdateFactory.zoomTo(17));
                mListener.onLocationChanged(aMapLocation);

                searchCloud(new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude()));
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode()+ ": " + aMapLocation.getErrorInfo();
                Log.e("AmapErr",errText);
                showAlert(errText);
            }
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (locationClient == null) {
            locationClient = new AMapLocationClient(getApplicationContext());
            locationClientOption = new AMapLocationClientOption();

            locationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            locationClientOption.setOnceLocation(true); //单次定位

            locationClient.setLocationOption(locationClientOption);
            locationClient.setLocationListener(this);
        }
        locationClient.startLocation();

        mAmap.clear();
    }

    @Override
    public void deactivate() {
        mListener = null;
        if(locationClient != null) {
            locationClient.stopLocation();
            locationClient.onDestroy();
        }
        locationClient = null;
    }

    @Override
    public void onMapLoaded() {
        mAmap.moveCamera(CameraUpdateFactory.zoomTo(17));
    }

    @Override
    public void onCloudSearched(CloudResult cloudResult, int i) {
        Log.e("onCloudSearched", "rCode => " + i);
        List<CloudItem> itemList = cloudResult.getClouds();
        for(CloudItem item : itemList) {
            Log.e("addmarkersToMap", item.toString());
            Marker mark = mAmap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                    .position(new LatLng(item.getLatLonPoint().getLatitude(),
                            item.getLatLonPoint().getLongitude()))
                    .title(item.getTitle())
                    .snippet(item.getSnippet())
                    .draggable(false));
//            mark.showInfoWindow();
        }
    }

    @Override
    public void onCloudItemDetailSearched(CloudItemDetail cloudItemDetail, int i) {
        Log.e("onCloudItemSearched", cloudItemDetail.toString());
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String msg ="title => " + marker.getTitle() + " snippet => " + marker.getSnippet();
        showAlert(msg);
//        marker.showInfoWindow();
        return true;
    }

    private void showAlert(String errMsg) {
        Toast.makeText(this, errMsg, Toast.LENGTH_SHORT).show();
    }
}
