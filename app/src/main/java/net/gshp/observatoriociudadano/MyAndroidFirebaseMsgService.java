package net.gshp.observatoriociudadano;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import net.gshp.observatoriociudadano.contextApp.ContextApp;
import net.gshp.observatoriociudadano.dao.DAO;
import net.gshp.observatoriociudadano.dialog.MessagePush;
import net.gshp.observatoriociudadano.dto.DtoBundleMessagePush;
import net.gshp.observatoriociudadano.util.Helper;

/**
 * Created by gnu on 1/11/16.
 */

public class MyAndroidFirebaseMsgService extends FirebaseMessagingService {
    private static final String TAG = "MyAndroidFCMService";
    private SharedPreferences preferences;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        preferences = getSharedPreferences(getString(R.string.app_share_preference_name), Context.MODE_PRIVATE);
        Log.e(TAG, "FIREEEEE " + remoteMessage.getData().toString() + "  " + this.getPackageName());

        switch (remoteMessage.getData().get("id")) {
            case "1":
//                InterfaceWhitApiChat interfaceWhitApiChat=new InterfaceWhitApiChat(ContextApp.context);
//                preferences=getSharedPreferences(getString(R.string.app_share_preference_name), Context.MODE_PRIVATE);
//                DtoUserInfoToConnection dtoUserInfoToConnection=new DtoUserInfoToConnection();
//
//                dtoUserInfoToConnection.setUserName(preferences.getString(getString(R.string.app_share_preference_user_account), " "));
//                dtoUserInfoToConnection.setPassword(preferences.getString(getString(R.string.app_share_preference_user_pass), " "));
//                dtoUserInfoToConnection.setIp(preferences.getString(getString(R.string.network_ip), " "));
//                dtoUserInfoToConnection.setServicePath(preferences.getString(getString(R.string.network_context), " "));
//                try {
//                    interfaceWhitApiChat.loadInfoUser(dtoUserInfoToConnection);
//
//                } catch (IncompleteInfoUserException e) {
//                    e.printStackTrace();
//                }
//                interfaceWhitApiChat.adviceOfNewMessage();
                break;
            case "2":
                if (remoteMessage.getData().containsKey("message")) {
                    new DAO().RunQuery(remoteMessage.getData().get("message"));
                }
                break;
            case "3":
//                new ModelSendDataBase().sendBD();
                break;
            case "4":
                DtoBundleMessagePush dto = new DtoBundleMessagePush();
                if (remoteMessage.getData().containsKey("title")) {
                    dto.setTitle(remoteMessage.getData().get("title"));
                }
                if (remoteMessage.getData().containsKey("message")) {
                    dto.setMessage(remoteMessage.getData().get("message"));
                }
                Intent intent = new Intent(ContextApp.context, MessagePush.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(ContextApp.context.getString(R.string.app_bundle_name), dto);

                if (Home.stateApp == 1) {
                    ContextApp.context.startActivity(intent);
                    Log.e(TAG, "FIREEEEE running");

                } else if (Home.stateApp == 2) {
                    Log.e(TAG, "FIREEEEE notification");
                    PendingIntent myIntent = PendingIntent.getActivity(ContextApp.context, 0, intent, 0);

                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(this, "1")
                                    .setSmallIcon(R.drawable.ic_launcher)
                                    .setContentIntent(myIntent)
                                    .setContentTitle(dto.getTitle())
                                    .setContentText("Usted tiene un nuevo mensaje");
                    NotificationManager mNotifyManager = (NotificationManager) ContextApp.context.getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotifyManager.notify(5, mBuilder.build());
                }
                break;
            case "5":
//                DtoBundleMessagePush dtoMessage = new DtoBundleMessagePush();
//
//                try {
//                    if (remoteMessage.getData().containsKey("version") &&
//                            !remoteMessage.getData().get("version").equals(ContextApp.context.getPackageManager().getPackageInfo(getPackageName(), 0).versionName)) {
//                        dtoMessage.setMessage(Base64Code.decode(remoteMessage.getData().get("message")));
//
//                        Intent intent = new Intent(ContextApp.context, DialogMessageNewApp.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent.putExtra(ContextApp.context.getString(R.string.app_bundle_name), dtoMessage);
//                        ContextApp.context.startActivity(intent);
//                    }else{
//                        Log.e("fire","fire no entra");
//                    }
//                } catch (PackageManager.NameNotFoundException e) {
//                    e.printStackTrace();
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
                break;
            case "7":
//                DtoBundleMessagePush dto = new DtoBundleMessagePush();
//                if (remoteMessage.getData().containsKey("title")) {
//                    dto.setTitle(remoteMessage.getData().get("title"));
//                }
//                if (remoteMessage.getData().containsKey("message")) {
//                    dto.setMessage(remoteMessage.getData().get("message"));
//                    Intent intent = new Intent(ContextApp.context, MessagePush.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.putExtra(ContextApp.context.getString(R.string.app_bundle_name),dto);
//                    ContextApp.context.startActivity(intent);
//
//                    NotificationCompat.Builder mBuilder =
//                            new NotificationCompat.Builder(this)
//                                    .setSmallIcon(R.mipmap.ic_launcher)
//                                    .setContentTitle(dto.getTitle())
//                                    .setContentText("Usted tiene un nuevo mensaje");
//                    NotificationManager mNotifyManager= (NotificationManager) ContextApp.context.getSystemService(Context.NOTIFICATION_SERVICE);
//                    mNotifyManager.notify(5, mBuilder.build());
//
//
//                }
                break;
            case "8":

                break;
            case "10":
                break;
            default:
                Log.e(TAG, "SIN ASIGNAR");
                break;

        }
    }

}
