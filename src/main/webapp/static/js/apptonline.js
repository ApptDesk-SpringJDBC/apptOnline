$(document).ready(function () {	
	//Auto Tab
	$("input[class='autotab']").bind('keyup', function() {
		var limit = parseInt($(this).attr('maxlength'));  
		var text = $(this).val();  			
		var chars = text.length; 
		//check if there are more characters then allowed
		if(chars >=limit){
			$("#"+$(this).next().attr("id")).focus();
		}	
	});
});

  function onLogoutBtnClick(){
	 window.location="logout.html";
  } 

 function  applyBackBtnClickedEffects(leftCurrentDiv,leftPrevDiv,rightCurrentDiv,rightPrevDiv){
		try{
		    var url = "changePageIndex.html?btnType=Back";
		    $.get(url,function(res,stat){
				res = $.parseJSON(res);
				if(!res.displayErrorPage){
					if(!res.errorStatus){
						$('#appt_online_error_msg').html("");
						$('#appt_online_error_msg').hide();

						$("#"+leftCurrentDiv).slideUp(300);
						$("#"+leftPrevDiv).slideDown(300);
						$("#"+leftCurrentDiv).prev().removeClass("ui-state-active").addClass("ui-state-default");
						$("#"+leftPrevDiv).prev().removeClass("ui-state-default").addClass("ui-state-active");
						$("#"+leftCurrentDiv).prev().find("span").removeClass("ui-icon-triangle-1-s").addClass("ui-icon-triangle-1-e");
						$("#"+leftPrevDiv).prev().find("span").removeClass("ui-icon-triangle-1-e").addClass("ui-icon-triangle-1-s");
						
						//if ($("#"+leftCurrentDiv).prev().hasClass( "ui-state-active" ) ) {
							$(".ui-state-active span").addClass("ui-icon-triangle-1-s").removeClass("ui-icon-triangle-1-e");
							//$('#righCont div').removeClass('active');
							//$("#"+rightPrevDiv).addClass('active');
							$("#"+rightCurrentDiv).hide();
							$("#"+rightPrevDiv).show();
						//}
					}else{
						$('#appt_online_error_msg').html("");
						$('#appt_online_error_msg').show();
						$("#appt_online_error_msg").html("<span class='ui-icon ui-icon-alert float_left'></span>&nbsp;"+(res.errorMessage));	
					}
				}else{
					window.location=res.errorPage;
				}
			});		  	
	    }catch(e){
		   alert("Error : "+e);
	    }
	}

   function applyNextBtnClickedEffects(leftCurrentDiv,leftNextDiv,rightCurrentDiv,rightNextDiv){
	  try{
		  var url = "changePageIndex.html?btnType=Next";
		  $.get(url,function(res,stat){
			  res = $.parseJSON(res);
				if(!res.displayErrorPage){
					if(!res.errorStatus){
						$('#appt_online_error_msg').html("");
						$('#appt_online_error_msg').hide();

						$("#"+leftCurrentDiv).slideUp(300);
						$("#"+leftNextDiv).slideDown(300);
						$("#"+leftCurrentDiv).prev().removeClass("ui-state-active").addClass("ui-state-default");
						$("#"+leftNextDiv).prev().removeClass("ui-state-default").addClass("ui-state-active");
						$("#"+leftCurrentDiv).prev().find("span").removeClass("ui-icon-triangle-1-s").addClass("ui-icon-triangle-1-e");
						$("#"+leftNextDiv).prev().find("span").removeClass("ui-icon-triangle-1-e").addClass("ui-icon-triangle-1-s");
						  
						//if ( $( "#"+leftCurrentDiv ).next().hasClass( "ui-state-active" ) ) {
						  //$('#righCont div').removeClass('active');
						  //$("#"+rightNextDiv).addClass('active');
						  $("#"+rightCurrentDiv).hide();
						  $("#"+rightNextDiv).show();
						//}
					}else{
						$('#appt_online_error_msg').html("");
						$('#appt_online_error_msg').show();
						$("#appt_online_error_msg").html("<span class='ui-icon ui-icon-alert float_left'></span>&nbsp;"+(res.errorMessage));	
					}
				}else{
					window.location=res.errorPage;
				}
		  });		  	
	  }catch(e){
		 alert("Error : "+e);
	  }
  }

