package vn.edu.hcmuaf.fit.employeeexpense.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmuaf.fit.employeeexpense.Mail.JavaMail;
import vn.edu.hcmuaf.fit.employeeexpense.Model.Accountant;
import vn.edu.hcmuaf.fit.employeeexpense.Model.Employee;
import vn.edu.hcmuaf.fit.employeeexpense.Repository.AccountantRepository;
import vn.edu.hcmuaf.fit.employeeexpense.Repository.EmployeeRepository;
import vn.edu.hcmuaf.fit.employeeexpense.service.OtpGenerator;

@RestController
@RequestMapping(value = "/api/user")
public class User {

    @Autowired
    private AccountantRepository accountantRepository;

    @Autowired
    private OtpGenerator otpGenerator;

    @Autowired
    private EmployeeRepository employeeRepository;


    @Autowired
    private UserDTO userDTO;

    @PostMapping(value = "/sendOTP")
    public ResponseEntity<?> userOTP(@RequestParam("email") String email){
        // check email
        //accountantRepository.findOneByEmail(email);
        Employee employee = employeeRepository.findOneByEmail(email);
        //System.out.println(employee);
        if(employee == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("user not found");
        }
        String otp = otpGenerator.createOtp();
        boolean checkSend = JavaMail.getInstance().sendMail(email,otp,"Mã OTP ĐĂNG NHAP","Dùng mã này để đăng nhập");
        if(!checkSend){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("cant send mail");
        } else {
            employee.setOtp(otp);
            employeeRepository.save(employee);
            return ResponseEntity.ok().body(null);
        }
    }

    @PostMapping(value = "/loginWithOtp")
    public ResponseEntity<?> userLogin(@RequestParam(value = "email") String email,@RequestParam("otp") String otp){
        // check email
        //accountantRepository.findOneByEmail(email);
        Employee employee = employeeRepository.findOneByEmail(email);
        if(employee == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("user not found");
        }
        if(employee.getOtp().equals(otp)){
            return ResponseEntity.ok(userDTO.toDto(employee));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("otp not found");
        }
    }
}
