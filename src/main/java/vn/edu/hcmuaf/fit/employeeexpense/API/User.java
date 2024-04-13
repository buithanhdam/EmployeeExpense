package vn.edu.hcmuaf.fit.employeeexpense.API;

import jakarta.servlet.http.HttpSession;
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
@CrossOrigin(maxAge = 3600)
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

    @CrossOrigin(origins = "http://localhost:63342")
    @PostMapping(value = "/sendOTP")
    public ResponseEntity<?> userOTP(@RequestParam("email") String email){
        // check email

        Employee employee = employeeRepository.findOneByEmail(email);
        Accountant accountant = accountantRepository.findOneByEmail(email);
        //System.out.println(employee);
        if(employee == null){
            if(accountant == null){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("user not found");
            }
        }
        String otp = otpGenerator.createOtp();
        boolean checkSend = JavaMail.getInstance().sendMail(email,otp,"Mã OTP ĐĂNG NHAP","Dùng mã này để đăng nhập");

        if(!checkSend){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("cant send mail");
        } else if(employee != null){
            employee.setOtp(otp);
            employeeRepository.save(employee);
            return ResponseEntity.ok().body(null);
        } else {
            accountant.setOtp(otp);
            accountantRepository.save(accountant);
            return ResponseEntity.ok().body(null);
        }

    }

    @CrossOrigin(origins = "http://localhost:63342")
    @PostMapping(value = "/loginWithOtp")
    public ResponseEntity<?> userLogin(@RequestParam("email") String email, @RequestParam("otp") String otp, HttpSession session){
        // check email
        //accountantRepository.findOneByEmail(email);
        Employee employee = employeeRepository.findOneByEmail(email);
        Accountant accountant = accountantRepository.findOneByEmail(email);
        if(employee == null && accountant == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("user not found");
        }
        if(employee != null){
            if(employee.getOtp().equals(otp)){
                //session.setAttribute("user",employee);
                return ResponseEntity.ok(userDTO.toDto(employee));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("otp not found");
            }
        } else {
            if(accountant.getOtp().equals(otp)){
                //session.setAttribute("user",accountant);
                return ResponseEntity.ok(userDTO.toDto(accountant));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("otp not found");
            }
        }
    }
}
