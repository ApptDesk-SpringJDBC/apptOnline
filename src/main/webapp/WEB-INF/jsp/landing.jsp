<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<!doctype html>
<html lang="us">
<head>
<meta charset="utf-8">
<title>Online Appointment system</title>

<link href="${landingPageInfo.cssFileName}" rel="stylesheet"/>
<link href='https://fonts.googleapis.com/css?family=Roboto' rel='stylesheet' type='text/css'>

<script src="static/js/jquery.js"></script>

<script> 
    $(document).ready(function () {
		$("#newAppointment").click(function(){
			window.location="appt.html";
		});
		$("#existingAppts").click(function(){
			window.location="cancel.html";
		});
		$("#viewPledgeAmount").click(function(){
			window.location="pledge_amount.html";
		});
		$("#uploadTranscripts").click(function(){
			window.location="upload_transcripts.html";
		});
    });  
</script>
</head>

<body>
<!-- Header starts -->
<header id="branding">
  <div class="header_container">
    <div class="logo"><img src="${landingPageInfo.logoFileName}"></div>
    <!-- <div class="header_baseline">Some text goes here</div> -->
	
  </div>
</header>
<!-- Header ends --> 

<!-- Page starts -->
	${landingPageInfo.landingPageText}
<!-- Page ends -->

<!-- Footer starts -->
<div id="footer">
  <div class="float_left">
     ${landingPageInfo.footerContent}
  </div>
  <div class="float_right">
	 ${landingPageInfo.footerLinks}
  </div>
  <div class="clearAll"></div>  
</div>
<!-- Footer ends -->
</body>
</html>
