package Client;

import Protocol.MyTcpProtocol;
import TestData.RequestExampleHandler;

import java.net.Socket;
import java.util.logging.Logger;

public class MyTcpClient {

    public static Logger logger = Logger.getLogger("Tcp Client");
    // import my protocol
    public static MyTcpProtocol protocol = new MyTcpProtocol();

    public static void main(String[] args) {
        String port = RequestExampleHandler.getInstance().getProperty("PORT");
        String hostName = RequestExampleHandler.getInstance().getProperty("HOST_NAME");
        int portNumber = Integer.parseInt(port);

        put5Times(hostName, portNumber);
        get5Times(hostName, portNumber);
        delete5Times(hostName, portNumber);
    }


    private static void put5Times(String hostName, int portNumber) {
        // put keys and values into server's map
        String putData = RequestExampleHandler.getInstance().getProperty("TCP_PUT_REQUEST_DATA");
        logger.config("Putting\n" + putData + "\ninto map");

        System.out.println("===========================================");
        System.out.println("Putting\n" + putData + "\ninto map...");

        // use " | " (white space + vertical bar + white space) to split array
        String[] items = putData.split("\\s*\\|\\s*");

        for (String tokens : items) {
            protocol.toServer(tokens, hostName, portNumber);
        }
    }


    private static void get5Times(String hostName, int portNumber) {
        // get all keys used to retrieve data from server
        String keySets = RequestExampleHandler.getInstance().getProperty("TCP_GET_REQUEST_DATA");
        logger.config("Getting\n" + keySets + "\nfrom map");

        System.out.println("===========================================");
        System.out.println("Getting\n" + keySets + "\nfrom map...");

        // use ", " (common + white space) to split array
        String[] items = keySets.split("\\s*\\|\\s*");

        for (String tokens : items) {
            protocol.toServer(tokens, hostName, portNumber);
        }
    }


    private static void delete5Times(String hostName, int portNumber) {
        String keySets = RequestExampleHandler.getInstance().getProperty("TCP_DEL_REQUEST_DATA");
        logger.config("Deleting data from server: " + keySets);

        System.out.println("===========================================");
        System.out.println("Deleting data from server: " + keySets);
        System.out.println("...");
        Socket client = null;

        // use ", " (common + white space) to split array
        String[] items = keySets.split("\\s*\\|\\s*");

        for (String tokens : items) {
            protocol.toServer(tokens, hostName, portNumber);
        }
    }
}
