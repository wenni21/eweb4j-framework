package org.eweb4j.util;

import java.awt.image.BufferedImage;

/**
 * this filter illustrate the brightness and contrast of the image
 * and demo how to change the both attribute of the image.
 * 
 * @author gloomy fish
 *
 */
public class ContrastFilter extends AbstractBufferedImageOp {

	private float contrast = 1.2f; // default value;
	private float brightness = 1.0f; // default value;
	
	public ContrastFilter() {
		// do stuff here if you need......
	}
	
	public BufferedImage filter(BufferedImage src, BufferedImage dest) {
		int width = src.getWidth();
        int height = src.getHeight();

        if ( dest == null )
            dest = createCompatibleDestImage( src, null );

        int[] inPixels = new int[width*height];
        int[] outPixels = new int[width*height];
        src.getRGB( 0, 0, width, height, inPixels, 0, width );
        
        // calculate RED, GREEN, BLUE means of pixel
		int index = 0;
		int[] rgbmeans = new int[3];
		double redSum = 0, greenSum = 0, blueSum = 0;
		double total = height * width;
        for(int row=0; row<height; row++) {
        	int ta = 0, tr = 0, tg = 0, tb = 0;
        	for(int col=0; col<width; col++) {
        		index = row * width + col;
        		ta = (inPixels[index] >> 24) & 0xff;
                tr = (inPixels[index] >> 16) & 0xff;
                tg = (inPixels[index] >> 8) & 0xff;
                tb = inPixels[index] & 0xff;
                redSum += tr;
                greenSum += tg;
                blueSum +=tb;
        	}
        }
        
        rgbmeans[0] = (int)(redSum / total);
        rgbmeans[1] = (int)(greenSum / total);
        rgbmeans[2] = (int)(blueSum / total);
        
        // adjust contrast and brightness algorithm, here
        for(int row=0; row<height; row++) {
        	int ta = 0, tr = 0, tg = 0, tb = 0;
        	for(int col=0; col<width; col++) {
        		index = row * width + col;
        		ta = (inPixels[index] >> 24) & 0xff;
                tr = (inPixels[index] >> 16) & 0xff;
                tg = (inPixels[index] >> 8) & 0xff;
                tb = inPixels[index] & 0xff;
                
                // remove means
                tr -=rgbmeans[0];
                tg -=rgbmeans[1];
                tb -=rgbmeans[2];
                
                // adjust contrast now !!!
                tr = (int)(tr * getContrast());
                tg = (int)(tg * getContrast());
                tb = (int)(tb * getContrast());
                
                // adjust brightness
                tr += (int)(rgbmeans[0] * getBrightness());
                tg += (int)(rgbmeans[1] * getBrightness());
                tb += (int)(rgbmeans[2] * getBrightness());
                outPixels[index] = (ta << 24) | (clamp(tr) << 16) | (clamp(tg) << 8) | clamp(tb);
        	}
        }
        setRGB( dest, 0, 0, width, height, outPixels );
        return dest;
	}
	
	public int clamp(int value) {
		return value > 255 ? 255 :(value < 0 ? 0 : value);
	}

	public float getContrast() {
		return contrast;
	}

	public void setContrast(float contrast) {
		this.contrast = contrast;
	}

	public float getBrightness() {
		return brightness;
	}

	public void setBrightness(float brightness) {
		this.brightness = brightness;
	}

}
