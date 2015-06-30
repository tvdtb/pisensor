package com.trivadis.grovepi.device;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;

import com.github.jbman.jgrove.GrovePi;
import com.github.jbman.jgrove.I2cPin;
import com.trivadis.grovepi.model.Distance;

public class UltrasonicSensor {

	private static final int ULTRASONIC_READ_CMD = 7;

	I2cPin pin;

	public void init() {
		try {
			pin = GrovePi.getInstance().createI2cPin(4);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public Distance readDistance() {
		try {
			synchronized (GrovePi.class) {

				pin.writeCommand(ULTRASONIC_READ_CMD);
				GrovePi.sleep(500);
				byte[] buffer = pin.read(3);
				return new Distance(((buffer[1] & 0xFF) << 8)
						+ (buffer[2] & 0xFF), LocalDateTime.now());
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
