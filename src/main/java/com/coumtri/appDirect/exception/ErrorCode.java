package com.coumtri.appDirect.exception;

/**
 * http://info.appdirect.com/developers/docs/event_references/api_error_codes/
 * Created by Julien on 2015-08-18.
 */
public enum ErrorCode {
    USER_ALREADY_EXISTS,
    USER_NOT_FOUND,
    ACCOUNT_NOT_FOUND,
    MAX_USERS_REACHED,
    UNAUTHORIZED,
    OPERATION_CANCELED,
    CONFIGURATION_ERROR,
    INVALID_RESPONSE,
    UNKNOWN_ERROR,
    PENDING;
}
