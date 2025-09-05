package roads;

enum AnsiCodes {
    ANSI_RED("\u001B[31m"),
    ANSI_GREEN("\u001B[32m"),
    ANSI_RESET("\u001B[0m");

    private final String ansiCode;

    AnsiCodes(String ansiCode) {
        this.ansiCode = ansiCode;
    }

    public String getAnsiCode() {
        return ansiCode;
    }
}


public class Roads {
    private final int interval;
    private int front = 0;
    private int rear = 0;
    private final int size;
    private final Road[] roads;
    private static int currentTime = 0;
    private static int currRoadIdx = 0;
    private static int count = 0;

    public Roads(int size, int interval) {
        this.size = size;
        this.roads = new Road[size];
        this.interval = interval;
    }

    public int getSize() {
        return size;
    }

    public int getInterval() {
        return interval;
    }

    public void enqueue(String elem) {
        if (isFull()) {
            System.out.println("Queue is full");
            return;
        }

        Road road = new Road(elem, isEmpty() ? "open" : "closed");
        roads[rear] = road;
        road.distance = getDistFromCurr(road);
        rear = incrementPointer(rear);
        count++;
        System.out.printf("%s Added!\n", elem);
    }

    public void dequeue() {
        if (isEmpty()) {
            System.out.println("Queue is empty");
            return;
        }

        String temp = roads[front].name;
        roads[front] = null;
        front = incrementPointer(front);
        count--;
        System.out.printf("%s deleted!\n", temp);

        if (count == 1) {
            roads[front].state = "open";
        }
    }

    private boolean isFull() {
        for (Road n : roads) {
            if (n == null) {
                return false;
            }
        }

        return true;
    }

    public boolean isEmpty() {
        for (Road n : roads) {
            if (n != null) {
                return false;
            }
        }

        return true;
    }

    private int incrementPointer(int pointer) {
        return (pointer + 1) % size;
    }

    public void printRoads() {
        for (Road road : roads) {
            if (road != null) {
                System.out.printf("Road \"%s\" will be " +
                                getStringColor(road.state) +
                                "%s for %ds." +
                                getStringColor("reset") +
                                "\n",
                        road.name,
                        road.state,
                        road.state.equals("open") ?
                                interval - currentTime :
                                interval * road.distance - currentTime
                );
            }
        }
    }

    private String getStringColor(String roadState) {
        return switch (roadState) {
            case "open" -> AnsiCodes.ANSI_GREEN.getAnsiCode();
            case "closed" -> AnsiCodes.ANSI_RED.getAnsiCode();
            default -> AnsiCodes.ANSI_RESET.getAnsiCode();
        };
    }

    private int getDistFromCurr(Road road) {
        int i = currRoadIdx;
        int distanceFromCurr = 0;

        while (!isEmpty()) {
            i = incrementPointer(i);

            if (roads[i] != null) {
                distanceFromCurr++;
                if (roads[i] == road) {
                    break;
                }
            }
        }

        return distanceFromCurr;
    }

    public void updateRoads() {

        currentTime++;

        if (count == 1) {
            currRoadIdx = front;
        } else if (count == 0) {
            return;
        }

        if (currentTime % interval == 0) {
            Road currRoad = roads[currRoadIdx];

            int nextRoadIdx = incrementPointer(currRoadIdx);
            if (roads[nextRoadIdx] != null) {
                currRoad.state = "closed";
                Road nextRoad = roads[nextRoadIdx];
                nextRoad.state = "open";
                currRoadIdx = nextRoadIdx;
            }
            updateDistances();
            currentTime = 0;
        }
    }

    private void updateDistances() {
        for (Road road : roads) {
            if (road != null) {
                road.distance = getDistFromCurr(road);
            }
        }
    }
}