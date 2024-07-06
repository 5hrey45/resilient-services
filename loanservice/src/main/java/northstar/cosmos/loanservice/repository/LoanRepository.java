package northstar.cosmos.loanservice.repository;

import northstar.cosmos.loanservice.dto.LoanDto;
import northstar.cosmos.loanservice.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    Optional<Loan> findByMobileNumber(String mobileNumber);
}
