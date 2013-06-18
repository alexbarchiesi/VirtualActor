package directorui;

import processing.core.*;
import ddf.minim.*;
import ddf.minim.analysis.*;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import javax.sound.sampled.AudioInputStream;

/**
 * Processing sketch for rendering the audio waveform.
 *
 */
public class WaveFormAreaSketch extends PApplet {
	private static final long serialVersionUID = 1L;

	public static final int[] BG_COLOR 		= {   2,  52,  77, 255};
	public static final int[] STROKE_COLOR 	= {   0, 180, 234, 255};
	public static final int[] CP_COLOR 		= { 255, 255, 255, 255};

	private final int x0, y0;	// top left
	private final int w,  h;	// size of box

	private PGraphics pg;

	private TreeMap<Integer,Integer> graphPoints;

	public WaveFormAreaSketch(int x0, int y0, int width, int height, PGraphics pg) {
		this.x0 = x0;
		this.y0 = y0;
		this.w  = width;
		this.h  = height;
		this.pg = pg;

		this.graphPoints = new TreeMap<Integer,Integer>();
	}

	public void setup() {
		size(w, h);
	}

	public void draw() {
		pg.beginDraw();
		pg.background(BG_COLOR[0], BG_COLOR[1], BG_COLOR[2], BG_COLOR[3]);

		if(graphPoints.isEmpty()) {
			pg.endDraw();
			return;
		}

		pg.fill(STROKE_COLOR[0], STROKE_COLOR[1], STROKE_COLOR[2], STROKE_COLOR[3]);
		pg.noStroke();
		pg.beginShape();

		for(Map.Entry<Integer,Integer> point : graphPoints.entrySet()) {
			int key = point.getKey();
			float value = point.getValue();

			pg.vertex(key, this.h/2 - value);
		}

		for(Map.Entry<Integer,Integer> point : graphPoints.descendingMap().entrySet()) {
			int key = point.getKey();
			float value = point.getValue();

			pg.vertex(key, this.h/2 + value);
		}

		pg.endShape();
		pg.endDraw();
	}

	void convertToGraph(float[][] spectra) {
		graphPoints = new TreeMap<Integer,Integer>();
		TreeMap<Integer,Float> tmp = new TreeMap<Integer,Float>();

		float maxValue = -1;

		//render the spectra going back into the screen
		//		println(width);
		//		println(spectra.length);

		for(int s = 0; s < spectra.length; s++) {
			int i =0;
			float total = 0; 
			for(i = 0; i < spectra[s].length-1; i++) {
				total += spectra[s][i];
			}
			total = total / 10;

			// identify maximum
			if(total > maxValue) maxValue = total;

			tmp.put(s,total);
		}

		for(Map.Entry<Integer,Float> point : tmp.entrySet()) {
			int key = point.getKey();
			float value = point.getValue();

			// scale to draw area
			graphPoints.put(key * this.w /tmp.size(), round((float)(this.h/2 - 10) * value / maxValue));
		}
	}

	/**
	 * Based on code originally posted here:
	 * http://code.compartmental.net/minim/examples/FFT/offlineAnalysis/
	 * and here:
	 * https://forum.processing.org/topic/how-to-generate-a-simple-waveform-of-an-entire-sound-file
	 * @return 
	 */
	float[][] analyzeUsingAudioSample(AudioInputStream audio) {

		Minim minim = new Minim(this);

		//AudioSample jingle = minim.loadSample("E:/EPFL/MSc Semester Project II - Virtual Actor/minim test/waveform/data/row.mp3", 2048);
		AudioSample jingle = minim.loadSample(audio);

		// get the left channel of the audio as a float array
		// getChannel is defined in the interface BuffereAudio, 
		// which also defines two constants to use as an argument
		// BufferedAudio.LEFT and BufferedAudio.RIGHT
		float[] leftChannel = jingle.getChannel(AudioSample.LEFT);

		// then we create an array we'll copy sample data into for the FFT object
		// this should be as large as you want your FFT to be. generally speaking, 1024 is probably fine.
		int fftSize = 1024;
		float[] fftSamples = new float[fftSize];
		FFT fft = new FFT( fftSize, jingle.sampleRate() );

		// now we'll analyze the samples in chunks
		int totalChunks = (leftChannel.length / fftSize) + 1;

		// allocate a 2-dimentional array that will hold all of the spectrum data for all of the chunks.
		// the second dimension if fftSize/2 because the spectrum size is always half the number of samples analyzed.
		float[][] spectra = new float[totalChunks][fftSize/2];

		for(int chunkIdx = 0; chunkIdx < totalChunks; ++chunkIdx) {
			int chunkStartIndex = chunkIdx * fftSize;

			// the chunk size will always be fftSize, except for the 
			// last chunk, which will be however many samples are left in source
			int chunkSize = min( leftChannel.length - chunkStartIndex, fftSize );

			// copy first chunk into our analysis array
			arrayCopy( leftChannel, // source of the copy
					chunkStartIndex, // index to start in the source
					fftSamples, // destination of the copy
					0, // index to copy to
					chunkSize // how many samples to copy
					);

			// if the chunk was smaller than the fftSize, we need to pad the analysis buffer with zeroes        
			if ( chunkSize < fftSize ) {
				// we use a system call for this
				Arrays.fill(fftSamples, chunkSize, fftSamples.length - 1, 0.0f );
			}

			// now analyze this buffer
			fft.forward( fftSamples );

			// and copy the resulting spectrum into our spectra array
			for(int i = 0; i < 512; ++i) {
				spectra[chunkIdx][i] = fft.getBand(i);
			}
		}

		jingle.close();
		minim.stop();

		return spectra;
	}

	public PGraphics getPg() {
		return pg;
	}

	public float getX0() {
		return x0;
	}

	public float getY0() {
		return y0;
	}

	public void loadAudio(AudioInputStream audio) {
		convertToGraph(analyzeUsingAudioSample(audio));
	}
}
