package system;

import roads.Roads;
import traffic.Main;

public class QueueThread extends Thread {
    private static int time = 0;
    private static volatile boolean isRunning = true;

    Roads roads;
    String state;

    public QueueThread(String name, String state, Roads roads) {
        super(name);
        this.roads = roads;
        this.state = state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void stopRunning() {
        isRunning = false;
    }

    @Override
    public void run() {
        while (isRunning) {
            if (state.equals("system")) {
                Main.clearConsole();

                System.out.printf("! %ds. have passed since system startup !\n", time);
                System.out.printf("! Number of roads: %d !\n", roads.getSize());
                System.out.printf("! Interval: %d !\n\n", roads.getInterval());

                roads.printRoads();
                System.out.println();

                System.out.println("! Press \"Enter\" to open menu !");
            }

            try {
                //noinspection BusyWait
                sleep(1000L);
            } catch (InterruptedException ignored) {
            }

            if (!roads.isEmpty() && time > 0) {
                roads.updateRoads();
            }

            time++;
        }
    }
}
