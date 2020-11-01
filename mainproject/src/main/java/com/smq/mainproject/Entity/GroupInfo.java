package com.smq.mainproject.Entity;

import java.util.List;
import java.util.Map;

public class GroupInfo {
    int question_count;
    List<Map<String,Object>>problemlist;
    List<String>submitlist;
    public GroupInfo(){}
    public GroupInfo(int question_count, List<Map<String, Object>> problemlist, List<String> submitlist) {
        this.question_count = question_count;
        this.problemlist = problemlist;
        this.submitlist = submitlist;
    }

    public int getQuestion_count() {
        return question_count;
    }

    public void setQuestion_count(int question_count) {
        this.question_count = question_count;
    }

    public List<Map<String, Object>> getProblemlist() {
        return problemlist;
    }

    public void setProblemlist(List<Map<String, Object>> problemlist) {
        this.problemlist = problemlist;
    }

    public List<String> getSubmitlist() {
        return submitlist;
    }

    public void setSubmitlist(List<String> submitlist) {
        this.submitlist = submitlist;
    }
}
