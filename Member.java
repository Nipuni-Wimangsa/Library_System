import java.util.Scanner;

public class Member extends User {
    private Scanner input = new Scanner(System.in);
    private Book book = new Book();

    //main portal for a normal member of library system
    public void memberMainMenu() {
        System.out.println();
        System.out.println("welcome to the member portal...");
        System.out.println("1. search for a book.");
        System.out.println("2. return a book.");
        System.out.println("3. renew a book.");
        System.out.println("4. log out.");
        System.out.print("choose your option: ");
        int option = input.nextInt();

        switch (option) {
            case 1: //search a book
                searchCatalog("member");
                break;
            case 2: //return a book
                book.returnBooks();
                break;
            case 3: //renew a book (borrow the same book again)
                book.renewBooks();
                break;
            case 4: //log out
                loggingOut();
                break;
            default:
                choosingWrongOption();
                memberMainMenu();
                break;
        }
    }
}
