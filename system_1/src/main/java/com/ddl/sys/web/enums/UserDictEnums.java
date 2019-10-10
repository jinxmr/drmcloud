package com.ddl.sys.web.enums;

public enum UserDictEnums {

    /**
     * 启用停用
     **/
    OK(0, "正常"),
    DISABLE(1, "停用"),
    DELETED(2, "删除");

    private final Integer code;
    private final String info;

    UserDictEnums(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public Integer getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
