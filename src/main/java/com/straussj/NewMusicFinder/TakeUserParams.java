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
import com.wrapper.spotify.methods.AlbumRequest;
import com.wrapper.spotify.methods.AudioFeatureRequest;
import com.wrapper.spotify.methods.NewReleasesRequest;
import com.wrapper.spotify.methods.authentication.ClientCredentialsGrantRequest;
import com.wrapper.spotify.models.Album;
import com.wrapper.spotify.models.AudioFeature;
import com.wrapper.spotify.models.ClientCredentials;
import com.wrapper.spotify.models.NewReleases;
import com.wrapper.spotify.models.Page;
import com.wrapper.spotify.models.SimpleAlbum;
import com.wrapper.spotify.models.SimpleTrack;

/**
 * Servlet implementation class TakeUserParams
 */
public class TakeUserParams extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TakeUserParams() {
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
		//first get authorization details worked out
		
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
				 * Set access token on the Api object so that it's used going
				 * forward
				 */
				api.setAccessToken(clientCredentials.getAccessToken());

				/*
				 * Please note that this flow does not return a refresh token.
				 * That's only for the Authorization code flow
				 */
			}

			public void onFailure(Throwable throwable) {
				/*
				 * An error occurred while getting the access token. This is
				 * probably caused by the client id or client secret is invalid.
				 */
			}
		});

		//retrieve information from jsp page
		double dance = Double.parseDouble(request.getParameter("dance"));
		double energy = Double.parseDouble(request.getParameter("energy"));
		double instrumentalness = Double.parseDouble(request.getParameter("instrumentalness"));
		double loud = Double.parseDouble(request.getParameter("loud"));
		boolean live = request.getParameter("live").equals("Yes") ? true : false;
		boolean acoustic = request.getParameter("acoustic").equals("Yes") ? true : false;
		
		//get new releases
		final NewReleasesRequest releaseRequest = api.getNewReleases().limit(5).offset(0).country("US").build();
		List<Album> albums = new ArrayList<Album>();
		try {
			NewReleases newReleases = releaseRequest.get();
			Page<SimpleAlbum> simpleAlbumPage = newReleases.getAlbums();
			List<SimpleAlbum> simpleAlbumList = simpleAlbumPage.getItems();
			for(SimpleAlbum a : simpleAlbumList) {
				AlbumRequest aRequest = api.getAlbum(a.getId()).build();
				try {
					Album album = aRequest.get();
					albums.add(album);
				} catch(Exception e) {
					System.out.println("Error in getting full albums");
				}
			}
		} catch(Exception e) {
			System.out.println("error in getting new releases");
		}
		
		//break albums into songs
		List<SimpleTrack> simpleTracks = new ArrayList<SimpleTrack>();
		for(Album a : albums) {
			simpleTracks.addAll(a.getTracks().getItems());
		}
		
		//analyze each song and find which is best according to user's specs
		//this array will match up 1:1 with simpleTrack list
		double[] similarityScores = new double[simpleTracks.size()];
		int i = -1;
		for(SimpleTrack sel : simpleTracks) {
			i++;
			final AudioFeatureRequest afRequest = api.getAudioFeature(sel.getId()).build();
			try {
				AudioFeature selAF = afRequest.get();
				if(acoustic) {
					if(selAF.getAcousticness() < .85) {
						similarityScores[i] = 10000000; //do not consider this song because not acoustic
						continue;
					}
				}
				if(live) {
					if(selAF.getLiveness() < .85) {
						similarityScores[i] = 10000000;
						continue;
					}
				}
				double eucDist = Math.sqrt(Math.pow(selAF.getDanceability()-dance,2) + 
											Math.pow(selAF.getEnergy()-energy, 2) +
											Math.pow(selAF.getInstrumentalness()-instrumentalness, 2) + 
											Math.pow(selAF.getLoudness()-loud, 2));
				similarityScores[i] = eucDist;
											
			} catch(Exception e) {
				System.out.println("error in getting audio feature object, error is " + e.getMessage());
			}
		}
		
		//get minimum of similarityScores
		double min = 100000000;
		int minInd = -1;
		for(int j = 0; j < similarityScores.length; j++) {
			if(similarityScores[j] < min) {
				min = similarityScores[j];
				minInd = j;
			}
		}
		
		//return best matched song
		SimpleTrack bestTrack = simpleTracks.get(minInd);
		request.getSession().setAttribute("bestTrack", bestTrack);
		System.out.println(bestTrack.getName() + " " + bestTrack.getArtists().get(0).getName());
		response.sendRedirect("ReturnRec.jsp");
		
	}

}
