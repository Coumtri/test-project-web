package com.coumtri.appDirect;

import com.coumtri.appDirect.exception.ErrorCode;
import com.coumtri.appDirect.exception.ForbiddenException;
import com.coumtri.appDirect.notice.SubscriptionNoticeType;
import com.coumtri.appDirect.response.AppDirectResponse;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.openid4java.OpenIDException;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.Identifier;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.AuthSuccess;
import org.openid4java.message.ParameterList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.List;

/**
 * Created by Julien on 2015-08-20.
 */
@Controller
@SessionAttributes("discovered")
public class AppDirectControllerOpenID {
    protected static final Logger log = LoggerFactory.getLogger(AppDirectControllerOpenID.class);

    // Sources : https://crisdev.wordpress.com/2011/03/23/openid4java-login-example/
    //           https://github.com/jbufu/openid4java/blob/master/samples/appengine-consumer/src/main/webapp/consumer_redirect.jsp
    //           https://code.google.com/p/openid4java/wiki/QuickStart
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/appdirect/login", method = RequestMethod.GET)
    public
    String subscriptionStatusNotification(WebRequest request, HttpServletResponse response, Model model,
                                          @RequestParam(value="openid_url", required = true) String openidUrl,
                                                     @RequestParam(value="accountId", required = true) String accountIdentifier) throws OAuthExpectationFailedException, OAuthCommunicationException, OAuthMessageSignerException, IOException, ParserConfigurationException, SAXException, XPathExpressionException {
        log.debug("params = {}", request.getParameterMap());
        try {
            // configure the return_to URL where your application will receive
            // the authentication responses from the OpenID provider
            String returnToUrl = openidUrl;

            ConsumerManager manager = new ConsumerManager();

            // perform discovery on the user-supplied identifier
            List discoveries = manager.discover(openidUrl);

            // attempt to associate with the OpenID provider
            // and retrieve one service endpoint for authentication
            DiscoveryInformation discovered = manager.associate(discoveries);

            // store the discovery information in the user's session for later use
            // leave out for stateless operation / if there is no session
            model.addAttribute("discovered", discovered);

            // obtain a AuthRequest message to be sent to the OpenID provider
            AuthRequest authReq = manager.authenticate(discovered, returnToUrl);

            return "redirect:" + authReq.getDestinationUrl(true);

        } catch (OpenIDException e) {
            // present error to the user
            log.error("Unable to log with open id the user", e);
        }
        return null;
    }


}
