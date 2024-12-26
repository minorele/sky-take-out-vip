package org.cheems.constant;

public enum JwtClaimsConstant {
    EMP_ID("empId"),
    USER_ID("userId"),
    PHONE("phone"),
    USERNAME ("username"),
    NAME( "name");

    private final String value;

    JwtClaimsConstant(String value) {
        this.value = value;
    }
    // 获取描述方法
    public String getValue() {
        return value;
    }

}
