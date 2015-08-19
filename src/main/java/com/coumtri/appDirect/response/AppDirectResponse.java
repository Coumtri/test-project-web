package com.coumtri.appDirect.response;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Source : http://www.beingjavaguys.com/2014/05/xml-response-with-responsebody.html
 *  <?xml version="1.0" encoding="UTF-8" standalone="yes"?><result><success>true</success><accountIdentifier>")
 * .append(accountIdentifier).append("</accountIdentifier></result>
 * <?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><result><success>false</success><errorCode>")
 * .append(errorCode.name()).append("</errorCode><message>").append(message).append("</message></result>
 * Created by Julien on 2015-08-18.
 */
@XmlRootElement(name = "result")
public class AppDirectResponse {
    private boolean success;
    private String accountIdentifier;
    private String errorCode;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    @XmlElement
    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getAccountIdentifier() {
        return accountIdentifier;
    }

    @XmlElement
    public void setAccountIdentifier(String accountIdentifier) {
        this.accountIdentifier = accountIdentifier;
    }

    public String getErrorCode() {
        return errorCode;
    }

    @XmlElement
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    @XmlElement
    public void setMessage(String message) {
        this.message = message;
    }
}
