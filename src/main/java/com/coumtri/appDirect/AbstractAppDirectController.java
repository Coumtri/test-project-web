package com.coumtri.appDirect;

import com.coumtri.account.IUserService;
import com.coumtri.appDirect.exception.ErrorCode;
import com.coumtri.appDirect.response.AppDirectResponse;
import com.coumtri.home.form.UserInformation;
import com.coumtri.signpost.SignpostService;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;

/**
 * Created by Julien on 2015-08-18.
 */
public abstract class AbstractAppDirectController {
    protected static final Logger log = LoggerFactory.getLogger(AbstractAppDirectController.class);

    @Autowired
    protected SignpostService signpostService;

    @Autowired
    protected IUserService userService;

    protected boolean isValid(String eventUrl) {
        // TODO validate url  - http://info.appdirect.com/developers/docs/sample_code_libraries/oauth_api_authorization
        return true;
    }

    protected boolean isAlreadyExist(String email) {
        for (UserInformation user : userService.showUserDetails()) {
            if (StringUtils.equals(StringUtils.trim(user.getUsername()), StringUtils.trim(email))) {
                return true;
            }
        }
        return false;
    }

    protected boolean isAlreadyExistId(String id) {
        return userService.findAccount(id) != null;
    }

    /**
     * Source : http://stackoverflow.com/questions/2811001/how-to-read-xml-using-xpath-in-java
     * @param xpathExp
     * @param doc
     * @return
     */
    protected String valueExtractor(String xpathExp, Document doc) {
        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();
        XPathExpression expr = null;
        try {
            expr = xpath.compile(xpathExp);
            return (String) expr.evaluate(doc, XPathConstants.STRING);
        } catch (XPathExpressionException e) {
            log.error("Xpath extraction error", e);
        }
        return null;
    }

    protected AppDirectResponse succesValue(String accountIdentifier) {
        AppDirectResponse response = new AppDirectResponse();
        response.setAccountIdentifier(accountIdentifier);
        response.setSuccess(true);
        return response;
    }

    protected AppDirectResponse errorValue(ErrorCode errorCode, String message) {
        AppDirectResponse response = new AppDirectResponse();
        response.setErrorCode(errorCode.name());
        response.setMessage(message);
        response.setSuccess(false);
        return response;
    }

    protected AppDirectResponse removeAccount(String url) throws SAXException, OAuthExpectationFailedException, IOException, OAuthMessageSignerException, ParserConfigurationException, OAuthCommunicationException {
        Document doc = signpostService.request(url);
        // Source : http://xmltoolbox.appspot.com/xpath_generator.html
        String accountIdentifier = valueExtractor("/event/payload/account/accountIdentifier/text()", doc);
        Long accountIdentifierDeleted = userService.removeAccount(accountIdentifier);
        if (accountIdentifierDeleted == null) {
            return errorValue(ErrorCode.ACCOUNT_NOT_FOUND, " account " + accountIdentifierDeleted + "not found in database");
        }
        return succesValue(String.valueOf(accountIdentifierDeleted));
    }
}
