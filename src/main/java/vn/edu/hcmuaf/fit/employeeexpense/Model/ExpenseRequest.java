package vn.edu.hcmuaf.fit.employeeexpense.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "expense_request")
@Data
public class ExpenseRequest implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;
    @Column(name = "type")
    private String type;
    @Column(name = "description")
    private String decription;
    @Column(name = "amount")
    private float amount;
    @Column(name = "file_attachment")
    private String fileAttachment;
    @Column(name = "created_at")
    private Timestamp createdAt;
    @ManyToOne
    @JoinColumn(name = "created_by")
    private Employee employee;
    @Column(name = "status")
    private String status;
    @Column(name = "rejection_reason")
    private String rejection_reason;

    public ExpenseRequest(String type, String decription, float amount, String fileAttachment, Timestamp createdAt, Employee employee, String status) {
        this.type = type;
        this.decription = decription;
        this.amount = amount;
        this.fileAttachment = fileAttachment;
        this.createdAt = createdAt;
        this.employee = employee;
        this.status = status;
    }

    public ExpenseRequest() {

    }
}
