import java.util.Scanner;

public class Main {
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        User user = new User();
        Admin admin = new Admin();
        Member member = new Member();

        String loginStatus = welcomeMessage(); //get the login or signup status

        if(loginStatus.equals("login")) {
            user.loginToAccount(); //user would log into their account
            System.out.println("successfully logged in to your account...");
        }
        else if(loginStatus.equals("signup")) {
            System.out.println();
            System.out.println("======== creating a new account ========");
            user.createAccount();
            System.out.println("your account has been successfully created! login to carry on...");
            user.loginToAccount();
            System.out.println("successfully logged in to your account...");
        }

        String memberType = user.getMemberType();
        if(memberType.equals("admin")) {
            admin.adminMainMenu();
        } else if (memberType.equals("member")) {
            member.memberMainMenu();
        }
    }


    //method to welcome the users to the interface
    private static String welcomeMessage() {
        Library library = new Library("example library");
        System.out.println();
        System.out.println("======== welcome to the " + library.getLibraryName() + " ========");
        System.out.println("login or signup to continue...");
        return input.next();
    }

}
