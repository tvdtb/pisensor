package com.trivadis.grovepi.device;

import java.io.IOException;
import java.nio.charset.Charset;

import com.github.jbman.jgrove.GrovePi;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

public class RgbLCDv2 {
	private static final int DISPLAY_RGB_ADDR = 0x62;
	private static final int DISPLAY_TEXT_ADDR = 0x3e;

	private I2CDevice lightDevice;
	private I2CDevice textDevice;

	public void init() {
		try {
			synchronized (GrovePi.class) {
				
				lightDevice = GrovePi.getInstance().getDevice(DISPLAY_RGB_ADDR);
				textDevice = GrovePi.getInstance().getDevice(DISPLAY_TEXT_ADDR);

				setColor(255, 255, 255);

				textDevice(32 + 16 + 8 /* 2-line */+ 4 /* on */); // Function
																	// set
				GrovePi.sleep(5);

				textDevice(8 + 4 /* on */+ 0 /* cursor off */+ 0 /* blink off */); // on/off
				GrovePi.sleep(5);
			}
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}

	public void setText(String text) {
		clear();
		
		try {
			synchronized (GrovePi.class) {

				byte[] bytes = text.getBytes(Charset.forName("US-ASCII"));
				int count = 0;
				int row = 0;
				for (byte c : bytes) {
					if (c == '\n' || count == 16) {
						while (count<16) {
							textDevice.write(0x40, (byte)' ');
							count++;
						}
						
						count = 0;
						row += 1;
						if (row == 2) {
							break;
						}
						textDevice(0xc0);
						if (c == '\n') {
							continue;
						}
					}
					count += 1;
					textDevice.write(0x40, c);
				}
				GrovePi.sleep(10);
			}
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}

	private void clear() {
		try {
			synchronized (GrovePi.class) {

				textDevice(1); // clear
				GrovePi.sleep(10);
			}
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}

	private void textDevice(int cmd) throws IOException {
		textDevice.write(0x80, (byte) cmd);
	}

	public void setColor(int r, int g, int b) {
		synchronized (GrovePi.class) {
			try {
				lightDevice.write(0, (byte) 0);
				lightDevice.write(1, (byte) 0);
				lightDevice.write(0x08, (byte) 0xaa);
				lightDevice.write(4, (byte) r);
				lightDevice.write(3, (byte) g);
				lightDevice.write(2, (byte) b);
			} catch (IOException ioe) {
				throw new RuntimeException(ioe);
			}
		}

	}

}
