package com.example.examineetimer;

public class DiaryListDO {
    private String studyTime;
    private String studyDate;

    public String getStudyTime() {
        return studyTime;
    }

    public void setStudyTime(String studyTime) {
        this.studyTime = studyTime;
    }

    public String getStudyDate() {
        return studyDate;
    }

    public void setStudyDate(String studyDate) {
        this.studyDate = studyDate;
    }

    public DiaryListDO(String studyTime, String studyDate) {
        this.studyTime = studyTime;
        this.studyDate = studyDate;
    }
}
