import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;

class Server {
	ServerSocket server;
	static Socket socket;
	int port = 7778;

	BufferedReader br;   //Reading
	PrintWriter out;     //Writing
	public Runnable[] runnables = new Runnable[10];
	public HashMap<String, String>  nodesInfo = new HashMap<>();
	public Runnable r1, r2;

	public  int clientAddress=0;


	//Constructor..
	public Server() {
		try {
			server = new ServerSocket(port);
			System.out.println("server is ready to accept connection");
			System.out.println("waiting...");
			InetSocketAddress socketAddress=null;
			while (clientAddress<Main.clientNumbers) {
				// creating connection everytime the clients wants connection
				socket = server.accept();
				 socketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
				//Hashmap to store respective clients with their ports
				 nodesInfo.put("Client Address of "+
						String.valueOf(clientAddress), "uses port of"+ String.valueOf(socketAddress.getPort()));
				System.out.println(nodesInfo);
				//startReading();
				InputStreamReader inputStreamReader = new
						InputStreamReader(socket.getInputStream());
				br = new BufferedReader(inputStreamReader);
				String message = br.readLine();
				System.out.println(message);
				if (message.length() <= 0) {
					System.out.println("No data recieved");
				}
				//Send this data to reffered clients
				PrintWriter printWriter=new PrintWriter(socket.getOutputStream());
				printWriter.println(message);
				printWriter.flush();
				System.out.println("Writen sucessssss");


				clientAddress++;

			}


			//br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			//out = new PrintWriter(socket.getOutputStream());


			//startWriting();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startReading() {
		//reading threads
		Arrays.fill(runnables, (Runnable) () -> {
			try {
				InetSocketAddress socketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
				System.out.println("The port is:" + socketAddress.getPort());
				//Thread.sleep(10);

				InputStreamReader inputStreamReader = new
						InputStreamReader(socket.getInputStream());
				br = new BufferedReader(inputStreamReader);
				String message = br.readLine();
				System.out.println(message);
				if (message.length() <= 0) {
					System.out.println("No data recieved");
				}

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		});
		;



		new Thread(r1).start();
	}

	public void startWriting() {
		for (int i = 0; i < runnables.length; i++) {
			if (r2 != null) {
				r2 = runnables[i];

			}


			r2 = () -> {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
				System.out.println("Second Thread");
				try {
					while (!socket.isClosed()) {
						BufferedReader br1 = new BufferedReader
								(new InputStreamReader(System.in));
						String content = br1.readLine();

						out.println(content);
						out.flush();

						if (content.equals("exit")) {
							socket.close();
							break;
						}
					}

					System.out.println("Connection to  server is closed.");
				} catch (Exception e) {
					e.printStackTrace();
				}
			};

			new Thread(r2).start();
		}


	}
}
