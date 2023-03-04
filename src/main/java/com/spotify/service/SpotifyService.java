package com.spotify.service;

import com.neovisionaries.i18n.CountryCode;
import com.staticvoid.songsuggestion.domain.Song;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.model_objects.specification.Paging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class SpotifyService {

    //TODO move these to config/secret manager
    private static final String CLIENT_ID = "";
    private static final String CLIENT_SECRET = "";
    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(CLIENT_ID)
            .setClientSecret(CLIENT_SECRET)
            .build();
    private static final ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials()
            .build();

    public void init() {
        clientCredentials();
    }

    public static void clientCredentials() {
        try {
            final ClientCredentials clientCredentials = clientCredentialsRequest.execute();

            // Set access token for further "spotifyApi" object usage
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());

            System.out.println("Expires in: " + clientCredentials.getExpiresIn());
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public List<String> getTracks(Song[] songs) {
        List<String> result = new ArrayList<>();
        for (Song song : songs) {
            result.add(getTrack(song));
        }
        return result;
    }

    public String getTrack(Song song) {
        try {
            String query = song.getName() + " " + song.getArtist();
            SearchTracksRequest searchTracksRequest = spotifyApi.searchTracks(query)
                    .market(CountryCode.AU)
                    .limit(10)
                    .offset(0)
                    .includeExternal("audio")
                    .build();
            Paging<Track> trackPaging = searchTracksRequest.execute();

            Track track = Objects.requireNonNull(Arrays.stream(trackPaging.getItems()).filter(t -> t.getPreviewUrl() != null).findFirst().orElse(null));
            String previewUrl = track.getPreviewUrl();
            String spotifyUrl = track.getExternalUrls().getExternalUrls().get("spotify");
            song.setPreviewUrl(previewUrl);
            song.setSpotifyUrl(spotifyUrl);
            return previewUrl;
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            throw new RuntimeException("Could not get track listings", e);
        }
    }

}
