package com.example.FileUploadApplication.controller;

import com.example.FileUploadApplication.entity.AccountDetails;
import com.example.FileUploadApplication.repository.AccountRepository;
import com.univocity.parsers.common.record.Record;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
public class BankController {

    @Autowired
    AccountRepository service;
    @PostMapping("/upload")
    public String uploadData(@RequestParam("file")MultipartFile file) throws Exception {
        List<AccountDetails> accountList = new ArrayList<>();
        InputStream inputStream = file.getInputStream();
        CsvParserSettings setting = new CsvParserSettings();
        setting.setHeaderExtractionEnabled(true);
        CsvParser parser = new CsvParser(setting);
        List<Record> parseAllRecords = parser.parseAllRecords(inputStream);
        parseAllRecords.forEach(record -> {
            AccountDetails account = new AccountDetails();
            account.setAccountNumber(record.getString("account_number"));
            account.setFirstName(record.getString("first_name"));
            account.setLastName(record.getString("last_name"));
            account.setAccountType(record.getString("account_type"));
            account.setEmailId(record.getString("email_id"));
            account.setAddress(record.getString("person_address"));
            account.setZipCode(record.getString("zip_code"));
            accountList.add(account);
        });
        service.saveAll(accountList);
        return "Data Uploaded Successfully";
    }
}
