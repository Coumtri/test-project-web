package com.coumtri.appDirect;

import com.coumtri.account.Account;
import com.coumtri.account.IUserService;
import com.coumtri.appDirect.exception.ForbiddenException;
import com.coumtri.home.form.ApplicationVersion;
import com.coumtri.signpost.SignpostService;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import java.util.Enumeration;

/**
 * Created by Julien on 2015-08-14.
 */

@RestController
public class AppDirectController {
    private static final Logger log = LoggerFactory.getLogger(AppDirectController.class);

    @Autowired
    private SignpostService signpostService;

    @Autowired
    private IUserService userService;

    @RequestMapping(value = "/subscription/create/notification", method = RequestMethod.GET)
    public String subscriptionOrderEvent(WebRequest request, HttpServletResponse response, @RequestParam(value="url", required=false) String eventUrl) throws OAuthExpectationFailedException, OAuthCommunicationException, OAuthMessageSignerException, IOException, ParserConfigurationException, SAXException, XPathExpressionException {
        System.out.println("params =" + request.getParameterMap());
        if (!isValid(eventUrl)) {
            throw new ForbiddenException();
        }
        try {
            Document doc = signpostService.request(eventUrl);
            // Source : http://xmltoolbox.appspot.com/xpath_generator.html
            String email = valueExtractor("//creator/email/text()", doc);
            String appDirectId = valueExtractor("//creator/uuid/text()", doc);
            String applicationVersion = valueExtractor("//editionCode/text()", doc);
            userService.signin(new Account(email, appDirectId, ApplicationVersion.valueOf(applicationVersion)));
        }
        catch (Exception exception) {
            log.error("Unable to process document", exception);
        }
        return null;
    }

    private boolean isValid(String eventUrl) {
        // TODO validate url  - http://info.appdirect.com/developers/docs/sample_code_libraries/oauth_api_authorization
        return true;
    }

    /**
     * Source : http://stackoverflow.com/questions/2811001/how-to-read-xml-using-xpath-in-java
     * @param xpathExp
     * @param doc
     * @return
     */
    private String valueExtractor(String xpathExp, Document doc) {
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
}
