package com.valhalla.studiac.utility;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.valhalla.studiac.R;
import com.valhalla.studiac.activities.BulletinBoardActivity;
import com.valhalla.studiac.database.Firebase;

import java.util.HashMap;


/**
 * the notifications are sent using data payload, since data messages can work on background
 * and foreground. The data message will be handled by onMessageReceived().
 * handles 3 types of notification events: admin, post, chat message
 */
public class FirebaseCloudMessageService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseFCM";
    private static final int BROADCAST_NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "channel_id";
    private static final int NOTIFICATION_ID = 2;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        Log.d(TAG, "****message received****");
        boolean notify = true;
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        String authorUid = "";

        PendingIntent resultPendingIntent = null;
        int smallIcon = R.mipmap.ic_launcher;

       /* Map<String, String> params =  message.getData();

        System.out.println(params);
        JSONObject object = new JSONObject(params);
        Log.e("JSON OBJECT", object.toString());
*/
        //  System.out.println(object.toString());
        if (message.getData().size() > 0) {
            // dataType can be either post_notification or chat_notification
            String dataType = message.getData().get(getString(R.string.notification_type));

            if (dataType != null) {

                authorUid = message.getData().get("author");
                // Log.d(TAG, authorUid);
                // if notification type is bulletin post
                if (dataType.equals(getString(R.string.post_notification))) {
                    // if it's a post notification, then open bulletin board activity
                    Log.d(TAG, "post notification");
                    Intent intent = new Intent(this, BulletinBoardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    // checks which category it is, and sets the corresponding image
                    // category can be either: official, advertisement, others, help
                    String category = message.getData().get(getString(R.string.data_message));
                    if (category != null) {
                        smallIcon = getCategoryType(category);
                        if (smallIcon == -1) {
                            smallIcon = R.mipmap.ic_launcher;
                        }
                    }
                    resultPendingIntent = PendingIntent.getActivity(this, BROADCAST_NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                } else if (dataType.equals(getString(R.string.admin_notification))) {
                    // if notification type is send from admin console
                    Log.d(TAG, "admin notification");
                } else {
                    // if notification is of type chat message
                    Log.d(TAG, "chat notification");
                }
            } else {
                Log.d(TAG, "data type is null");
            }


            if (dataType != null) {
                // mark the user's current activity as bulletin Board Activity
                SharedPreference preference = SharedPreference.getInstance(getApplicationContext());
                String currentActivity = (String) preference.getData(String.class.getSimpleName(),
                        getString(R.string.current_activity));

                if (authorUid != null && authorUid.equals(mUser.getUid())) {
                    notify = false;
                } else if (currentActivity != null && currentActivity.equals(BulletinBoardActivity.class.getSimpleName()) &&
                        dataType.equals(getString(R.string.post_notification))) {
                    notify = false;
                    Log.d(TAG, "user currently in bulletin board activity. So no showing notification");
                }

                if (notify) {
                    createNotification(message.getData().get(getString(R.string.data_title)),
                            message.getData().get(getString(R.string.data_body)),
                            smallIcon, resultPendingIntent);

                }

            }


        }

    }

    private void createNotification(String title, String content, int smallIcon, PendingIntent resultPendingIntent) {
        // build the notification
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ic_launcher))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(smallIcon) // add logo here
                .setAutoCancel(true);

        if (resultPendingIntent != null) {
            notificationBuilder.setContentIntent(resultPendingIntent);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(createNotificationChannel());
            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());

        } else {
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
        }
    }

    private int getCategoryType(String s) {
        if (s.equals(Common.POST_CATEGORY_1)) {
            return R.drawable.bulletin_ic_official;
        } else if (s.equals(Common.POST_CATEGORY_2)) {
            return R.drawable.bulletin_ic_help;
        } else if (s.equals(Common.POST_CATEGORY_3)) {
            return R.drawable.bulletin_ic_advertisement;
        } else if (s.equals(Common.POST_CATEGORY_4)) {
            return R.drawable.bulletin_ic_others;
        } else {
            return -1;
        }
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(@NonNull String s) {
        updateDeviceToken(s);
    }

    /*
     * updates the user's current device token in the database whenever it's is refreshed
     * and also save it in the shared preference
     */
    private void updateDeviceToken(String token) {
        if (token != null) {
            Log.d(TAG, token);
            Log.d(TAG, "sendRegistrationToServer: sending token to server: " + token);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                HashMap<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("status", true);
                childUpdates.put("token", token);

                reference.child(Firebase.USERS)
                        .child(user.getUid())
                        .child("device-status").
                        updateChildren(childUpdates);

                // save it in local cache
                SharedPreference preference = SharedPreference.getInstance(getApplicationContext());
                preference.saveData(String.class.getSimpleName(), getString(R.string.device_token), token);
            }
        }
    }

    /**
     * @return channel a channel for the "bulletin post" notification
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private NotificationChannel createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library

        CharSequence name = getString(R.string.channel_name_post);
        String description = getString(R.string.channel_post_description);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        channel.setShowBadge(true); // a small dot on the app icon if it has any notifications
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this

        return channel;


    }


}
