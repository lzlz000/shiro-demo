package lzlzgame;


public class Test1 {
    private static boolean ready;
    private static int number;
    private static class ReaderThread extends Thread {
        @Override
        public void run() {
            while(!ready) {
                System.out.println(number);
            }
            System.out.println(number);
        }
    }
    public static void main(String[] args) throws Exception {
        new ReaderThread().start();
        Thread.sleep(1);
        number = 42;
        ready = true;
    }
}
