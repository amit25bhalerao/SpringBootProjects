package com.example.QnATool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@SpringBootApplication
public class QnAToolApplication {

	public static void main(String[] args) {
		SpringApplication.run(QnAToolApplication.class, args);
		try {
			// Specify the Python script file and Python interpreter (assuming it's in the system PATH).
			String pythonScript = "C:\\Users\\P7113583\\Desktop\\QnATool\\src\\main\\resources\\static\\app.py";
			String pythonInterpreter = "python"; // Use "python3" if you have Python 3.x installed.

			// Build the command to run the Python script.
			String[] command = {pythonInterpreter, pythonScript};

			// Start the process.
			Process process = Runtime.getRuntime().exec(command);

			// Read the output from the Python script.
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}

			// Wait for the process to finish and get the exit code.
			int exitCode = process.waitFor();

			// Handle the exit code (optional).
			if (exitCode == 0) {
				System.out.println("Python script executed successfully.");
			} else {
				System.out.println("Python script execution failed with exit code: " + exitCode);
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}

