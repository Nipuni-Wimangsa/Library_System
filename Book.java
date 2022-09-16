import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Scanner;

public class Book {
    private String title;
    private String author;
    private String category;
    private String ISBN;
    private static final int FINE = 2; //Rs. 2 per day for every late submission

    private Scanner input = new Scanner(System.in);
    private Database database = new Database();

    //get the data of the book that needed to be inserted to the database
    private void getData() {
        System.out.print("book title: ");
        title = input.nextLine();
        System.out.print("author: ");
        author = input.nextLine();
        System.out.print("isbn number: ");
        ISBN = input.nextLine();
        System.out.print("category: ");
        category = input.nextLine();
    }

    //enter the data into the database
    private void insertData() {
        //search whether there already exist a similar book
        String searchSql = "select exists(select * from books where isbn = '"+ISBN+"' " +
                "and title = '"+title+"' and author = '"+author+"') as count";
        ArrayList<String> array = database.getData(searchSql, "count");
        //if there is a similar book array will have a value of 1, if not 0


        if(array.get(0).equals("1")) { //there exists a similar book
            int numberOfCopies = getNumberOfCopies() + 1;

            //updating the number of copies in the database
            database.updateData("update books " +
                    "set number_of_copies = '"+numberOfCopies+"' where isbn = '"+ISBN+"'");
        }

        else { //no similar books
            String sql = "INSERT INTO `books`(title,author,category,isbn,number_of_copies) VALUE ('"+title+"','"+author+"','"+category+"','"+ISBN+"', '1')";
            database.updateData(sql);
        }
    }

    //get the number of copies of a book that exist
    private int getNumberOfCopies() {
        String sql = "select number_of_copies from books where isbn = '"+ISBN+"'";
        ArrayList<String> n_copies = database.getData(sql, "number_of_copies");
        return Integer.parseInt(n_copies.get(0));
    }


    //add books to the system
    public void addBooks() {
        getData();
        insertData();
    }

    //remove books from the system
    public void removeBooks() {
        System.out.print("isbn of the book needed to be removed: ");
        ISBN = input.next();

        int n_copies = getNumberOfCopies();
        String sql = "";

        if(n_copies == 1) sql = "delete from books where isbn = '"+ISBN+"'"; //there is only one book from the same isbn
        else if (n_copies > 1) {
            //there are several books from the same isbn so reduce the n_copies of the book
            n_copies -= 1;
            sql = "update books set number_of_copies = '"+n_copies+"' where isbn = '"+ISBN+"'";
        }
        database.updateData(sql);
    }


    //method to display the information of the particular book user has chosen
    public void viewBookInformation() {
        System.out.println();
        System.out.print("the book you want to view: ");
        title = input.nextLine();

        ArrayList<String> array;
        String sql = "select isbn, title, author, category from books where title = '"+title+"'";

        System.out.println();
        System.out.println("======== information of the book ========");

        array = database.getData(sql,"isbn");
        ISBN = array.get(0);
        System.out.println("isbn of the book: " + ISBN);

        array = database.getData(sql,"title");
        System.out.println("book title: " + array.get(0));

        array = database.getData(sql, "author");
        System.out.println("author of the book: " + array.get(0));

        array = database.getData(sql, "category");
        System.out.println("category of the book: " + array.get(0));

        System.out.println();
        System.out.print("borrow this book (yes/no): ");
        String nextAction = input.nextLine();

        if(nextAction.equals("yes")) borrowBooks();
    }

    //return a borrowed book
    public void returnBooks() {
        String sql = "";
        System.out.println();
        System.out.print("ISBN of the book you want to return: ");
        ISBN = input.nextLine();

        System.out.print("submission date (yyyy-mm-dd): "); //the date the book is returned
        String date = input.nextLine();
        LocalDate submission_date = LocalDate.parse(date);

        System.out.print("your username: ");
        String username = input.nextLine();

        calculateFine(submission_date); //calculate fine if there is any

        sql = "delete from borrowed_books where isbn = '"+ISBN+"' and username = '"+username+"'";
        database.updateData(sql); //delete the book from the borrowed books table


        //increase the number of copies in the books table
        int n_copies = getNumberOfCopies() + 1;
        String numberOfCopies = String.valueOf(n_copies);
        sql = "update books set number_of_copies = '"+numberOfCopies+"' where isbn = '"+ISBN+"'";
        database.updateData(sql);

        System.out.println("thank you for returning the book...");
    }

