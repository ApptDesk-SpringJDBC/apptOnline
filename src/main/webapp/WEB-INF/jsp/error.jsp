<!doctype html>
<html lang="us">
<head>
<meta charset="utf-8">
<title>Online Appointment system</title>

<link href="static/css/global.css" rel="stylesheet"/>
<link href='https://fonts.googleapis.com/css?family=Roboto' rel='stylesheet' type='text/css'>
</head>

<body>
<!-- Header starts -->
<header id="branding">
  <div class="header_container">
    <!-- <div class="logo"><img src="${sessionData.logoFileName}"></div> -->
  </div>
</header>
<!-- Header ends --> 

<!-- Page starts -->
<div id="page">
  <div class="page_container"> 
   <div style="padding-bottom:10px">
<div id="google_translate_element" class="float_right" style="padding-right:10px"></div><script type="text/javascript">
function googleTranslateElementInit() {
  new google.translate.TranslateElement({pageLanguage: 'en', includedLanguages: 'es,ru,so,vi,zh-CN,zh-TW', layout: google.translate.TranslateElement.InlineLayout.SIMPLE, autoDisplay: false}, 'google_translate_element');
}
</script><script type="text/javascript" src="//translate.google.com/translate_a/element.js?cb=googleTranslateElementInit"></script>
</div>      
    <!-- Accordion starts -->
	<div id="leftCont">
	  <div id="accordion"> 
			<h3 class="ui-header ui-state-active">
				<span  class="ui-icon-triangle"></span>
				Error
			</h3>
			<div class="ui-content">
				<div class="msgDisplay">
					Your appointment scheduling is unexpectedly interrupted and appointment is NOT booked. Please try again. If you get this error again, please email to support@itfrontdesk.com with your name, email address and phone number. Also provide the scheduler URL (copy from address bar) and how you got this error. Thank you
				</div>
			</div>
	  </div>
	</div>
	<!-- Accordion ends --> 

	<!-- Info Message starts 
      <div id="righCont"> 
        Error!!!!!!       
     </div>-->
    <!-- Info Message ends -->

    <div class="clearAll"></div>
  </div>
</div>
<!-- Page ends --> 

<!-- Footer starts -->
<div id="footer">
  <div class="float_left"> &copy;2014. IT Front Desk. All Rights Reserved <br />
    This online service uses <a href="#">cookies</a> <br />
    Version 4.2.1 </div>
  <div class="float_right itfrontdesk"> Powered By </div>
  <div class="clearAll"></div>
</div>
<!-- Footer ends --> 
</body>
</html>
