package zt.demo.ClassExample;
import zt.demo.ClassExample.Problem;
import java.util.ArrayList;
import java.util.List;

public class ProblemList {
    String group_name;
    int question_count;
    List<Problem> data;
    public ProblemList(){}
    public String getGroup_name() { return group_name; }
    public void setGroup_name(String group_name) { this.group_name = group_name; }
    public int getQuestion_count() {
        return question_count;
    }
    public void setQuestion_count(int question_count) {
        this.question_count = question_count;
    }
    public List<Problem> getData() {
        return data;
    }
    public void setData(List<Problem> data) {
        this.data = data;
    }
}
