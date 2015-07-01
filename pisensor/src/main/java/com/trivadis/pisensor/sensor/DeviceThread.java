package com.trivadis.pisensor.sensor;

public class DeviceThread extends Thread {
	
	private long sleepTime;


	public DeviceThread(long sleepTime) {
		this.sleepTime = sleepTime;
		setDaemon(true);
	}

	
	public void run() {
		int errors = 0;
		while (true) {
			try {
				
				performDeviceAccess();
				
				Thread.sleep(2000);

			} catch (Exception e) {
				e.printStackTrace();
				errors++;
				if (errors > 10)
					return;
				else {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}
	}


	public void performDeviceAccess() {
	}
}
