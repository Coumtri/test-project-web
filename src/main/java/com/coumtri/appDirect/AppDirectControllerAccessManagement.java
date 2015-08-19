package com.coumtri.appDirect;

import com.coumtri.account.Account;
import com.coumtri.appDirect.exception.ErrorCode;
import com.coumtri.appDirect.exception.ForbiddenException;
import com.coumtri.appDirect.response.AppDirectResponse;
import com.coumtri.home.form.ApplicationVersion;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;

/**
 * Created by Julien on 2015-08-18.
 */
@RestController
public class AppDirectControllerAccessManagement extends AbstractAppDirectController {

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/user/assignment/notification", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    public @ResponseBody AppDirectResponse userAssignmentNotification(WebRequest request, HttpServletResponse response, @RequestParam(value="url", required=false) String eventUrl) throws OAuthExpectationFailedException, OAuthCommunicationException, OAuthMessageSignerException, IOException, ParserConfigurationException, SAXException, XPathExpressionException {
        log.debug("params = {}", request.getParameterMap());
        if (isValid(eventUrl)) {
            try {
                Document doc = signpostService.request(eventUrl);
                // Source : http://xmltoolbox.appspot.com/xpath_generator.html
                String email = valueExtractor("/event/payload/user/email/text()", doc);
                String accountIdentifier = valueExtractor("/event/payload/account/accountIdentifier/text()", doc);
                if (isAlreadyExist(email)) {
                    return succesValue(String.valueOf(userService.changeAssignment(accountIdentifier, true)));
                }
                String status = valueExtractor("/event/payload/account/status/text()", doc);
                log.debug("Account {} is {}", accountIdentifier, status);
                String appDirectId = valueExtractor("/event/payload/user/uuid/text()", doc);
                return succesValue(String.valueOf(userService.signin(new Account(email, Long.valueOf(accountIdentifier), appDirectId,  ApplicationVersion.LIMITED))));
            }
            catch (Exception exception) {
                log.error("Unable to process assignment event", exception);
                return errorValue(ErrorCode.UNKNOWN_ERROR, exception.getMessage());
            }
        }
        throw new ForbiddenException();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/user/unassignment/notification", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    public @ResponseBody AppDirectResponse userUnassignmentNotification(WebRequest request, HttpServletResponse response, @RequestParam(value="url", required=false) String eventUrl) throws OAuthExpectationFailedException, OAuthCommunicationException, OAuthMessageSignerException, IOException, ParserConfigurationException, SAXException, XPathExpressionException {
        log.debug("params = {}", request.getParameterMap());
        if (isValid(eventUrl)) {
            try {
                Document doc = signpostService.request(eventUrl);
                String accountIdentifier = valueExtractor("/event/payload/account/accountIdentifier/text()", doc);
                if (isAlreadyExistId(accountIdentifier)) {
                    return succesValue(String.valueOf(userService.changeAssignment(accountIdentifier, false)));
                } else {
                    return errorValue(ErrorCode.USER_NOT_FOUND, "User not found in database");
                }
            }
            catch (Exception exception) {
                log.error("Unable to process assignment event", exception);
                return errorValue(ErrorCode.UNKNOWN_ERROR, exception.getMessage());
            }
        }
        throw new ForbiddenException();
    }
}
