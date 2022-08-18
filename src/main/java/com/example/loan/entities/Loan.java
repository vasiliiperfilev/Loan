package com.example.loan.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "loantable")
public class Loan {
    @Id
    private String clientno;
    private String clientname;
    private double loanamount;
    private int years;
    private String loantype;
}
