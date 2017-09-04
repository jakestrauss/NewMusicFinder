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
	<c:if test="${not empty exported}">
    		<% String playlistURL = (String)request.getSession().getAttribute("spotifyPlaylistURL"); %>
    		<script>
	    		function OpenInNewTab(url) {
	    		  var win = window.open(url, '_blank');
	    		  win.focus();
	    		}
    		
    			OpenInNewTab('<%=playlistURL%>');
    		</script>
    		
		<div class="container alert alert-warning left" style="margin-bottom: -5px; margin-top:5px;">Recommended songs successfully exported to your Spotify account!
		Feel free to adjust your preferences and find more music if you wish!</div>
	</c:if>
	
	<div class="container">
		<h2>Welcome to NewMusicFinder! Please specify your preferences for the options
		below to find new music!</h2>
	</div>
	<div class="container">
		<form method="post" action="TakeUserParams">
			<div class="row">
				<div class="col-md-6 col-xs-6" style="margin-bottom:15px">
					<h3>Pick up to 5 genres you like</h3>
					<div class="row">
						<div class = "col-sm-3">
							<label class="checkbox-inline">
								<input class="custom-required" type="checkbox" 
								value="alternative" name="genres" required>Alternative
							</label>
						</div>
						<div class = "col-sm-3">
							<label class="checkbox-inline">
								<input class="custom-required" type="checkbox" 
								value="blues" name="genres" required>Blues
							</label>
						</div>
						<div class = "col-sm-3">
							<label class="checkbox-inline">
								<input class="custom-required" type="checkbox" 
								value="classical" name="genres" required>Classical
							</label>
						</div>
						<div class = "col-sm-3">
							<label class="checkbox-inline">
								<input class="custom-required" type="checkbox" 
								value="country" name="genres" required>Country
							</label>
						</div>
					</div>
					<div class="row">
						<div class = "col-sm-3">
							<label class="checkbox-inline">
								<input class="custom-required" type="checkbox" 
								value="electronic" name="genres" required>Electronic
							</label>
						</div>
						<div class = "col-sm-3">
							<label class="checkbox-inline">
								<input class="custom-required" type="checkbox" 
								value="folk" name="genres" required>Folk
							</label>
						</div>
						<div class = "col-sm-3">
							<label class="checkbox-inline">
								<input class="custom-required" type="checkbox" 
								value="hard-rock" name="genres" required>Hard Rock
							</label>
						</div>
						<div class = "col-sm-3">
							<label class="checkbox-inline">
								<input class="custom-required" type="checkbox" 
								value="hip-hop" name="genres" required>Hip Hop
							</label>
						</div>
					</div>
					<div class="row">
						<div class = "col-sm-3">
							<label class="checkbox-inline">
								<input class="custom-required" type="checkbox" 
								value="jazz" name="genres" required>Jazz
							</label>
						</div>
						<div class = "col-sm-3">
							<label class="checkbox-inline">
								<input class="custom-required" type="checkbox" 
								value="pop" name="genres" required>Pop
							</label>
						</div>
						<div class = "col-sm-3">
							<label class="checkbox-inline">
								<input class="custom-required" type="checkbox" 
								value="reggae" name="genres" required>Reggae
							</label>
						</div>
						<div class = "col-sm-3">
							<label class="checkbox-inline">
								<input class="custom-required" type="checkbox" 
								value="rock" name="genres" required>Rock
							</label>
						</div>
					</div>
				</div>
			</div>
			
			<div class="row">
				<div class="col-md-6 col-xs-6">
					<label for="dance">Danceability (higher means more danceable)</label>
					<input id="dance" name="dance" type="range" min="0" max="100" step="1" value="50"
						oninput="outputUpdateDance(value)" style="float: left"/>
					<output for="dance" id="danceVal">50</output>
				</div>
			</div>
			<script type="text/javascript">
			function outputUpdateDance(sel) {
				document.querySelector('#danceVal').value = sel;
			}
			</script>
			
			
			<div class="row">
				<div class="col-md-6 col-xs-6">
					<label for="loud">Loudness</label>
					<input id="loud" name="loud" type="range" min="0" max="100" step="1" value="50"
						oninput="outputUpdateLoud(value)" style="float: left"/>
					<output for="loud" id="loudVal">50</output>
				</div>
			</div>
			<script type="text/javascript">
			function outputUpdateLoud(sel) {
				document.querySelector('#loudVal').value = sel;
			}
			</script>
			
			
			<div class="row">
				<div class="col-md-6 col-xs-6">
					<label for="energy">Energy level (100 is highest energy)</label>
					<input id="energy" name="energy" type="range" min="0" max="100" step="1" value="50"
						oninput="outputUpdateEnergy(value)" style="float: left"/>
					<output for="energy" id="energyVal">50</output>
				</div>
			</div>
			<script type="text/javascript">
			function outputUpdateEnergy(sel) {
				document.querySelector('#energyVal').value = sel;
			}
			</script>
			
			<div class="row" style="margin-bottom:15px">
				<div class="col-md-6 col-xs-6">
					Instrumental (no lyrics)?<br/>
					<label class="radio-inline"><input type="radio" name="instrumental" value="Yes" required>
					Yes</label>
					<label class="radio-inline"><input type="radio" name="instrumental" value="No">
					No</label>
				</div>
			</div>
			
			<div class="row" style="margin-bottom:15px">
				<div class="col-md-6 col-xs-6">
					Live?<br/>
					<label class="radio-inline"><input type="radio" name="live" value="Yes" required>
					Yes</label>
					<label class="radio-inline"><input type="radio" name="live" value="No">
					No</label>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6 col-xs-6">
					Acoustic?<br/>
					<label class="radio-inline"><input type="radio" name="acoustic" value="Yes" required>
					Yes</label>
					<label class="radio-inline"><input type="radio" name="acoustic" value="No" >
					No</label>
				</div>
			</div>
			
			<div class="row">
				<div class="col-md-2">
					<button class="btn btn-success" id="checkBtn" type="submit" style="margin-top:10px">Find music</button>
				</div>
			</div>
		</form>
	</div>
	<script type="text/javascript" src="js/index_management.js"></script>
</body>
</html>
