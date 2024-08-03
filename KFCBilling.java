import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class KFCBilling {

    public static void main(String[] args) {
        List<String> menu = new ArrayList<>();
        List<String> items = new ArrayList<>();
        List<Double> prices = new ArrayList<>();
        double total = 0;
        boolean adminAccess = false;

        // Reading the menu from the file
        try (BufferedReader br = new BufferedReader(new FileReader("menu.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                menu.add(line);
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Reading the admin credentials
        String adminUser = "";
        String adminPassword = "";
        try (BufferedReader br = new BufferedReader(new FileReader("password.txt"))) {
            adminUser = br.readLine().trim();
            adminPassword = br.readLine().trim();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter your order (or 'stop' to finish): ");
            String userOrder = scanner.nextLine().trim();

            if (userOrder.equalsIgnoreCase("stop")) {
                break;
            }

            if (userOrder.equals(adminUser)) {
                System.out.print("Enter your password: ");
                String password = scanner.nextLine().trim();
                if (password.equals(adminPassword)) {
                    adminAccess = true;
                    System.out.println("You have admin access!");

                    System.out.println("Enter your action (adding item, removing item, change price only): ");
                    String action = scanner.nextLine().trim().toLowerCase();

                    if (action.equals("adding item")) {
                        System.out.println("Enter the category (Burgers, Chicken, Rice Bowlz, Snacks, Drinks): ");
                        String category = scanner.nextLine().trim();
                        boolean categoryFound = false;

                        for (int i = 0; i < menu.size(); i++) {
                            if (menu.get(i).trim().equalsIgnoreCase(category)) {
                                categoryFound = true;
                                System.out.println("Enter the name of the new item: ");
                                String newItem = scanner.nextLine().trim();
                                System.out.println("Enter the price of the new item: ");
                                String newPrice = scanner.nextLine().trim();
                                menu.add(i + 1, newItem + "     Rs " + newPrice);
                                break;
                            }
                        }

                        if (!categoryFound) {
                            System.out.println("Category not found!");
                        }
                    } else if (action.equals("removing item")) {
                        System.out.println("Enter the name of the item to remove: ");
                        String itemToRemove = scanner.nextLine().trim().toLowerCase();
                        boolean itemFound = false;

                        for (int i = 0; i < menu.size(); i++) {
                            if (menu.get(i).toLowerCase().startsWith(itemToRemove)) {
                                itemFound = true;
                                menu.remove(i);
                                System.out.println("Item removed successfully!");
                                break;
                            }
                        }

                        if (!itemFound) {
                            System.out.println("Item not found!");
                        }
                    } else if (action.equals("change price only")) {
                        System.out.println("Enter the name of the item to change price: ");
                        String itemToChange = scanner.nextLine().trim().toLowerCase();
                        boolean itemFound = false;

                        for (int i = 0; i < menu.size(); i++) {
                            if (menu.get(i).toLowerCase().startsWith(itemToChange)) {
                                itemFound = true;
                                System.out.println("Enter the new price: ");
                                String newPrice = scanner.nextLine().trim();
                                String[] parts = menu.get(i).split("Rs");
                                menu.set(i, parts[0] + "Rs " + newPrice);
                                System.out.println("Price updated successfully!");
                                break;
                            }
                        }

                        if (!itemFound) {
                            System.out.println("Item not found!");
                        }
                    }

                    // Writing updated menu back to file
                    try (BufferedWriter bw = new BufferedWriter(new FileWriter("menu.txt"))) {
                        for (String menuItem : menu) {
                            bw.write(menuItem);
                            bw.newLine();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    System.out.println("Incorrect password!");
                }
            } else {
                boolean itemFound = false;
                for (String menuItem : menu) {
                    if (menuItem.toLowerCase().startsWith(userOrder.toLowerCase())) {
                        String[] parts = menuItem.split("Rs");
                        items.add(userOrder);
                        double price = Double.parseDouble(parts[1].trim());
                        prices.add(price);
                        total += price;
                        itemFound = true;
                        break;
                    }
                }
                if (!itemFound) {
                    System.out.println(userOrder + " is not in the menu list or is empty.");
                }
            }
        }

        if (!adminAccess) {
            System.out.println("\nYour Bill");
            System.out.println("----------------------------------------------");
            System.out.println("---------------------KFC----------------------");
            System.out.println("Item                                     Price");
            for (int i = 0; i < items.size(); i++) {
                String item = items.get(i);
                double price = prices.get(i);
                System.out.printf("%-40s %.2f\n", item, price);
            }
            System.out.printf("\nTotal: %37.2f\n", total);
            System.out.println("----------------------------------------------");
        }

        scanner.close();
    }
}
