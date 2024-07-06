package northstar.cosmos.accountservice.service;

import lombok.AllArgsConstructor;
import northstar.cosmos.accountservice.dto.AccountDto;
import northstar.cosmos.accountservice.dto.CardDto;
import northstar.cosmos.accountservice.dto.CustomerDetailsDto;
import northstar.cosmos.accountservice.dto.LoanDto;
import northstar.cosmos.accountservice.entity.Account;
import northstar.cosmos.accountservice.entity.Customer;
import northstar.cosmos.accountservice.exception.ResourceNotFoundException;
import northstar.cosmos.accountservice.mapper.AccountMapper;
import northstar.cosmos.accountservice.mapper.CustomerMapper;
import northstar.cosmos.accountservice.repository.AccountRepository;
import northstar.cosmos.accountservice.repository.CustomerRepository;
import northstar.cosmos.accountservice.service.client.CardFeignClient;
import northstar.cosmos.accountservice.service.client.LoanFeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor // provides single all args constructor, whenever a single constructor is available,
// auto-wiring will be performed implicitly
public class CustomerServiceImpl implements CustomerService {

    private AccountRepository accountRepository;
    private CustomerRepository customerRepository;
    private CardFeignClient cardFeignClient;
    private LoanFeignClient loanFeignClient;

    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber) {
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(mobileNumber);
        if (optionalCustomer.isEmpty()) {
            throw new ResourceNotFoundException("Customer not found with mobile number: "
                    + mobileNumber);
        }
        Customer customer = optionalCustomer.get();

        Optional<Account> optionalAccount = accountRepository.findByCustomerId(customer.getCustomerId());
        if (optionalAccount.isEmpty()) {
            throw new ResourceNotFoundException("Account not found with customer id: "
                    + customer.getCustomerId());
        }
        Account account = optionalAccount.get();

        CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(
                customer, new CustomerDetailsDto());
        customerDetailsDto.setAccountDto(AccountMapper.mapToAccountDto(account, new AccountDto()));

        ResponseEntity<LoanDto> loanDtoResponseEntity = loanFeignClient
                .getLoanDetails(mobileNumber);
        if (loanDtoResponseEntity != null) {
            customerDetailsDto.setLoanDto(loanDtoResponseEntity.getBody());
        }

        ResponseEntity<CardDto> cardDtoResponseEntity = cardFeignClient
                .getCardDetails(mobileNumber);
        if (cardDtoResponseEntity != null) {
            customerDetailsDto.setCardDto(cardDtoResponseEntity.getBody());
        }

        return customerDetailsDto;
    }
}
