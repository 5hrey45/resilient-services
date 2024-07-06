package northstar.cosmos.accountservice.service;

import northstar.cosmos.accountservice.dto.CustomerDetailsDto;

public interface CustomerService {
    CustomerDetailsDto fetchCustomerDetails(String mobileNumber);
}
