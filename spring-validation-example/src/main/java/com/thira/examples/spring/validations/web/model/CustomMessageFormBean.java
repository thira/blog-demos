/**
 * 
 */
package com.thira.examples.spring.validations.web.model;

import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.thira.examples.spring.validations.web.controllers.SimpleFormValidationWithCustomMessagesController;

/**
 * Used in {@link SimpleFormValidationWithCustomMessagesController}.
 * 
 * Has the same constraints as the {@link SimpleFormBean}
 * 
 * @author Thiranjith
 * 
 *         Created at 25/03/2013 1:09:20 PM
 * 
 */
public class CustomMessageFormBean {

    @NotEmpty(message = "Hi, first name cannot be empty. Please enter something here :)")
    private String firstName;
    @NotBlank(message = "{com.thira.example.spring.model.CustomMessageFormBean.notBlank.firstNameField}")
    private String lastName;
    @NotNull(message = "{com.thira.example.spring.model.CustomMessageFormBean.notNull.AgeField}")
    @Min(10)
    private Integer age;

    @DateTimeFormat(iso = ISO.DATE)
    @Past
    private Date dateOfBirth;
    @Length(max = 40)
    private String comments;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SimpleFormBean:[").append("firstName").append(firstName).append(",");
        sb.append("lastName:").append(lastName).append(",");
        sb.append("age:").append(age).append(",");
        sb.append("dateOfBirth:").append(dateOfBirth).append(",");
        sb.append("comments:").append(comments).append("]");
        return sb.toString();
    }

}
