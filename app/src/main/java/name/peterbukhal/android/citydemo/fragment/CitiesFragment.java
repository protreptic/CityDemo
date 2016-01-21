package name.peterbukhal.android.citydemo.fragment;

import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import name.peterbukhal.android.citydemo.R;
import name.peterbukhal.android.citydemo.model.City;
import name.peterbukhal.android.citydemo.util.Fonts;

public class CitiesFragment extends Fragment implements OnRefreshListener {
	
	protected Typeface mRobotoCondensedBold;
	protected Typeface mRobotoCondensedRegular;
	protected Typeface mRobotoCondensedLight;

	protected RecyclerView mRecyclerView;
	private RecyclerView.Adapter<?> mRecyclerViewAdapter;
	
	private SwipeRefreshLayout mSwipeRefreshWidget;
	
	protected ArrayList<City> mCities = new ArrayList<>();
	
	private Handler mHandler = new Handler();
	
    private final Runnable mRefreshDone = new Runnable() {

        @Override
        public void run() {
            mSwipeRefreshWidget.setRefreshing(false);
        }

    };
    
    private final Runnable mRefreshBegin = new Runnable() {

        @Override
        public void run() {
            mSwipeRefreshWidget.setRefreshing(true);
        }

    };
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), columns());
		layoutManager.setReverseLayout(false);
		layoutManager.setSmoothScrollbarEnabled(true);

		mRecyclerView = new RecyclerView(getContext());
		mRecyclerView.setPadding(0, 3, 4, 3);
		mRecyclerView.setHasFixedSize(false);
		mRecyclerView.setLayoutManager(layoutManager);
		mRecyclerView.setVerticalScrollBarEnabled(true);

		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		mRecyclerView.setLayoutParams(layoutParams);

		mSwipeRefreshWidget = new SwipeRefreshLayout(getContext());
		mSwipeRefreshWidget.setOnRefreshListener(this);
		mSwipeRefreshWidget.setLayoutParams(layoutParams);
		mSwipeRefreshWidget.addView(mRecyclerView);

		return mSwipeRefreshWidget;
	}

	@Override
	public void onRefresh() {
		refreshData();
	}
	
	private void refreshData() {
		mAsyncHttpClient.get(CITY_SERVICE_URL, new JsonHttpResponseHandler() {

			@Override
			public void onStart() {
				mHandler.removeCallbacks(mRefreshDone);
				mHandler.removeCallbacks(mRefreshBegin);
				mHandler.postDelayed(mRefreshBegin, 250);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, String responseString) {
				mCities.clear();

				GsonBuilder gsonBuilder = new GsonBuilder();
				gsonBuilder.registerTypeAdapter(City.class, new City.CityJsonDeserializer());
				gsonBuilder.registerTypeAdapter(City.class, new City.CityJsonSerializer());
				gsonBuilder.setPrettyPrinting();
				gsonBuilder.serializeNulls();

				Gson gson = gsonBuilder.create();

				try {
					JSONObject jsonObject = new JSONObject(responseString);
					JSONArray jsonArray = jsonObject.getJSONArray("cities");

					mCities = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<City>>() {}.getType());
				} catch (JSONException e) {
					e.printStackTrace();
				}

				mRecyclerViewAdapter.notifyDataSetChanged();

				mHandler.removeCallbacks(mRefreshBegin);
				mHandler.removeCallbacks(mRefreshDone);
				mHandler.postDelayed(mRefreshDone, 10);

				if (getActivity() != null)
					Toast.makeText(getActivity(), R.string.updat_success, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				onSuccess(statusCode, headers, response.toString());
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				if (getActivity() != null)
					Toast.makeText(getActivity(), R.string.update_failture, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onProgress(long bytesWritten, long totalSize) {
				Log.d(LOG_TAG, "download bytesWritten = " + bytesWritten + " totalSize = " + totalSize);
			}

		});
	}

	public class CityAdapter extends RecyclerView.Adapter<CityViewHolder> {
		
		@Override
		public int getItemCount() {
			return mCities.size();
		}
		
		@Override
		public CityViewHolder onCreateViewHolder(ViewGroup parent, int position) {
			return new CityViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.city_view, parent, false));
		}

		@Override
		public void onBindViewHolder(CityViewHolder holder, int position) {
			final City city = mCities.get(position);

			holder.cityLocation.setImageDrawable(null);

			holder.cityName.setText(city.getName().toUpperCase());
			holder.cityName.setTypeface(mRobotoCondensedBold);

			holder.itemView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view) {
					Bundle arguments = new Bundle();
					arguments.putParcelable(CityFragment.ARG_CITY, city);

					Fragment fragment = new CityFragment();
					fragment.setArguments(arguments);

					FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

					FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
					fragmentTransaction.replace(R.id.content_layout, fragment);
					fragmentTransaction.addToBackStack(null);
					fragmentTransaction.commit();
				}
			});

			String yandex = "https://static-maps.yandex.ru/1.x/?" +
					"ll="+city.getLongitude()+","+city.getLatitude()+"&" +
					"size=340,150&" +
					"scale=1&" +
					"spn="+city.getSpnLongitude()+","+city.getSpnLatitude()+"&" +
					"l=map";

			ImageLoader imageLoader = ImageLoader.getInstance();
			imageLoader.displayImage(yandex, holder.cityLocation);
		}

	}

	private static final String LOG_TAG = "CityFragment";
	private static final String CITY_SERVICE_URL = "http://beta.taxistock.ru/taxik/api/client/query_cities";

	private AsyncHttpClient mAsyncHttpClient;

	public CitiesFragment() {
		mAsyncHttpClient = new AsyncHttpClient();
	}
    
	private int columns() {
		int columns = 2;
		
		Configuration config = getResources().getConfiguration();
		
		int screenSize = config.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
		
		if (screenSize == Configuration.SCREENLAYOUT_SIZE_SMALL) {
		    if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
		    	columns = 2;
		    } else {
		    	columns = 1;
		    }
		} 
		if (screenSize == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
		    if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
		    	columns = 2;
		    } else {
		    	columns = 1;
		    }
		} 
		if (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE) {
		    if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
		    	columns = 3;
		    } else {
		    	columns = 2;
		    }
		} 
		if (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
		    if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
		    	columns = 4;
		    } else {
		    	columns = 2;
		    }
		} 
		
		return columns;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putSerializable("cities", mCities);
	}

	private boolean mRestored;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mRobotoCondensedBold = Fonts.get(getActivity()).getTypeface("RobotoCondensed-Bold");
		mRobotoCondensedRegular = Fonts.get(getActivity()).getTypeface("RobotoCondensed-Regular");
		mRobotoCondensedLight = Fonts.get(getActivity()).getTypeface("RobotoCondensed-Light");

		mRecyclerViewAdapter = new CityAdapter();
		mRecyclerView.setAdapter(mRecyclerViewAdapter); 

		if (savedInstanceState != null && savedInstanceState.containsKey("cities")) {
			mCities.addAll((ArrayList) savedInstanceState.getSerializable("cities"));
			mRecyclerViewAdapter.notifyDataSetChanged();
		}

		if (mCities.isEmpty())
			refreshData();
	}
	
	public static class CityViewHolder extends RecyclerView.ViewHolder {
		
		public TextView cityName;
		public ImageView cityLocation;
		
		public CityViewHolder(View itemView) {
			super(itemView); 

			cityName = (TextView) itemView.findViewById(R.id.city_name);
			cityLocation = (ImageView) itemView.findViewById(R.id.city_location);
		}
		
	}

}
