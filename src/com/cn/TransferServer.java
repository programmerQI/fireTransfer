package com.cn;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TransferServer {
	
	private static final int PORT = 9898;
	
	private ServerSocket serverSocket;
	private Socket socket;
	
	public TransferServer() {
		
	}
	
	public void EstablishConnection() {

		try {
			serverSocket = new ServerSocket(PORT);
			System.out.println("The Server Socket has bee created.");
			System.out.println("Waiting for connection...");
			socket = serverSocket.accept();
			System.out.println("Connection Success!");
			
		} catch (IOException e) {
			System.out.println("Fail to establish the connection!");
			e.printStackTrace();
			StopProgram();
		}
		
	}
	
	public void TransferFile(File file) {
		
		try {
			PrintWriter pWriter = new PrintWriter(socket.getOutputStream());
			pWriter.println(file.getName() + "*");
		} catch (IOException e) {
			System.out.println("Fail to trasfer the file!");
			e.printStackTrace();
			StopProgram();
		}
		
		try {
			DataOutputStream doStream = new DataOutputStream(socket.getOutputStream());
			FileInputStream fiStream = new FileInputStream(file);
			byte[] buffer = new byte[4096];
			while(fiStream.read(buffer) != 0) {
				doStream.write(buffer);
			}
		} catch (IOException e) {
			System.out.println("Fail to trasfer the file!");
			e.printStackTrace();
			StopProgram();
		}
		
	}
	
	public void StopProgram() {
		
		try {
			if(!socket.isClosed()) {
					socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			if(!serverSocket.isClosed()) {
					serverSocket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.exit(-1);
		
	}
	
	public static void main(String[] args) {
		if(args.length == 0) {
			System.out.println("Pls Choose a file!");
			System.exit(-1);
		}
		File file = new File(args[0]);
		if(!file.exists()) {
			System.out.println("File does not exsit!");
			System.exit(-1);
		}
		TransferServer tServer = new TransferServer();
		tServer.EstablishConnection();
		tServer.TransferFile(file);
	}
	
}
