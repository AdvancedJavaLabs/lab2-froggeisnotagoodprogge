public class WorkerLauncher {

    public static void main(String[] args) {

        int workers = args.length != 0 ? Integer.parseInt(args[0]) : 4;

        for (int i = 0; i < workers; i++) {
            Thread t = new Thread(() -> WorkerApp.start());
            t.setName("worker-" + i);
            t.start();
        }
    }
}