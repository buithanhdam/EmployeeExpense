package vn.edu.hcmuaf.fit.employeeexpense.API;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.hcmuaf.fit.employeeexpense.Model.Employee;
import vn.edu.hcmuaf.fit.employeeexpense.Model.ExpenseRequest;
import vn.edu.hcmuaf.fit.employeeexpense.Repository.EmployeeRepository;
import vn.edu.hcmuaf.fit.employeeexpense.Repository.ExpenseRequestRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;

@Controller
@RequestMapping("/sendrequest")
public class SendRequestController {
    @Autowired
    private ExpenseRequestRepository expenseRequestRepository;

    @Autowired
    EmployeeRepository employeeRepository;
    @CrossOrigin(origins = "http://localhost:63342")
    @PostMapping("/create")
    public String createRequest(@RequestParam("type") String type,
                                @RequestParam("description") String description,
                                @RequestParam("amount") float amount,
                                @RequestParam("filename") MultipartFile file,
                                @RequestParam("employee") String id
                                ) {
        // Create ExpenseRequest object
        Employee employee = employeeRepository.findByEmployeeId(Long.parseLong(id));
//        Employee employee = emrepo.getOne(1);
        if (employee==null){
            return "redirect:./login.html";
        }

        String filename = "";
        if (!file.isEmpty()) {
            try {
                // Lưu file vào thư mục trên máy chủ
                byte[] bytes = file.getBytes();
                Path path = Paths.get("src\\main\\resources\\files\\"+ file.getOriginalFilename());
                System.out.println(file.getResource());
                Files.write(path, bytes);

                // Lấy đường dẫn tuyệt đối của file
                String filePath = path.toString();
                filename = filePath;
                // Tiếp tục xử lý và lưu thông tin vào cơ sở dữ liệu
                // ...
            } catch (Exception e) {
            }
        }
        String status = "Submit";
        if (employee.getIsManager() != 1){
             status = "Confirm";
        }
        ExpenseRequest expenseRequest = new ExpenseRequest(type, description, amount, filename, new Timestamp(System.currentTimeMillis()),employee, status);

        // Save ExpenseRequest
        expenseRequestRepository.save(expenseRequest);

        // Redirect to a success page or return the view name
        return "redirect:/expense_request_group.html"; // Redirect to a success page
    }


}
