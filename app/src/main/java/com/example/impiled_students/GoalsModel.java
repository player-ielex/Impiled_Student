package com.example.impiled_students;

public class GoalsModel {
    private String progress, status, rewards_id;

    public GoalsModel() { }

    public GoalsModel(String progress, String status, String rewards_id) {
        this.progress = progress;
        this.status = status;
        this.rewards_id = rewards_id;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRewards_id() {
        return rewards_id;
    }

    public void setRewards_id(String rewards_id) {
        this.rewards_id = rewards_id;
    }
}
