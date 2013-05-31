package org.toilelibre.libe.phonecomposer.thread;

public class Tone {


	public static byte[] sound(float sampleRate, int hz, int msecs, double vol) {

		if (hz <= 0)
			throw new IllegalArgumentException("Frequency <= 0 hz");

		if (msecs <= 0)
			throw new IllegalArgumentException("Duration <= 0 msecs");

		if (vol > 1.0 || vol < 0.0)
			throw new IllegalArgumentException("Volume out of range 0.0 -  1.0");

		byte[] buf = new byte[(int) sampleRate * msecs / 1000];

		for (int i = 0; i < buf.length; i++) {
			double angle = i / (sampleRate / hz) * 2.0 * Math.PI;
			buf[i] = (byte) (Math.sin(angle) * 127.0 * vol);
		}

		// shape the front and back 10ms of the wave form
		for (int i = 0; i < sampleRate / 100.0 && i < buf.length / 2; i++) {
			buf[i] = (byte) (buf[i] * i / (sampleRate / 100.0));
			buf[buf.length - 1 - i] = (byte) (buf[buf.length - 1 - i] * i / (sampleRate / 100.0));
		}
		return buf;

	}
	public static byte[] twoFreqsSound(float sampleRate, int hz1, int hz2, int msecs, double vol) {

		if (sampleRate <= 0)
			throw new IllegalArgumentException("Sample Rate <= 0 ");
		
		if (hz1 <= 0)
			throw new IllegalArgumentException("Frequency <= 0 hz");

		if (hz2 <= 0)
			throw new IllegalArgumentException("Frequency <= 0 hz");

		if (msecs <= 0)
			throw new IllegalArgumentException("Duration <= 0 msecs");

		if (vol > 1.0 || vol < 0.0)
			throw new IllegalArgumentException("Volume out of range 0.0 -  1.0");

		byte[] buf = new byte[(int) sampleRate * msecs * 2 / 1000];

		for (int i = 0; i < buf.length; i++) {
			double angle1 = i / (sampleRate * 2 / hz1) * 2.0 * Math.PI;
			double angle2 = i / (sampleRate * 2 / hz2) * 2.0 * Math.PI;
			buf[i] = (byte) (Math.sin(angle1) * 255.0 * vol / 2 + 
					Math.sin(angle2) * 255.0 * vol / 2);
		}

		// shape the front and back 10ms of the wave form
		for (int i = 0; i < sampleRate / 100.0 && i < buf.length / 2; i++) {
			buf[i] = (byte) (buf[i] * i / (sampleRate / 100.0));
			buf[buf.length - 1 - i] = (byte) (buf[buf.length - 1 - i] * i / (sampleRate / 100.0));
		}
		return buf;

	}
}
