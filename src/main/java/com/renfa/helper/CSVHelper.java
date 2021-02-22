package com.renfa.helper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.springframework.web.multipart.MultipartFile;

import com.renfa.exception.FileUploadContentException;
import com.renfa.model.User;

public class CSVHelper {
  public static String[] TYPE = {"text/csv", "application/vnd.ms-excel"};

  public static String[] HEADERS = { "id", "login", "name", "salary" };

  public static boolean hasCSVFormat(MultipartFile file) {

    if (!Arrays.stream(TYPE).anyMatch(file.getContentType()::equals)) {
      return false;
    }

    return true;
  }

  public static List<User> csvToUsers(InputStream is) {
    try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        CSVParser csvParser = new CSVParser(fileReader,
            CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim().withCommentMarker('#'));) {

      List<User> users = new ArrayList<User>();

      Iterable<CSVRecord> csvRecords = csvParser.getRecords();
      
      for (CSVRecord csvRecord : csvRecords) {
        if(csvRecord.size() != HEADERS.length) {
          throw new FileUploadContentException(String.format("Following record has %d data instead of %d: %s", csvRecord.size(), HEADERS.length, csvRecord.toString()));
        }
        User user = new User(
              csvRecord.get(HEADERS[0]),
              csvRecord.get(HEADERS[1]),
              csvRecord.get(HEADERS[2]),
              Float.parseFloat(csvRecord.get(HEADERS[3])));
        
        if(user.getSalary() < 0) {
          throw new FileUploadContentException(String.format("User %s has salary %.2f", user.getName(), user.getSalary()));
        }
        users.add(user);
      }

      if(users.size() == 0){
        throw new FileUploadContentException(String.format("File is empty, please upload new file"));
      }

      return users;
    } catch (FileUploadContentException e) {
      throw new RuntimeException("Fail to parse CSV file due to data error: " + e.getMessage());
    } catch (NumberFormatException e) {
      throw new RuntimeException("Fail to parse CSV file when parsing salary: " + e.getMessage());
    } catch (IOException e) {
      throw new RuntimeException("Fail to parse CSV file: " + e.getMessage());
    } catch (Exception e) {
      throw new RuntimeException("Fail to parse CSV file due to unknown error: " + e.getMessage());
    }
  }

  public static ByteArrayInputStream usersToCSV(List<User> users) {
    final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

    try (ByteArrayOutputStream out = new ByteArrayOutputStream();
      CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
      
      //add Header
      csvPrinter.printRecord(Arrays.asList(HEADERS));

      //add Data
      for (User user : users) {
        List<String> data = Arrays.asList(
              user.getId(),
              user.getLogin(),
              user.getName(),
              String.valueOf(user.getSalary())
            );

        csvPrinter.printRecord(data);
      }

      csvPrinter.flush();
      return new ByteArrayInputStream(out.toByteArray());
    } catch (IOException e) {
      throw new RuntimeException("Fail to import data to CSV file: " + e.getMessage());
    }
  }
}
