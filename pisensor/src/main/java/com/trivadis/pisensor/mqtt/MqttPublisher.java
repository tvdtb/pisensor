package com.trivadis.pisensor.mqtt;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.trivadis.pisensor.ISODateAdapter;
import com.trivadis.pisensor.TemperatureEvent;

@Singleton
public class MqttPublisher {

	private MqttClient client;
	private MqttTopic topic;
	private long errorTime = 0L;

	@Inject
	ISODateAdapter iso;

	@PostConstruct
	public void connect() {
		try {
			if (getMqttTopic() == null || getMqttUrl() == null)
				return;

			client = new MqttClient(getMqttUrl(),
					MqttClient.generateClientId(), new MemoryPersistence());
			client.connect();
			topic = client.getTopic(getMqttTopic());
			errorTime = 0L;
			
			System.out.println("MQTT -> "+getMqttUrl()+" -> "+getMqttTopic());
			
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	private String getMqttTopic() {
		// -Dmqtt.topic=temperature
		return System.getProperty("mqtt.topic");
	}

	private String getMqttUrl() {
		// -Dmqtt.url=tcp://test.mosquitto.org:1883
		return System.getProperty("mqtt.url");
	}

	private String getSource() {
		// -Dmqtt.source=tvdtb
		String source = System.getProperty("mqtt.source");
		if (source != null)
			return source;

		try {
			String result = InetAddress.getLocalHost().getHostName();
			if (result != null && !result.isEmpty())
				return result;
		} catch (UnknownHostException e) {
			// failed; try alternate means.
		}
		String host = System.getenv("COMPUTERNAME");
		if (host != null)
			return host;
		host = System.getenv("HOSTNAME");
		if (host != null)
			return host;
		return null;
	}

	private String payLoadTemplate(final TemperatureEvent temp) {
		String val = "{" //
				+ "\"source\":" + "\"" + getSource() + "\"," //
				+ "\"time\":" + "\"" + iso.marshal(temp.getTime()) + "\"," //
				+ "\"temperature\":" //
				/*        */+ "\"" + temp.getTemperature() + "\", "//
				+ "\"pressure\":" + "\"" + temp.getPressure() + "\"" //
				+ "}";

		return val;
	}

	public void publishToTopic(@Observes TemperatureEvent temperature) {
		if (topic == null) {
			if (client != null
					&& errorTime + 300000 < System.currentTimeMillis())
				connect(); // reconnect after 5 minutes
			return;
		}

		try {
			topic.publish(new MqttMessage(payLoadTemplate(temperature)
					.getBytes()));
		} catch (MqttException e) {
			topic = null;
			errorTime = System.currentTimeMillis();
			e.printStackTrace();
		}
	}

}
