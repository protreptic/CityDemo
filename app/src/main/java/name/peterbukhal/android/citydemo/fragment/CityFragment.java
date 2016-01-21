package name.peterbukhal.android.citydemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import name.peterbukhal.android.citydemo.R;
import name.peterbukhal.android.citydemo.activity.MainActivity;
import name.peterbukhal.android.citydemo.model.City;
import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.MapView;
import ru.yandex.yandexmapkit.OverlayManager;
import ru.yandex.yandexmapkit.overlay.Overlay;
import ru.yandex.yandexmapkit.overlay.OverlayItem;
import ru.yandex.yandexmapkit.overlay.balloon.BalloonItem;
import ru.yandex.yandexmapkit.utils.GeoPoint;

public class CityFragment extends Fragment {

    public static final String ARG_CITY = "city";

    private MapView mMapView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMapView = new MapView(getContext(), "1234567890");
        return mMapView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments().containsKey(ARG_CITY)) {
            City city = getArguments().getParcelable(ARG_CITY);

            ((MainActivity) getActivity()).getSupportActionBar().setTitle(city.getName().toUpperCase());

            GeoPoint cityPoint = new GeoPoint(city.getLatitude(), city.getLongitude());

            MapController controller = mMapView.getMapController();
            controller.setPositionNoAnimationTo(cityPoint);

            OverlayManager manager = controller.getOverlayManager();
            manager.getMyLocation().setEnabled(false);

            //Создаем новый слой
            Overlay overlay = new Overlay(controller);
            // Создаем объект слоя
            OverlayItem flag = new OverlayItem(cityPoint, getResources().getDrawable(R.drawable.flag));
            // Добавляем пузырь
            BalloonItem balloonItem = new BalloonItem(getActivity(), flag.getGeoPoint());
            balloonItem.setText(city.getName());
            flag.setBalloonItem(balloonItem);
            // Добавляем объект на слой
            overlay.addOverlayItem(flag);
            // Добавляем слой на карту
            manager.addOverlay(overlay);
        }
    }

}
