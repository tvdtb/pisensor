package com.trivadis.grovepi.service;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;

import com.trivadis.cdi.eager.Eager;
import com.trivadis.grovepi.device.RgbLCDv2;

@Singleton
@Eager
public class LCDService {

	private RgbLCDv2 lcd;

	private long locked;

	@PostConstruct
	public void init() {
			locked = 0L;
			lcd = new RgbLCDv2();
			lcd.init();
			setText("Server started", 0L);
			setColor(0, 0, 255);
	}

	public void setText(String txt, long validity) {
		long newValidity = System.currentTimeMillis() + validity;
		if (newValidity > locked) {
			lcd.setText(txt);
		}
	}

	public void setColor(int r, int g, int b) {
		lcd.setColor(r, g, b);
	}

}
