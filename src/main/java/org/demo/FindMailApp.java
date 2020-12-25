package org.demo;

import org.apache.commons.validator.routines.EmailValidator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FindMailApp {

    static EmailValidator emailValidator = EmailValidator.getInstance(true);

    public static void main(String[] args) throws Exception {
        System.out.println("[INFO] Program started ...");
        long duration = -System.currentTimeMillis();

        List<String> emails = readDataFromFile("emails.data");
        processEmails(emails);
        duration += System.currentTimeMillis();

        System.out.println(String.format("[INFO] Elapsed time %d msec", duration));
        System.out.println("[INFO] Program finished ...");
    }

    static List<String> readDataFromFile(String fileName) throws Exception {
        System.out.println("[INFO] Reading data from file started ...");

        List<String> emails = new ArrayList<>();

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream(fileName);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.isBlank() && !line.isEmpty()) {
                    emails.add(line);
                }

            }
        } catch (IOException ex) {
            //
        }

        System.out.println("[INFO] Reading data from file finished ...");
        return emails;
    }

    static void processEmails(List<String> emailList) {
        emailList.stream()
                .forEach((String email) -> System.out.println(String.format("%s is valid ? : %s", email,
                        emailValidator.isValid(email))));
    }

}
