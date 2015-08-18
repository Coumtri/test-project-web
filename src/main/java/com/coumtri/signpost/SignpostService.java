package com.coumtri.signpost;

import com.coumtri.appDirect.exception.ForbiddenException;
import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.signature.QueryStringSigningStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Source : https://github.com/mttkay/signpost/blob/master/docs/GettingStarted.md
 * Created by Julien on 2015-08-17.
 */
@Configuration
public class SignpostService {

    private static final Logger log = LoggerFactory.getLogger(SignpostService.class);

    @Value("${oauth-consumer-key}")
    private String key;
    @Value("${oauth-consumer-secret}")
    private String secret;

    // Source : http://info.appdirect.com/developers/docs/sample_code_libraries/oauth_api_authorization/
    //          http://www.codejava.net/java-se/networking/use-httpurlconnection-to-download-file-from-an-http-url
    public Document request(String urlParam) throws IOException, OAuthCommunicationException, OAuthExpectationFailedException, OAuthMessageSignerException, ParserConfigurationException, SAXException {
        OAuthConsumer consumer = new DefaultOAuthConsumer(key, secret);
        URL url = new URL(urlParam);
        Document document = null;
        InputStream is = null;
        HttpURLConnection request = null;
        try {
            request = (HttpURLConnection) url.openConnection();
            consumer.sign(request);
            request.connect();
            int responseCode = request.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

            } else {
                log.error("HTTP error code {}", responseCode);
                throw new ForbiddenException();
            }
            is = request.getInputStream();
            //log.debug("Xml on appdirect server = {}", getStringFromInputStream(is));
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            document = factory.newDocumentBuilder().parse(is);
        } finally {
            request.disconnect();
            is.close();
        }
        return document;
    }

    // Source : http://www.mkyong.com/java/how-to-convert-inputstream-to-string-in-java/
    // convert InputStream to String
    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }

    public String outgoing(String urlParam) throws OAuthCommunicationException, OAuthExpectationFailedException, OAuthMessageSignerException {
        OAuthConsumer consumer = new DefaultOAuthConsumer(key, secret);
        consumer.setSigningStrategy(new QueryStringSigningStrategy());
        String signedUrl = consumer.sign(urlParam);
        return signedUrl;
    }

    // Source : https://code.google.com/p/oauth/source/browse/code/java/core/src/main/java/net/oauth/SimpleOAuthValidator.java?r=905&cm_mc_uid=71569500613014398226581&cm_mc_sid_50200000=1439822658
    /*public validate(OAuthMessage message, OAuthAccessor accessor)
            throws OAuthException, IOException, URISyntaxException {
        message.requireParameters(OAuth.OAUTH_CONSUMER_KEY,
                OAuth.OAUTH_SIGNATURE_METHOD, OAuth.OAUTH_SIGNATURE);
        OAuthSignatureMethod.newSigner(message, accessor).validate(message);
    }*/
}
