import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Employee } from './employee';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {
  private baseURL = "http://localhost:8080/api/v1/employees";

  constructor(private httpClient: HttpClient) { }
  
  getEmployees(): Observable<Employee[]> {
    return this.httpClient.get<Employee[]>(this.baseURL);
  }

  createEmployee(employee: Employee): Observable<Employee> {
    return this.httpClient.post<Employee>(this.baseURL, employee);
  }

  getEmployeeById(id: number): Observable<Employee> {
    const url = `${this.baseURL}/${id}`;
    return this.httpClient.get<Employee>(url);
  }

  updateEmployeeById(id: number, employee: Employee): Observable<Employee> {
    const url = `${this.baseURL}/${id}`;
    return this.httpClient.put<Employee>(url, employee);
  }

  deleteEmployee(id: number){
    const url = `${this.baseURL}/${id}`;
    return this.httpClient.delete<Employee>(url);
  }
}
