<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Recommendation return</title>
</head>
<body>
	<jsp:include page="Header.jsp" />
	
	<h2 style="margin-left:10px">Recommended Songs</h2>
	<div class="container">
		<div class="row">
			<div class="col-md-2"><h3>Song name</h3></div>
			<div class="col-md-2"><h3>Artist name</h3></div>
			<div class="col-md-3"><h3>Song preview</h3></div>
		</div>

		<c:forEach items="${recTracks}" var="track">
			<div class="row">
				<div class="col-md-2"><p class="lead">${track.name}</p></div>
				<div class="col-md-2"><p class="lead">${track.artists[0].name}</p></div>
				<div class="col-md-2">
					<c:set var="thisSongURL" value="${track.previewUrl}"/>
					<audio src="${thisSongURL}" preload controls controlsList="nodownload" 
					style="width: 145px;" ></audio>
				</div>
				<div class="col-md-2">
					<c:set var="spotifyURL" value="${track.externalUrls.get('spotify')}"/>
					<a href="${spotifyURL}" target="_blank" class="btn btn-primary">Open in Spotify</a>
				</div>
				
			</div>
		</c:forEach>
	</div>


</body>
</html>