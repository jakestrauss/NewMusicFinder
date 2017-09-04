package com.straussj.NewMusicFinder;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.methods.TracksRequest;
import com.wrapper.spotify.methods.authentication.ClientCredentialsGrantRequest;
import com.wrapper.spotify.models.ClientCredentials;
import com.wrapper.spotify.models.Track;


/**
 * Servlet implementation class TakeUserParams
 */
public class TakeUserParams extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// save authorization token for when making recommendation request
	private static String accessToken = "";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TakeUserParams() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// first get authorization details worked out

		// configure api
		final String clientId = "205f9b7102884e5f8fb117c513ded8de";
		final String clientSecret = "490347c94dd04cc8b151fa1b0d097b2b";
		final Api api = Api.builder().clientId(clientId).clientSecret(clientSecret).build();
		request.getSession().setAttribute("publicAPI", api);

		final ClientCredentialsGrantRequest clientRequest = api.clientCredentialsGrant().build();
		final SettableFuture<ClientCredentials> responseFuture = clientRequest.getAsync();

		/* Add callbacks to handle success and failure */
		Futures.addCallback(responseFuture, new FutureCallback<ClientCredentials>() {
			public void onSuccess(ClientCredentials clientCredentials) {
				/* The tokens were retrieved successfully! */
				System.out.println("Successfully retrieved an access token! " + clientCredentials.getAccessToken());
				System.out.println("The access token expires in " + clientCredentials.getExpiresIn() + " seconds");

				/*
				 * Set access token on the Api object so that it's used going forward
				 */
				api.setAccessToken(clientCredentials.getAccessToken());
				accessToken = clientCredentials.getAccessToken();

				/*
				 * Please note that this flow does not return a refresh token. That's only for
				 * the Authorization code flow
				 */
			}

			public void onFailure(Throwable throwable) {
				/*
				 * An error occurred while getting the access token. This is probably caused by
				 * the client id or client secret is invalid.
				 */
			}
		});

		request.getSession().removeAttribute("exported");
		
		
		// retrieve information from jsp page
		double dance = Double.parseDouble(request.getParameter("dance"));
		double energy = Double.parseDouble(request.getParameter("energy"));
		boolean instrumental = request.getParameter("instrumental").equals("Yes") ? true : false;
		double loud = Double.parseDouble(request.getParameter("loud"));
		loud = loud/1.66666666 - 60;
		boolean live = request.getParameter("live").equals("Yes") ? true : false;
		boolean acoustic = request.getParameter("acoustic").equals("Yes") ? true : false;
		String[] genres = request.getParameterValues("genres");

		// convert genres into comma separated list of genres
		String genreString = "";
		for (int i = 0; i < genres.length; i++) {
			genreString += genres[i];
			if (i != genres.length - 1) {
				genreString += ",";
			}
		}

		// create url for GET request
		String url = "https://api.spotify.com/v1/recommendations?";
		if (genres.length != 0) {
			url += "seed_genres=";
			url += genreString;
		}
		url += "&";
		url += "target_danceability=" + dance / 100 + "&";
		url += "target_energy=" + energy / 100 + "&";
		if (instrumental) {
			url += "min_instrumentalness=.8&";
		} else {
			url += "max_instrumentalness=.45&";
		}
		url += "target_loud=" + loud + "&";
		if (acoustic) {
			url += "min_acousticness=.8&";
		} else {
			url += "max_acousticness=.45&";
		}
		if (live) {
			url += "min_liveness=.8&";
		} else {
			url += "max_liveness=.45&";
		}
		url += "market=US";

		String rawRecJSONString = "";
		try {
			rawRecJSONString = getHTML(url);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		
		FileUtils.writeStringToFile(new File("log.txt"), rawRecJSONString, Charset.defaultCharset());
		JSONObject obj = new JSONObject(rawRecJSONString);

		List<String> listIds = new ArrayList<String>();
		JSONArray array = obj.getJSONArray("tracks");
		for(int i = 0 ; i < array.length() ; i++){
		    listIds.add(array.getJSONObject(i).getString("id"));
		}

		
		//get Track objects from ids using api wrapper
		List<Track> recTracks = new ArrayList<Track>();
		TracksRequest tRequest = api.getTracks(listIds).build();
		try {
			recTracks = tRequest.get();
		} catch(Exception e) {
			System.out.println("Was unable to get recommended tracks from ID");
		}
		
		List<Track> recTracksWithPreview = new ArrayList<Track>();
		for(Track t : recTracks) {
			if(!(t.getPreviewUrl().equals("null"))) {
				recTracksWithPreview.add(t);
			}
		}
		request.getSession().setAttribute("recTracks", recTracksWithPreview);
		response.sendRedirect("ReturnRec.jsp");

	}

	// helper method to send GET html request for song recommendations
	public static String getHTML(String urlToRead) throws IOException {
		StringBuilder result = new StringBuilder();
		URL url = new URL(urlToRead);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		System.out.println(accessToken);
		conn.setRequestProperty("Authorization", "Bearer " + accessToken);
		conn.setRequestMethod("GET");
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		rd.close();
		return result.toString();
	}

}
