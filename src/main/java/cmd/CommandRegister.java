package cmd;

import client.UserSession;

import java.io.IOException;
import java.util.Scanner;

public class CommandRegister implements Command, Preparable, Registerable {

    private String login;
    private String password;
    private static final long serialVersionUID = 1337000050L;

    @Override
    public String execute(String[] args) throws IOException {
        prepare(args);
        return "Approved";
    }

    @Override
    public void prepare(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter login:\n>");
        login = scanner.next().trim();
        System.out.print("Enter password:\n>");
        password = scanner.next().trim();
    }
}
