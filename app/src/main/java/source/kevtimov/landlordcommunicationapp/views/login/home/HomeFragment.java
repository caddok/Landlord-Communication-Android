package source.kevtimov.landlordcommunicationapp.views.login.home;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.emredavarci.circleprogressbar.CircleProgressBar;
import com.muddzdev.styleabletoast.StyleableToast;
import com.vstechlab.easyfonts.EasyFonts;

import java.util.Calendar;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import source.kevtimov.landlordcommunicationapp.R;
import source.kevtimov.landlordcommunicationapp.models.Rent;
import source.kevtimov.landlordcommunicationapp.utils.receiver.ReceiverNotification;

public class HomeFragment extends Fragment implements ContractsHome.View {


    @BindView(R.id.iv_chats)
    ImageView mImageViewChats;

    @BindView(R.id.iv_payments)
    ImageView mImageViewPayments;

    @BindView(R.id.iv_users)
    ImageView mImageViewUsers;

    @BindView(R.id.iv_places)
    ImageView mImageViewPlaces;

    @BindView(R.id.progress_bar)
    CircleProgressBar mProgressBar;

    private ContractsHome.Presenter mPresenter;
    private ContractsHome.Navigator mNavigator;
    private Calendar mCalendar;
//    private AlarmManager mAlarmManager;

    @Inject
    public HomeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, root);


        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe(this);
        mPresenter.manageNotifications();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void sendNotification(int year, int month, int day, Rent rent) {

        mCalendar = Calendar.getInstance();

        mCalendar.set(Calendar.YEAR, 2018);
        mCalendar.set(Calendar.MONTH, month - 1);
        mCalendar.set(Calendar.DAY_OF_MONTH, day);
        mCalendar.set(Calendar.HOUR_OF_DAY, 19);
        mCalendar.set(Calendar.MINUTE, 55);
        mCalendar.set(Calendar.SECOND, 0);


        if (mCalendar.getTimeInMillis() > System.currentTimeMillis()) {

            mNavigator.sendNotification(rent, mCalendar);
        }
    }

    @Override
    public void navigateToChats() {
        mNavigator.navigateToChats();
    }

    @Override
    public void navigateToPayments() {
        mNavigator.navigateToPayments();
    }

    @Override
    public void navigateToUsers() {
        mNavigator.navigateToUsers();
    }

    @Override
    public void navigateToPlaces() {
        mNavigator.navigateToPlaces();
    }


    @Override
    public void setPresenter(ContractsHome.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void setNavigator(ContractsHome.Navigator navigator) {
        mNavigator = navigator;
    }

    @Override
    public void showError(Throwable error) {
        StyleableToast.makeText(getContext(), error.getMessage(),
                Toast.LENGTH_LONG, R.style.accept_login_toast).show();
    }

    @OnClick(R.id.btn_chat)
    public void onClickChat(View v){
        mPresenter.allowNavigationToChats();
    }

    @OnClick(R.id.btn_payments)
    public void onClickPayments(View v){
        mPresenter.allowNavigationToPayments();
    }

    @OnClick(R.id.btn_users)
    public void onClickUsers(View v){
        mPresenter.allowNavigationToUsers();
    }

    @OnClick(R.id.btn_places)
    public void onClickPlaces(View v){
        mPresenter.allowNavigationToPlaces();
    }
}
