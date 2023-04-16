package com.staticvoid.spotify.service;

import com.neovisionaries.i18n.CountryCode;
import com.staticvoid.songsuggestion.domain.Song;
import com.staticvoid.songsuggestion.domain.SongMetadata;
import com.staticvoid.songsuggestion.repository.SongMetadataRepository;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
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
    private final SongMetadataRepository songMetadataRepository;

    public void init() {
        clientCredentials();
    }

    public static void clientCredentials() {
        try {
            final ClientCredentials clientCredentials = clientCredentialsRequest.execute();

            // Set access token for further "spotifyApi" object usage
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());

            log.info("Expires in: " + clientCredentials.getExpiresIn());
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            log.error("Error: " + e.getMessage());
        }
    }

    public SongMetadata getSongMetadata(Song song) {
        try {
            init();
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
            String albumArtUrl = track.getAlbum().getImages()[0].getUrl();
            return songMetadataRepository.save(new SongMetadata(song, spotifyUrl, previewUrl, albumArtUrl));
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            throw new RuntimeException("Could not get metadata", e);
        }
    }

}
