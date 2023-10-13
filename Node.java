import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


class Node
{

	ServerSocket server;
	Socket socket;
	int port = 7778;

	BufferedReader br;   //Reading
	PrintWriter out;     //Writing


	//Constructor..
	public Node()
	{
		try {
			server=new ServerSocket(port);
			System.out.println("server is ready to accept connection");
			System.out.println("waiting...");
			socket = server.accept();
			
			br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			out= new PrintWriter(socket.getOutputStream());
			
			startReading();
			startWriting();
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void startReading()
	{
		
		Runnable r1=()->{
			
			System.out.println("reader started..");
			
			try {
				while(true)
				{				
						String msg=br.readLine();
						if(msg.equals("exit"))
						{
							System.out.println("Client terminated the chat");
							socket.close();
							break;
						}
						
						System.out.println("Client : "+msg);
					} 
				
				}catch (Exception e)
				{
					//e.printStackTrace();
					System.out.println("Connection closed.");
				}
		};
		
		new Thread(r1).start();
	}
	
	public void startWriting()
	{
		
		Runnable r2=()->{
			System.out.println("Writer started..");
			try {
				while(!socket.isClosed())
				{
						BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
						String content = br1.readLine();
						
						out.println(content);
						out.flush();
						
						if(content.equals("exit"))
						{
							socket.close();
							break;
						}						
					} 
				

				System.out.println("Connection is closed.");
				}catch (Exception e)
				{
					e.printStackTrace();	
				}		
		};
		
		new Thread(r2).start();
	}
	
	
	
	public static void main(String[] args) {
		System.out.println("this is server... going to start.");
		Node s = new Node();
	}

}
