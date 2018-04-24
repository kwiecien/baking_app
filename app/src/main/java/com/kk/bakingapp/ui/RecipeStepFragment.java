package com.kk.bakingapp.ui;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.kk.bakingapp.R;
import com.kk.bakingapp.data.Recipe;
import com.kk.bakingapp.util.Recipes;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepFragment extends Fragment implements ExoPlayer.EventListener {

    static final String ARG_RECIPE_STEP = "recipe_step";
    static final String ARG_RECIPE_ID = "recipe_id";
    private static final String TAG = RecipeStepFragment.class.getSimpleName();
    private static final String CHANNEL_ID = "123";
    private static MediaSessionCompat mMediaSession;
    @BindView(R.id.simple_exo_player_view)
    SimpleExoPlayerView mExoPlayerView;
    @BindView(R.id.recipe_step_short_description_tv)
    TextView mShortDescriptionTextView;
    @BindView(R.id.recipe_step_description_tv)
    TextView mDescriptionTextView;
    @BindView(R.id.next_step_bt)
    ImageButton mNextStepButton;
    @BindView(R.id.previous_step_bt)
    ImageButton mPreviousStepButton;
    private SimpleExoPlayer mExoPlayer;
    private PlaybackStateCompat.Builder mPlaybackStateBuilder;
    private Recipe.Step mStep;
    private List<Recipe.Step> mSteps;
    private NotificationManager mNotificationManager;

    public RecipeStepFragment() {
        /*
         * Mandatory empty constructor for the fragment manager to instantiate the
         * fragment (e.g. upon screen orientation changes).
         */
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_RECIPE_STEP)) {
            mStep = Parcels.unwrap(getArguments().getParcelable(ARG_RECIPE_STEP));
            long recipeId = getArguments().getLong(ARG_RECIPE_ID);
            mSteps = Recipes.getRecipes().get((int) recipeId - 1).getSteps();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_step, container, false);
        ButterKnife.bind(this, rootView);
        setupViews(mStep);
        setupExoPlayer(mStep);
        setupListeners();
        return rootView;
    }

    private void setupViews(Recipe.Step step) {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(step.getShortDescription());
        }
        mDescriptionTextView.setText(step.getDescription());
        mShortDescriptionTextView.setText(step.getShortDescription());
        mPreviousStepButton.setVisibility(View.VISIBLE);
        mNextStepButton.setVisibility(View.VISIBLE);
        if (step.getId() == 0) {
            mPreviousStepButton.setVisibility(View.INVISIBLE);
        }
        if (step.getId() == mSteps.size() - 1) {
            mNextStepButton.setVisibility(View.INVISIBLE);
        }
    }

    private void setupListeners() {
        mNextStepButton.setOnClickListener(view -> {
            mStep = mSteps.get((int) mStep.getId() + 1);
            refreshUi(mStep);
        });
        mPreviousStepButton.setOnClickListener(view -> {
            mStep = mSteps.get((int) mStep.getId() - 1);
            refreshUi(mStep);
        });
    }

    private void refreshUi(Recipe.Step step) {
        setupViews(step);
        releaseExoPlayer();
        setupExoPlayer(step);
    }

    private void setupExoPlayer(Recipe.Step step) {
        mExoPlayerView.setDefaultArtwork(BitmapFactory.decodeResource((getResources()), R.drawable.question_mark));
        initializeMediaSession();
        initializeExoPlayer(Uri.parse(step.getVideoUrl()));
    }

    private void initializeMediaSession() {
        mMediaSession = new MediaSessionCompat(getActivity(), TAG);
        mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS |
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS);
        mMediaSession.setMediaButtonReceiver(null);
        mPlaybackStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                        PlaybackStateCompat.ACTION_REWIND |
                        PlaybackStateCompat.ACTION_PLAY |
                        PlaybackStateCompat.ACTION_PLAY_PAUSE |
                        PlaybackStateCompat.ACTION_PAUSE |
                        PlaybackStateCompat.ACTION_STOP |
                        PlaybackStateCompat.ACTION_FAST_FORWARD |
                        PlaybackStateCompat.ACTION_SEEK_TO);
        mMediaSession.setPlaybackState(mPlaybackStateBuilder.build());
        mMediaSession.setCallback(new MediaSessionCallback());
        mMediaSession.setActive(true);
    }

    private void initializeExoPlayer(@NonNull Uri mediaUri) {
        if (mExoPlayer == null) {
            DefaultTrackSelector trackSelector = new DefaultTrackSelector();
            DefaultLoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            mExoPlayerView.setPlayer(mExoPlayer);
            mExoPlayer.addListener(this);
            String userAgent = Util.getUserAgent(getActivity(), "BakingApp");
            DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(getActivity(), userAgent);
            if (!mediaUri.toString().isEmpty()) {
                ExtractorMediaSource extractorMediaSource = new ExtractorMediaSource(mediaUri, dataSourceFactory,
                        new DefaultExtractorsFactory(), null, null); // TODO Handle wrong uri
                mExoPlayer.prepare(extractorMediaSource);
            }
            mExoPlayer.setPlayWhenReady(true);
            mExoPlayerView.hideController();
        }
    }

    private void releaseExoPlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseExoPlayer();
        mMediaSession.setActive(false);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
        // Method not supported
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        // Method not supported
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        // Method not supported
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == ExoPlayer.STATE_READY && playWhenReady) {
            mPlaybackStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, mExoPlayer.getCurrentPosition(), 1.0f);
        } else if (playbackState == ExoPlayer.STATE_READY) {
            mPlaybackStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, mExoPlayer.getCurrentPosition(), 0.0f);
        }
        mMediaSession.setPlaybackState(mPlaybackStateBuilder.build());
        showNotification(mPlaybackStateBuilder.build());
    }

    private void showNotification(PlaybackStateCompat playbackState) {
        int icon = determinePlayPauseNotificationIcon(playbackState);
        String playPause = determinePlayPauseNotificationTitle(playbackState);
        NotificationCompat.Action playPauseAction =
                createAction(icon, playPause, PlaybackStateCompat.ACTION_PLAY_PAUSE);
        NotificationCompat.Action restartAction =
                createAction(R.drawable.exo_controls_previous, "Restart", PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS);
        PendingIntent contentPendingIntent = createContentPendingIntent();
        mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel(mNotificationManager);
        Notification notification = createNotification(playPauseAction, restartAction, contentPendingIntent);
        mNotificationManager.notify(0, notification);
    }

    int determinePlayPauseNotificationIcon(PlaybackStateCompat playbackState) {
        return playbackState.getState() == PlaybackStateCompat.STATE_PLAYING ?
                R.drawable.exo_controls_pause :
                R.drawable.exo_controls_play;
    }

    @NonNull
    String determinePlayPauseNotificationTitle(PlaybackStateCompat playbackState) {
        return playbackState.getState() == PlaybackStateCompat.STATE_PLAYING ?
                getString(R.string.pause) :
                getString(R.string.play);
    }

    @NonNull
    private NotificationCompat.Action createAction(int icon, String title, long action) {
        return new NotificationCompat.Action(
                icon,
                title,
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                        getActivity(),
                        action
                ));
    }

    private PendingIntent createContentPendingIntent() {
        return PendingIntent.getActivity(getActivity(),
                0,
                new Intent(getActivity(), RecipeStepActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private Notification createNotification(NotificationCompat.Action playPauseAction,
                                            NotificationCompat.Action restartAction,
                                            PendingIntent contentPendingIntent) {
        return new NotificationCompat.Builder(getActivity(), CHANNEL_ID)
                .setContentTitle("Can you bake it?")
                .setContentText("Press play to watch the instructions!")
                .setContentIntent(contentPendingIntent)
                .setSmallIcon(android.R.drawable.gallery_thumb)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                //.setAutoCancel(true)
                .setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mMediaSession.getSessionToken())
                        .setShowActionsInCompactView(0, 1))
                .addAction(playPauseAction)
                .addAction(restartAction)
                .build();
    }

    private void createNotificationChannel(NotificationManager notificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel =
                    new NotificationChannel(CHANNEL_ID, getString(R.string.baking_channel_name), importance);
            notificationChannel.setDescription(getString(R.string.baking_channel_description));
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        // Method not supported
    }

    @Override
    public void onPositionDiscontinuity() {
        // Method not supported
    }

    /**
     * Broadcast Receiver registered to receive the MEDIA_BUTTON intent coming from clients.
     */
    public static class MediaReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mMediaSession, intent);
        }
    }

    /**
     * Media Session Callbacks, where all external clients (e.g. Notifications) control the player.
     */
    private class MediaSessionCallback extends MediaSessionCompat.Callback {

        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }

    }

}
