/**
 * 
 */
package com.thira.examples.spring.validations.web.model;

import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.thira.examples.spring.validations.web.controllers.SimpleFormValidationController;

/**
 * Used in {@link SimpleFormValidationController}. The bean has the following constraints:
 * <ul>
 * <li>The first name must not be empty</li>
 * <li>The last name must not be blank (i.e. just spaces are not allowed)</li>
 * <li>Age must be at least 10</li>
 * <li>Date of birth must be specified as yyyy-MM-dd format and must be a date in the past (and not empty/null)</li>
 * <li>Comments field can be empty and at most 40 characters in length</li>
 * </ul>
 * 
 * @author Thiranjith
 * 
 *         Created at 25/03/2013 1:09:20 PM
 * 
 */
public class SimpleFormBean {

    @NotEmpty
    private String firstName;
    @NotBlank
    private String lastName;
    @Min(10)
    private int age;

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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
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
