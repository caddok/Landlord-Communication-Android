package source.kevtimov.landlordcommunicationapp.views.login.myplaces;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.emredavarci.circleprogressbar.CircleProgressBar;
import com.muddzdev.styleabletoast.StyleableToast;
import com.vstechlab.easyfonts.EasyFonts;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import source.kevtimov.landlordcommunicationapp.R;
import source.kevtimov.landlordcommunicationapp.models.Place;
import source.kevtimov.landlordcommunicationapp.utils.Constants;

public class MyPlacesFragment extends Fragment implements ContractsMyPlaces.View, RecyclerViewMyPlacesAdapter.OnPlaceClickListener {


    private ContractsMyPlaces.Presenter mPresenter;
    private ContractsMyPlaces.Navigator mNavigator;
    private RecyclerViewMyPlacesAdapter mPlacesAdapter;

    @BindView(R.id.tv_my_places)
    TextView mTextViewMyPlaces;

    @BindView(R.id.rv_my_places)
    RecyclerView mRecViewMyPlaces;

    @BindView(R.id.progress_bar)
    CircleProgressBar mProgressBar;

    @Inject
    public MyPlacesFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_my_places, container, false);
        ButterKnife.bind(this, root);
        mPlacesAdapter = new RecyclerViewMyPlacesAdapter(Objects.requireNonNull(getContext()));
        mRecViewMyPlaces.setAdapter(mPlacesAdapter);
        mRecViewMyPlaces.setLayoutManager(new LinearLayoutManager(getContext()));
        mPlacesAdapter.setOnPlaceClickListener(this);

        initFont();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe(this);
        mPresenter.loadPlaces();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void showPlaces(List<Place> places) {
        mPlacesAdapter.clear();
        mPlacesAdapter.addAll(places);
        mPlacesAdapter.notifyDataSetChanged();
    }

    @Override
    public void showEmptyList(){
        StyleableToast.makeText(getContext(), Constants.NO_PLACES_FOUND,
                Toast.LENGTH_LONG, R.style.reject_login_toast)
                .show();
    }

    @Override
    public void navigateToDetails(Place place) {
        mNavigator.navigateToPlaceDetails(place);
    }

    @Override
    public void navigateToManagePlace() {
        mNavigator.navigateToManagePlace();
    }

    @Override
    public void setPresenter(ContractsMyPlaces.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void setNavigator(ContractsMyPlaces.Navigator navigator) {
        this.mNavigator = navigator;
    }

    @Override
    public void showLoading() {
        this.mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        this.mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showError(Throwable error) {
        StyleableToast.makeText(getContext(), error.getMessage(),
                Toast.LENGTH_LONG, R.style.reject_login_toast)
                .show();
    }

    @OnClick(R.id.btn_plus)
    public void onClickAdd(View v){
        mPresenter.allowNavigationToManagePlaces();
    }

    private void initFont() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        int selectedFont = Integer.parseInt(sharedPreferences.getString(Constants.FONT_LIST, "1"));

        switch (selectedFont) {
            case 1:
                mTextViewMyPlaces.setTypeface(EasyFonts.droidSerifBold(getContext()));
                break;
            case 2:
                mTextViewMyPlaces.setTypeface(EasyFonts.funRaiser(getContext()));
                break;
            case 3:
                mTextViewMyPlaces.setTypeface(EasyFonts.walkwayBold(getContext()));
                break;

        }
    }

    @Override
    public void onPlaceClick(int position) {
        Place place = mPlacesAdapter.getItem(position);

        mPresenter.allowNavigationToDetails(place);
    }
}
