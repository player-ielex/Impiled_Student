package com.example.impiled_students;

public class RewardsModel {

    private String rewards_name;
    private String rewards_description;
    private String rewards_photo;
    private String rewards_status;
    private long rewards_timestamp;

    public String getRewards_status() {
        return rewards_status;
    }

    public void setRewards_status(String rewards_status) {
        this.rewards_status = rewards_status;
    }

    public RewardsModel(){}

    public RewardsModel(String rewards_name, String rewards_description, String rewards_photo, String rewards_status, long rewards_timestamp ){
        this.rewards_name = rewards_name;
        this.rewards_description = rewards_description;
        this.rewards_photo = rewards_photo;
        this.rewards_status = rewards_status;
        this.rewards_timestamp = rewards_timestamp;
    }

    public String getRewards_name() {
        return rewards_name;
    }

    public void setRewards_name(String rewards_name) {
        this.rewards_name = rewards_name;
    }

    public long getRewards_timestamp() {
        return rewards_timestamp;
    }

    public void setRewards_timestamp(long rewards_timestamp) {
        this.rewards_timestamp = rewards_timestamp;
    }


    public String getRewards_description() {
        return rewards_description;
    }

    public void setRewards_description(String rewards_description) {
        this.rewards_description = rewards_description;
    }

    public String getRewards_photo() {
        return rewards_photo;
    }

    public void setRewards_photo(String rewards_photo) {
        this.rewards_photo = rewards_photo;
    }
}
