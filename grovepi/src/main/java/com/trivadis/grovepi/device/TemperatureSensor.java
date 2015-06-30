package com.trivadis.grovepi.device;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.time.LocalDateTime;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;

import com.github.jbman.jgrove.GrovePi;
import com.github.jbman.jgrove.I2cPin;
import com.trivadis.grovepi.model.TemperatureHumidity;

public class TemperatureSensor {

	private static final int READ_CMD = 40;

	I2cPin pin;

	public void init() {
		try {
			pin = GrovePi.getInstance().createI2cPin(3);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public TemperatureHumidity read() {
		try {
			synchronized (GrovePi.class) {
				pin.writeCommand(READ_CMD);
				GrovePi.sleep(100);
				byte[] buffer = pin.read(9);

				float temp = ByteBuffer.wrap(buffer, 1, 4)
						.order(ByteOrder.LITTLE_ENDIAN).getFloat();
				float humidity = ByteBuffer.wrap(buffer, 5, 4)
						.order(ByteOrder.LITTLE_ENDIAN).getFloat();
				return new TemperatureHumidity(temp, humidity,
						LocalDateTime.now());
			}
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}

}
