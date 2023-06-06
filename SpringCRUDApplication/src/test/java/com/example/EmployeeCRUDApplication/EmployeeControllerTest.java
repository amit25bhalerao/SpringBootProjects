package com.example.EmployeeCRUDApplication;

import com.example.EmployeeCRUDApplication.connect.DefineBook;
import com.example.EmployeeCRUDApplication.connect.EmployeeBookConnect;
import com.example.EmployeeCRUDApplication.controller.EmployeeController;
import com.example.EmployeeCRUDApplication.model.Employee;
import com.example.EmployeeCRUDApplication.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class EmployeeControllerTest {

	private EmployeeController employeeController;

	@Mock
	private EmployeeRepository employeeRepositoryMock;

	@Mock
	private EmployeeBookConnect employeeBookConnectMock;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		employeeController = new EmployeeController(employeeRepositoryMock, employeeBookConnectMock);
	}

	@Test
	void testFetchEmployeeAndBookDetailsSuccess() {
		int userId = 1;
		int bookId = 2;

		Employee employee = new Employee();
		employee.setId((long) userId);

		DefineBook book = new DefineBook();
		book.setId((long) bookId);

		when(employeeRepositoryMock.findById((long) userId)).thenReturn(Optional.of(employee));
		when(employeeBookConnectMock.fetchBookDetails(bookId)).thenReturn(
				Mono.just(book)
		);

		ResponseEntity<String> response = employeeController.fetchEmployeeAndBookDetails(userId, bookId);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Employee details: " + employee.toString() + "\nBook details: " + book.toString(), response.getBody());

		verify(employeeRepositoryMock, times(1)).findById((long) userId);
		verify(employeeBookConnectMock, times(1)).fetchBookDetails(bookId);
	}
}
