package com.bridgelabz.javaIO;
import java.util.ArrayList;
import java.util.Scanner;

public class EmployeePayrollService {
	
	Scanner scanner = new Scanner(System.in);
	ArrayList<EmployeePayroll> employeePayrollList = new ArrayList<>();
	
	public static void main(String[] args) {
		
		EmployeePayrollService service = new EmployeePayrollService();
		service.readEmployeePayrollData();
		service.writeEmployeePayrollData();
	}
	
	public void readEmployeePayrollData() {
		System.out.println("Enter Employee ID : ");
		int id = scanner.nextInt();
		System.out.println("Enter Employee Name : ");
		String name = scanner.next();
		System.out.println("Enter Employee Salary : ");
		double salary = scanner.nextDouble();
		System.out.println("Details Added!");
		employeePayrollList.add(new EmployeePayroll(id, name, salary));
	}
	
	public void writeEmployeePayrollData() {
		System.out.println("Writing Employee Payroll Data to the console : "+employeePayrollList);
	}

}