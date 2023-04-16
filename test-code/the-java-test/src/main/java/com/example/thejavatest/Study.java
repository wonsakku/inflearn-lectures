package com.example.thejavatest;

public class Study {

    private StudyStatus status;
    private int limit;

    public Study(int limit) {
        if(limit < 0){
            throw new IllegalArgumentException("limit은 0보다 커야 한다.");
        }
        this.status = StudyStatus.DRAFT;
        this.limit = limit;
    }

    public StudyStatus getStatus() {
        return status;
    }

    public int getLimit() {
        return limit;
    }
}
