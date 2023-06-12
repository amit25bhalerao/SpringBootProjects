package com.example.XMLtoDatabase.controller;

import com.example.XMLtoDatabase.entity.StudentEntity;
import com.example.XMLtoDatabase.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/students")
public class StudentController {
    private final StudentRepository studentRepository;

    @Autowired
    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @PostMapping("/import")
    public void importStudents(MultipartFile file) throws ParserConfigurationException, IOException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        InputStream inputStream = file.getInputStream();
        org.w3c.dom.Document document = null;
        try {
            document = dBuilder.parse(inputStream);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
        document.getDocumentElement().normalize();
        org.w3c.dom.NodeList nodeList = document.getElementsByTagName("Student");
        for (int i = 0; i < nodeList.getLength(); i++) {
            org.w3c.dom.Node node = nodeList.item(i);
            if (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                org.w3c.dom.Element element = (org.w3c.dom.Element) node;
                StudentEntity student = new StudentEntity();
                student.setFirstName(element.getElementsByTagName("firstName").item(0).getTextContent());
                student.setLastName(element.getElementsByTagName("lastName").item(0).getTextContent());
                student.setEmail(element.getElementsByTagName("email").item(0).getTextContent());
                studentRepository.save(student);
            }
        }
        System.out.println("Data Stored Successfully!");
    }
}
