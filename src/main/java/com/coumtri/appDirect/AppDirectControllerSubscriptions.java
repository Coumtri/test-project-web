package com.coumtri.appDirect;

import com.coumtri.account.Account;
import com.coumtri.account.IUserService;
import com.coumtri.appDirect.exception.ErrorCode;
import com.coumtri.appDirect.exception.ForbiddenException;
import com.coumtri.appDirect.notice.SubscriptionNoticeType;
import com.coumtri.appDirect.response.AppDirectResponse;
import com.coumtri.home.form.ApplicationVersion;
import com.coumtri.home.form.UserInformation;
import com.coumtri.signpost.SignpostService;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;

/**
 * Created by Julien on 2015-08-14.
 */

@RestController
public class AppDirectControllerSubscriptions extends AbstractAppDirectController {

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/subscription/create/notification", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    public @ResponseBody AppDirectResponse subscriptionCreateNotification(WebRequest request, HttpServletResponse response, @RequestParam(value="url", required=false) String eventUrl) throws OAuthExpectationFailedException, OAuthCommunicationException, OAuthMessageSignerException, IOException, ParserConfigurationException, SAXException, XPathExpressionException {
        log.debug("params = {}", request.getParameterMap());
        if (isValid(eventUrl)) {
            try {
                Document doc = signpostService.request(eventUrl);
                // Source : http://xmltoolbox.appspot.com/xpath_generator.html
                String email = valueExtractor("//creator/email/text()", doc);
                if (isAlreadyExist(email)) {
                    return errorValue(ErrorCode.USER_ALREADY_EXISTS, email + " already found in database");
                }
                String appDirectId = valueExtractor("//creator/uuid/text()", doc);
                String applicationVersion = valueExtractor("//editionCode/text()", doc);
                return succesValue(String.valueOf(userService.signin(new Account(email, appDirectId, ApplicationVersion.valueOf(applicationVersion)))));
            }
            catch (Exception exception) {
                log.error("Unable to process subscription event", exception);
                return errorValue(ErrorCode.UNKNOWN_ERROR, exception.getMessage());
            }
        }
        throw new ForbiddenException();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/subscription/change/notification", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    public @ResponseBody AppDirectResponse subscriptionChangeNotification(WebRequest request, HttpServletResponse response, @RequestParam(value="url", required=false) String eventUrl) throws OAuthExpectationFailedException, OAuthCommunicationException, OAuthMessageSignerException, IOException, ParserConfigurationException, SAXException, XPathExpressionException {
        log.debug("params = {}", request.getParameterMap());
        if (isValid(eventUrl)) {
            try {
                Document doc = signpostService.request(eventUrl);
                // Source : http://xmltoolbox.appspot.com/xpath_generator.html
                String accountIdentifier = valueExtractor("/event/payload/account/accountIdentifier/text()", doc);
                String applicationVersion = valueExtractor("/event/payload/order/editionCode/text()", doc);
                Account account = userService.changeApplicationVersion(accountIdentifier, ApplicationVersion.valueOf(applicationVersion));
                if (account == null) {
                    return errorValue(ErrorCode.ACCOUNT_NOT_FOUND, " account " + accountIdentifier + "not found in database");
                }

                return succesValue(String.valueOf(account.getId()));
            }
            catch (Exception exception) {
                log.error("Unable to process subscription event", exception);
                return errorValue(ErrorCode.UNKNOWN_ERROR, exception.getMessage());
            }
        }
        throw new ForbiddenException();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/subscription/cancel/notification", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    public @ResponseBody AppDirectResponse subscriptionCancelNotification(WebRequest request, HttpServletResponse response, @RequestParam(value="url", required=false) String eventUrl) throws OAuthExpectationFailedException, OAuthCommunicationException, OAuthMessageSignerException, IOException, ParserConfigurationException, SAXException, XPathExpressionException {
        log.debug("params = {}", request.getParameterMap());
        if (isValid(eventUrl)) {
            try {
                return super.removeAccount(eventUrl);
            }
            catch (Exception exception) {
                log.error("Unable to process subscription event", exception);
                return errorValue(ErrorCode.UNKNOWN_ERROR, exception.getMessage());
            }
        }
        throw new ForbiddenException();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/subscription/status/notification", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    public @ResponseBody AppDirectResponse subscriptionStatusNotification(WebRequest request, HttpServletResponse response, @RequestParam(value="url", required=false) String eventUrl) throws OAuthExpectationFailedException, OAuthCommunicationException, OAuthMessageSignerException, IOException, ParserConfigurationException, SAXException, XPathExpressionException {
        log.debug("params = {}", request.getParameterMap());
        if (isValid(eventUrl)) {
            try {
                Document doc = signpostService.request(eventUrl);
                // Source : http://xmltoolbox.appspot.com/xpath_generator.html
                String accountIdentifier = valueExtractor("/event/payload/account/accountIdentifier/text()", doc);
                SubscriptionNoticeType notice = SubscriptionNoticeType.valueOf(valueExtractor("/event/payload/notice/type/text()", doc));
                switch (notice) {
                    case CLOSED:
                        userService.removeIncomingInvoice(accountIdentifier);
                        return subscriptionCancelNotification(request, response, eventUrl);
                    case DEACTIVATED:
                        userService.removeIncomingInvoice(accountIdentifier);
                        return succesValue(String.valueOf(userService.suspendAccount(accountIdentifier)));
                    case INVOICE:
                        return succesValue(String.valueOf(userService.addIncomingInvoice(accountIdentifier)));
                    case REACTIVATED:
                        userService.removeIncomingInvoice(accountIdentifier);
                        return succesValue(String.valueOf(userService.unsuspendAccount(accountIdentifier)));
                }
            }
            catch (Exception exception) {
                log.error("Unable to process subscription event", exception);
                return errorValue(ErrorCode.UNKNOWN_ERROR, exception.getMessage());
            }
        }
        throw new ForbiddenException();
    }
}
