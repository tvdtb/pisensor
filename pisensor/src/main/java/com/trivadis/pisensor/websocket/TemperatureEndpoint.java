package com.trivadis.pisensor.websocket;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.event.Observes;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.trivadis.pisensor.TemperatureEvent;

@ServerEndpoint(value = "/temperature")
public class TemperatureEndpoint {

	private static final Logger LOGGER = Logger
			.getLogger(TemperatureEndpoint.class.getName());
	
	private static Collection<Session> sessions = Collections.synchronizedSet(new HashSet<>());

	private volatile static TemperatureEvent currentTemp;

	public TemperatureEndpoint() {
	}
	
	public void receiveTemperatureEvent(@Observes TemperatureEvent temp) {
		this.currentTemp = temp;
		for (Session s:sessions) {
			try {
				s.getBasicRemote().sendText(temp.toString());
			} catch (IOException e) {
				// ignore
				try {
					s.close();
				} catch (IOException e1) {
					// ignore
				}
			}
		}
	}
	
	
	
	@OnOpen
	public void onOpen(Session session) throws IOException {
		LOGGER.log(Level.INFO, "New connection with client: {0}",
				session.getId());
		sessions.add(session);
		session.getBasicRemote().sendText(currentTemp.toString());
	}

	@OnMessage
	public String onMessage(String message, Session session) {
		LOGGER.log(Level.INFO, "New message from Client [{0}]: {1}",
				new Object[] { session.getId(), message });
		return currentTemp==null ? "no data" : currentTemp.toString();
	}

	@OnClose
	public void onClose(Session session) {
		LOGGER.log(Level.INFO, "Close connection for client: {0}",
				session.getId());
	}

	@OnError
	public void onError(Throwable exception, Session session) {
		LOGGER.log(Level.INFO, "Error for client: {0}", session.getId());
	}
}