package vn.edu.hcmuaf.fit.employeeexpense.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "ExpensePayment")
@Data
public class ExpensePayment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;
    @ManyToOne
    @JoinColumn(name = "request_id")
    private ExpenseRequest expenseRequest;
    @ManyToOne
    @JoinColumn(name = "paid_by")
    private Accountant accountant;
}
