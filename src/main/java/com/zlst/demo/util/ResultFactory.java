package com.zlst.demo.util;
import com.zlst.demo.result.Result;
import com.zlst.demo.result.ResultCode;


public class ResultFactory {
    public static Result buildSuccessResult(Object obj) {
        Result result = new Result(ResultCode.SUCCESS.code,"成功获取数据",obj);
        return result;
    }
    public static Result buildSuccessResult() {
        Result result = new Result(ResultCode.SUCCESS.code,"成功获取数据",null);
        return result;
    }

    public static Result buildErrorResult() {
        Result result = new Result(ResultCode.FAIL.code,"获取数据错误",null);
        return result;
    }

    public static Result buildFailResult(String message) {
        Result result = new Result(ResultCode.FAIL.code,message,null);
        return result;
    }
}
