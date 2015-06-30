package com.trivadis.grovepi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class ISODateAdapter extends XmlAdapter<String, LocalDateTime>{
	SimpleDateFormat format;
	
	public ISODateAdapter() {
		TimeZone tz = TimeZone.getTimeZone("UTC");
		format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
		format.setTimeZone(tz);
	}
	@Override
	public LocalDateTime unmarshal(String v) {
		try {
			Date date = format.parse(v);
			
			return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String marshal(LocalDateTime dateTime) {
		Date d = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
		String iso = format.format(d);
		return iso;
	}
	
	public static void main(String[] args) {
		try {
			ISODateAdapter ida = new ISODateAdapter();
			String marshalled = ida.marshal(LocalDateTime.now());
			System.out.println(marshalled);
			String unmarshalled = ida.marshal(ida.unmarshal(marshalled));
			System.out.println(unmarshalled);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
