package source.kevtimov.landlordcommunicationapp.utils.receiver;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.util.Objects;

import source.kevtimov.landlordcommunicationapp.R;
import source.kevtimov.landlordcommunicationapp.utils.Constants;
import source.kevtimov.landlordcommunicationapp.views.login.myplaces.MyPlacesActivity;

public class ReceiverNotification extends BroadcastReceiver {

    private NotificationManagerCompat mManager;
    private Notification mNotification;

    @Override
    public void onReceive(Context context, Intent intent) {
        mManager = NotificationManagerCompat.from(context);

        Intent navigateToMyPlaces = new Intent(context, MyPlacesActivity.class);
        navigateToMyPlaces.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        String total = String.valueOf(intent.getDoubleExtra("TotalAmount", 0));
        String remain = String.valueOf(intent.getDoubleExtra("RemainingAmount", 0));
        int rentId = intent.getIntExtra("RentId", 0);
        String date = intent.getStringExtra("DueDate");

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 595, navigateToMyPlaces, 0);

        mNotification = new NotificationCompat.Builder(context, Constants.CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.rent_day)
                .setContentTitle("RENT!")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("You have 5 days till rent due date!\n" + "Due date: " + date + "\n" + "Total amount: " + total + "\n" +
                        "Remain: " + remain + "\n"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build();

        Objects.requireNonNull(mManager).notify(rentId, mNotification);
    }
}
