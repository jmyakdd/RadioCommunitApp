package radio.crte.com.radiocommunitapp.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class BroadCastSendUtil {
    public static void sendBroadcast(Context context,String action){
        Intent intent = new Intent();
        intent.setAction(action);
        context.sendBroadcast(intent);
    }

    public static void sendBroadcast(Context context,String[] actions){
        Intent intent = new Intent();
        for (String str:actions) {
            intent.setAction(str);
        }
        context.sendBroadcast(intent);
    }
    public static void sendBroadcast(Context context, String action, Bundle bundle){
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra("bundle",bundle);
        context.sendBroadcast(intent);
    }

    public static void sendBroadcast(Context context,String[] actions, Bundle bundle){
        Intent intent = new Intent();
        intent.putExtra("bundle",bundle);
        for (String str:actions) {
            intent.setAction(str);
        }
        context.sendBroadcast(intent);
    }
}
