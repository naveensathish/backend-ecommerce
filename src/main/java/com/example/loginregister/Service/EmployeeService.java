package com.example.loginregister.Service;
import com.example.loginregister.Dto.EmployeeDTO;
import com.example.loginregister.Dto.LoginDTO;
import com.example.loginregister.payload.response.LoginMesage;

public interface EmployeeService {
    String addEmployee(EmployeeDTO employeeDTO);

    LoginMesage loginEmployee(LoginDTO loginDTO);

}