    //method to calculate the fine
    private void calculateFine(LocalDate submission_date) {
        String sql = "select submission_date from borrowed_books where isbn = '"+ISBN+"'";
        ArrayList<String> array = database.getData(sql, "submission_date");
        LocalDate originalDate = LocalDate.parse(array.get(0)); //date the book had to be submitted

        boolean isAfter = submission_date.isAfter(originalDate); //checking whether the submission is late

        if(isAfter) { //if it is late calculate fine
            long diff = originalDate.until(submission_date, ChronoUnit.DAYS);
            long fine = diff * FINE;
            System.out.println("you have a fine of Rs. " + fine + " for the late submission of the book.");
        }
    }

    //borrow the same book again
    public void renewBooks() {
        System.out.print("ISBN of the book you want to renew: ");
        ISBN = input.nextLine();

        System.out.print("renewal date (yyyy-mm-dd): ");
        String date = input.nextLine();
        LocalDate renewal_date = LocalDate.parse(date); //date the book is again borrowed

        LocalDate submission_date = LocalDate.parse(date).plusDays(7); //date book has to be submitted again

        System.out.print("your username: ");
        String username = input.nextLine();

        calculateFine(renewal_date); //calculate fine if there is any

        String sql = "update borrowed_books " +
                "set borrowed_date = '"+renewal_date+"', submission_date = '"+submission_date+"' " +
                "where isbn = '"+ISBN+"' and username = '"+username+"'";
        database.updateData(sql);

        System.out.println();
        System.out.println("the date you have to return the book: " + submission_date);
        System.out.println("the late submissions would be fined (Rs. 2 per each late day)...");
        System.out.println("have a happy time reading...");
    }

    //borrow books
    private void borrowBooks() {
        int n_copies = getNumberOfCopies();

        //if there is more than one copy of the same book reduce that by one after borrowed
        if(n_copies > 0) {
            System.out.print("enter your username: ");
            String username = input.nextLine();
            System.out.print("borrowing date (yyyy-mm-dd): ");
            String date = input.nextLine();
            LocalDate borrow_date= LocalDate.parse(date);

            LocalDate submission_date = LocalDate.parse(date).plusDays(7);

            String sql = "INSERT INTO `borrowed_books`(isbn,username,borrowed_date,submission_date) VALUE ('"+ISBN+"', '"+username+"', '"+borrow_date+"', '"+submission_date+"')";
            database.updateData(sql);

            n_copies -= 1;
            String numberOfCopies = String.valueOf(n_copies);
            database.updateData("update books set number_of_copies = '"+numberOfCopies+"' where isbn = '"+ISBN+"'");

            System.out.println();
            System.out.println("the date you have to return the book: " + submission_date);
            System.out.println("the late submissions would be fined (Rs. 2 per each late day)...");
            System.out.println("have a happy time reading...");
        }

        else if (n_copies == 0) { //if the books had been already borrowed earlier
            System.out.println("the book is currently unavailable to borrow....");
            System.out.print("would you like to reserve it (yes/no): ");
            String choice = input.nextLine();

            if(choice.equals("yes")) reserveBooks();
            else System.out.println("thank you for visiting the library...");
        }
    }

    //reserve the book for a later date
    private void reserveBooks() {
        String sql = "";
        ArrayList<String> array;

        sql = "select count(*) from reserved_books where isbn = '"+ISBN+"'";
        array = database.getData(sql, "count(*)");
        int n_reservedBooks = Integer.parseInt(array.get(0));

        sql = "select count(*) from borrowed_books where isbn = '"+ISBN+"'";
        array = database.getData(sql, "count(*)");
        int n_borrowedBooks = Integer.parseInt(array.get(0));

        if((n_borrowedBooks - n_reservedBooks) > 0) {
            System.out.print("enter your username: ");
            String username = input.nextLine();
            sql = "INSERT INTO `reserved_books`(isbn,username) VALUE ('"+ISBN+"', '"+username+"')";
            database.updateData(sql);
            System.out.println("the book has been reserved for you!!");
        }
        else System.out.println("sorry for the inconvenience... this book is not available to reserve... visit later...");
    }
}
