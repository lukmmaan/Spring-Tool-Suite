package com.cimb.tokolapak.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cimb.tokolapak.dao.DepartmentRepo;
import com.cimb.tokolapak.dao.EmployeeAddressRepo;
import com.cimb.tokolapak.dao.EmployeeRepo;
import com.cimb.tokolapak.dao.ProjectRepo;
import com.cimb.tokolapak.entity.Department;
import com.cimb.tokolapak.entity.Employee;
import com.cimb.tokolapak.entity.EmployeeAddress;
import com.cimb.tokolapak.entity.Project;
//import com.cimb.tokolapak.service.DepartmentService;
import com.cimb.tokolapak.service.EmployeeService;
import com.cimb.tokolapak.util.EmailUtil;

@RestController
@RequestMapping("/employee")
//@CrossOrigin(origins = "http://localhost:3000")
public class EmployeeController {
	@Autowired
	private EmployeeRepo employeeRepo;
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private EmployeeAddressRepo employeeAddressRepo;
	@Autowired
	private DepartmentRepo departmentRepo;
	@Autowired
	private ProjectRepo projectRepo;
	@Autowired
	private EmailUtil emailUtil;
	@PostMapping("/department/{departmentId}")
	public Employee addEmployee(@RequestBody Employee employee, @PathVariable int departmentId) {
		Department findDepartment = departmentRepo.findById(departmentId).get();
		if (findDepartment == null) {
			throw new RuntimeException("Department not found");
		}
		employee.setDepartment(findDepartment);
		
//		this.emailUtil.sendEmail(employee.getEmail(), "Registrasi Karyawan", "<h1>Selamat !</h1>/n Anda telah bergabung bersama kami!");
		this.emailUtil.sendEmail("lyudokusumo@gmail.com", "registrasi Spring Mail", employee.getEmail()	);
//		System.out.println(employee.getEmail());
		return employeeRepo.save(employee);
	}
	
	@GetMapping
	public Iterable<Employee> getEmployees(){
		return employeeRepo.findAll();
	}
	
	@DeleteMapping("/address/{id}")
	public void deleteEmployeeAddressById(@PathVariable int id) {
		Optional<EmployeeAddress> employeeAddress = employeeAddressRepo.findById(id);
		if(employeeAddress.get() == null) 
			throw new RuntimeException("Employee Address Not Found");
	
		employeeService.deleteEmployeeAddress(employeeAddress.get());
	
	}	
	@PutMapping
	public Employee updateEmployeeAddress(@RequestBody Employee employee) {
		return employeeService.updateEmployeeAddress(employee);
	}
	
	@PostMapping("/{employeeId}/projects/{projectId}")
	public Employee addProjectToEmployee(@PathVariable int employeeId,@PathVariable int projectId) {
		Employee findEmployee = employeeRepo.findById(employeeId).get();
		
		Project findProject = projectRepo.findById(projectId).get();
		
		findEmployee.getProjects().add(findProject);
		return employeeRepo.save(findEmployee);
	}

	@PutMapping("/{employeeId}/address")
	public Employee addAddressToEmployee(@RequestBody EmployeeAddress employeeAddress, @PathVariable int employeeId) {
		Employee findEmployee = employeeRepo.findById(employeeId).get();
		
		if (findEmployee == null)
			throw new RuntimeException("Employee not found");
		
		findEmployee.setEmployeeAddress(employeeAddress);
		
		return employeeRepo.save(findEmployee); 
	}
	@PutMapping("{employeeId}/department/{departmentId}")
	public Employee addEmployeeToDepartment(@PathVariable int departmentId, @PathVariable int employeeId) {
		Employee findEmployee = employeeRepo.findById(employeeId).get();
		if (findEmployee==null) {
			throw new RuntimeException("Employee Not Found");
		}
		Department findDepartment = departmentRepo.findById(departmentId).get();
		if (findDepartment ==null) {
			throw new RuntimeException("Department Not Found");
		}
		findEmployee.setDepartment(findDepartment);
		return employeeRepo.save(findEmployee);
//		return departmentRepo.findById(departmentId).map(department -> {
//			findEmployee.setDepartment(department);
//			return employeeRepo.save(findEmployee);
//		}).orElseThrow(() -> new RuntimeException("Department Not Found"));
	}
}
