package com.coumtri.appDirect;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

/**
 * Created by Julien on 2015-08-14.
 */

@Controller
public class AppDirectController {

    @RequestMapping(value = "/subscription/create/notification", method = RequestMethod.GET)
    public String subscriptionOrderEvent(Principal principal) {
        return principal != null ? "home/homeSignedIn" : "home/homeNotSignedIn";
    }
}
