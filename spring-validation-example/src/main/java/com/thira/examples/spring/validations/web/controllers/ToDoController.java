/**
 * 
 */
package com.thira.examples.spring.validations.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Thiranjith
 * 
 *         Created at 25/03/2013 2:41:00 PM
 * 
 */
@Controller
public class ToDoController {

    @RequestMapping("/validatorExample")
    public String validatorExample() {
        return "todo";
    }

    @RequestMapping("/annotatedValidationExample")
    public String customAnnotationsExample() {
        return "todo";
    }

    @RequestMapping("/conditionalValidationExample")
    public String conditionalAnnotationsExample() {
        return "todo";
    }
}