function clearFormFieldsMsgs(formFields,startField){
	//alert("formFields ::::::::: "+formFields);
	if(formFields!="" && formFields!=null && formFields!=undefined){
		var formFieldsArr = formFields.split("|");
		var continueClear = false;
		for(var i=0;i<formFieldsArr.length;++i){
			try{
				 if(formFieldsArr[i]==startField){
					 continueClear = true;
				 }
				 if(continueClear){				
					$('#'+formFieldsArr[i]).html("");
					$('#'+formFieldsArr[i]).hide();
				 }
			}catch(e){
				alert("Error : "+e);
			}
		}
	}
}

function addFormFieldsMsgs(fieldsAndMessages){
	//[{"field":"procedure_warning","msg":"warning_msg","display":"ON_DEMAND","displayType":"warning_label"}]
	var fieldsAndMessages = $.parseJSON(fieldsAndMessages);
	for (var i=0;i<fieldsAndMessages.length;++i){
		try{
			var msg = fieldsAndMessages[i].msg;
			//alert("Field :: "+fieldsAndMessages[i].field+"\n MSG :: "+msg+"\n Display :: "+fieldsAndMessages[i].display+"\n DisplayType :: "+fieldsAndMessages[i].displayType);
			//if(msg!="" && msg!=null && msg!=undefined){
				$('#'+(fieldsAndMessages[i].field)).html("");
				$('#'+(fieldsAndMessages[i].field)).show();
				
				if(fieldsAndMessages[i].displayType!=undefined){
					if(fieldsAndMessages[i].displayType.trim()=='label'){
						$("#"+(fieldsAndMessages[i].field)).html(msg);
					}else{
						$("#"+(fieldsAndMessages[i].field)).html("<span class='ui-icon ui-icon-alert float_left'></span>&nbsp;"+msg);
					}	
				}else{
					$("#"+(fieldsAndMessages[i].field)).html("<span class='ui-icon ui-icon-alert float_left'></span>&nbsp;"+msg);
				}
			//}
		}catch(e){
			 alert("Error : "+e);
		}
	}
}

function clearOrAddFieldMsgs(fieldId,show,msg){
	try{
		 if(show){		
			$('#'+fieldId).show();
			$("#"+fieldId).html("<span class='ui-icon ui-icon-alert float_left'></span>&nbsp;"+msg);
		 }else{
			$('#'+fieldId).html("");
			$('#'+fieldId).hide();			
		 }		 
	}catch(e){
		alert("Error : "+e);
	}
}

function clearFormValidationMsgs(formFields){
	try{
		if(formFields!="" && formFields!=null && formFields!=undefined){
			var formFieldsArr = formFields.split("|");
			for(var i=0;i<formFieldsArr.length;++i){
				 $('#'+formFieldsArr[i]+"_ErrorDiv").html("");
				 $('#'+formFieldsArr[i]+"_ErrorDiv").hide();
			}	
		}
	}catch(e){
		alert("Error : "+e);
	}
}

function addFormValidationMsgs(validationResponse){
	try{
		if(validationResponse!="" && validationResponse!=null && validationResponse!=undefined){
			for (var i=0;i<validationResponse.length;++i){
				//alert("Field :: "+validationResponse[i].field+"\n Error :: "+validationResponse[i].msg);
				var errorDiv = validationResponse[i].field+"_ErrorDiv";
				$('#'+errorDiv).html("");
				$('#'+errorDiv).show();
				$("#"+errorDiv).html("<span class='ui-icon ui-icon-alert float_left'></span>&nbsp;"+validationResponse[i].msg);	
			}
		}
	}catch(e){
		alert("Error : "+e);
	}
}