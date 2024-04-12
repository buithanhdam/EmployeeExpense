package vn.edu.hcmuaf.fit.employeeexpense.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.hcmuaf.fit.employeeexpense.Model.ExpenseApproval;
@Repository
public interface ExpenseRequestRepository extends JpaRepository<ExpenseApproval, Integer> {


}
