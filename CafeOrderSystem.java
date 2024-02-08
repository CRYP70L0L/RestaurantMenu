//Alyaan Mir and Diraj Grewal
//CIS-18A Final Project Option 1: Restaurant Menu
//Febuaray 6 2023
//Professor Kasey Nguyen

// Imported Packages
import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;

// Interface for customizable items
interface Customizable {
    int getUserInput(String message);
}

// Interface for menu items
interface MenuItem {
    String getName(); // Method to get the name of the menu item
    float processItem();
}

// Base class for menu items
class BaseMenuItem implements MenuItem {
    String name; // Name of menu is regarded as a string
    float cost; // The original cost of each item on the menu is held in a float
    String[] addOns; // Add-ons are held in an array
    float addOnCost; // The cost of each add-on is also head in a float

    // This constructor initializes the properties of the menu items
    BaseMenuItem(String name, float cost, String[] addOns, float addOnCost) {
        // These assign values to the properties based on the constructor parameters
        this.name = name;
        this.cost = cost;
        this.addOns = addOns;
        this.addOnCost = addOnCost;
    }

    // This is process method records user input and totals the cost of everything they order including menu items and their add-ons
    public float processItem() {
        float total = cost;
        for (String addOn : addOns) {
            int choice = getUserInput(addOn + "?\n1. Yes\n2. No");
            if (choice == 1) {
                total += addOnCost;
            }
        }
        return total;
    }

    // This gets the User Input by asking the user Yes/No questions for the food they order along with any possible add-ons
    public int getUserInput(String message) {
        return JOptionPane.showOptionDialog(
                null,
                message,
                "Customization",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                null
        ) + 1;
    }

    // Implementation of the getName method from the MenuItem interface
    public String getName() {
        return name;
    }
}

// Subclass representing a specific menu item
class CafeMenuItem extends BaseMenuItem {
    // Constructor for CafeMenuItem
    CafeMenuItem(String name, float cost, String[] addOns, float addOnCost) {
        super(name, cost, addOns, addOnCost);
    }
}

// The ordering system for the Cafe is organized in this public class
public class CafeOrderSystem {
    //This is what the user first sees which is the menu asking for their name
    public static void main(String[] args) {
        String customerName = JOptionPane.showInputDialog("Please enter your name:");
        if (customerName == null || customerName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Invalid name. Exiting program.");
            System.exit(0);
        }

        int choice;
        float total = 0.0f;
// This is a welcome message the user will see when the program starts
        JOptionPane.showMessageDialog(null, "Welcome to the Java Cafe, " + customerName + "!");
// When user is done with the order, the program will write a text file named order summary that shows what the user order and total cost
        try (FileWriter writer = new FileWriter("order_summary.txt")) {
            writer.write("Customer Name: " + customerName + "\n\n");

            do { //The do statement here shows the options the user can click on
                choice = getMenuChoice();

                while (!isValidChoice(choice)) {
                    JOptionPane.showMessageDialog(null, "Invalid choice. Please enter a number between 1 and 5.");
                    choice = getMenuChoice();
                }

                if (choice != 5) {
                    MenuItem menuItem = new CafeMenuItem(getMenuItemName(choice), getMenuItemCost(choice), getMenuItemAddOns(choice), getMenuItemAddOnCost(choice));
                    total += menuItem.processItem();

                    writer.write("Item: " + menuItem.getName() + "\n"); // Use getName() instead of directly accessing 'name'
                    writer.write("Add-ons: " + String.join(", ", ((BaseMenuItem) menuItem).addOns) + "\n");
                    writer.write("Cost: $" + String.format("%.2f", ((BaseMenuItem) menuItem).cost) + "\n\n");
                }

            } while (choice != 5);

            JOptionPane.showMessageDialog(null, "Thank you for visiting Java Cafe, " + customerName + "!\nYour total for all items ordered is $" + String.format("%.2f", total));
            writer.write("Total Cost: $" + String.format("%.2f", total) + "\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static boolean isValidChoice(int input) { // This checks if the user chose something valid
        return (input >= 1 && input <= 5);
    }

    static int getMenuChoice() {
        Object[] options = {"Pizza", "Chicken Sandwich", "Burger", "Ice Cream", "Quit"};
        int choice = JOptionPane.showOptionDialog(
                null,
                "Choose a menu item:",
                "Menu",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );
        return choice + 1;
    }


    static String getMenuItemName(int choice) { // This is how the menu can get which menu item the user wants and uses a switch statement
        switch (choice) {
            case 1:
                return "Pizza";
            case 2:
                return "Chicken Sandwich";
            case 3:
                return "Burger";
            case 4:
                return "Ice Cream";
            default:
                return "";
        }
    }

    // This method has the cost of the menu items and whatever the user chooses will be added to total
    static float getMenuItemCost(int choice) {
        switch (choice) {
            case 1:
                return 7.99f;
            case 2:
                return 8.99f;
            case 3:
                return 6.99f;
            case 4:
                return 3.99f;
            default:
                return 0.0f;
        }
    }

    //This method is what add-ons the user will see depending on what menu item they chose
    static String[] getMenuItemAddOns(int choice) {
        switch (choice) {
            case 1:
                return new String[]{"Veggies", "Pepperoni", "Cheese"};
            case 2:
                return new String[]{"Cheese", "Bacon", "BBQ Sauce"};
            case 3:
                return new String[]{"Fries", "Tomatoes", "Onions", "Lettuce"};
            case 4:
                return new String[]{"Sprinkles", "Whipped Cream", "Cherry"};
            default:
                return new String[0];
        }
    }

    static float getMenuItemAddOnCost(int choice) { // This is the fixed cost for add-ons that will eventually be added into the total
        return 1.99f;
    }
}
