package audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler implements com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener {
    private final AudioPlayer playerManager;
    private final BlockingQueue<AudioTrack> queue;

    public TrackScheduler(AudioPlayer manager) {
        this.playerManager = manager;
        this.queue = new LinkedBlockingQueue<>();
    }

    public void queue(AudioTrack track) {
        if (!playerManager.startTrack(track, true)) {
            queue.offer(track);
        }
    }

    public void nextTrack() {
        playerManager.startTrack(queue.poll(), false);
    }

    @Override
    public void onEvent(com.sedmelluq.discord.lavaplayer.player.event.AudioEvent event) {
        if (event instanceof com.sedmelluq.discord.lavaplayer.player.event.TrackEndEvent endEvent) {
            if (endEvent.endReason == AudioTrackEndReason.FINISHED) {
                nextTrack();
            }
        }
    }
}
