pisensor = {
	options : {

	}

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
	updateTemperature : function() {
		jQuery.ajax("rs/temperature", {
			success : function(data, textStatus, jqXHR) {
				window.tmp = data;
				console.log(data);
				console.log(new Date(data.time));
				$('#currentDate').text(new Date(data.time).toLocaleString());
				$('#currentTemp').text("" + data.temperature);
				$('#currentPressure').text("" + data.pressure);
			}
		});
	}

	,
	updateChars : function() {
		jQuery.ajax("rs/temperature/history",
				{
					success : function(result, textStatus, jqXHR) {

						var dataTemp = [];
						var dataPressure = [];
						var labels = [];
						var lastKey = {};

						for (var i = 0; i < result.length; i++) {
							if (labels.length > 50)
								break;

							// console.log(new Date(tempdata[i].timeOfEvent)+"
							// "+tempdata[i].celsius);
							var eventDate = new Date(result[i].timeOfEvent);
							var key = pisensor.dateKey(eventDate);

							if (key != lastKey) {
								console.log(key + " " + result[i].celsius)
								dataTemp.push(result[i].celsius);
								dataPressure.push(result[i].pressure);
								labels.push(pisensor.fmtDate(eventDate));

								lastKey = key;
							}

						}

						dataTemp.reverse();
						dataPressure.reverse();
						labels.reverse();

						var ctx = document.getElementById("tempChart")
								.getContext("2d");
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

						ctx = document.getElementById("baroChart").getContext(
								"2d");
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
					}
				});

	}
}
