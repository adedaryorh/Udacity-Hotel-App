package model.customer;

import com.sun.source.doctree.ReferenceTree;

import java.util.regex.Pattern;

public class Customer {
    //required email format (adedayo@gmail.com)
    private static final String REQUIRED_EMAIL_FORMAT_REGEX_PATTERN = "^(.+)@(.+).(.+)$";
    private final String firstName;
    private final String lastName;
    private final String email;

    public Customer(final String firstName, final String lastName, final String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    private void isValidEmail(final String email){
        Pattern pattern = Pattern.compile(REQUIRED_EMAIL_FORMAT_REGEX_PATTERN);
        if(pattern.matcher(email).matches()){
            throw new IllegalArgumentException("Invalid email format supplied");
        }
    }
    public String getEmail() {return this.email;}

    @Override
    public String toString() {
        return "First Name: "+ this.firstName
                + " LastName: "+ this.lastName
                + " Email: " + this.email;

    }
}
