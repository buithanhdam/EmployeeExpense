package vn.edu.hcmuaf.fit.employeeexpense.api;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springframework.stereotype.Component;
import vn.edu.hcmuaf.fit.employeeexpense.Model.Department;
import vn.edu.hcmuaf.fit.employeeexpense.Model.Employee;

@Component
public class UserDTO {

    private Long employeeId;
    private String name;
    private String email;
    private String phone;
    private Department department;
    private boolean isManager;

    UserDTO toDto(Employee employee){
        UserDTO result = new UserDTO();
        result.setEmployeeId(employee.getEmployeeId());
        result.setEmail(employee.getEmail());
        result.setName(employee.getName());
        result.setManager(employee.getIsManager()==1?true:false);
        result.setDepartment(employee.getDepartment());
        result.setPhone(employee.getPhone());
        return result;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
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

    public boolean isManager() {
        return isManager;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }
}
