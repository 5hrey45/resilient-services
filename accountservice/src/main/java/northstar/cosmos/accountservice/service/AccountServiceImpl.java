package northstar.cosmos.accountservice.service;

import northstar.cosmos.accountservice.dto.AccountDto;
import northstar.cosmos.accountservice.dto.CustomerDto;
import northstar.cosmos.accountservice.entity.Account;
import northstar.cosmos.accountservice.entity.Customer;
import northstar.cosmos.accountservice.exception.CustomerAlreadyExistsException;
import northstar.cosmos.accountservice.exception.ResourceNotFoundException;
import northstar.cosmos.accountservice.mapper.AccountMapper;
import northstar.cosmos.accountservice.mapper.CustomerMapper;
import northstar.cosmos.accountservice.repository.AccountRepository;
import northstar.cosmos.accountservice.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;
    private CustomerRepository customerRepository;

    @Autowired
    AccountServiceImpl(AccountRepository accountRepository, CustomerRepository customerRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());

        // check if the customer is already registered with email or mobile number
        Optional<Customer> optionalCustomer = customerRepository.findByEmail(customer.getEmail());
        if (optionalCustomer.isPresent()) {
            throw new CustomerAlreadyExistsException("Customer already registered with email: "
                    + customer.getEmail());
        }

        Optional<Customer> customerOptional = customerRepository
                .findByMobileNumber(customer.getMobileNumber());
        if (customerOptional.isPresent()) {
            throw new CustomerAlreadyExistsException("Customer already registered with mobile number: "
                    + customer.getMobileNumber());
        }

        Customer savedCustomer = customerRepository.save(customer);

        // create a bank account for the saved customer
        accountRepository.save(createNewAccount(customer));
    }

    private Account createNewAccount(Customer customer) {
        Account account = new Account();
        account.setCustomerId(customer.getCustomerId());
        account.setAccountType("Savings");
        account.setBranchAddress("123 Main Street, New York");
        return account;
    }

    public CustomerDto fetchAccountDetails(String email) {
        Optional<Customer> optionalCustomer = customerRepository.findByEmail(email);
        if (optionalCustomer.isEmpty()) {
            throw new ResourceNotFoundException("Customer not found with email: "
                    + email);
        }
        Customer customer = optionalCustomer.get();
        Optional<Account> optionalAccount = accountRepository.findByCustomerId(customer.getCustomerId());

        if (optionalAccount.isEmpty()) {
            throw new ResourceNotFoundException("Account not found with customer id: "
                    + customer.getCustomerId());
        }

        Account account = optionalAccount.get();
        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        customerDto.setAccountDto(AccountMapper.mapToAccountDto(account, new AccountDto()));

        return customerDto;
    }

    public void updateAccount(CustomerDto customerDto) {
        AccountDto accountDto = customerDto.getAccountDto();
        if (accountDto != null) {
            Optional<Account> optionalAccount = accountRepository
                    .findById(accountDto.getAccountNumber());
            if (optionalAccount.isEmpty()) {
                throw new ResourceNotFoundException("Account not found with account number: "
                        + accountDto.getAccountNumber());
            }
            Account account = AccountMapper.mapToAccount(accountDto, optionalAccount.get());
            accountRepository.save(account);

            Optional<Customer> optionalCustomer = customerRepository
                    .findById(account.getCustomerId());
            if (optionalAccount.isEmpty()) {
                throw new ResourceNotFoundException("Customer not found with customer id: "
                        + account.getCustomerId());
            }
            Customer customer = CustomerMapper.mapToCustomer(customerDto, optionalCustomer.get());
            customerRepository.save(customer);
        }
    }

    public void deleteAccount(String email) {
        Optional<Customer> optionalCustomer = customerRepository.findByEmail(email);
        if (optionalCustomer.isEmpty()) {
            throw new ResourceNotFoundException("Customer not found with email: " + email);
        }

        Customer customer = optionalCustomer.get();
        customerRepository.delete(customer);

        Optional<Account> optionalAccount = accountRepository.findByCustomerId(customer.getCustomerId());
        if (optionalAccount.isEmpty()) {
            throw new ResourceNotFoundException("Account not found with customer id: "
                    + customer.getCustomerId());
        }

        Account account = optionalAccount.get();
        accountRepository.delete(account);
    }
}
