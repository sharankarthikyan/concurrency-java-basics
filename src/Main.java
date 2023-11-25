public class Main {
    public static void main(String[] args) {
        Thread thread = Thread.currentThread();
        printThreadState(thread);

        thread.setName("My Thread");
        thread.setPriority(Thread.MAX_PRIORITY);
        printThreadState(thread);
    }
    public static void printThreadState(Thread thread) {
        System.out.println("---------------------------");
        System.out.println("Thread ID: " + thread.getId());
        System.out.println("Thread Name: " + thread.getName());
        System.out.println("Thread Priority: " + thread.getPriority());
        System.out.println("Thread State: " + thread.getState());
        System.out.println("Thread Group: " + thread.getThreadGroup());
        System.out.println("Thread Is Alive: " + thread.isAlive());
        System.out.println("---------------------------");
    }
}