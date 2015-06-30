package com.trivadis.pisensor.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


public class MqttListener {

	public static void main(String[] args) {
		try {
			MqttClient client = new MqttClient( //
//					"tcp://test.mosquitto.org:1883" //
					"tcp://172.16.19.86:9000" //
					,
					MqttClient.generateClientId(), new MemoryPersistence());
			client.connect();
			System.out.println("Client connected!");
			client.subscribe("/environmentalSensor/stuttgart/1");
			client.setCallback(new MqttCallback() {
				
				@Override
				public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
					System.out.println("message "+arg0+"  "+arg1);
					
				}
				
				@Override
				public void deliveryComplete(IMqttDeliveryToken arg0) {
					System.out.println("deliveryComplete");
					
				}
				
				@Override
				public void connectionLost(Throwable arg0) {
					System.out.println("connectionLost");
				}
			});
			
			Thread.currentThread().sleep(300000);
			client.disconnect();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
