<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Home</title>
</head>
<body>
	<jsp:include page="Header.jsp"/>
	<div class="container">
		<h2>Welcome to NewMusicFinder! Fill in the information below 
		to find NEW music you might like!</h2>
	</div>
	<div class="container">
		<div class="row">
			<div class="col-md-3 col-xs-3">
				<form method="post" action="TakeUserParams">
					<label for="loud">Select Loudness</label>
					<input id="loud" type="range" min="0" max="100" step="1" value="50"
						oninput="outputUpdate(value)" style="float: left"/>
					<output for="loud" id="loudValue">   50</output>
				</form>
			</div>
		</div>
		<script type="text/javascript">
			function outputUpdate(sel) {
				document.querySelector('#loudValue').value = sel;
			}
		</script>
	</div>
</body>
</html>
