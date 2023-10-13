import java.net.InetSocketAddress;
import java.util.Scanner;

public class Main {
    public static int clientNumbers=0;

    public static void main(String[] args) {
        System.out.println("[SERVER STARTED] enter number of nodes");
        //Enter the number of clients you want
        Scanner scanner= new Scanner(System.in);
        clientNumbers= scanner.nextInt();

        //instantiating array of nodes between 2 to 255
        Runnable allnodes=()->{
            int[] nodes= new int[clientNumbers];
            for (int i = 0; i < nodes.length; i++) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Client c= new Client();


            }

        };
        new Thread(allnodes).start();
        Server s = new Server();




    }


}
