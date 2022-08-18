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

@Controller
@AllArgsConstructor
public class LoanController {
    //dependency injection
    @Autowired
    private LoanRepository loanRepository;

    String[] types = { "Business", "Personal" };

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
    public String addTodo(Model model){
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
    public String editStudents(Model model, String clientno){
        Loan loan = loanRepository.findLoanByClientno(clientno);
        if (loan == null) throw new RuntimeException("Todo does not exist");
        model.addAttribute("loan", loan);
        model.addAttribute("types", new String[]{"Personal", "Business"});
        return "editForm";
    }

}
