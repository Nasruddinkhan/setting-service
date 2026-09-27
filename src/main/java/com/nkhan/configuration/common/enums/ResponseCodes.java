package com.nkhan.configuration.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum ResponseCodes {
    SUCCESSFUL("I000000", "Successful Operation", ""),
    FAILURE("E00001", "Failed Operation", ""),

    INTEGRATION_ERROR("E999954", "Integration Error", "integration.error.code"),
    UNKNOWN_ERROR("E0000001", "Error while processing", "unknown.error.code");


    private static final Map<String, ResponseCodes> CODE_MAP =
            Arrays.stream(values())
                    .collect(Collectors.toUnmodifiableMap(ResponseCodes::getCode, Function.identity()));
    private final String code;
    private final String message;
    private final String desc;

    public static ResponseCodes fromCode(String code) {
        return CODE_MAP.getOrDefault(code, ResponseCodes.UNKNOWN_ERROR);

    }
}