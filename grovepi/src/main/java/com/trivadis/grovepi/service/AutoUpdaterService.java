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
	private int increment=1;
	
	@Inject
	LCDService service;
	
	private int[][] colors = new int[][] {
			{ 255, 0, 0 }
			, { 255, 255, 0 }
			, { 0, 255, 0 }
			, { 180, 180, 180 }
	};
	private int[] ranges = new int[] {15, 25, 35, Integer.MAX_VALUE};
	private int[] color = colors[2];

	
	
	@PostConstruct
	public void init() {
			nf1 = DecimalFormat.getNumberInstance();
			nf1.setMinimumFractionDigits(1);
			nf1.setMaximumFractionDigits(1);

			nf0 = DecimalFormat.getNumberInstance();
			nf0.setMaximumFractionDigits(0);
			nf0.setGroupingUsed(false);
	}

	public void updateText(@Observes TemperatureHumidity tempHum)  {
		line1 = nf1.format(tempHum.getTemp())+" Grad C, " //
				+nf0.format(tempHum.getHumidity())+"%";
		updateText();
	}
	public void updateText(@Observes Distance distance)  {
		if (distance.getDistance()>0) {
			line2 = nf0.format(distance.getDistance())+"cm";
			int colorIndex = 0;
			while (distance.getDistance() > ranges[colorIndex])
				colorIndex++;
			color = colors[colorIndex];
			updateText();
		}
	}
	
	private void updateText() {
		String msg = line1+"\n"+line2+"  #"+(increment++);
		service.setText(msg, 5000);
		service.setColor(color[0], color[1], color[2]);
	}


}
