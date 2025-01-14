package com.kjh.boardback.grobal.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    SUCCESS(200,"SU","Success"),

    VALIDATION_FAILED(400,"VF","Validation Failed"),
    DUPLICATE_EMAIL(400,"DE","Duplicate Email"),
    DUPLICATE_NICKNAME(400,"DN","Duplicate Nickname"),
    DUPLICATE_TEL_NUMBER(400,"DT","Duplicate Tel Number"),
    NOT_EXISTED_USER(400,"NU","This user does not exist."),
    NOT_EXISTED_BOARD(400,"NB","This board does not exist."),

    SIGN_IN_FAIL(401,"SF","Login information mismatch."),
    AUTHORIZATION_FAIL(401,"AF","Authorization Failed."),

    NO_PERMISSION(403,"NP","Do not have permission."),

    DATABASE_ERROR(500,"DBE","Database Error."),

    ;
    private final int status;
    private final String code;
    private final String message;
}
