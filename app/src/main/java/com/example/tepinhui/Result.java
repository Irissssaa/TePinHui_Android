package com.example.tepinhui; // 注意包名，如果报错请根据你实际情况改
public class Result<T> {
    private String code; // "0"表示成功，"-1"表示失败
    private String msg; // 提示信息，如"登录成功"
    private T data; // 返回的具体数据（比如用户信息）
    // 成功的构造器
    public static <T> Result<T> success(T data, String msg) {
        Result<T> result = new Result<>();
        result.code = "0";
        result.msg = msg;
        result.data = data;
        return result;
    }
    // 失败的构造器 (没有泛型 T)
    public static Result error(String msg) {
        Result result = new Result();
        result.code = "-1";
        result.msg = msg;
        return result;
    }
    // Getter & Setter (必须有，否则转JSON会报错)
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }

    // 辅助方法：判断是否成功
    public boolean isSuccess() {
        // 对应截图第9行：code为"0"表示成功
        return "0".equals(code);
    }
}
