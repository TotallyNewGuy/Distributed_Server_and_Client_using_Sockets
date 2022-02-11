package Client;

import Test.RequestExampleHandler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import java.net.SocketTimeoutException;

public class MyUdpClient {

    public static void main(String[] args) {
        String hostName = "127.0.0.1";
        int portNumber = 8888;

        try {
            InetAddress host = InetAddress.getByName(hostName);
            putRequest(host, portNumber);
            getRequest(host, portNumber);
            deleteRequest(host, portNumber);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void putRequest(InetAddress host, int portNumber) {
        // put keys and values into server's map
        String putData = RequestExampleHandler.getInstance().getProperty("UDP_PUT_REQUEST_DATA");
        System.out.println("Putting\n" + putData + "\ninto map");
        System.out.println("...");
        DatagramSocket client = null;
        try {
            // use " | " (white space + vertical bar + white space) to split array
            String[] items = putData.split("\\s*\\|\\s*");
            for (String tokens : items) {
                // client send msg to server
                client = new DatagramSocket();
                String clientMsg = "PUT " + tokens;
                DatagramPacket clientPacket = new DatagramPacket(clientMsg.getBytes(),clientMsg.length(),host,portNumber);
                String sendMsg = new String(clientPacket.getData(), clientPacket.getOffset(), clientPacket.getLength());
                System.out.println("Data sent from client: " + sendMsg);
                // send PUT request to server
                client.send(clientPacket);

                // client receive msg from server
                msgFromServer(client);
                client.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // close server
            client.close();
        }
    }

    private static void getRequest(InetAddress host, int portNumber) {
        // get all keys used to retrieve data from server
        String keySets = RequestExampleHandler.getInstance().getProperty("UDP_GET_REQUEST_DATA");
        DatagramSocket client = null;
        System.out.println("Getting\n" + keySets + "\nfrom map");
        System.out.println("...");
        try {
            // use ", " (common + white space) to split array
            String[] items = keySets.split(",\\s*");
            for (String tokens : items) {
                client = new DatagramSocket();
                String clientMsg = "GET " + tokens;
                DatagramPacket clientPacket = new DatagramPacket(clientMsg.getBytes(),clientMsg.length(),host,portNumber);
                String sendMsg = new String(clientPacket.getData(), clientPacket.getOffset(), clientPacket.getLength());
                System.out.println("Data sent from client: " + sendMsg);
                // send GET request to server
                client.send(clientPacket);

                // get response from server
                msgFromServer(client);

                // close client
                client.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            client.close();
        }
    }

    private static void deleteRequest(InetAddress host, int portNumber) {
        String keySets = RequestExampleHandler.getInstance().getProperty("UDP_DEL_REQUEST_DATA");
        DatagramSocket client = null;
        System.out.println("Deleting\n" + keySets + "\nfrom map");
        System.out.println("...");

        try {
            String[] items = keySets.split(",\\s*");
            for (String tokens : items) {
                client = new DatagramSocket();
                String clientMsg = "DELETE " + tokens;
                DatagramPacket clientPacket = new DatagramPacket(clientMsg.getBytes(),clientMsg.length(),host,portNumber);
                String sendMsg = new String(clientPacket.getData(), clientPacket.getOffset(), clientPacket.getLength());
                System.out.println("Data sent from client: " + sendMsg);

                // send DELETE request to server
                client.send(clientPacket);

                // get respond from server
                msgFromServer(client);

                // close client
                client.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            client.close();
        }
    }


    private static void msgFromServer(DatagramSocket client) {
        try {
            // set time out error
            String timeOut = RequestExampleHandler.getInstance().getProperty("CLIENT_SOCKET_TIMEOUT");
            client.setSoTimeout(Integer.parseInt(timeOut));

            // get massage from server
            byte[] msgBuffer = new byte[1024];
            DatagramPacket returnPacket = new DatagramPacket(msgBuffer, msgBuffer.length);

            client.receive(returnPacket);
            msgBuffer = returnPacket.getData();
            int nlen = returnPacket.getLength();
            String message = new String(msgBuffer, 0, nlen, "ISO-8859-1");

            System.out.println("Message from server:\n" + message);
            System.out.println("===========================================");

        } catch (SocketTimeoutException e) {
            System.out.println("Timeout error");
            System.out.println("===========================================");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
