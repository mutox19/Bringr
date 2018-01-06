package com.example.danilo.testingmap;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;


import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

//import com.firebase.client.Firebase;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.Messages;
import com.google.android.gms.nearby.messages.MessagesOptions;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

/**
 * Created by Danilo on 2016-08-30.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    private static int holder = 0;


    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            //Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            Log.d(TAG, "notification payload: " + remoteMessage.getData());
            //Log.e(TAG, "Notification Body: " + remoteMessage.getData().get(0));
            handleNotification(remoteMessage.getNotification().getBody(),remoteMessage);

        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload in size: " + remoteMessage.getData().toString());
            Log.d(TAG, "Message data payload in size: " + remoteMessage.getData());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleNotification(String message, RemoteMessage remoteMessage) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(config.PUSH_NOTIFICATION);
            pushNotification.putExtra("driver", remoteMessage.getData().get("driver"));
            pushNotification.putExtra("customer", remoteMessage.getData().get("customer"));
            pushNotification.putExtra("order", remoteMessage.getData().get("order"));

            String notifyType =  remoteMessage.getData().get("Type");
            if(notifyType.equals("driverAccept"))
            {
                //pushNotification.putExtra("driver", remoteMessage.getData().get("driver"));
               // LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                Intent intentSignUP=new Intent(getApplicationContext(),finalizeOrder.class);
                // play notification sound

                intentSignUP.putExtra("driver", remoteMessage.getData().get("driver"));
                intentSignUP.putExtra("customer", remoteMessage.getData().get("customer"));
                intentSignUP.putExtra("order", remoteMessage.getData().get("order"));
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
                showNotificationMessage(getApplicationContext(),remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody(),"",intentSignUP);
                Log.d(TAG, "Message data in handle notify: " + message);
            }
            if(notifyType.equals("customerAcceptDriver"))
            {
                //pushNotification.putExtra("driver", remoteMessage.getData().get("driver"));
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                Intent intentSignUP=new Intent(getApplicationContext(),viewer.class);
                intentSignUP.putExtra("driver", remoteMessage.getData().get("driver"));
                intentSignUP.putExtra("customer", remoteMessage.getData().get("customer"));
                intentSignUP.putExtra("order", remoteMessage.getData().get("order"));
                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
                showNotificationMessage(getApplicationContext(),remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody(),"",intentSignUP);
                Log.d(TAG, "Message data in handle notify: " + message);
            }
            if(notifyType.equals("customerDeleteOrder"))
            {
                //pushNotification.putExtra("driver", remoteMessage.getData().get("driver"));
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                Intent intentSignUP=new Intent(getApplicationContext(),CustomerDelOrder.class);
                intentSignUP.putExtra("driver", remoteMessage.getData().get("driver"));
                intentSignUP.putExtra("customer", remoteMessage.getData().get("customer"));
                intentSignUP.putExtra("order", remoteMessage.getData().get("order"));
                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
                showNotificationMessage(getApplicationContext(),remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody(),"",intentSignUP);
                Log.d(TAG, "Message data in handle notify: " + message);
            }
            if(notifyType.equals("customerChangeDriver"))
            {
                //pushNotification.putExtra("driver", remoteMessage.getData().get("driver"));
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                Intent intentSignUP=new Intent(getApplicationContext(),customerChangeDriver.class);
                intentSignUP.putExtra("driver", remoteMessage.getData().get("driver"));
                intentSignUP.putExtra("customer", remoteMessage.getData().get("customer"));
                intentSignUP.putExtra("order", remoteMessage.getData().get("order"));
                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
                showNotificationMessage(getApplicationContext(),remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody(),"",intentSignUP);
                Log.d(TAG, "Message data in handle notify: " + message);
            }
            if(notifyType.equals("ProcessTotal"))
            {
                //pushNotification.putExtra("driver", remoteMessage.getData().get("driver"));
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                Intent intentSignUP=new Intent(getApplicationContext(),customerCompleteOptions.class);
                intentSignUP.putExtra("driver", remoteMessage.getData().get("driver"));
                intentSignUP.putExtra("customer", remoteMessage.getData().get("customer"));
                intentSignUP.putExtra("order", remoteMessage.getData().get("order"));
                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
                showNotificationMessage(getApplicationContext(),remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody(),"",intentSignUP);
                Log.d(TAG, "Message data in handle notify: " + message);
            }


        }else{
            // If the app is in background, firebase itself handles the notification
            Log.d(TAG, "app in background in handlenotit: " + message);
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            String message = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = data.getString("image");
            String timestamp = data.getString("timestamp");
            JSONObject payload = data.getJSONObject("payload");


            String testDriver = data.getString("driver");

            Log.e(TAG, "title: " + testDriver);
            Log.e(TAG, "message: " + message);
            Log.e(TAG, "isBackground: " + isBackground);
            Log.e(TAG, "payload: " + payload.getString("type"));
            Log.e(TAG, "imageUrl: " + imageUrl);
            Log.e(TAG, "timestamp: " + timestamp);

            Intent resultIntent = new Intent(getApplicationContext(), MapsActivity.class);
            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                //Intent pushNotification = new Intent(getApplicationContext(),MapsActivity.class);
                //pushNotification.putExtra("message", message);
                //LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);


                // play notification sound
                //NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                //notificationUtils.playNotificationSound();


                //this section is for when the driver accepts the order
                if(payload.getString("type").equals("driverAccept"))
                {
                    Intent intentSignUP = new Intent(getApplicationContext(),finalizeOrder.class);
                    String orderNumb = payload.getString("order");
                    String driverNumb = payload.getString("driver");
                    String customerID = payload.getString("customer");
                    intentSignUP.putExtra("customer", customerID );
                    intentSignUP.putExtra("driver",driverNumb );
                    intentSignUP.putExtra("order",orderNumb);
                    Log.d("DRIVERINFO", "BigUpChristmas: " + orderNumb + " order: " + driverNumb);
                    //startActivity(intentSignUP);
                    PendingIntent pItent = PendingIntent.getActivity(this, 0, intentSignUP, PendingIntent.FLAG_ONE_SHOT);
                    Notification noti = new Notification.Builder(this)
                            .setTicker(title)   //ticker title
                            .setContentTitle("Food Delivering")
                            .setContentText(
                                    message)
                            .setSmallIcon(R.mipmap.yellowtaxi)
                            .setContentIntent(pItent).getNotification();
                    noti.flags = Notification.FLAG_AUTO_CANCEL;
                    NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    nm.notify(0, noti);
                }
                //this section is for the cutomer confirm sending notification to driver and direct driver to correct page
                else if(payload.getString("type").equals("customerAcceptDriver"))
                {
                    Intent intentSignUP=new Intent(getApplicationContext(),viewer.class);
                    intentSignUP.putExtra("order", payload.getString("order"));
                    intentSignUP.putExtra("driver", payload.getString("driver"));
                    intentSignUP.putExtra("customer", payload.getString("customer"));
                    Log.d("CUSTOMERACCEPT", "handleDataMessage: " + payload.getString("customer") + " " + payload.get("order") +" driver: " + payload.get("driver") );
                    //startActivity(intentSignUP);
                    PendingIntent pItent = PendingIntent.getActivity(this, 0, intentSignUP, PendingIntent.FLAG_ONE_SHOT);
                    Notification noti = new Notification.Builder(this)
                            .setTicker(title)   //ticker title
                            .setContentTitle("Food Delivering")
                            .setContentText(
                                    message)
                            .setSmallIcon(R.mipmap.yellowtaxi)
                            .setContentIntent(pItent).getNotification();
                    noti.flags = Notification.FLAG_AUTO_CANCEL;
                    NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    nm.notify(0, noti);
                }
                else if(payload.getString("type").equals("customerDeleteOrder"))
                {
                    Intent intentSignUP=new Intent(getApplicationContext(),CustomerDelOrder.class);
                    intentSignUP.putExtra("driver", payload.getString("driver"));
                    intentSignUP.putExtra("order", payload.getString("order"));
                    intentSignUP.putExtra("customer", payload.getString("customer"));
                    Log.d("DRIVERINFO", "handleDataMessage: " + payload.getString("driver") + " " + payload.get("driver"));
                    //startActivity(intentSignUP);
                    PendingIntent pItent = PendingIntent.getActivity(this, 0, intentSignUP, 0);
                    Notification noti = new Notification.Builder(this)
                            .setTicker(title)   //ticker title
                            .setContentTitle("Food Delivering")
                            .setContentText(
                                    message)
                            .setSmallIcon(R.mipmap.yellowtaxi)
                            .setContentIntent(pItent).getNotification();
                    noti.flags = Notification.FLAG_AUTO_CANCEL;
                    NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    nm.notify(0, noti);
                }
                else if(payload.getString("type").equals("customerChangeDriver"))
                {
                    Intent intentSignUP=new Intent(getApplicationContext(),customerChangeDriver.class);
                    intentSignUP.putExtra("driver", payload.getString("driver"));
                    intentSignUP.putExtra("order", payload.getString("order"));
                    intentSignUP.putExtra("customer", payload.getString("customer"));
                    Log.d("DRIVERINFO", "handleDataMessage: " + payload.getString("driver") + " " + payload.get("driver"));
                    //startActivity(intentSignUP);
                    PendingIntent pItent = PendingIntent.getActivity(this, 0, intentSignUP, PendingIntent.FLAG_ONE_SHOT);
                    Notification noti = new Notification.Builder(this)
                            .setTicker(title)   //ticker title
                            .setContentTitle("Food Delivering")
                            .setContentText(
                                    message)
                            .setSmallIcon(R.mipmap.yellowtaxi)
                            .setContentIntent(pItent).getNotification();
                    noti.flags = Notification.FLAG_AUTO_CANCEL;
                    NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    nm.notify(0, noti);
                }
                else if(payload.getString("type").equals("ProcessTotal"))
                {
                    Intent intentSignUP=new Intent(getApplicationContext(),customerCompleteOptions.class);
                    intentSignUP.putExtra("driver", payload.getString("driver"));
                    intentSignUP.putExtra("order", payload.getString("order"));
                    intentSignUP.putExtra("customer", payload.getString("customer"));
                    Log.d("DRIVERINFO", "handleDataMessage: " + payload.getString("driver") + " " + payload.get("driver"));
                    //startActivity(intentSignUP);
                    PendingIntent pItent = PendingIntent.getActivity(this, 0, intentSignUP, PendingIntent.FLAG_ONE_SHOT);
                    Notification noti = new Notification.Builder(this)
                            .setTicker(title)   //ticker title
                            .setContentTitle("Food Delivering")
                            .setContentText(
                                    message)
                            .setSmallIcon(R.mipmap.yellowtaxi)
                            .setContentIntent(pItent).getNotification();
                    noti.flags = Notification.FLAG_AUTO_CANCEL;
                    NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    nm.notify(0, noti);
                }
                else if(payload.getString("type").equals("StripePaySuccess"))
                {
                    //send customer to the thank you, your payment was success screen of just have the message sent
                    // have the customer go back to customer main
                    if(payload.getString("customer") != null)
                    {

                        //send customer to customerStripeSuccess
                        Intent intentSignUP=new Intent(getApplicationContext(),MainActivity.class);
                        intentSignUP.putExtra("driver", payload.getString("driver"));
                        intentSignUP.putExtra("order", payload.getString("order"));
                        intentSignUP.putExtra("customer", payload.getString("customer"));
                        Log.d("DRIVERINFO", "handleDataMessage: " + payload.getString("driver") + " " + payload.get("driver"));
                        //startActivity(intentSignUP);

                        PendingIntent pItent = PendingIntent.getActivity(this, 0, intentSignUP, PendingIntent.FLAG_ONE_SHOT);
                        Notification noti = new Notification.Builder(this)
                                .setTicker(title)   //ticker title
                                .setContentTitle("Food Delivering")
                                .setContentText(
                                        message)
                                .setSmallIcon(R.mipmap.yellowtaxi)
                                .setContentIntent(pItent).getNotification();
                        noti.flags = Notification.FLAG_AUTO_CANCEL;
                        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        nm.notify(0, noti);
                    }
                    //send driver that the payment was a success and they can now close the order
                    if(payload.getString("driver") != null)
                    {

                        //send driver to driverStripeSuccess
                        Intent intentSignUP=new Intent(getApplicationContext(),MainActivity.class);
                        intentSignUP.putExtra("driver", payload.getString("driver"));
                        intentSignUP.putExtra("order", payload.getString("order"));
                        intentSignUP.putExtra("customer", payload.getString("customer"));

                        Log.d("DRIVERINFO", "handleDataMessage: " + payload.getString("driver") + " " + payload.get("driver"));
                        //startActivity(intentSignUP);
                        PendingIntent pItent = PendingIntent.getActivity(this, 0, intentSignUP, PendingIntent.FLAG_ONE_SHOT);
                        Notification noti = new Notification.Builder(this)
                                .setTicker(title)   //ticker title
                                .setContentTitle("Food Delivering")
                                .setContentText(
                                        message)
                                .setSmallIcon(R.mipmap.yellowtaxi)
                                .setContentIntent(pItent).getNotification();
                        noti.flags = Notification.FLAG_AUTO_CANCEL;
                        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        nm.notify(0, noti);
                    }

                }
                //this section here is for if the customers payment was a failure
                else if(payload.getString("type").equals("StripePayFailure"))
                {
                    Intent intentSignUP=new Intent(getApplicationContext(),customerCompleteOptions.class);
                    intentSignUP.putExtra("driver", payload.getString("driver"));
                    intentSignUP.putExtra("order", payload.getString("order"));
                    Log.d("DRIVERINFO", "handleDataMessage: " + payload.getString("driver") + " " + payload.get("driver"));
                    //startActivity(intentSignUP);
                    PendingIntent pItent = PendingIntent.getActivity(this, 0, intentSignUP, PendingIntent.FLAG_ONE_SHOT);
                    Notification noti = new Notification.Builder(this)
                            .setTicker(title)   //ticker title
                            .setContentTitle("Food Delivering")
                            .setContentText(
                                    message)
                            .setSmallIcon(R.mipmap.yellowtaxi)
                            .setContentIntent(pItent).getNotification();
                    noti.flags = Notification.FLAG_AUTO_CANCEL;
                    NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    nm.notify(0, noti);
                }

            } else {
                // app is in background, show the notification in notification tray

                //***********************************************************************
                //****************************************************************************
                //
                //
                //this is where you would handle if the dont have the app in the screen
                //*********************************************************************
                //***********************************************************************
                //**************************************************************************
                resultIntent.putExtra("message", message);
                Log.d(TAG, "handleDataMessage: APP IN BACKGROUND" );
                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }


    /*
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        Log.d(TAG, "From: " + remoteMessage.getFrom());




        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {

            sendNotification(remoteMessage);
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }


        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            //sendNotification(remoteMessage.getNotification().getBody());
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    public void send (String key)
    {
        String msg ="";

        try{

        }
        catch (Exception e)
        {
            Log.d(TAG, "send: " + e.getMessage());
        }
    }
    private void sendNotification(RemoteMessage messageBody) {
        /*Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code *///, intent,
                //PendingIntent.FLAG_ONE_SHOT);

        /*Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("FCM Message")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification *///, //notificationBuilder.build());




/*

        Intent intentSignUP=new Intent(getApplicationContext(),finalizeOrder.class);
        //startActivity(intentSignUP);
        PendingIntent pItent = PendingIntent.getActivity(this, 0, intentSignUP, 0);
        Notification noti = new Notification.Builder(this)
                .setTicker("Ticker Title")   //ticker title
                .setContentTitle("Food Delivering")
                .setContentText(
                        messageBody.getData().toString())
                .setSmallIcon(R.mipmap.yellowtaxi)
                .setContentIntent(pItent).getNotification();
        noti.flags = Notification.FLAG_AUTO_CANCEL;
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(0, noti);
    }*/
}
