package zt.demo.ClassExample;

public class Result {
    String status_code;
    String message;
    public Result(){}
    public Result(String status_code,String message){
        this.status_code=status_code;
        this.message=message;
    }
    public String getStatus_code() {
        return status_code;
    }
    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
