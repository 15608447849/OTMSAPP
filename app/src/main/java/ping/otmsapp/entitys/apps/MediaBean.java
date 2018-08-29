package ping.otmsapp.entitys.apps;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;

/**
 * Created by Leeping on 2018/3/23.
 * email: 793065165@qq.com
 * 音频播放
 */

public class MediaBean {
    //音频播放的
    private MediaPlayer mMediaPlayer;
    private Context context;

    public MediaBean(Context context) {
        this.context = context;

    }

    public void destroy(){
        close();
        context = null;
    }

    public void replay(int rid){
        close();
        play(rid);
    }
    public void play(int rid){
        if (mMediaPlayer == null) {
            setMaxVolumes();
            try {
                mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mMediaPlayer.setLooping(false);
                AssetFileDescriptor file = context.getResources().openRawResourceFd(rid);
                mMediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mMediaPlayer.setVolume(1, 1);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            } catch (Exception e) {
                close();
            }
        }
    }
    public void close(){
        if (mMediaPlayer != null) {
            try {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                    mMediaPlayer.stop();
                }
            } catch (IllegalStateException ignored) {
            }finally {
                mMediaPlayer.release();
                mMediaPlayer = null;
            }

        }
    }

    public void setMaxVolumes(){
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
    }


}
