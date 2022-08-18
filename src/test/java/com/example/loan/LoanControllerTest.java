package com.example.loan;

import com.example.loan.entities.Loan;
import com.example.loan.repositories.LoanRepository;
import com.example.loan.web.LoanController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import java.text.ParseException;
import java.util.*;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.View;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


@ExtendWith(MockitoExtension.class)
@WebAppConfiguration
class LoanControllerTest {
    Loan loan;

    @Autowired
    private MockMvc mockMvc;
    @Mock
    LoanRepository loanRepository;

    @Mock
    View mockView;

    @InjectMocks
    LoanController loanController;

    @BeforeEach
    void setup() throws ParseException {

        loan = new Loan();
        loan.setClientno("1000");
        loan.setClientname("John Doe");
        loan.setLoanamount(10000);
        loan.setYears(100);
        loan.setLoantype("Business");


        MockitoAnnotations.openMocks(this);

        mockMvc = standaloneSetup(loanController).setSingleView(mockView).build();
    }

    // display test
    @Test
    public void findAll_ListView() throws Exception {
        List<Loan> list = new ArrayList<>();
        list.add(loan);
        list.add(loan);

        when(loanRepository.findAll()).thenReturn(list);
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("loans", list))
                .andExpect(view().name("loans"))
                .andExpect(model().attribute("loans", hasSize(2)));

        verify(loanRepository, times(1)).findAll();
        verifyNoMoreInteractions(loanRepository);
    }

    // delete test
    @Test
    void delete() {
        ArgumentCaptor<String> clientnoCapture = ArgumentCaptor.forClass(String.class);
        doNothing().when(loanRepository).deleteById(clientnoCapture.capture());
        loanRepository.deleteById("1000");
        assertEquals("1000", clientnoCapture.getValue());
        verify(loanRepository, times(1)).deleteById("1000");
    }

    // edit form test
    @Test
    void editForm() throws Exception {
        String clientno= "1002";
        Loan loan = new Loan();
        loan.setClientno(clientno);
        loan.setClientname("Joe Doha");
        loan.setLoantype("Business");
        loan.setLoanamount(100000);
        loan.setYears(10);

        when(loanRepository.findLoanByClientno(clientno)).thenReturn(loan);

        mockMvc.perform(get("/editForm").param("clientno", clientno))
                .andExpect(status().isOk())
                .andExpect(model().attribute("loan", loan))
                .andExpect(view().name("editForm"));

        verify(loanRepository, times(1)).findLoanByClientno(anyString());
        verifyNoMoreInteractions(loanRepository);
    }

    // add form test
    @Test
    void addForm() throws Exception {
        mockMvc.perform(get("/addForm"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("loan", new Loan()))
                .andExpect(view().name("addForm"));
    }

    // save/edit test
    @Test
    void save() throws Exception {
        when(loanRepository.save(loan)).thenReturn(loan);
        loanRepository.save(loan);
        verify(loanRepository, times(1)).save(loan);
    }
}
