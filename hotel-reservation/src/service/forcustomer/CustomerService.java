package service.forcustomer;

import model.customer.Customer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CustomerService {
    private static final CustomerService SINGLETON = new CustomerService();
    private final Map<String, Customer> customer = new HashMap<>();
    private CustomerService(){ }
    public static CustomerService getSingleton(){ return SINGLETON; }
    public void addCustomer(final String email, final String firstName, final String lastName){
        customer.put(email, new Customer(firstName, lastName, email));
    }
    public Customer getCustomer(final String customerEmail){
        return  customer.get(customerEmail);}
    public Collection<Customer>getAllCustomer(){return customer.values();}


}
