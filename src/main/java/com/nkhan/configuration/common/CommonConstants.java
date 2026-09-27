package com.nkhan.configuration.common;

public class CommonConstants {

    public static final String CORRELATION_ID_HEADER ="x-nkhan-client-version";
    public static final String SPRING = "spring";
    public static final String MESSAGES = "classpath:messages";
    public static final String UTF_8 = "UTF-8";

    public static final String APP_NAME = "{app.config.applicationName.required}";
    public static final String APP_NAME_SIZE = "{app.config.applicationName.maxSize}";
    public static final String APP_CODE = "{app.config.code.required}";
    public static final String APP_CODE_SIZE = "{app.config.code.maxSize}";

    public static final String APP_TYPE = "{app.config.type.required}";
    public static final String APP_TYPE_SIZE = "{app.config.type.maxSize}";
    public static final int SIZE_50 = 50;
    public static final int SIZE_100 = 100;
    public static final String SYSTEM = "SYSTEM";
    public static final String DUPLICATE_ERROR_MSG = "application.config.duplicate";
    public static final String ID_NOT_ALLOWED_MSG = "application.config.id.not.allowed";
    public static final String FEATURE_NOT_ALLOWED_MSG = "feature.id.not.allowed";
    public static final String FEATURE_MUST_BE_PRESENT = "feature.must.be.present";
    public static final String DUPLICATE_CONFLICT = "feature.conflict";
    public static final String FEATURE_RULE_MUST_BE_PRESENT = "feature.rule.must.be.present";
    private CommonConstants(){}
}
