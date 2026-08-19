package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        VisitorLog log = new VisitorLog();

        Staff staff1 = new Staff(1, "John Banda", "Sales");
        Staff staff2 = new Staff(2, "Mary Phiri", "HR");
        Staff staff3 = new Staff(3, "James Mwale", "IT");
        Staff[] staffList = { staff1, staff2, staff3 };

        boolean running = true;

        while (running) {
            System.out.println("\n===== Visitor Log Menu =====");
            System.out.println("1. Sign In Visitor");
            System.out.println("2. View Current Visitors");
            System.out.println("3. Sign Out Visitor");
            System.out.println("4. Search by Name");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    System.out.print("Full name: ");
                    String name = scanner.nextLine();
                    System.out.print("Phone number: ");
                    String phone = scanner.nextLine();
                    System.out.print("Purpose of visit: ");
                    String purpose = scanner.nextLine();

                    System.out.println("Who are they visiting?");
                    for (Staff s : staffList) {
                        System.out.println(s.getId() + ". " + s.getName() + " (" + s.getDepartment() + ")");
                    }
                    int staffId = Integer.parseInt(scanner.nextLine());
                    Staff chosenStaff = staffList[staffId - 1];

                    System.out.print("Time in (e.g. 09:30): ");
                    String timeIn = scanner.nextLine();

                    log.signIn(name, phone, purpose, chosenStaff, timeIn);
                    break;

                case 2:
                    log.listCurrentVisitors();
                    break;

                case 3:
                    System.out.print("Enter visitor ID to sign out: ");
                    int visitorId = Integer.parseInt(scanner.nextLine());
                    System.out.print("Time out (e.g. 10:45): ");
                    String timeOut = scanner.nextLine();
                    log.signOut(visitorId, timeOut);
                    break;

                case 4:
                    System.out.print("Enter name to search: ");
                    String searchName = scanner.nextLine();
                    log.searchByName(searchName);
                    break;

                case 5:
                    running = false;
                    System.out.println("Goodbye!");
                    break;

                default:
                    System.out.println("Invalid option, try again.");
            }
        }

        scanner.close();
    }
}