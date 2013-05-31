package org.toilelibre.libe.phonecomposer.thread;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

public class PlaySound {

	public static void playBa(final int sampleRate,
			final byte[] baSoundByteArray, final OnPlaySoundComplete opsc) {
		final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
				sampleRate, AudioFormat.CHANNEL_OUT_MONO,
				AudioFormat.ENCODING_PCM_16BIT, baSoundByteArray.length,
				AudioTrack.MODE_STATIC);
		Log.i(PlaySound.class.getName(), "PlaySound - Size of the stream : "
				+ baSoundByteArray.length);
		audioTrack.write(baSoundByteArray, 0, baSoundByteArray.length);
		audioTrack.flush();
		audioTrack.play();

		new Thread() {
			@Override
			public void run() {
				int lastFrame = -1;
				while (lastFrame != audioTrack.getPlaybackHeadPosition()) {
					lastFrame = audioTrack.getPlaybackHeadPosition();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
				}
				audioTrack.stop();
				audioTrack.release();
				opsc.onPlaySoundComplete();
			}
		}.start();

	}
}
