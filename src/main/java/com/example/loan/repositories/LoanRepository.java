package com.example.loan.repositories;

import com.example.loan.entities.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import javax.persistence.Table;
import javax.transaction.Transactional;

@Component
@Table(name = "loantable")
public interface LoanRepository extends JpaRepository<Loan, String> {
    Loan findLoanByClientno(String clientno);
    @Transactional
    void deleteLoanByClientno(String clientno);
}
