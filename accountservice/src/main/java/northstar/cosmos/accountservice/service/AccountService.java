package northstar.cosmos.accountservice.service;

import northstar.cosmos.accountservice.dto.CustomerDto;

import java.util.List;

public interface AccountService {

    CustomerDto fetchAccountDetails(String email);
    void createAccount(CustomerDto customerDto);

    void updateAccount(CustomerDto customerDto);

    void deleteAccount(String email);
}
