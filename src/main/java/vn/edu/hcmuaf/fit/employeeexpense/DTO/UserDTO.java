package vn.edu.hcmuaf.fit.employeeexpense.DTO;

import lombok.Data;
import org.springframework.stereotype.Component;
import vn.edu.hcmuaf.fit.employeeexpense.Model.Accountant;
import vn.edu.hcmuaf.fit.employeeexpense.Model.Department;
import vn.edu.hcmuaf.fit.employeeexpense.Model.Employee;

@Data
@Component
public class UserDTO {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private Department department;
    private Integer role;

    public UserDTO toDto(Employee employee){
        UserDTO result = new UserDTO();
        result.setId(employee.getEmployeeId());
        result.setEmail(employee.getEmail());
        result.setName(employee.getName());
        result.setRole(employee.getIsManager());
        result.setDepartment(employee.getDepartment());
        result.setPhone(employee.getPhone());
        return result;
    }

    public UserDTO toDto(Accountant accountant){
        UserDTO result = new UserDTO();
        result.setId(accountant.getAccountantId());
        result.setEmail(accountant.getEmail());
        result.setName(accountant.getName());
        result.setRole(2);
        return result;
    }
}
