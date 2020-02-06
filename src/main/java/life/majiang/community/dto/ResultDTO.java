package life.majiang.community.dto;

import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.exception.CustomizeException;
import lombok.Data;

@Data
public class ResultDTO {
    private Integer code;
    private String message;

    //主静态方法
    public static ResultDTO errorof(Integer code, String message) {//静态方法通过类直接调用，不需要new
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(code);
        resultDTO.setMessage(message);
        return resultDTO;
    }
    //副静态方法，负责传值并且调用主静态方法然后传回
    public static ResultDTO errorof(CustomizeErrorCode noLogin) {
        return errorof(noLogin.getCode(), noLogin.getMessage());
    }

    public static ResultDTO errorof(CustomizeException e) {
        return errorof(e.getCode(),e.getMessage());
    }
    public static ResultDTO ok() {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("請求成功");
        return resultDTO;
    }
}
