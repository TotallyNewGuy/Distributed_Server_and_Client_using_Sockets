import Test.RequestExampleHandler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;



public class UdpClientSock {

	public static void main(String[] args) {

		if (args.length < 2) {
			System.exit(1);
		}

		String hostName = args[0].toString();

		int portNumber = Integer.valueOf(args[1]).intValue();
		
		try {
			
			InetAddress host = InetAddress.getByName(hostName);
			
			
			PutTrans(host,portNumber);
			GetTrans(host,portNumber);
			DeleteTrans(host,portNumber);

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void PutTrans(InetAddress host, int portNumber) {
		String putReqData = RequestExampleHandler.getInstance().getProperty("UDP_PUT_REQUEST_DATA");
		DatagramSocket client = null;
		try {
			List<String> items = Arrays.asList(putReqData.split("\\s*\\|\\s*"));
			for (String tokens : items) {
				client = new DatagramSocket();
				String clientMsg = "PUT " + tokens;
				DatagramPacket clientMsgPacket = new DatagramPacket(clientMsg.getBytes(),clientMsg.length(),host,portNumber);
				client.send(clientMsgPacket);
				AckFromServer(client);
				client.close();
			}

		} catch (IOException e) {
		} finally {
			client.close();
		}

	}
	
	private static void GetTrans(InetAddress host, int portNumber) {
		String reqReqData = RequestExampleHandler.getInstance().getProperty("UDP_GET_REQUEST_DATA");
		DatagramSocket client = null;
		try {
			List<String> items = Arrays.asList(reqReqData.split("\\s*,\\s*"));
			for (String tokens : items) {
				client = new DatagramSocket();
				String clientMsg = "GET " + tokens;
				DatagramPacket clientMsgPacket = new DatagramPacket(clientMsg.getBytes(),clientMsg.length(),host,portNumber);
				client.send(clientMsgPacket);
				AckFromServer(client);
				client.close();
			}

		} catch (IOException e) {
		} finally {
			client.close();
		}

	}
	
	private static void DeleteTrans(InetAddress host, int portNumber) {
		String reqReqData = RequestExampleHandler.getInstance().getProperty("UDP_DEL_REQUEST_DATA");
		DatagramSocket client = null;
		try {
			List<String> items = Arrays.asList(reqReqData.split("\\s*,\\s*"));
			for (String tokens : items) {
				client = new DatagramSocket();
				String clientMsg = "DEL " + tokens;
				DatagramPacket clientMsgPacket = new DatagramPacket(clientMsg.getBytes(),clientMsg.length(),host,portNumber);
				client.send(clientMsgPacket);
				AckFromServer(client);
				client.close();
			}

		} catch (IOException e) {
		} finally {
			client.close();
		}

	}

	private static void AckFromServer(DatagramSocket client) {
		try {

			client.setSoTimeout(
					Integer.valueOf(RequestExampleHandler.getInstance().getProperty("CLIENT_SOCKET_TIMEOUT")));
			byte[] ackMsgBuffer = new byte[500];
			DatagramPacket returnMsgPacket = new DatagramPacket(ackMsgBuffer, ackMsgBuffer.length);
			client.receive(returnMsgPacket);
		} catch (SocketTimeoutException e) {
		} catch (IOException e) {
		} catch (Exception ex) {
		}
	}

	
}