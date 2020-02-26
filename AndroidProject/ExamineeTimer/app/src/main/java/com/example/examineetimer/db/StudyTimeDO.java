package com.example.examineetimer.db;

import android.provider.BaseColumns;

import java.util.Date;

public class StudyTimeDO {
    private int id;
    private Date startDateTime;
    private int studySec;

    public StudyTimeDO(int id, Date startDateTime, int studySec) {
        this.id = id;
        this.startDateTime = startDateTime;
        this.studySec = studySec;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public int getStudySec() {
        return studySec;
    }

    public void setStudySec(int studySec) {
        this.studySec = studySec;
    }

    private StudyTimeDO() {}

    public static class StudyTimeEntry implements BaseColumns {
        public static final String TABLE_NAME = "study_time";
        public static final String COLUMN_NAME_id = "id";
        public static final String COLUMN_NAME_START_DATETIME = "start_datetime";
        public static final String COLUMN_NAME_STUDY_SEC = "study_sec";
    }
}
