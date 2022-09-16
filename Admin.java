import java.util.Scanner;

public class Admin extends User {
    private Scanner input = new Scanner(System.in);
    private Database database = new Database();
    private Book book = new Book();
    private int adminMainPortalNumber = 0;

    //admin main portal - works admin can do in the system
    public void adminMainMenu() {
        System.out.println();
        System.out.println("welcome to the admin portal...");
        System.out.println("1. register a new user.");
        System.out.println("2. remove an old user.");
        System.out.println("3. add a new book.");
        System.out.println("4. remove a book.");
        System.out.println("5. search for a book.");
        System.out.println("6. log out.");
        System.out.print("choose your option: ");
        adminMainPortalNumber = input.nextInt();

        adminPortal();
    }


    //method to add new users from admin portal
    public void addUsers(){
        System.out.println();
        System.out.println("======== registering a new user ========");
        createAccount();
        System.out.println();
        System.out.println("The user has been added successfully!");
        sideMenu("add another user."); //after adding the user prompt a menu to choose for future works
    }


    //method to remove users from the system
    public void removeUsers(){
        System.out.println();
        System.out.println("======== removing an old user ========");
        System.out.print("username of the person: ");
        String user = input.next();

        String sql = "delete from user where username = '"+user+"'";
        database.updateData(sql);

        System.out.println();
        System.out.println("the user has been successfully removed!");
        sideMenu("remove another user."); //after removing the user prompt a menu to choose for future works
    }


    //method to add books to the system
    public void addBooks(){
        System.out.println();
        System.out.println("======== adding a new book to the system ========");
        book.addBooks();
        System.out.println();
        System.out.println("the book has been added to the system...");
        sideMenu("add another book."); //after adding the book prompt a menu to choose for future works
    }


    //method to remove books from the system
    public void removeBooks(){
        System.out.println();
        System.out.println("======== removing a book from system ========");
        book.removeBooks();
        System.out.println();
        System.out.println("the book has been successfully deleted!");
        sideMenu("remove another book."); //after removing the book prompt a menu to choose for future works
    }


    //method to let user choose what to do after doing the main job from the admin portal
    private void sideMenu(String op1) {
        System.out.println("1. " + op1); //do the same thing again
        System.out.println("2. go to main menu."); //go to the main portal once again
        System.out.println("3. log out.");
        System.out.print("your option: ");
        int option = input.nextInt();

        switch (option) {
            case 1:
                adminPortal(); //do the same thing again (same as the main admin portal option number)
                break;
            case 2:
                adminMainMenu();
                break;
            case 3:
                loggingOut();
                break;
            default:
                choosingWrongOption();
                sideMenu(op1);
                break;
        }
    }

    /* 1-> add users, 2-> remove users, 3-> add books, 4-> remove books 5-> search books */
    private void adminPortal() {
        switch (adminMainPortalNumber) {
            case 1: //adding new users to the system
                addUsers();
                break;
            case 2: //remove users from the system
                removeUsers();
                break;
            case 3: //add books to the system
                addBooks();
                break;
            case 4: //remove books from the system
                removeBooks();
                break;
            case 5: //search the books in the system
                searchCatalog("admin");
                break;
            case 6: //log out
                loggingOut();
                break;
            default:
                choosingWrongOption();
                adminMainMenu();
                break;
        }
    }
}
