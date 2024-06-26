package vn.edu.hcmuaf.fit.employeeexpense.Repository;

import org.springframework.data.jpa.repository.JpaRepository; 
import org.springframework.stereotype.Repository;
import vn.edu.hcmuaf.fit.employeeexpense.Model.ExpenseApproval;
import vn.edu.hcmuaf.fit.employeeexpense.Model.ExpenseRequest;

@Repository
public interface ExpenseApprovalRepository extends JpaRepository<ExpenseApproval, Integer> {

    ExpenseApproval findByExpenseRequest(ExpenseRequest expenseRequest);

}
