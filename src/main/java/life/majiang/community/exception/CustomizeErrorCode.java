package life.majiang.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {
    //问题集合
    QUESTION_NOT_FOUND(2001, "你找的问题不存在"),
    PARAM_TAR_NOT_FOUND(2002,"未选中回复进行评论"),
    NO_LOGIN(2003,"用户未登录"),
    SYS_ERROR(2004,"服务器错误"),
    OK(2005,"评论成功"),
    USER_NOT_QUESTIONTOR(2006,"用户不是问题发起人"),
    TYPE_PARAM_WRONG(2007,"评论类型错误或者不存在"),
    COMMENT_NOT_FOUND(2008,"回复的评论不存在了"),
    ContentIsNone(2009,"评论内容不能为空");

    @Override
    public String getMessage() {
        return message;
    }
    private String message;

    @Override
    public Integer getCode() {
        return code;
    }

    private Integer code;

    CustomizeErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
