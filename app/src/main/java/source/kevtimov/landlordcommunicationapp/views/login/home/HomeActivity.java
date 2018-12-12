package source.kevtimov.landlordcommunicationapp.views.login.home;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;

import java.util.Calendar;
import java.util.Objects;

import javax.inject.Inject;

import source.kevtimov.landlordcommunicationapp.R;
import source.kevtimov.landlordcommunicationapp.chat.sessions.ChatSessionActivity;
import source.kevtimov.landlordcommunicationapp.models.Rent;
import source.kevtimov.landlordcommunicationapp.models.User;
import source.kevtimov.landlordcommunicationapp.parsers.base.JsonParser;
import source.kevtimov.landlordcommunicationapp.utils.Constants;
import source.kevtimov.landlordcommunicationapp.utils.drawer.BaseDrawer;
import source.kevtimov.landlordcommunicationapp.utils.androidservices.MyNotificationService;
import source.kevtimov.landlordcommunicationapp.utils.receiver.ReceiverNotification;
import source.kevtimov.landlordcommunicationapp.views.login.mypayments.MyPaymentsActivity;
import source.kevtimov.landlordcommunicationapp.views.login.myplaces.MyPlacesActivity;
import source.kevtimov.landlordcommunicationapp.views.login.myusers.MyUsersActivity;

public class HomeActivity extends BaseDrawer implements ContractsHome.Navigator{


    private static final int IDENTIFIER = 631;
    private Toolbar mToolbar;
    private User mLogInUser;
    private AlarmManager mAlarmManager;

    @Inject
    JsonParser<User> mJsonParser;

    @Inject
    HomeFragment mHomeFragment;

    @Inject
    ContractsHome.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mToolbar = this.findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mLogInUser = getUserFromSharedPref();
        mLogInUser.setOnline(true);

        mPresenter.setUser(mLogInUser);
        mHomeFragment.setNavigator(this);
        mHomeFragment.setPresenter(mPresenter);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.home_content, mHomeFragment)
                .commit();
    }

    @Override
    protected long getIdentifier() {
        return IDENTIFIER;
    }

    @Override
    protected Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    protected String getUsername() {
        return this.mLogInUser.getUsername();
    }

    @Override
    protected String getEmail() {
        return this.mLogInUser.getEmail();
    }

    @Override
    public void navigateToChats() {
        Intent intent = new Intent(this, ChatSessionActivity.class);
        startActivity(intent);
    }

    @Override
    public void navigateToPayments() {
        Intent intent = new Intent(this, MyPaymentsActivity.class);
        startActivity(intent);
    }

    @Override
    public void navigateToUsers() {
        Intent intent = new Intent(this, MyUsersActivity.class);
        startActivity(intent);
    }

    @Override
    public void navigateToPlaces() {
        Intent intent = new Intent(this, MyPlacesActivity.class);
        startActivity(intent);
    }

    @Override
    public void sendNotification(Rent rent, Calendar mCalendar) {
        Intent intent = new Intent(this, ReceiverNotification.class);
        intent.putExtra("TotalAmount", rent.getTotalAmount());
        intent.putExtra("RemainingAmount", rent.getRemainingAmount());
        intent.putExtra("DueDate", rent.getDueDate());
        intent.putExtra("RentId", rent.getRentID());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, rent.getRentID(),
                intent, 0);

        mAlarmManager = (AlarmManager) Objects.requireNonNull(this)
                .getSystemService(Context.ALARM_SERVICE);

        Objects.requireNonNull(mAlarmManager).setExact(AlarmManager.RTC_WAKEUP,
                mCalendar.getTimeInMillis(), pendingIntent);
    }

    private User getUserFromSharedPref() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String userInfo = sharedPreferences.getString(Constants.SHARED_PREFERENCES_KEY_USER_INFO, "");
        return mJsonParser.fromJson(userInfo);
    }

    private void setTheme(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int theme =  Integer.parseInt(sharedPreferences.getString(Constants.THEME_LIST, "1"));

        switch(theme){
            case 1:
                setTheme(R.style.AppThemeCustom);
                break;
            case 2:
                setTheme(R.style.AppThemeCustomDark);
                break;

        }
    }
}
