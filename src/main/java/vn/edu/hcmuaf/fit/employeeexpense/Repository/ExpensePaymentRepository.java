package vn.edu.hcmuaf.fit.employeeexpense.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.hcmuaf.fit.employeeexpense.Model.Accountant;
import vn.edu.hcmuaf.fit.employeeexpense.Model.ExpenseApproval;
import vn.edu.hcmuaf.fit.employeeexpense.Model.ExpensePayment;
import vn.edu.hcmuaf.fit.employeeexpense.Model.ExpenseRequest;

import java.util.List;

@Repository
public interface ExpensePaymentRepository extends JpaRepository<ExpensePayment, Integer> {

ExpensePayment findByExpenseRequest(ExpenseRequest expenseRequest);

List<ExpensePayment> findAllByAccountant(Accountant accountant);

}
