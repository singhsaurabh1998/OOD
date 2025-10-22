# Complete Design Flow of the Music Player System

This is a **music player application** built using several design patterns. Here's the complete flow:

## 1. **Application Entry Point**

`MusicPlayerApplication` (Singleton) acts as the main interface. It manages:
- **Song library** - stores all available songs
- Creating playlists
- Delegating playback operations to `MusicPlayerFacade`

## 2. **Core Components**

### **MusicPlayerFacade** (Facade Pattern)
Simplifies interaction with multiple subsystems:
- Manages `AudioEngine` (playback control)
- Coordinates with managers: `DeviceManager`, `PlaylistManager`, `StrategyManager`
- Provides unified methods: `playSong()`, `pauseSong()`, `playNextTrack()`, etc.

### **AudioEngine**
Handles actual playback state:
- Tracks current song and pause state
- Uses `IAudioOutputDevice` to output audio
- Manages play/pause/resume logic

## 3. **Device Management** (Adapter Pattern)

### **DeviceManager** (Singleton)
- Ensures only one audio device is connected at a time
- Uses `DeviceFactory` to create devices

### **DeviceFactory** (Factory Pattern)
Creates appropriate adapters for:
- **BluetoothSpeakerAdapter** → wraps `BluetoothSpeakerAPI`
- **WiredSpeakerAdapter** → wraps `WiredSpeakerAPI`
- **HeadphonesAdapter** → wraps `HeadphonesAPI`

All adapters implement `IAudioOutputDevice` interface.

## 4. **Playback Strategies** (Strategy Pattern)

### **StrategyManager** (Singleton)
Provides three play strategies:

1. **SequentialPlayStrategy** - plays songs in order
2. **RandomPlayStrategy** - plays songs randomly, maintains history
3. **CustomQueueStrategy** - allows enqueueing songs, falls back to sequential

All implement `PlayStrategy` interface with methods: `next()`, `previous()`, `hasNext()`, `hasPrevious()`, `addToNext()`

## 5. **Playlist Management**

### **PlaylistManager** (Singleton)
- Creates and stores playlists
- Adds songs to playlists
- Retrieves playlists by name

### **Playlist Model**
- Contains name and list of songs
- Provides size and song access

## 6. **Typical User Flow**

```
1. Create songs → MusicPlayerApplication.createSongInLibrary()
2. Create playlist → createPlaylist()
3. Add songs to playlist → addSongToPlaylist()
4. Connect device → connectAudioDevice(DeviceType)
5. Select strategy → selectPlayStrategy(PlayStrategyType)
6. Load playlist → loadPlaylist()
7. Play tracks → playSingleSong() / playAllTracksInPlaylist()
8. Control playback → playPreviousTrackInPlaylist() / pauseCurrentSong()
9. Queue songs → queueSongNext() (for CUSTOM_QUEUE strategy)
```

## 7. **Design Patterns Used**

- **Singleton**: `MusicPlayerApplication`, `MusicPlayerFacade`, all managers
- **Facade**: `MusicPlayerFacade` simplifies complex subsystem interactions
- **Adapter**: Device adapters wrap external APIs to common interface
- **Factory**: `DeviceFactory` creates device adapters
- **Strategy**: Different playback algorithms encapsulated as strategies

The design achieves **loose coupling**, **single responsibility**, and **extensibility** through proper pattern usage.

# Database Schema and REST API Design

## Database Schema

### Tables

#### 1. **users**
```sql
CREATE TABLE users (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

#### 2. **songs**
```sql
CREATE TABLE songs (
    song_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    artist VARCHAR(255) NOT NULL,
    duration_seconds INT,
    file_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_title (title),
    INDEX idx_artist (artist)
);
```

#### 3. **playlists**
```sql
CREATE TABLE playlists (
    playlist_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    playlist_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_user_playlist (user_id, playlist_name)
);
```

#### 4. **playlist_songs**
```sql
CREATE TABLE playlist_songs (
    playlist_song_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    playlist_id BIGINT NOT NULL,
    song_id BIGINT NOT NULL,
    position INT NOT NULL,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (playlist_id) REFERENCES playlists(playlist_id) ON DELETE CASCADE,
    FOREIGN KEY (song_id) REFERENCES songs(song_id) ON DELETE CASCADE,
    UNIQUE KEY unique_playlist_song (playlist_id, song_id),
    INDEX idx_playlist_position (playlist_id, position)
);
```

#### 5. **user_devices**
```sql
CREATE TABLE user_devices (
    device_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    device_type ENUM('BLUETOOTH', 'WIRED', 'HEADPHONES') NOT NULL,
    device_name VARCHAR(100),
    is_active BOOLEAN DEFAULT FALSE,
    last_connected TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_user_active_device (user_id, is_active)
);
```

#### 6. **playback_history**
```sql
CREATE TABLE playback_history (
    history_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    song_id BIGINT NOT NULL,
    played_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    duration_played_seconds INT,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (song_id) REFERENCES songs(song_id) ON DELETE CASCADE,
    INDEX idx_user_history (user_id, played_at)
);
```

#### 7. **user_preferences**
```sql
CREATE TABLE user_preferences (
    preference_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT UNIQUE NOT NULL,
    default_play_strategy ENUM('SEQUENTIAL', 'RANDOM', 'CUSTOM_QUEUE') DEFAULT 'SEQUENTIAL',
    volume_level INT DEFAULT 50,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);
