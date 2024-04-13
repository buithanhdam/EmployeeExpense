package vn.edu.hcmuaf.fit.employeeexpense.API;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmuaf.fit.employeeexpense.Model.ExpenseRequest;
import vn.edu.hcmuaf.fit.employeeexpense.Repository.EmployeeRepository;
import vn.edu.hcmuaf.fit.employeeexpense.Repository.ExpenseApprovalRepository;
import vn.edu.hcmuaf.fit.employeeexpense.Repository.ExpenseRequestRepository;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/api/request")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class Request {
    @Autowired
    ExpenseRequestRepository expenseRequestRepository;
    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    ExpenseApprovalRepository expenseApprovalRepository;

    @CrossOrigin(origins = "http://localhost:63342")
    @GetMapping("/getRequest/{id}")
    public ResponseEntity<ExpenseRequest> getRequest(@PathVariable Long id) {
        try{
           ExpenseRequest expenseRequest = expenseRequestRepository.findByRequestId(id);
           if (expenseRequest != null){
               return new ResponseEntity<>(expenseRequest,HttpStatus.OK);
           }else {
               return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
           }
        }catch (Exception e){
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
    }
}
