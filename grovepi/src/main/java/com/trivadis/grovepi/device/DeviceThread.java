package com.trivadis.grovepi.device;

public class DeviceThread extends Thread {
	
	private long sleepTime;
	private String name;


	public DeviceThread(String name, long sleepTime) {
		this.name = name;
		this.sleepTime = sleepTime;
		setDaemon(true);
		setName("DeviceThread-"+name);
	}

	
	public void run() {
		int errors = 0;
		while (true) {
			try {
				
				performDeviceAccess();
				
				Thread.sleep(sleepTime);

			} catch (Throwable e) {
				System.out.println("IOError: "+name+" "+e.getMessage());
//				e.printStackTrace();
				errors++;
//				if (errors > 10)
//					return;
//				else {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
//				}
			}
		}
	}


	public void performDeviceAccess() {
	}
}
