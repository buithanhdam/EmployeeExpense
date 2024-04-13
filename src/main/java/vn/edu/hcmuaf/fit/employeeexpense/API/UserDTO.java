package vn.edu.hcmuaf.fit.employeeexpense.API;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springframework.stereotype.Component;
import vn.edu.hcmuaf.fit.employeeexpense.Model.Accountant;
import vn.edu.hcmuaf.fit.employeeexpense.Model.Department;
import vn.edu.hcmuaf.fit.employeeexpense.Model.Employee;

@Component
public class UserDTO {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private Department department;
    private Integer role;

    UserDTO toDto(Employee employee){
        UserDTO result = new UserDTO();
        result.setId(employee.getEmployeeId());
        result.setEmail(employee.getEmail());
        result.setName(employee.getName());
        result.setRole(employee.getIsManager());
        result.setDepartment(employee.getDepartment());
        result.setPhone(employee.getPhone());
        return result;
    }

    UserDTO toDto(Accountant accountant){
        UserDTO result = new UserDTO();
        result.setId(accountant.getAccountantId());
        result.setEmail(accountant.getEmail());
        result.setName(accountant.getName());
        result.setRole(3);
        return result;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }
}
