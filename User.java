import java.util.ArrayList;
import java.util.Scanner;

public class User {
    private Scanner input = new Scanner(System.in);
    private Database database = new Database();
    private Book book = new Book();

    private String username;
    private String password;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String memberType;

    //method to create the account of a new user
    public void createAccount() {
        getData(); //get the user inputs
        insertData(); //insert them into the database
    }

    //method to log users into the system
    public void loginToAccount() {
        //users need the correct username and password for the login
        System.out.println();
        System.out.print("Enter username: ");
        username = input.next();
        loginUserNameValidation(); //validating whether there is a user from that username

        System.out.print("Enter password: ");
        password = input.next();
        passwordValidation(); //validating whether the entered password matches with the username
    }

    //method to check whether to see the logged in user is an admin or a member
    public String getMemberType() {
        String sql = "select memberType from user where username = '"+username+"'";
        ArrayList<String> array = database.getData(sql,"memberType");
        return array.get(0);
    }


    //method to search through the book list and return the book title
    public void searchCatalog(String member) {
        System.out.println();
        System.out.print("search by isbn/title/author/category: "); //get the search keyword
        String keyword = input.nextLine();

        //print the titles of the books that user has searched according to the keyword
        String sql = "select title from books" +
                " where isbn like '%"+keyword+"%' or title like '%"+keyword+"%' " +
                "or author like '%"+keyword+"%' or category like '%"+keyword+"%'";

        System.out.println();
        System.out.println("======== search results ========");
        ArrayList<String> array = database.getData(sql,"title");
        System.out.println(array.toString());

        searchSideMenu(member); //a side menu to choose what to do after the search results
    }


    //method to prompt a side menu after search results
    private void searchSideMenu(String member) {
        System.out.println();
        System.out.println("1. choose a book."); //for viewing more information about the book
        System.out.println("2. search again.");
        System.out.println("3. go to main menu.");
        System.out.println("4. log out.");
        System.out.print("your option: ");
        int option = Integer.parseInt(input.nextLine());

        Admin admin = new Admin();
        Member member1 = new Member();

        switch (option) {
            case 1: //choose a book
                book.viewBookInformation(); //view the information of the chosen book
                break;
            case 2: //search again
                searchCatalog(member);
                break;
            case 3: //go to main menu
                if(member.equals("admin")) admin.adminMainMenu();
                else member1.memberMainMenu();
                break;
            case 4: //log out
                loggingOut();
                break;
            default:
                choosingWrongOption();
                searchSideMenu(member);
                break;
        }
    }

    //method to see whether the entered username is correct or exist in the database (for login purpose)
    private void loginUserNameValidation() {
        String sql = "select count(1) from user where username = '"+username+"'"; //sql query for searching the matching username
        ArrayList<String> array = database.getData(sql,"count(1)");

        /*
           array get only one data item either 1 or 0
           if it is 1 -> there is a data field registered with that username (user has successfully entered the username)
           if it is 0 -> there is no data field registered with that username (user has entered the wrong username)
        */

        if(array.get(0).equals("0")) {
            System.out.println("wrong username... try again!");
            System.out.println();
            System.out.print("Enter username: "); //since the first entered username was wrong prompt to enter another username
            username = input.next();
            loginUserNameValidation(); //check the new entered username is correct
        }
    }

    //method to see whether the entered password is correct (match with the username)
    private void passwordValidation() {
        String sql = "select password from user where username = '"+username+"'";
        ArrayList<String> array = database.getData(sql,"password"); //get the password that match with the entered username

        if(!array.get(0).equals(password)) printWrongPasswordOptionList(); //an option list for user if the entered password is wrong

    }


    //option list for the users who entered the wrong password
    private void printWrongPasswordOptionList() {
        System.out.println();
        System.out.println("entered password is wrong...");
        System.out.println("1. enter the password again.");
        System.out.println("2. reset the password.");
        System.out.println("3. exit the library system.");
        System.out.print("Your option: ");
        int option = input.nextInt();

        switch (option) {
            case 1: //enter the password again
                System.out.print("Enter Password: ");
                password = input.next();
                passwordValidation(); //check the correction of the password
                break;
            case 2: //reset the password
                resetPassword();
                break;
            case 3: //exit the library system
               loggingOut();
            default: //entered the wrong option number
                choosingWrongOption();
                printWrongPasswordOptionList(); //prompt the option list again
                break;
        }
    }


    //method to reset the password
    private void resetPassword() {
        System.out.println();
        System.out.print("Enter the new password: ");
        password = input.next();

        String sql = "update user set password = '"+password+"' where username = '"+username+"'";
        database.updateData(sql);
        System.out.println("successfully updated the password! login to your account...");
        loginToAccount();
    }

    //method to get user inputs while registering (signup) to the system
    private void getData() {
        System.out.print("name: ");
        name = input.next();
        System.out.print("phone number: ");
        phone = input.next();
        System.out.print("email: ");
        email = input.next();
        System.out.print("address: ");
        address = input.next();
        System.out.print("username: ");
        username = input.next();
        registerUserNameValidation(); //checking whether the entered username is already taken

        System.out.print("password: ");
        password = input.next();
        System.out.print("user type: ");
        memberType = input.next();
    }

    //method to insert user data into the database
    private void insertData() {
        String sql = "INSERT INTO `user`(username,password,name,phone,email,address,memberType) " +
                "VALUE ('"+username+"','"+password+"','"+name+"','"+phone+"','"+email+"','"+address+"','"+memberType+"')";
        database.updateData(sql);
    }

    //checking whether the entered username is already taken
    private void registerUserNameValidation() {
        String sql = "select count(1) from user where username = '"+username+"'";
        ArrayList<String> array = database.getData(sql,"count(1)");

        /*
           array get only one data item either 1 or 0
           if it is 1 -> there is a data field registered with that username (new user can't use that username)
           if it is 0 -> there is no data field registered with that username (new user can use the entered username without a problem)
        */

        if(array.get(0).equals("1")) {
            System.out.println("there is already an account from that username... try again!");
            System.out.print("Enter username: ");
            username = input.next();
            registerUserNameValidation();
        }
    }

    //exit from the system
    protected void loggingOut() {
        System.out.println();
        System.out.println("logging you out...");
        System.exit(0);
    }

    //user has entered the wrong option that is not in the mentioned list.
    protected void choosingWrongOption() {
        System.out.println();
        System.out.println("entered option is wrong... choose again.");
    }

}
