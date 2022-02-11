package Client;

import Test.RequestExampleHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class MyTcpClient {
    public static void main(String[] args) {
        String hostName = "127.0.0.1";
        int portNumber = 8888;

        Put(hostName, portNumber);
        Get(hostName, portNumber);
        Delete(hostName, portNumber);
    }

    private static void Put(String hostName, int portNumber) {
        // put keys and values into server's map
        String putData = RequestExampleHandler.getInstance().getProperty("TCP_PUT_REQUEST_DATA");
        System.out.println("Putting\n" + putData + "\ninto map");
        System.out.println("...");
        try {
            // use " | " (white space + vertical bar + white space) to split array
            String[] items = putData.split("\\s*\\|\\s*");

            DataOutputStream outputStream = null;
            Socket client = null;
            for (String tokens : items) {
                // send message to Server
                client = new Socket(hostName, portNumber);
                outputStream = new DataOutputStream(client.getOutputStream());
                System.out.println("Data sent from client: " + tokens);
                // send PUT request to server
                outputStream.writeUTF("PUT " + tokens);

                // get respond from server
                msgFromServer(client);
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private static void Get(String hostName, int portNumber) {
        // get all keys used to retrieve data from server
        String keySets = RequestExampleHandler.getInstance().getProperty("TCP_GET_REQUEST_DATA");
        System.out.println("Getting\n" + keySets + "\nfrom map");
        System.out.println("...");
        Socket client = null;
        try {
            // use ", " (common + white space) to split array
            String[] items = keySets.split(",\\s*");
            DataOutputStream outputStream = null;

            for (String tokens : items) {
                client = new Socket(hostName, portNumber);
                outputStream = new DataOutputStream(client.getOutputStream());
                System.out.println("Data sent from client: " + tokens);
                // send GET request to server
                outputStream.writeUTF("GET " + tokens);

                // get respond from server
                msgFromServer(client);
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    private static void Delete(String hostName, int portNumber) {
        String keySets = RequestExampleHandler.getInstance().getProperty("TCP_DEL_REQUEST_DATA");
        System.out.println("Deleting data from server: " + keySets);
        System.out.println("...");
        Socket client = null;
        try {
            String[] items = keySets.split(",\\s*");
            DataOutputStream outputStream = null;

            for (String tokens : items) {
                client = new Socket(hostName, portNumber);
                outputStream = new DataOutputStream(client.getOutputStream());
                System.out.println("Delete item: " + tokens);
                outputStream.writeUTF("DELETE " + tokens);
                msgFromServer(client);
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    private static void msgFromServer(Socket client) {
        try {
            DataInputStream inputStream = new DataInputStream(client.getInputStream());
            String timeOut = RequestExampleHandler.getInstance().getProperty("CLIENT_SOCKET_TIMEOUT");
            client.setSoTimeout(Integer.parseInt(timeOut));
            String message = inputStream.readUTF();
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
