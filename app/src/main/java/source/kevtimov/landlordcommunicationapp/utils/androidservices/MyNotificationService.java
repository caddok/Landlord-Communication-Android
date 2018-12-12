package source.kevtimov.landlordcommunicationapp.utils.androidservices;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.util.Objects;
import java.util.Observable;

import source.kevtimov.landlordcommunicationapp.R;
import source.kevtimov.landlordcommunicationapp.utils.Constants;
import source.kevtimov.landlordcommunicationapp.views.login.myplaces.MyPlacesActivity;

public class MyNotificationService extends IntentService {

    private NotificationManagerCompat mManager;

    public MyNotificationService() {
        super("MyNotificationService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String total = String.valueOf(Objects.requireNonNull(intent).getDoubleExtra("TotalAmount", 0));
        String remain = String.valueOf(intent.getDoubleExtra("RemainingAmount", 0));
        int rentId = intent.getIntExtra("RentId", 0);
        String date = intent.getStringExtra("DueDate");

        showNotification(total, remain, rentId, date);
    }


    private void showNotification(String total, String remain, int rentId, String date){
        mManager = NotificationManagerCompat.from(this);
        Intent intent = new Intent(this, MyPlacesActivity.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addParentStack(MyPlacesActivity.class);
        taskStackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notBuilder = new NotificationCompat.Builder(getApplicationContext(), Constants.CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.rent_day)
                .setContentTitle("RENT!")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("You have 5 days till rent due date!\n" + "Due date: " + date + "\n" + "Total amount: " + total + "\n" +
                                "Remain: " + remain + "\n"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        Objects.requireNonNull(mManager).notify(rentId, notBuilder.build());
    }
}
