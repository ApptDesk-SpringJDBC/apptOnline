<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!doctype html>
<html lang="us">
<head>
<meta charset="utf-8">
<title>Online Appointment system</title>

<link href="${sessionData.cssFileName}?version=1.0" rel="stylesheet"/>
<link href="static/css/jquery-ui.css" rel="stylesheet"/>
<link href='https://fonts.googleapis.com/css?family=Roboto' rel='stylesheet' type='text/css'>

<link href="static/css/alertify/alertify.core.css" rel="stylesheet"/>
<link href="static/css/alertify/alertify.default.css" rel="stylesheet"/>

<script src="static/js/jquery-1.11.1.min.js" type="text/javascript"></script>
<script src="static/js/apptonline.js?version=1.1" type="text/javascript"></script> 
<script src="static/js/jquery.js"></script> 
<script src="static/js/jquery-ui.js"></script> 

<script src="static/js/alertify/alertify.min.js"></script>

<script src="static/js/idleTimeout/store.min.js" type="text/javascript"></script>
<script src="static/js/idleTimeout/jquery-idleTimeout.min.js?version=1.0" type="text/javascript"></script>

 <script type="text/javascript" charset="utf-8">
    $(document).ready(function (){
      $(document).idleTimeout({
		  redirectUrl: 'logout.html',      // redirect to this url on logout. Set to "redirectUrl: false" to disable redirect

		  // idle settings
		  idleTimeLimit: 300,           // 'No activity' time limit in seconds. 1200 = 20 Minutes
		  idleCheckHeartbeat: 5,       // Frequency to check for idle timeouts in seconds

		  // optional custom callback to perform before logout
		   customCallback: false,       // set to false for no customCallback
		   //customCallback:    function () {    // define optional custom js function
		   //  alert("perform custom action before logout");
		   //},

		  // configure which activity events to detect
		  // http://www.quirksmode.org/dom/events/
		  // https://developer.mozilla.org/en-US/docs/Web/Reference/Events
		  activityEvents: 'click keypress scroll wheel mousewheel mousemove', // separate each event with a space

		  // warning dialog box configuration
		  enableDialog: true,           // set to false for logout without warning dialog
		  dialogDisplayLimit: 20,       // 20 seconds for testing. Time to display the warning dialog before logout (and optional callback) in seconds. 180 = 3 Minutes
		  dialogTitle: 'Session Expiration Warning', // also displays on browser title bar
		  dialogText: 'Because you have been inactive, your session is about to expire.',
		  dialogTimeRemaining: 'Time remaining',
		  dialogStayLoggedInButton: 'Stay Logged In',
		  dialogLogOutNowButton: 'Log Out Now',

		  // error message if https://github.com/marcuswestin/store.js not enabled
		  errorAlertMessage: 'Please disable "Private Mode", or upgrade to a modern browser.',

		  // server-side session keep-alive timer
		  sessionKeepAliveTimer: 840,   // ping the server at this interval in seconds. 600 = 10 Minutes. Set to false to disable pings
		  sessionKeepAliveUrl: "activateSession.html" // set URL to ping - does not apply if sessionKeepAliveTimer: false
	   });
    });
  </script>

</head>

<body>

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
<div class="float_right">
      <input type="button" value="Logout" class="float_right logout" onclick="onLogoutBtnClick()">
<div id="google_translate_element" class="float_right" style="padding-right:10px"></div><script type="text/javascript">
function googleTranslateElementInit() {
  new google.translate.TranslateElement({pageLanguage: 'en', includedLanguages: 'es,ru,so,vi,zh-CN,zh-TW', layout: google.translate.TranslateElement.InlineLayout.SIMPLE, autoDisplay: false}, 'google_translate_element');
}
</script><script type="text/javascript" src="//translate.google.com/translate_a/element.js?cb=googleTranslateElementInit"></script>
</div>&nbsp;&nbsp;
	  <br/>
    </div>
    <!-- header ends --> 
    
    <!-- Accordion starts -->
    <div id="leftCont">
      <div id="accordion"> 
		<div class="errorMsg ui-state-error" id="appt_online_error_msg" style="display:none;"></div>
        <c:forEach var="onlinePageContent" items="${onlinePageData.onlinePageContentMap[sessionData.page_method_type]}" varStatus="loop">
			<h3 class="${loop.index==0 ? 'ui-header ui-state-active' : 'ui-header ui-state-default'}">
				<span  class="${(sessionData.page_method_type ne 'appt_scheduler_closed' && sessionData.page_method_type ne 'appt_no_funding') ? (loop.index==0 ? 'ui-accordion-header-icon ui-icon-triangle-1-s' : 'ui-accordion-header-icon ui-icon-triangle-1-e') : ('ui-icon-triangle')}"></span>
				${onlinePageContent.header}
			</h3>
			${onlinePageContent.leftSideContent}
			${onlinePageContent.script}
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