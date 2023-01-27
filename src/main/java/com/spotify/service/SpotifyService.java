package com.spotify.service;

import com.neovisionaries.i18n.CountryCode;
import com.staticvoid.songsuggestion.domain.Song;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.model_objects.specification.Paging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SpotifyService {

    private final SpotifyApi spotifyApi;
    //TODO move these to config/secret manager
    private static final String CLIENT_ID = "";
    private static final String CLIENT_SECRET = "";

    public SpotifyService() {
        spotifyApi = new SpotifyApi.Builder()
                .setClientId(CLIENT_ID)
                .setClientSecret(CLIENT_SECRET)
                .build();
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

            String href = trackPaging.getHref();
            song.setYoutubeVideoId(href);
            return href;
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            throw new RuntimeException("Could not get track listings", e);
        }
    }

}
