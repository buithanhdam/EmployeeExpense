package vn.edu.hcmuaf.fit.employeeexpense.API;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.hcmuaf.fit.employeeexpense.Model.*;
import vn.edu.hcmuaf.fit.employeeexpense.Repository.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/api/request")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RequestController {
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
    @GetMapping("/getRequest/{id}")
    public ResponseEntity<ExpenseRequest> getRequest(@PathVariable Long id) {
        try {
            ExpenseRequest expenseRequest = expenseRequestRepository.findByRequestId(id);
            if (expenseRequest != null) {
                return new ResponseEntity<>(expenseRequest, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:63342")
    @GetMapping("/getEmployeeHistoryRequests/{id}")
    public ResponseEntity<List<ExpenseRequest>> getEmployeeHistoryRequests(@PathVariable Long id) {
        try {
            List<ExpenseRequest> result = getAllRequestByEmployee(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:63342")
    @GetMapping("/getSubmitRequests/{id}")
    public ResponseEntity<List<ExpenseRequest>> getRequetsOnSubmit(@PathVariable Long id) {
        try {
            List<ExpenseRequest> listR = getAllRequestByEmployee(id);
            List<ExpenseRequest> result = new LinkedList<>();
            for (ExpenseRequest er : listR) {
                if (er.getStatus().equals("Submit")) result.add(er);
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:63342")
    @GetMapping("/getConfirmRequests/{id}")
    public ResponseEntity<List<ExpenseRequest>> getRequetsOnConfirm(@PathVariable Long id) {
        try {
            List<ExpenseRequest> listR = getAllRequestByEmployee(id);
            List<ExpenseRequest> result = new LinkedList<>();
            for (ExpenseRequest er : listR) {
                if (er.getStatus().equals("Confirm")) result.add(er);
                if (er.getStatus().equals("Reject")) result.add(er);
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:63342")
    @GetMapping("/getRejectRequests/{id}")
    public ResponseEntity<List<ExpenseRequest>> getRequetsOnReject(@PathVariable Long id) {
        try {
            List<ExpenseRequest> listR = getAllRequestByEmployee(id);
            List<ExpenseRequest> result = new LinkedList<>();
            for (ExpenseRequest er : listR) {
                if (er.getStatus().equals("Reject")) result.add(er);
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:63342")
    @PostMapping("/confirmRequest")
    public ResponseEntity<ExpenseRequest> confirmRequest(@RequestParam Long request_id, @RequestParam Long manager_id) {
        try {
            System.out.println("Request ID: " + request_id);
            System.out.println("Manager ID: " + manager_id);
            ExpenseRequest expenseRequest = expenseRequestRepository.findByRequestId(request_id);
            if (expenseRequest != null) {
                Department department = expenseRequest.getEmployee().getDepartment();
//                if (department.getManager_id() == manager_id) {
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
//                } else {
//                    System.out.println("Manager ID does not match");
//                }
            } else {
                System.out.println("ExpenseRequest not found");
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e.toString());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }


    @CrossOrigin(origins = "http://localhost:63342")
    @PostMapping("/approveRequest")
    public ResponseEntity<ExpenseRequest> approveRequest(@RequestParam Long request_id, @RequestParam Long manager_id) {
        try {
            ExpenseRequest expenseRequest = expenseRequestRepository.findByRequestId(request_id);
            if (expenseRequest != null) {
                Accountant accountant = accountantRepository.findByAccountantId(manager_id);
                if (accountant != null) {
                    if (expenseRequest.getStatus().equals("Confirm")) {
                        expenseRequest.setStatus("Approve");
                        expenseRequestRepository.save(expenseRequest);

                        ExpensePayment expensePayment = expensePaymentRepository.findByExpenseRequest(expenseRequest);
                        if (expensePayment == null) {
                            expensePayment = new ExpensePayment();
                            expensePayment.setExpenseRequest(expenseRequest);
                            expensePayment.setAccountant(accountant);
                            expensePayment.setPaidAt(new Timestamp(new Date().getTime()));
                        } else {
                            expensePayment.setAccountant(accountant);
                            expensePayment.setPaidAt(new Timestamp(new Date().getTime()));
                        }

                        expensePaymentRepository.save(expensePayment);
                        return new ResponseEntity<>(expenseRequest, HttpStatus.OK);
                    }
                } else {
                    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @CrossOrigin(origins = "http://localhost:63342")
    @PostMapping("/rejectRequest")
    public ResponseEntity<ExpenseRequest> rejectRequest(@RequestParam Long request_id, @RequestParam Long manager_id, @RequestParam String reason) {
        try {
            ExpenseRequest expenseRequest = expenseRequestRepository.findByRequestId(request_id);
            if (expenseRequest != null) {
                Department department = expenseRequest.getEmployee().getDepartment();
//                if (department.getManager_id() == manager_id) {
                    if (!expenseRequest.getStatus().equals("Approve")) {
                        expenseRequest.setStatus("Reject");
                        expenseRequest.setRejection_reason(reason);
                        expenseRequestRepository.save(expenseRequest);

                        ExpenseApproval expenseApproval = expenseApprovalRepository.findByExpenseRequest(expenseRequest);
                        if (expenseApproval == null) {
                            expenseApproval = new ExpenseApproval();
                            expenseApproval.setExpenseRequest(expenseRequest);
                            expenseApproval.setApprovedAt(new Timestamp(new Date().getTime()));
                            expenseApproval.setEmployee(employeeRepository.findByEmployeeId(manager_id));
                        }
                        expenseApproval.setRejection_reason(reason);
                        expenseApproval.setStatus("Reject");
                        expenseApprovalRepository.save(expenseApproval);

                        return new ResponseEntity<>(expenseRequest, HttpStatus.OK);
                    }
//                }
            } else {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @CrossOrigin(origins = "http://localhost:63342")
    @PostMapping("/create")
    public String createRequest(@RequestParam("type") String type,
                                @RequestParam("description") String description,
                                @RequestParam("amount") float amount,
                                @RequestParam("filename") MultipartFile file,
                                @RequestParam("employee") String id) {
        // Create ExpenseRequest object
        Employee employee = employeeRepository.findByEmployeeId(Long.parseLong(id));
        if (employee == null) {
            return "redirect:http://localhost:63342/EmployeeExpense/static/login.html";
        }

        String filename = "";
        if (!file.isEmpty()) {
            try {
                // Lưu file vào thư mục trên máy chủ
                byte[] bytes = file.getBytes();
                Path path = Paths.get("src/main/resources/static/data/" + file.getOriginalFilename());
                Files.write(path, bytes);

                // Lấy đường dẫn tương đối của file
                filename = "http://localhost:63342/EmployeeExpense/static/data/" + file.getOriginalFilename();
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }

        // Save ExpenseRequest
        String status = "Submit";
        if (employee.getIsManager() == 1) {
            status = "Confirm";
            ExpenseRequest expenseRequest = new ExpenseRequest(type, description, amount, filename, new Timestamp(System.currentTimeMillis()), employee, status);

            expenseRequestRepository.save(expenseRequest);
            ExpenseApproval expenseApproval = new ExpenseApproval();
            expenseApproval.setExpenseRequest(expenseRequest);
            expenseApproval.setApprovedAt(new Timestamp(new Date().getTime()));
            expenseApproval.setEmployee(employee);
            expenseApproval.setStatus("Confirm");
            expenseApprovalRepository.save(expenseApproval);
            return "redirect:http://localhost:63342/EmployeeExpense/static/manager_management.html"; // Redirect to a success page

        } else {
            ExpenseRequest expenseRequest = new ExpenseRequest(type, description, amount, filename, new Timestamp(System.currentTimeMillis()), employee, status);
            expenseRequestRepository.save(expenseRequest);
            return "redirect:http://localhost:63342/EmployeeExpense/static/expense_request_personal.html"; // Redirect to a success page
        }
    }

    public List<ExpenseRequest> getAllRequestByEmployee(Long managerID) {
        Employee currentEmployee = employeeRepository.findByEmployeeId(managerID);
        if (currentEmployee.getIsManager() == 1) {
            Department department = currentEmployee.getDepartment();
            List<Employee> employees = employeeRepository.findAllByDepartment(department);
            List<ExpenseRequest> result = new LinkedList<>();
            for (Employee e : employees) {
                List<ExpenseRequest> l = expenseRequestRepository.findAllByEmployee(e);
                result.addAll(l);
            }
            return result;
        } else {
            return expenseRequestRepository.findAllByEmployee(currentEmployee);
        }
    }

    @CrossOrigin(origins = "http://localhost:63342")
    @GetMapping("/getAllSubmitRequests")
    public ResponseEntity<List<ExpenseRequest>> getAllConfirmRequest() {
        try {
            List<ExpenseRequest> listR = expenseRequestRepository.findAllBy();
            List<ExpenseRequest> result = new LinkedList<>();
            for (ExpenseRequest er : listR) {
                if (er.getStatus().equals("Confirm")) result.add(er);
                //if (er.getStatus().equals("Reject")) result.add(er);
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:63342")
    @GetMapping("/historyAccountant")
    public ResponseEntity<List<ExpenseRequest>> getAllRequestByAccountId(@RequestParam("id") Long id) {
        try {
            List<ExpensePayment> listR = expensePaymentRepository.findAllByAccountant(accountantRepository.findByAccountantId(id));
            List<ExpenseRequest> result = new LinkedList<>();
            for (ExpensePayment er : listR) {
                result.add(er.getExpenseRequest());
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:63342")
    @PostMapping("/accRejectRequest")
    public ResponseEntity<ExpenseRequest> accRejectRequest(@RequestParam Long request_id, @RequestParam Long manager_id, @RequestParam String reason) {
        try {
            ExpenseRequest expenseRequest = expenseRequestRepository.findByRequestId(request_id);
            if (expenseRequest != null) {
                Accountant accountant = accountantRepository.findByAccountantId(manager_id);
                if (accountant != null) {
                    if (!expenseRequest.getStatus().equals("Approve")) {
                        expenseRequest.setStatus("Reject");
                        expenseRequest.setRejection_reason(reason);
                        expenseRequestRepository.save(expenseRequest);

                        ExpensePayment expensePayment = expensePaymentRepository.findByExpenseRequest(expenseRequest);
                        if (expensePayment == null) {
                            expensePayment = new ExpensePayment();
                            expensePayment.setExpenseRequest(expenseRequest);
                            expensePayment.setAccountant(accountant);
                            expensePayment.setPaidAt(new Timestamp(new Date().getTime()));
                        } else {
                            expensePayment.setAccountant(accountant);
                            expensePayment.setPaidAt(new Timestamp(new Date().getTime()));
                        }

                        expensePaymentRepository.save(expensePayment);
                        return new ResponseEntity<>(expenseRequest, HttpStatus.OK);
                    }
                }
            } else {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

}
