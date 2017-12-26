package es.josmangarsal.flash;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.os.Handler;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

public class NotificationService extends NotificationListenerService {

    Context context;

    private SharedPreferences settings;
    boolean active;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

        settings = getApplicationContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        active = settings.getBoolean("notificationServiceStatus", false);
        //Log.i("NotificationService", "" + active);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        //Log.d("DEBUG", "Notification added");

        active = settings.getBoolean("notificationServiceStatus", false);
        //Log.i("ServiceStatus", "" + active);

        if (!active)
            return;

        String pack = sbn.getPackageName();

        String text = "";
        String title = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            Bundle extras = extras = sbn.getNotification().extras;
            text = extras.getCharSequence("android.text").toString();
            title = extras.getString("android.title");
        }

        //Log.i("Package",pack);
        //Log.i("Title",title);
        //Log.i("Text",text);

        if (text.contains("Sheyla") == false && title.contains("Sheyla") == false
                && text.contains("sheyla") == false && title.contains("sheyla") == false)
            return;

        Handler h = new Handler(NotificationService.this.getMainLooper());

        h.post(new Runnable() {
            @Override
            public void run() {
                CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                try {
                    String cameraId = cameraManager.getCameraIdList()[1]; // Front camera

                    cameraManager.setTorchMode(cameraId, true);
                    Thread.sleep(500);
                    cameraManager.setTorchMode(cameraId, false);
                    Thread.sleep(500);
                    cameraManager.setTorchMode(cameraId, true);
                    Thread.sleep(500);
                    cameraManager.setTorchMode(cameraId, false);
                    Thread.sleep(500);
                    cameraManager.setTorchMode(cameraId, true);
                    Thread.sleep(500);
                    cameraManager.setTorchMode(cameraId, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        //Log.d("DEBUG", "Notification removed");
    }
}
