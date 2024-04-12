package vn.edu.hcmuaf.fit.employeeexpense.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.hcmuaf.fit.employeeexpense.Model.Employee;
import vn.edu.hcmuaf.fit.employeeexpense.Model.ExpenseApproval;
import vn.edu.hcmuaf.fit.employeeexpense.Model.ExpenseRequest;

import java.util.List;

@Repository
public interface ExpenseRequestRepository extends JpaRepository<ExpenseRequest, Integer> {

    List<ExpenseRequest> findAllByEmployee(Employee e);
    ExpenseRequest findByRequestId(Long id);



}
