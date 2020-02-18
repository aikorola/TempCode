package god.netease.randomcomment;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;

public class Temp {
    CommentBean commentBean = null; // 网络请求并解析JSON后得到的实体类
    MediaPlayer mediaPlayer = null;

    public void playOrPause() {
        if (commentBean == null && !isFirstIn) {
            ToastUtil.show("资源获取有误，请稍后再试！");
            return;
        }
        //实例化MediaPlayer
        if (mediaPlayer == null) {
            if ((commentBean == null) || (commentBean.getData() == null) || TextUtils.isEmpty(commentBean.getData().getUrl())) {
                return;
            }

            view.showLoading();

            mediaPlayer = new MediaPlayer();

            //设置音频流的类型
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            //设置音源
            try {
                //mediaPlayer.setDataSource(this, Uri.parse("file://"+sdPath+"/s1.mp3"));
                mediaPlayer.setDataSource(view, Uri.parse(commentBean.getData().getUrl()));

                mediaPlayer.prepareAsync();

            } catch (IOException e) {
                e.printStackTrace();
            }
            //设置准备监听
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Log.i(TAG, "onPrepared: ");
                    view.showSuccess();

                    mediaPlayer.start();
                    //暂停图标
                    view.binding.musicState.setImageResource(android.R.drawable.ic_media_pause);

                    //获取音乐的播放时间
//                    int time = mediaPlayer.getDuration();

                    //设置进度条的最大值 为  音乐的播放时间
//                        sb_main_bar.setMax(time);
                }
            });


            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Log.i(TAG, "onError: ");
                    view.showFail();
                    return false;
                }
            });

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    //播放图标
                    view.binding.musicState.setImageResource(android.R.drawable.ic_media_play);
                }
            });

        } else if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            //播放图标
            view.binding.musicState.setImageResource(android.R.drawable.ic_media_play);
        } else {
            mediaPlayer.start();
            //暂停图标
            view.binding.musicState.setImageResource(android.R.drawable.ic_media_pause);
        }
    }
}
