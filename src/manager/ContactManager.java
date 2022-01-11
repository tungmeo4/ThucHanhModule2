package manager;

import Regex.Regex;
import model.Contact;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class ContactManager {
    private final ArrayList<Contact> contacts;
    private final Scanner scanner = new Scanner(System.in);
    private final Regex regex = new Regex();
    public static final String PATH_NAME = "D:\\ThucHanhModule2\\ThucHanhModule2\\src\\contact.csv";

    public ArrayList<Contact> getContactList() {
        return contacts;
    }

    public ContactManager() {
        if (new File(PATH_NAME).length() == 0) {
            this.contacts = new ArrayList<>();
        } else {
            this.contacts = readFile(PATH_NAME);
        }
    }

    public String getGender(int choice) {
        String gender = "";
        switch (choice) {
            case 1:
                gender = "Nam";
                break;
            case 2:
                gender = "Nữ";
                break;
        }
        return gender;
    }

    public void addContact() {
        System.out.println("[Mời bạn nhập thông tin: ");
        System.out.println("--------------------");
        String phoneNumber = enterPhoneNumber();
        System.out.println("▹ Nhập tên nhóm:");
        String group = scanner.nextLine();
        System.out.println("▹ Nhập Họ tên:");
        String name = scanner.nextLine();
        System.out.println("▹ Nhập giới tính:");
        System.out.println("▹ 1. Nam");
        System.out.println("▹ 2. Nữ");
        int gender = Integer.parseInt(scanner.nextLine());
        System.out.println("▹ Nhập địa chỉ:");
        String address = scanner.nextLine();
        System.out.println("▹ Nhập ngày sinh(dd-mm-yyyy):");
        String date = scanner.nextLine();
        LocalDate dateOfBirth = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-LL-yyyy"));
        String email = enterEmail();
        if (getGender(gender).equals("")) {
            System.out.println("Nhập sai lựa chọn, mời nhập lại !");
            return;
        }
        for (Contact phone : contacts) {
            if (phone.getPhoneNumber().equals(phoneNumber)) {
                System.out.println("Số điện thoại bị trùng, mời kiểm tra lại !");
                return;
            }
        }
        Contact contact = new Contact(phoneNumber, group, name, getGender(gender), address, dateOfBirth, email);
        contacts.add(contact);
//        writeFile(contacts, PATH_NAME);
        System.out.println("Đã thêm " + phoneNumber + " vào danh bạ!");
    }

    public void updateContact(String phoneNumber) {
        Contact editContact = null;
        for (Contact contact : contacts) {
            if (contact.getPhoneNumber().equals(phoneNumber)) {
                editContact = contact;
            }
        }
        if (editContact != null) {
            int index = contacts.indexOf(editContact);
            System.out.println("▹ Nhập tên nhóm mới:");
            editContact.setGroup(scanner.nextLine());
            System.out.println("▹ Nhập Họ tên mới:");
            editContact.setName(scanner.nextLine());
            System.out.println("▹ Nhập giới tính mới:");
            System.out.println("▹ 1. Male");
            System.out.println("▹ 2. Female");
            int gender = Integer.parseInt(scanner.nextLine());
            editContact.setGender(getGender(gender));
            System.out.println("▹ Nhập địa chỉ mới:");
            editContact.setAddress(scanner.nextLine());
            System.out.println("▹ Nhập ngày sinh mới(dd-mm-yyyy):");
            String date = scanner.nextLine();
            LocalDate dateOfBirth = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-LL-yyyy"));
            editContact.setBirthDay(dateOfBirth);
            editContact.setEmail(enterEmail());
            contacts.set(index, editContact);
            System.out.println("Sửa " + phoneNumber + " thành công !");
            System.out.println("--------------------");
        } else {
            System.err.println("Không tìm được danh bạ với số điện thoại trên !");
        }
    }

    public void deleteContact(String phoneNumber) {
        Contact deleteContact = null;
        for (Contact contact : contacts) {
            if (contact.getPhoneNumber().equals(phoneNumber)) {
                deleteContact = contact;
            }
        }
        if (deleteContact != null) {
            System.out.println("▹ Nhập xác nhận:");
            System.out.println("▹ Y: Xóa");
            System.out.println("▹ Ký tự khác: Thoát");
            String confirm = scanner.next();
            if (confirm.equalsIgnoreCase("y")) {
                contacts.remove(deleteContact);
                System.out.println("Xóa " + phoneNumber + " thành công !");
            }
        } else {
            System.err.println("Không tìm thấy danh bạ với số điện thoại trên !");
        }
    }

    public void searchContactByNameOrPhone(String keyword) {
        ArrayList<Contact> contacts = new ArrayList<>();
        for (Contact contact : contacts) {
            if (regex.validatePhoneOrName(keyword, contact.getPhoneNumber()) || regex.validatePhoneOrName(keyword, contact.getName())) {
                contacts.add(contact);
            }
        }
        if (contacts.isEmpty()) {
            System.err.println("Không tìm thấy danh bạ với từ khóa trên !");
        } else {
            System.out.println("Danh bạ cần tìm:");
            contacts.forEach(System.out::println);
            System.out.println("--------------------");
        }
    }

    public void displayAll() {
        System.out.printf("| %-15s| %-10s| %-15s| %-10s| %-10s|\n", "Số điện thoại", "Nhóm", "Họ tên", "Giới tính", "Địa chỉ");
        for (Contact contact : contacts) {
            System.out.printf("| %-15s| %-10s| %-15s| %-10s| %-10s|\n", contact.getPhoneNumber(), contact.getGroup(), contact.getName(), contact.getGender(), contact.getAddress());
        }
    }

    public String enterPhoneNumber() {
        String phoneNumber;
        while (true) {
            System.out.print("▹ Nhập số điện thoại: ");
            String phone = scanner.nextLine();
            if (!regex.validatePhone(phone)) {
                System.out.println("Số điện thoại không hợp lệ !!!");
                System.out.println(">[Chú ý]: Số điện thoại phải có từ 8 - 11 số và bắt đầu từ số 0");
                System.out.println("--------------------");
            } else {
                phoneNumber = phone;
                break;
            }
        }
        return phoneNumber;
    }

    private String enterEmail() {
        String email;
        while (true) {
            System.out.print("▹ Nhập email: ");
            String inputEmail = scanner.nextLine();
            if (!regex.validateEmail(inputEmail)) {
                System.err.println("Email không hợp lệ !!!");
                System.out.println("Email phải có dạng: tungmeo4@yahoo.com/ tungmeo4@gmail.vn/...");
            } else {
                email = inputEmail;
                break;
            }
        }
        return email;
    }

    public void writeFile(ArrayList<Contact> contactList, String path) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path));
            for (Contact contact : contactList) {
                bufferedWriter.write(contact.getPhoneNumber() + "," + contact.getGroup() + "," + contact.getName() + "," + contact.getGender() + ","
                        + contact.getAddress() + "," + contact.getBirthDay() + "," + contact.getEmail() + "\n");
            }
            bufferedWriter.close();
            System.out.println("Ghi thành công!");
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }

    public ArrayList<Contact> readFile(String path) {
        ArrayList<Contact> contacts = new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] strings = line.split(",");
                contacts.add(new Contact(strings[0], strings[1], strings[2], strings[3], strings[4], LocalDate.parse(strings[5], DateTimeFormatter.ISO_LOCAL_DATE), strings[6]));
            }
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
        return contacts;
    }

}
