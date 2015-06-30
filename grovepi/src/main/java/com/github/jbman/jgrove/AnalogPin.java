package com.github.jbman.jgrove;

import java.io.IOException;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

public class AnalogPin {
	
	private static final int DUMMY = 0x01;
	private static final int CMD_ANALOG_READ = 0x03;
	private static final int GROVEPI_ADDRESS = 0x03;
	private I2CBus bus;
	private int pin;
	private I2CDevice arduino;

	public AnalogPin(GrovePi grovePi, int pin) throws IOException {
		this.pin = pin;
		bus = I2CFactory.getInstance(I2CBus.BUS_1);
		arduino = bus.getDevice(GROVEPI_ADDRESS);
	}
	
	public int readValue() throws Exception {
		byte[] cmd = new byte[] { DUMMY, CMD_ANALOG_READ, (byte) pin, 0x00, 0x00};
		//address, 1, aRead_cmd + [pin, unused, unused]
		arduino.write(cmd, 0, cmd.length);
		Thread.sleep(100);
		
		byte[] result = new byte[2];
		arduino.read(result, 0, result.length);
		
		System.out.print("READ:");
		System.out.print(result[0]);
		System.out.print("-");
		System.out.println(result[1]);
		return result[0]*256+result[1];
	}

}
