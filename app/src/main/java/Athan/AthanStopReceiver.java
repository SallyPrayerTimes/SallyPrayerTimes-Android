package Athan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AthanStopReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AthanPlayer.getInstance(context).stopAthan();
    }
}
