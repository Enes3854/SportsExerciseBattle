package com.seb.model;

import java.time.LocalDateTime;

public class Tournament {

    private String id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private Long winnerUserId;

    // Constructors, getters, and setters
    public Tournament() {
    }

    public Tournament(String id, LocalDateTime startTime, LocalDateTime endTime, String status, Long winnerUserId){
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.winnerUserId = winnerUserId;
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getWinnerUserId() {
        return winnerUserId;
    }

    public void setWinnerUserId(Long winnerUserId) {
        this.winnerUserId = winnerUserId;
    }
}
