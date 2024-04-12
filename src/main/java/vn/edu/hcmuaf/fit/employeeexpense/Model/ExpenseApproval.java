package vn.edu.hcmuaf.fit.employeeexpense.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "ExpenseApproval")
@Data
public class ExpenseApproval implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long approvalId;
    @ManyToOne
    @JoinColumn(name = "request_id")
    private ExpenseRequest expenseRequest;
    @ManyToOne
    @JoinColumn(name = "approved_by")
    private Employee employee;
    @Column(name = "approved_at")
    private Timestamp approvedAt;
    @Column(name = "status")
    private String status;
    @Column(name = "rejection_reason")
    private String rejection_reason;


}
