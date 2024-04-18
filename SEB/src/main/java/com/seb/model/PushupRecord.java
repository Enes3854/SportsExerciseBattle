package com.seb.model;

import java.time.LocalDateTime;

public class PushupRecord {
    private Long id;
    private int count;
    private Long userId;
    private long duration;
    private LocalDateTime timestamp;
    private String tournamentId;


    public PushupRecord() {
    }

    public PushupRecord(Long id, int count, Long userId, long duration, LocalDateTime timestamp, String tournamentId){
        this.id = id;
        this.count = count;
        this.userId = userId;
        this.duration = duration;
        this.timestamp = timestamp;
        this.tournamentId = tournamentId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getTournamentId() {
        return tournamentId;
    }
}
