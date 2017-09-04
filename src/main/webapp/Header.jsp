<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet">
<link rel = "stylesheet" type = "text/css" href = "styles/main.css" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Header</title>
</head>
<body>
	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
   	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <c:if test="${exported == true}">
    		<% String playlistURL = (String)request.getSession().getAttribute("spotifyPlaylistURL"); %>
    		<div onload="OpenInNewTab('<%=playlistURL%>')"></div>
    		
		<div class="container alert alert-warning left" style="margin-bottom: -5px; margin-top:5px;">Recommended songs successfully exported to your Spotify account!
		Feel free to adjust your preferences and find more music if you wish!</div>
	</c:if>
	<script>
		function OpenInNewTab(url) {
		  var win = window.open(url, '_blank');
		  win.focus();
		}
	</script>
</body>
</html>