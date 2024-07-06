package northstar.cosmos.loanservice.service;

import northstar.cosmos.loanservice.dto.LoanDto;
import northstar.cosmos.loanservice.entity.Loan;
import northstar.cosmos.loanservice.exception.LoanAlreadyExistsException;
import northstar.cosmos.loanservice.exception.ResourceNotFoundException;
import northstar.cosmos.loanservice.mapper.LoanMapper;
import northstar.cosmos.loanservice.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoanServiceImpl implements LoanService {

    private LoanRepository loanRepository;

    @Autowired
    public LoanServiceImpl(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    @Override
    public LoanDto getLoanDetails(String mobileNumber) {
        Optional<Loan> optionalLoan = loanRepository.findByMobileNumber(mobileNumber);
        if (optionalLoan.isEmpty()) {
            throw new ResourceNotFoundException("Loan not found with mobile number: " + mobileNumber);
        }

        Loan loan = optionalLoan.get();
        return LoanMapper.mapToLoanDTO(loan, new LoanDto());
    }

    @Override
    public void createLoan(LoanDto loanDto) {
        Optional<Loan> optionalLoan = loanRepository.findByMobileNumber(loanDto.getMobileNumber());
        if (optionalLoan.isPresent()) {
            throw new LoanAlreadyExistsException("Loan already exists with mobile number: "
                    + loanDto.getMobileNumber());
        }

        Loan loan = LoanMapper.mapToLoan(loanDto, new Loan());
        loanRepository.save(loan);
    }

    @Override
    public void updateLoan(LoanDto loanDto) {
        Optional<Loan> optionalLoan = loanRepository.findByMobileNumber(loanDto.getMobileNumber());
        if (optionalLoan.isEmpty()) {
            throw new ResourceNotFoundException("Loan does not exist with mobile number: "
                    + loanDto.getLoanNumber());
        }

        Loan loan = LoanMapper.mapToLoan(loanDto, optionalLoan.get());
        loanRepository.save(loan);
    }

    @Override
    public void deleteLoan(String mobileNumber) {
        Optional<Loan> optionalLoan = loanRepository.findByMobileNumber(mobileNumber);
        if (optionalLoan.isEmpty()) {
            throw new ResourceNotFoundException("Loan does not exist with mobile number: "
                    + mobileNumber);
        }

        Loan loan = optionalLoan.get();
        loanRepository.delete(loan);
    }
}
