package com.straussj.NewMusicFinder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.methods.AddTrackToPlaylistRequest;
import com.wrapper.spotify.methods.CurrentUserRequest;
import com.wrapper.spotify.methods.PlaylistCreationRequest;
import com.wrapper.spotify.models.AuthorizationCodeCredentials;
import com.wrapper.spotify.models.Playlist;
import com.wrapper.spotify.models.Track;
import com.wrapper.spotify.models.User;

/**
 * Servlet implementation class AddPlaylistProcessing
 */
public class AddPlaylistProcessing extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddPlaylistProcessing() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/* Application details necessary to get an access token */
		final String code = request.getParameter("code");
		String userID = "";
		final Api api = (Api)request.getSession().getAttribute("api");
		
		/* Make a token request. Asynchronous requests are made with the .getAsync method and synchronous requests
		 * are made with the .get method. This holds for all type of requests. */
		final SettableFuture<AuthorizationCodeCredentials> authorizationCodeCredentialsFuture = api.authorizationCodeGrant(code).build().getAsync();

		/* Add callbacks to handle success and failure */
		Futures.addCallback(authorizationCodeCredentialsFuture, new FutureCallback<AuthorizationCodeCredentials>() {
		  public void onSuccess(AuthorizationCodeCredentials authorizationCodeCredentials) {
		    /* The tokens were retrieved successfully! */
		    System.out.println("Successfully retrieved an access token! " + authorizationCodeCredentials.getAccessToken());
		    System.out.println("The access token expires in " + authorizationCodeCredentials.getExpiresIn() + " seconds");
		    System.out.println("Luckily, I can refresh it using this refresh token! " +     authorizationCodeCredentials.getRefreshToken());
		  
		    /* Set the access token and refresh token so that they are used whenever needed */
		    api.setAccessToken(authorizationCodeCredentials.getAccessToken());
		    api.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
		  }
		  
		  public void onFailure(Throwable throwable) {
		    /* Let's say that the client id is invalid, or the code has been used more than once,
		     * the request will fail. Why it fails is written in the throwable's message. */
		  }
		});
		
		final CurrentUserRequest currentuserrequest = api.getMe().build();
		try {
			final User user = currentuserrequest.get();
			userID = user.getId();
		} catch(Exception e) {
			System.out.println("didn't get userID, error code is " + e.getMessage());
		}
		
		//create new playlist for user
		final PlaylistCreationRequest PCrequest = api.createPlaylist(userID, "NewMusicFinder Recommendations").publicAccess(true).build();
		String playlistID = "";
		String playlistURL = "";
		try {
			final Playlist playlistCreated = PCrequest.get();
			playlistURL = playlistCreated.getExternalUrls().get("spotify");
			playlistID = playlistCreated.getId();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		//getplay
		
		//add recommended songs to Spotify playlist
		List<Track> recTracks = (List<Track>)request.getSession().getAttribute("recTracks");
		
		List<String> recTrackURIs = new ArrayList();
		for(Track t: recTracks) {
			recTrackURIs.add(t.getUri());
		}
	
		final AddTrackToPlaylistRequest aRequest = api.addTracksToPlaylist(userID, playlistID, recTrackURIs).build();
		try {
			aRequest.get();
		} catch(Exception e) {
			response.sendRedirect("Error.jsp");
		}
		
		//reset some attributes for correct home page
		
		request.getSession().setAttribute("exported", true);
		request.getSession().setAttribute("spotifyPlaylistURL", playlistURL);
		response.sendRedirect("index.jsp");
	}

}
