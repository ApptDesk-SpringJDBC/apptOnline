function initiateCalender(){
	try {
		$( "#date").datepicker({
			changeMonth: true,
			changeYear: true,
			dateFormat: 'mm/dd/yy',
			onSelect: populateTimeForSelectdate,
			beforeShowDay: showAvaliableDates
		});	
		//This is to set  default dates
		$( "#date").datepicker( "option", "defaultDate", getDefault_Date());
	} catch (e) {
		alert("error - "+e)
	}
}

function getDefault_Date() {
	var defaultDate = $("#defaultDate").val();
	if(defaultDate=="" || defaultDate==undefined){
		defaultDate = $.datepicker.formatDate('mm/dd/yy', new Date());
	}else{
		defaultDate = $.datepicker.formatDate('mm/dd/yy', new Date());
	}
	return defaultDate;
}

var Map = {};

function prepareDatesMap(dates){ //comma seperated
	Map = {};
	var currentDate=$.datepicker.formatDate('yy-mm-dd', new Date());	//date from Back end -- 2016-08-08
	try {
		if(dates!="" && dates != undefined){
			var datesArr = dates.split(",");
			for(var i=0;i<datesArr.length;i++){
				if(i==0){
					$("#defaultDate").val(datesArr[i]);
				}
				Map[datesArr[i]]=datesArr[i];
			}		
			/*
			for (var key in Map) {
				if (Map.hasOwnProperty(key)) {
					alert(key + " -> " + Map[key]);
				}
			}
			*/
		}
	} catch (e) {
		alert("Error -- "+e);
	}
}

function populateTimeForSelectdate(dateStr){ //08/08/2016 
	 try {
		//this we have to check with balaji what value he is accepting
		// 01/17/2014  ===>  2014-01-17
		var splitStr = dateStr.split("/");
		dateStr = splitStr[2]+"-"+splitStr[0]+"-"+splitStr[1];
		$("#time_div").show();
		var url = "getAvailableTimesCallcenter.html?apptDate="+dateStr;
		
		$.get(url,function(res,stat){
			res = $.parseJSON(res);
			if(!res.displayErrorPage){			
				if(!res.errorStatus){	
					$('#time_error').html("");
					$('#time_error').hide();
					var jsonMap = res.jsonMap;
					if(jsonMap.status){					
						try{
							var availableTimesStr =  jsonMap.availableTimes;
							var availableTimes = availableTimesStr.split(",");
							var optionLength = availableTimes.length;
							
							$('#time option:gt(0)').remove();
							if(optionLength==1){
								$("#time_label").show();
								$("#time").hide();
								$('#time').append($('<option>', {value:availableTimes[0], text:availableTimes[0]}));
								$('#time').val(availableTimes[0]);
								$("#time_label").html(availableTimes[0]);
							}else{
								$("#time").show();
								$("#time_label").html("");
								$("#time_label").hide();
								for (var i=0;i<optionLength;i++){									
									$('#time').append($('<option>', {value:availableTimes[i], text:availableTimes[i]}));
								}	
							}
							//alert("TIME :::: "+($('#time').val()))
						}catch(e){
							alert("Error : "+e);
						}
					}else{
						window.location=res.errorPage;
					}
				}else{
					$('#time_error').show();
					$("#time_error").html("<span class='ui-icon ui-icon-alert float_left'></span>&nbsp;"+res.errorMessage);
				}
			}else{
				window.location=res.errorPage;
			}
		});
	}catch (e) {
		//alert("Error -- "+e);
	}
}

function showAvaliableDates(date) {	
	try {
		Map[date] 
		date = $.datepicker.formatDate('yy-mm-dd',date); //date from Back end -- 2015-08-08
		if(date in Map) {
			return [true,"available","Available"];
		}else{
			return [false,"","Disabled"];
		}
	} catch (e) {
		alert("Error -- "+e);
	}
}

function onServiceNextBtnClick(leftCurrentDiv,leftNextDiv,rightCurrentDiv,rightNextDiv){
	try{
		 var date = $("#date").val();
		 var time = $("#time").val();
		 var url = "holdAppointmentCallCenter.html?date="+date+"&time"+time;
		 url = url.replace(/ /g, "%20");
		 $.get(url,function(res,stat){
			alert("RES  ::::::::::::  "+res);
			res = $.parseJSON(res);
			if(!res.displayErrorPage){			
				if(!res.errorStatus){		
					var jsonMap = res.jsonMap;
					alert(" jsonMap.status  ::::::::::::  "+(jsonMap.status));
					if(jsonMap.status){					
						if(jsonMap.errorFlag=="N"){	//This line updated on - 27/06/2016	
							$("#serviceNextBtn").show(); //This line added on - 27/06/2016	
							$("#eligibilityTableForServiceId"+serviceId).show();
							applyNextBtnClickedEffects(leftCurrentDiv,leftNextDiv,rightCurrentDiv,rightNextDiv);
						}else{
							$('#service_error').show();
							$("#service_error").html("<span class='ui-icon ui-icon-alert float_left'></span>&nbsp;"+jsonMap.errorMessage);
						}
					}else{
						window.location=res.errorPage;
					}
				}else{
					$('#service_error').show();
					$("#service_error").html("<span class='ui-icon ui-icon-alert float_left'></span>&nbsp;"+res.errorMessage);
				}
			}else{
				window.location=res.errorPage;
			}
		 });
	}catch(e){
		alert("Error : "+e);
	}
}
