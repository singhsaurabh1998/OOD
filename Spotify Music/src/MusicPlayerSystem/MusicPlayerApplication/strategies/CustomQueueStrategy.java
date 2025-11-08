package MusicPlayerSystem.MusicPlayerApplication.strategies;

import MusicPlayerSystem.MusicPlayerApplication.models.Playlist;
import MusicPlayerSystem.MusicPlayerApplication.models.Song;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class CustomQueueStrategy implements PlayStrategy {
    private Playlist currentPlaylist;
    private int currentIndex;
    private final Queue<Song> nextQueue;
    private final Stack<Song> prevStack;

    public CustomQueueStrategy() {
        currentPlaylist = null;
        currentIndex = -1;
        nextQueue = new LinkedList<>();
        prevStack = new Stack<>();
    }

    private Song nextSequential() {
        if (currentPlaylist.getSize() == 0) {
            throw new RuntimeException("Playlist is empty.");
        }
        currentIndex = currentIndex + 1;
        return currentPlaylist.getSongs().get(currentIndex);
    }

    private Song previousSequential() {
        if (currentPlaylist.getSize() == 0) {
            throw new RuntimeException("Playlist is empty.");
        }
        currentIndex = currentIndex - 1;
        return currentPlaylist.getSongs().get(currentIndex);
    }

    @Override
    public void setPlaylist(Playlist playlist) {
        currentPlaylist = playlist;
        currentIndex = -1;
        nextQueue.clear();
        prevStack.clear();
    }

    @Override
    public boolean hasNext() {
        return ((currentIndex + 1) < currentPlaylist.getSize());
    }

    @Override
    public Song next() {
        if (currentPlaylist == null || currentPlaylist.getSize() == 0) {
            throw new RuntimeException("No playlist loaded or playlist is empty.");
        }

        if (!nextQueue.isEmpty()) {
            Song s = nextQueue.poll();
            prevStack.push(s);

            // update index to match queued song
            for (int i = 0; i < currentPlaylist.getSongs().size(); ++i) {
                if (currentPlaylist.getSongs().get(i) == s) {
                    currentIndex = i;
                    break;
                }
            }
            return s;
        }

        // Otherwise sequential
        return nextSequential();
    }

    @Override
    public boolean hasPrevious() {
        return (currentIndex - 1 > 0);
    }

    @Override
    public Song previous() {
        if (currentPlaylist == null || currentPlaylist.getSize() == 0) {
            throw new RuntimeException("No playlist loaded or playlist is empty.");
        }

        if (!prevStack.isEmpty()) {
            Song s = prevStack.pop();

            // update index to match stacked song
            for (int i = 0; i < currentPlaylist.getSongs().size(); ++i) {
                if (currentPlaylist.getSongs().get(i) == s) {
                    currentIndex = i;
                    break;
                }
            }
            return s;
        }

        // Otherwise sequential
        return previousSequential();
    }

    @Override
    public void addToNext(Song song) {
        nextQueue.add(song);
    }
}
