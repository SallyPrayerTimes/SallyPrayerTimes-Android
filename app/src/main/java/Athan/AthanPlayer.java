package Athan;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

public class AthanPlayer {

    private static AthanPlayer sInstance;
    private Context mContext;
    private MediaPlayer mMediaPlayer;

    public AthanPlayer(Context context) {
        mContext = context;
    }

    public static AthanPlayer getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new AthanPlayer(context);
        }
        return sInstance;
    }

    public void playAthan(Uri uri) {
        stopAthan();
        mMediaPlayer = MediaPlayer.create(mContext, uri);
        mMediaPlayer.start();
    }

    public void stopAthan() {
        if(mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.seekTo(0);
        }
    }

}
