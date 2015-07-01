package com.trivadis.grovepi.service;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.trivadis.cdi.eager.Eager;
import com.trivadis.grovepi.model.Distance;
import com.trivadis.grovepi.model.TemperatureHumidity;

@Singleton
@Eager
public class AutoUpdaterService {

	private NumberFormat nf1;
	private NumberFormat nf0;

	private String line1="";
	private String line2="";
	
	@Inject
	LCDService service;
	
	@PostConstruct
	public void init() {
			nf1 = DecimalFormat.getNumberInstance();
			nf1.setMinimumFractionDigits(1);
			nf1.setMaximumFractionDigits(1);

			nf0 = DecimalFormat.getNumberInstance();
			nf0.setMaximumFractionDigits(0);
	}

	public void updateText(@Observes TemperatureHumidity tempHum)  {
		line1 = nf1.format(tempHum.getTemp())+"Grad C, " //
				+nf0.format(tempHum.getHumidity())+"%";
		updateText();
	}
	public void updateText(@Observes Distance distance)  {
		line2 = nf1.format(distance.getDistance())+"cm";
		updateText();
	}
	
	private void updateText() {
		String msg = line1+"\n"+line2;
		service.setText(msg, 5000);
	}


}
