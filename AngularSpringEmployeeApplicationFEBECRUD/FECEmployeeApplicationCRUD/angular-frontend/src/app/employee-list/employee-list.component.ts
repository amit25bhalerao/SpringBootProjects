import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { Employee } from '../employee';
import { EmployeeService } from '../employee.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-employee-list',
  templateUrl: './employee-list.component.html',
  styleUrls: ['./employee-list.component.css']
})

export class EmployeeListComponent implements OnInit {

  public empData:any;
  
  constructor(private employeeService: EmployeeService,
    private router: Router){

  }

  ngOnInit(){
    this.employeeService.getEmployees().subscribe((data)=>{
      this.empData = data;
    })
  }

  updateEmployee(id: number){
    this.router.navigate(['update-employee', id])
  }

  deleteEmployee(id: number) {
    this.employeeService.deleteEmployee(id).subscribe(() => {
      this.getEmployeeList(); // Fetch updated employee list
    });
  }

  getEmployeeList() {
    this.employeeService.getEmployees().subscribe((data) => {
      this.empData = data;
    });
  }
}