```

---

## REST API Endpoints

### **Authentication**

#### POST /api/auth/register
Register a new user
```json
Request:
{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "securePassword123"
}

Response: 201 Created
{
    "userId": 1,
    "username": "john_doe",
    "email": "john@example.com",
    "token": "jwt_token_here"
}
```

#### POST /api/auth/login
```json
Request:
{
    "email": "john@example.com",
    "password": "securePassword123"
}

Response: 200 OK
{
    "userId": 1,
    "token": "jwt_token_here"
}
```

---

### **Songs**

#### POST /api/songs
Create a new song in library
```json
Request:
{
    "title": "Bohemian Rhapsody",
    "artist": "Queen",
    "durationSeconds": 354,
    "fileUrl": "https://cdn.example.com/songs/bohemian.mp3"
}

Response: 201 Created
{
    "songId": 1,
    "title": "Bohemian Rhapsody",
    "artist": "Queen"
}
```

#### GET /api/songs
Get all songs
```json
Response: 200 OK
{
    "songs": [
        {
            "songId": 1,
            "title": "Bohemian Rhapsody",
            "artist": "Queen",
            "durationSeconds": 354
        }
    ]
}
```

#### GET /api/songs/{songId}
Get song by ID

#### GET /api/songs/search?q={query}
Search songs by title or artist

---

### **Playlists**

#### POST /api/playlists
Create a new playlist
```json
Request:
{
    "playlistName": "My Favorites"
}

Response: 201 Created
{
    "playlistId": 1,
    "playlistName": "My Favorites",
    "userId": 1
}
```

#### GET /api/playlists
Get all playlists for logged-in user

#### GET /api/playlists/{playlistId}
Get specific playlist with songs

#### POST /api/playlists/{playlistId}/songs
Add song to playlist
```json
Request:
{
    "songId": 1
}

Response: 200 OK
{
    "message": "Song added to playlist"
}
```

#### DELETE /api/playlists/{playlistId}/songs/{songId}
Remove song from playlist

---

### **Playback**

#### POST /api/playback/play
Play a single song
```json
Request:
{
    "songId": 1
}

Response: 200 OK
{
    "status": "playing",
    "currentSong": {
        "songId": 1,
        "title": "Bohemian Rhapsody"
    }
}
```

#### POST /api/playback/pause
Pause current song

#### POST /api/playback/resume
Resume paused song

#### POST /api/playback/playlist/{playlistId}/play
Load and play playlist
```json
Request:
{
    "strategyType": "SEQUENTIAL"
}
```

#### POST /api/playback/next
Play next track

#### POST /api/playback/previous
Play previous track

#### POST /api/playback/queue
Add song to play queue
```json
Request:
{
    "songId": 5
}
```

#### GET /api/playback/status
Get current playback status

---

### **Devices**

#### POST /api/devices/connect
Connect audio device
```json
Request:
{
    "deviceType": "BLUETOOTH",
    "deviceName": "JBL Speaker"
}

Response: 200 OK
{
    "deviceId": 1,
    "deviceType": "BLUETOOTH",
    "isActive": true
}
```

#### GET /api/devices
Get all user devices

#### PUT /api/devices/{deviceId}/disconnect
Disconnect device

---

### **User Preferences**

#### GET /api/preferences
Get user preferences

#### PUT /api/preferences
Update preferences
```json
Request:
{
    "defaultPlayStrategy": "RANDOM",
    "volumeLevel": 75
}
```

---

### **Playback History**

#### GET /api/history
Get playback history
```json
Response: 200 OK
{
    "history": [
        {
            "songId": 1,
            "title": "Bohemian Rhapsody",
            "artist": "Queen",
            "playedAt": "2025-01-15T10:30:00Z"
        }
    ]
}
```
![Screenshot (112).png](../../../../../../../Pictures/Screenshots/Screenshot%20%28112%29.png)
---

## Additional Considerations

- All endpoints require JWT authentication (except `/auth/*`)
- Use pagination for list endpoints: `?page=1&size=20`
- Implement rate limiting for API security
- Add WebSocket endpoint for real-time playback updates: `ws://api/playback/stream`