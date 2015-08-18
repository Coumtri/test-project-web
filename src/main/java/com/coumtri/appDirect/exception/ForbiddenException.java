package com.coumtri.appDirect.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Source : http://stackoverflow.com/questions/45546/how-do-i-return-a-403-forbidden-in-spring-mvc
 * Created by Julien on 2015-08-17.
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {
}
