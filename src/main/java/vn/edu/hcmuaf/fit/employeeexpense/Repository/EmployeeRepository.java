package vn.edu.hcmuaf.fit.employeeexpense.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.hcmuaf.fit.employeeexpense.Model.Department;
import vn.edu.hcmuaf.fit.employeeexpense.Model.Employee;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    Employee findByEmployeeId(Long employeeId);
    List<Employee> findAllByDepartment(Department department);
}
