package org.toilelibre.libe.phonecomposer.thread;


import android.util.Log;

import org.toilelibre.libe.phonecomposer.DialActivity;
import org.toilelibre.libe.phonecomposer.code.DTMFCode;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class DialThread extends Thread {
	
	private ByteBuffer bb;
	private String phoneNumber;
	private OnThreadCompleteListener onComplete;
	
	private int noteLength = 200;
	private int pauseLength = 50;
	private float sampleRate = 48000f;
	
	public DialThread (String phoneNumberParam, OnThreadCompleteListener onCompleteParam){
		this.phoneNumber = phoneNumberParam;
		this.onComplete = onCompleteParam;
	}
	
	
	@Override
	public void run () {
		final int totalLength = this.noteLength  + this.pauseLength;
		final int mult = (int) (this.sampleRate / 1000 * 2 * 2);
		this.bb = ByteBuffer.allocate(mult * totalLength * this.phoneNumber.length());
		byte [] zeros = new byte [mult * this.pauseLength];
		Arrays.fill(zeros, 0, zeros.length, (byte)0);
		for (int offset = 0 ; offset < this.phoneNumber.length() * totalLength ; offset += totalLength){
			char c = this.phoneNumber.charAt(offset / totalLength);
			int [] dtmf = DTMFCode.FREQUENCIES.get(Character.valueOf (c));
			byte [] tfs = Tone.twoFreqsSound(this.sampleRate, dtmf [0], dtmf [1], this.noteLength, 0.5);
			this.bb.put(tfs);
			this.bb.put(zeros);
		}
		
		PlaySound.playBa((int)this.sampleRate, this.bb.array(),
				new OnPlaySoundComplete () {
					
					@Override
					public void onPlaySoundComplete () {
				        Log.i(PlaySound.class.getName(), "DialThread - Stopping");
				        DialThread.this.end ();
					}
				});
	}

	public void end (){
		Log.i(DialActivity.class.getName(), "DialThread - end");
		this.bb.clear();
		this.onComplete.onThreadComplete();
	}
}
