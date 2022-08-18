package com.example.loan.web;

import com.example.loan.entities.Loan;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.loan.repositories.LoanRepository;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

// link to github https://github.com/vasiliiperfilev/Loan
@Controller
@AllArgsConstructor
public class LoanController {
    //dependency injection
    @Autowired
    private LoanRepository loanRepository;

    @GetMapping(path = "/")
    public String getLoans(Model model) {
        List<Loan> loans = loanRepository.findAll();
        model.addAttribute("loans", loans);
        return "loans";
    }

    @GetMapping("delete")
    public String deleteTodo(String clientno){
        loanRepository.deleteLoanByClientno(clientno);
        return "redirect:/";
    }

    @GetMapping("addForm")
    public String addLoan(Model model){
        model.addAttribute("loan", new Loan());
        return "addForm";
    }

    @PostMapping(path="save")
    public String save(Loan loan, BindingResult
            bindingResult, Model model) {
        Loan existingLoan = loanRepository.findLoanByClientno(loan.getClientno());
        if (bindingResult.hasErrors()) {
            return "addForm";
        } else if (existingLoan != null) {
            model.addAttribute("error", "The record you are trying to add is already existing. Choose a different customer number");
            List<Loan> loans = loanRepository.findAll();
            model.addAttribute("loans", loans);
            return "loans";
        } else {
            loanRepository.save(loan);
            model.addAttribute("error", null);
            return "redirect:/";
        }
    }
    @PostMapping(path="edit")
    public String edit(Loan loan, BindingResult
            bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "addForm";
        } else {
            loanRepository.save(loan);
            model.addAttribute("error", null);
            return "redirect:/";
        }
    }


    @GetMapping("editForm")
    public String editLoan(Model model, String clientno){
        Loan loan = loanRepository.findLoanByClientno(clientno);
        if (loan == null) throw new RuntimeException("Todo does not exist");
        model.addAttribute("loan", loan);
        model.addAttribute("types", new String[]{"Personal", "Business"});
        return "editForm";
    }

    @GetMapping("amortizationTable")
    public String getAmortizationTable(Model model, String clientno){
        Loan loan = loanRepository.findLoanByClientno(clientno);
        if (loan == null) throw new RuntimeException("Todo does not exist");
        int months = loan.getYears() * 12;
        double startBalance = loan.getLoanamount();
        double interestRate = (loan.getLoantype().equals("Business") ? 9 : 6) / 12;
        double monthlyPayment = (loan.getLoanamount() * interestRate) / (1 - Math.pow(1 + interestRate, -months));
        double[] endingBalances = new double[months];
        endingBalances[0] = startBalance;
        double[] interests = new double[months];
        interests[0] = 0;
        double[] startingAmounts = new double[months];
        startingAmounts[0] = 0;
        for (int i = 1; i < months; i++) {
            double interestMonthly = (endingBalances[i - 1] * interestRate);
            double principal = monthlyPayment - interestMonthly;
            interests[i] = interestMonthly;
            startingAmounts[i] = principal;
            endingBalances[i] = endingBalances[i - 1] - principal;
        }

        model.addAttribute("loan", loan);
        model.addAttribute("types", new String[]{"Personal", "Business"});
        model.addAttribute("endingBalances", endingBalances);
        model.addAttribute("interests", interests);
        model.addAttribute("startingAmounts", startingAmounts);
        model.addAttribute("monthlyPayment", monthlyPayment);
        return "amortizationTable";
    }

}
