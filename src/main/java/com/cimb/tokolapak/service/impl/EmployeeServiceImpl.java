package com.cimb.tokolapak.service.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cimb.tokolapak.dao.EmployeeAddressRepo;
import com.cimb.tokolapak.dao.EmployeeRepo;
import com.cimb.tokolapak.entity.Employee;
import com.cimb.tokolapak.entity.EmployeeAddress;
import com.cimb.tokolapak.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService{

	@Autowired
	private EmployeeRepo employeeRepo;
	@Autowired
	private EmployeeAddressRepo employeeAddressRepo;
	@Override
	@Transactional
	public void deleteEmployeeAddress(EmployeeAddress employeeAddress) {
		employeeAddress.getEmployee().setEmployeeAddress(null); //putuskan hubungan dari employee ke address
		employeeAddress.setEmployee(null); //putuskan hubungan dari address ke employee
		
		employeeAddressRepo.delete(employeeAddress);
	}
	@Override
	@Transactional
	public Employee updateEmployeeAddress(Employee employee) {
		Optional<Employee> findEmployee = employeeRepo.findById(employee.getId());
		if(findEmployee.toString()== "Optional.empty")
			throw new RuntimeException("Product with id " + employee.getId() + " doesn't exist");
		return employeeRepo.save(employee);
	}
	
}
