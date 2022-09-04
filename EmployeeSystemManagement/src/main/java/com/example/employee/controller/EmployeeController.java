package com.example.employee.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.employee.model.Employee;
import com.example.employee.service.EmployeeService;

@Controller
public class EmployeeController {
	
	private EmployeeService employeeServ;
	
	
	
	public EmployeeController(EmployeeService employeeServ) {
		super();
		this.employeeServ = employeeServ;
	}

	@GetMapping("/")
	public String viewHomePage(Model model) {
		/*
		 * model.addAttribute("listEmployees", employeeServ.gtAllEmployees()); return
		 * "index";
		 */
		return findPaginated(1,"firstName","asc",model);
	}
	
	@GetMapping("/newEmployeeForm")
	public String showNewEmployeeForm(Model model) {
		Employee employee = new Employee();
		model.addAttribute("employee", employee);
		return "new_employee";
	}
	
	@PostMapping("/saveEmployee")
	public String saveEmployee(@ModelAttribute ("employee") Employee employee) {
		employeeServ.saveEmployee(employee);
		return "redirect:/";
	}
	
	@GetMapping("/showFormForUpdate/{id}")
	public String showFormForUpdate(@PathVariable (value="id") Long id, Model model) {
			Employee employee = employeeServ.getEmployeeById(id);
			model.addAttribute("employee", employee);
			return "update_employee";
	}
	
	@GetMapping("/delete/{id}")
	public String deleteEMployee(@PathVariable long id) {
		employeeServ.deleteEmployeeById(id);
		return "redirect:/";	
	}
	
	@GetMapping("/page/{pageNo}")
	public String findPaginated(@PathVariable (value="pageNo") int pageNo,
			@RequestParam(required=false, name="sortField") String sortField,
			@RequestParam(required=false, name="sortDir") String sortDir,
			Model model) {
		int pageSize=5;
		Page<Employee> page= employeeServ.findPaginated(pageNo, pageSize, sortField, sortDir);
		List<Employee> listEmployees = page.getContent();
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc")?"desc":"asc");
		
		model.addAttribute("listEmployees", listEmployees);
		return "index";
	}
}
