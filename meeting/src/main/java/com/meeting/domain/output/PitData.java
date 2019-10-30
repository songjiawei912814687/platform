package com.meeting.domain.output;

import java.util.List;

public class PitData {
    private String date;

    private List<PitOutput> pitlist;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<PitOutput> getPitlist() {
        return pitlist;
    }

    public void setPitlist(List<PitOutput> pitlist) {
        this.pitlist = pitlist;
    }
}
