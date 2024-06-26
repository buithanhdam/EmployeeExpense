package vn.edu.hcmuaf.fit.employeeexpense.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmuaf.fit.employeeexpense.Model.*;
import vn.edu.hcmuaf.fit.employeeexpense.Repository.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/api/updateRequest")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ConfirmRequest {

@Autowired
ExpenseRequestRepository expenseRequestRepository;
@Autowired
EmployeeRepository employeeRepository;

@Autowired
ExpenseApprovalRepository expenseApprovalRepository;

@Autowired
AccountantRepository accountantRepository;

@Autowired
ExpensePaymentRepository expensePaymentRepository;
    @CrossOrigin(origins = "http://localhost:63342")
    @GetMapping("/getSubmitRequests/{id}")
    public ResponseEntity<List<ExpenseRequest>> getRequetsOnSubmit(@PathVariable Long id) {
        try{
            List<ExpenseRequest> listR = getAllRequestByDepartment(id);
            List<ExpenseRequest> result = new LinkedList<>();
            for (ExpenseRequest er:
                    listR) {
                if (er.getStatus().equals("Submit"))result.add(er);
            }
            return  new ResponseEntity<>(result, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
    }
    @CrossOrigin(origins = "http://localhost:63342")
    @GetMapping("/getConfirmRequests/{id}")
    public ResponseEntity<List<ExpenseRequest>> getRequetsOnConfirm(@PathVariable Long id) {
        try{
            List<ExpenseRequest> listR = getAllRequestByDepartment(id);
            List<ExpenseRequest> result = new LinkedList<>();
            for (ExpenseRequest er:
                    listR) {
                if (er.getStatus().equals("Confirm"))result.add(er);
                if (er.getStatus().equals("Reject"))result.add(er);
            }
            return  new ResponseEntity<>(result, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
    }
    @CrossOrigin(origins = "http://localhost:63342")
    @GetMapping("/getRejectRequests/{id}")
    public ResponseEntity<List<ExpenseRequest>> getRequetsOnReject(@PathVariable Long id) {
        try{
            List<ExpenseRequest> listR = getAllRequestByDepartment(id);
            List<ExpenseRequest> result = new LinkedList<>();
            for (ExpenseRequest er:
                    listR) {
                if (er.getStatus().equals("Reject"))result.add(er);
            }
            return  new ResponseEntity<>(result, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
    }
    @CrossOrigin(origins = "http://localhost:63342")
    @PostMapping("/confirmRequest")
    public ResponseEntity<ExpenseRequest> confirmRequest(@RequestParam Long request_id, @RequestParam Long manager_id){
        try {
            ExpenseRequest expenseRequest = expenseRequestRepository.findByRequestId(request_id);
            if (expenseRequest != null){
                Department department = expenseRequest.getEmployee().getDepartment();
                if (department.getManager_id()==manager_id) {
                    if (expenseRequest.getStatus().equals("Submit")) {
                        expenseRequest.setStatus("Confirm");
                        expenseRequestRepository.save(expenseRequest);

                        ExpenseApproval expenseApproval = new ExpenseApproval();
                        expenseApproval.setExpenseRequest(expenseRequest);
                        expenseApproval.setStatus("Confirm");
                        expenseApproval.setApprovedAt(new Timestamp(new Date().getTime()));
                        expenseApproval.setEmployee(employeeRepository.findByEmployeeId(manager_id));
                        expenseApprovalRepository.save(expenseApproval);

                        return new ResponseEntity<>(expenseRequest, HttpStatus.OK);
                    }
                }
            }else {
                return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            System.out.println(e.toString());
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }

    @CrossOrigin(origins = "http://localhost:63342")
    @PostMapping("/approveRequest")
    public ResponseEntity<ExpenseRequest> approveRequest(@RequestParam Long request_id, @RequestParam Long manager_id){
        try {
            ExpenseRequest expenseRequest = expenseRequestRepository.findByRequestId(request_id);
            if (expenseRequest != null){
                Accountant accountant = accountantRepository.findByAccountantId(manager_id);
                if (accountant!=null) {
                    if (expenseRequest.getStatus().equals("Confirm")) {
                        expenseRequest.setStatus("Approve");
                        expenseRequestRepository.save(expenseRequest);

                        ExpensePayment expensePayment = expensePaymentRepository.findByExpenseRequest(expenseRequest);
                        if (expensePayment ==null){
                            expensePayment = new ExpensePayment();
                            expensePayment.setExpenseRequest(expenseRequest);
                            expensePayment.setAccountant(accountant);
                            expensePayment.setPaidAt(new Timestamp(new Date().getTime()));
                        }else{
                            expensePayment.setAccountant(accountant);
                            expensePayment.setPaidAt(new Timestamp(new Date().getTime()));
                        }

                        expensePaymentRepository.save(expensePayment);
                        return new ResponseEntity<>(expenseRequest, HttpStatus.OK);
                    }
                }else {
                    return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
                }
            }else {
                return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            System.out.println(e.toString());
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }
    @CrossOrigin(origins = "http://localhost:63342")
    @PostMapping("/rejectRequest")
    public ResponseEntity<ExpenseRequest> rejectRequest(@RequestParam Long request_id, @RequestParam Long manager_id,@RequestParam String reason){
        try {
            ExpenseRequest expenseRequest = expenseRequestRepository.findByRequestId(request_id);
            if (expenseRequest != null){
                Department department = expenseRequest.getEmployee().getDepartment();
                if (department.getManager_id()==manager_id){
                    if (!expenseRequest.getStatus().equals("Approve")){
                        expenseRequest.setStatus("Reject");
                        expenseRequest.setRejection_reason(reason);
                        expenseRequestRepository.save(expenseRequest);

                        ExpenseApproval expenseApproval = expenseApprovalRepository.findByExpenseRequest(expenseRequest);
                        if (expenseApproval == null){
                            expenseApproval = new ExpenseApproval();
                            expenseApproval.setExpenseRequest(expenseRequest);
                            expenseApproval.setApprovedAt(new Timestamp(new Date().getTime()));
                            expenseApproval.setEmployee(employeeRepository.findByEmployeeId(manager_id));
                        }
                        expenseApproval.setRejection_reason(reason);
                        expenseApproval.setStatus("Reject");
                        expenseApprovalRepository.save(expenseApproval);

                        return new ResponseEntity<>(expenseRequest,HttpStatus.OK);
                    }
                }


            }else {
                return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            System.out.println(e.toString());
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }



    public List<ExpenseRequest> getAllRequestByDepartment(Long managerID){
        Employee currentEmployee = employeeRepository.findByEmployeeId(managerID);
        if (currentEmployee.getIsManager() == 1){
            Department department = currentEmployee.getDepartment();
            List<Employee> employees = employeeRepository.findAllByDepartment(department);
            List result = new LinkedList<ExpenseRequest>();
            for (Employee e:
                 employees) {
                List<ExpenseRequest> l = expenseRequestRepository.findAllByEmployee(e);
                for (ExpenseRequest er:
                     l) {
                    result.add(er);
                }
            }
            return  result;
        }else{
            List<ExpenseRequest> l = expenseRequestRepository.findAllByEmployee(currentEmployee);
            return l;
        }
    }




}
