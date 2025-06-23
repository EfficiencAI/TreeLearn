package io.github.EfficiencAI.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Data
public class Result {
    private int code;
    private String msg;
    private Object obj;

    public static Result success(){
        return success(200,"success",null);
    }

    public static Result success(Object obj) {
        return success(200, "success", obj);
    }


    public static Result success(int code, String msg) {
        return success(code, msg, null);
    }

    public static Result success(int code, String msg, Object obj) {
        return new Result(code, msg, obj);
    }

    public static Result error(){
        return error(500,"error",null);
    }

    public static Result error(String msg) {
        return error(500, msg, null);
    }

    public static Result error(int code,String msg){
        return error(code, msg, null);
    }

    public static Result error(int code,String msg,Object obj){
        return new Result(code, msg, obj);
    }
}
