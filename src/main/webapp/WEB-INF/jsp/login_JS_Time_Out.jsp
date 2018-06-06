<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!doctype html>
<html lang="us">
<head>
<meta charset="utf-8">
<title>Online Appointment system</title>

<link href="${sessionData.cssFileName}" rel="stylesheet"/>
<link href="static/css/jquery-ui.css" rel="stylesheet"/>
<link href='http://fonts.googleapis.com/css?family=Roboto' rel='stylesheet' type='text/css'>

<link href="static/css/alertify/alertify.core.css" rel="stylesheet"/>
<link href="static/css/alertify/alertify.default.css" rel="stylesheet"/>

<script src="static/js/jquery-1.11.1.min.js" type="text/javascript"></script>
<script src="static/js/onlineappt.js?version=1.3" type="text/javascript"></script>  
<script src="static/js/jquery.js"></script> 
<script src="static/js/jquery-ui.js"></script> 

<script src="static/js/alertify/alertify.min.js"></script>

<script>
	// Set timeout variables.
	var timoutNow = 1*60*1000; // Timeout in 15 mins.
	var logoutUrl = 'landing.html?client_code=DEMODOC2'; // URL to logout page.

	var timeoutTimer;

	function StartTimers() {
		timeoutTimer = setTimeout("IdleTimeout()", timoutNow);
	}

	// Reset timers.
	function ResetTimers() {
		clearTimeout(timeoutTimer);
		StartTimers();
	}

	// Logout the user.
	function IdleTimeout() {
		//window.location = logoutUrl;
	}

</script>

</head>

<body onload="StartTimers();" onmousemove="ResetTimers();"  onkeyup="ResetTimers();" onkeypress="ResetTimers();" onkeydown="ResetTimers();">

<!-- Header starts -->
<header id="branding">
  <div class="header_container">
    <div class="logo"><img src="${sessionData.logoFileName}"></div>
    <!-- <div class="header_baseline">Some text goes here</div> -->
  </div>
</header>
<!-- Header ends --> 

<!-- Page starts -->
<div id="page">
  <div class="page_container"> 
    <!-- header starts -->
    <div class="page_header">
      <!--<h1>Welcome, <span id="FirstLastName">John</span></h1> -->
      <input type="button" value="Logout" class="float_right logout" onclick="onLogoutBtnClick()">
	  <br/>
    </div>
    <!-- header ends --> 
    
    <!-- Appointment Confirmation message starts --> 
    <!--<div id="confirmation">Congratulations! Your appointment is confirmed and appointment details are as follows: </div>--> 
    <!-- Appointment Confirmation message ends --> 
    
    <!-- Accordion starts -->
    <div id="leftCont">
      <div id="accordion"> 
        <c:forEach var="onlinePageContent" items="${onlinePageData.onlinePageContentMap[sessionData.page_method_type]}" varStatus="loop">
			<h3 class="${loop.index==0 ? 'ui-header ui-state-active' : 'ui-header ui-state-default'}">
				<span  class="${loop.index==0 ? 'ui-accordion-header-icon ui-icon-triangle-1-s' : 'ui-accordion-header-icon ui-icon-triangle-1-e'}"></span>${onlinePageContent.header}
			</h3>
			${onlinePageContent.leftSideContent}

		</c:forEach>

      </div>
    </div>
    <!-- Accordion ends --> 
    
    <!-- Info Message starts -->
      <div id="righCont"> 
        <c:forEach var="onlinePageContent" items="${onlinePageData.onlinePageContentMap[sessionData.page_method_type]}" varStatus="loop"> 
			${onlinePageContent.rightSideContent}
		</c:forEach>          
     </div>
    <!-- Info Message ends -->
    
    <div class="clearAll"></div>
  </div>
</div>
<!-- Page ends --> 

<!-- Footer starts -->
<div id="footer">
  <div class="float_left">
     ${sessionData.footerContent}
  </div>
  <div class="float_right">
	 ${sessionData.footerLinks}
  </div>
  <div class="clearAll"></div>  
</div>
<!-- Footer ends -->

</body>
</html>

