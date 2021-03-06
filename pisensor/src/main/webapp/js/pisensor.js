pisensor = {
	options : {
		pointDotRadius : 2,
		pointHitDetectionRadius : 2,
	}
	,
	maxlength : 80

	,
	fmtDate : function(date) {
		var minutes = date.getMinutes();
		minutes = minutes < 10 ? "0" + minutes : "" + minutes;
		return "" + date.getDate() + "." + date.getMonth() + ". "
				+ date.getHours() + ":00";
	}

	,
	dateKey : function(date) {
		return "" + date.getMonth() + "-" + date.getDate() + "."
				+ date.getHours();
	}

	,
	updateTemperature : function(callback) {
		jQuery.ajax("rs/temperature", {
			success : function(data, textStatus, jqXHR) {
				window.tmp = data;
				$('#currentDate').text(new Date(data.time).toLocaleString());
				$('#currentTemp').text("" + data.temperature);
				$('#currentPressure').text("" + (data.pressure/100));
				
				callback(data);
			}
		});
	}

	,
	readData : function(link, labels, dataTemp, dataPressure, chartFunction) {
		jQuery.ajax(link, {
			success : function(result, textStatus, jqXHR) {
				var more = pisensor.collectData(result, labels, dataTemp,
						dataPressure)

				var links = hateoas.parse(jqXHR.getResponseHeader("Link"));
				if (more && links.next) {
					console.log("need more next=" + links.next);
					pisensor.readData(links.next, labels, dataTemp,
							dataPressure, chartFunction);
				} else {
					chartFunction(labels, dataTemp, dataPressure);
				}
			}
		});
	}

	,
	collectData : function(result, labels, dataTemp, dataPressure) {
		var lastLabel = {};
		if (labels.length > 0)
			lastLabel = labels[labels.length-1];

		for (var i = 0; i < result.length; i++) {
			if (labels.length > pisensor.maxlength)
				return false;

			var eventDate = new Date(result[i].timeOfEvent);
			var label = pisensor.fmtDate(eventDate);

			if (label != lastLabel) {
				labels.push(label);
				dataTemp.push(result[i].celsius);
				dataPressure.push(result[i].pressure / 100);

				lastLabel = label;
			}
		}
		return true;
	}

	,
	updateChartData : function(offset, scale) {

		pisensor.readData("rs/temperature/history?offset="+offset+"&limit=200&scale="+scale //
		, [], [], [] // labels, dataTemp, dataPressure //
		, function(labels, dataTemp, dataPressure) {
			labels.reverse();
			dataTemp.reverse();
			dataPressure.reverse();

			var div = document.getElementById("tempChart");
			div.innerHTML='';
			var canvas = document.createElement("canvas");
			canvas.width=1000;
			canvas.height=300;
			var ctx = canvas.getContext("2d");
			div.appendChild(canvas);
			
			var tempChart = new Chart(ctx).Line({
				labels : labels,
				datasets : [ {
					label : "Temperatur",
					fillColor : "rgba(151,187,205,0.2)",
					strokeColor : "rgba(151,187,205,1)",
					pointColor : "rgba(151,187,205,1)",
					pointStrokeColor : "#fff",
					pointHighlightFill : "#fff",
					pointHighlightStroke : "rgba(151,187,205,1)",
					data : dataTemp
				} ]
			}, pisensor.options);

			div = document.getElementById("baroChart");
			div.innerHTML='';
			canvas = document.createElement("canvas");
			canvas.width=1000;
			canvas.height=300;
			ctx = canvas.getContext("2d");
			div.appendChild(canvas);

			var baroChart = new Chart(ctx).Line({
				labels : labels,
				datasets : [ {
					label : "Luftdruck",
					fillColor : "rgba(220,220,220,0.2)",
					strokeColor : "rgba(220,220,220,1)",
					pointColor : "rgba(220,220,220,1)",
					pointStrokeColor : "#fff",
					pointHighlightFill : "#fff",
					pointHighlightStroke : "rgba(220,220,220,1)",
					data : dataPressure
				} ]
			}, pisensor.options);
		});
	}
}
