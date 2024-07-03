package vn.edu.hcmuaf.fit.employeeexpense.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "employee")
@Data
public class Employee implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;
    @Column(name = "name")
    private String name;
    @Column(name = "email")
    private String email;
    @Column(name = "phone")
    private String phone;
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
    @Column(name = "is_manager")
    private int isManager;
    @Column(name = "otp")
    private String otp;
}
