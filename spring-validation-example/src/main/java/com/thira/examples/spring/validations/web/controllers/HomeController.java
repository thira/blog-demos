/**
 * 
 */
package com.thira.examples.spring.validations.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Thiranjith
 * 
 *         Created at 25/03/2013 12:46:35 PM
 * 
 */
@Controller()
@RequestMapping("/home")
public class HomeController {

    @RequestMapping(method = RequestMethod.GET)
    public String home() {
        return "home";
    }
}
