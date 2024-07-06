package northstar.cosmos.loanservice.service;

import northstar.cosmos.loanservice.dto.LoanDto;

public interface LoanService {

    LoanDto getLoanDetails(String mobileNumber);

    void createLoan(LoanDto loanDto);

    void updateLoan(LoanDto loanDto);

    void deleteLoan(String mobileNumber);
}
