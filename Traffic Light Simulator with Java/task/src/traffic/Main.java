package traffic;

import java.io.IOException;
import java.util.Scanner;

import roads.Roads;
import system.QueueThread;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Welcome to the traffic management system!");

        System.out.print("Input the number of roads: ");
        int roads;
        while (true) {
            String roadsInput = sc.nextLine().trim();
            try {
                roads = Integer.parseInt(roadsInput);
                if (roads > 0) {
                    break;
                } else {
                    System.out.print("Error! Incorrect Input. Try again: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Error! Incorrect Input. Try again: ");
            }
        }

        System.out.print("Input the interval: ");
        int interval;
        while (true) {
            String intervalInput = sc.nextLine().trim();
            try {
                interval = Integer.parseInt(intervalInput);
                if (interval > 0) {
                    break;
                } else {
                    System.out.print("Error! Incorrect Input. Try again: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Error! Incorrect Input. Try again: ");
            }
        }

        Roads roadsQueue = new Roads(roads, interval);

        QueueThread system = new QueueThread("QueueThread", "idle", roadsQueue);
        system.start();

        boolean isMenuOpen = true;
        while (isMenuOpen) {
            System.out.println("""
                    Menu:
                    1. Add road
                    2. Delete road
                    3. Open System
                    0. Quit""");

            int option;
            String optionInput = sc.nextLine().trim();
            try {
                option = Integer.parseInt(optionInput);

                switch (option) {
                    case 1:
                        System.out.print("Input road name: ");
                        String roadToAdd = sc.nextLine().trim();
                        roadsQueue.enqueue(roadToAdd);
                        sc.nextLine();
                        clearConsole();
                        break;
                    case 2:
                        roadsQueue.dequeue();
                        sc.nextLine();
                        clearConsole();
                        break;
                    case 3:
                        system.setState("system");
                        sc.nextLine();
                        system.setState("idle");
                        clearConsole();
                        break;
                    case 0:
                        System.out.println("Bye!");
                        isMenuOpen = false;
                        break;
                    default:
                        System.out.println("Incorrect option");
                        sc.nextLine();
                        clearConsole();
                        break;
                }

            } catch (NumberFormatException e) {
                System.out.println("Incorrect option");
                sc.nextLine();
            }
        }

        system.stopRunning();
        try {
            system.join();
        } catch (InterruptedException ignored) {
        }
    }

    public static void clearConsole() {
        try {
            var clearCommand = System.getProperty("os.name").contains("Windows")
                    ? new ProcessBuilder("cmd", "/c", "cls")
                    : new ProcessBuilder("clear");
            clearCommand.inheritIO().start().waitFor();
        } catch (IOException | InterruptedException ignored) {
        }
    }
}
