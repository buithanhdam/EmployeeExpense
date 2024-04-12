package vn.edu.hcmuaf.fit.employeeexpense.Controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.hcmuaf.fit.employeeexpense.Model.Employee;
import vn.edu.hcmuaf.fit.employeeexpense.Model.ExpenseRequest;
import vn.edu.hcmuaf.fit.employeeexpense.Repository.ExpenseRequestRepository;

import java.sql.Timestamp;

@Controller
@RequestMapping("/send-request")
public class SendRequestController {
    @Autowired
    private ExpenseRequestRepository expenseRequestRepository;

    @PostMapping("/create")
    public String createRequest(@RequestParam("type") String type,
                                @RequestParam("description") String description,
                                @RequestParam("amount") float amount,
                                @RequestParam("filename") String filename,
                                HttpSession session) {
        // Create ExpenseRequest object
        Employee employee = (Employee) session.getAttribute("user");
        if (employee==null){
            return "redirect:/";
        }
        ExpenseRequest expenseRequest = new ExpenseRequest(type, description, amount, filename, new Timestamp(System.currentTimeMillis()),employee, "Requested");

        // Save ExpenseRequest
        expenseRequestRepository.save(expenseRequest);

        // Redirect to a success page or return the view name
        return "redirect:/home"; // Redirect to a success page
    }
    
}
