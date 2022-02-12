package Client;

import Protocol.MyUdpProtocol;
import TestData.RequestExampleHandler;

import java.io.IOException;
import java.net.InetAddress;

import java.util.logging.Logger;

public class MyUdpClient {

    public static Logger logger = Logger.getLogger("Udp Client");
    // import my protocol
    public static MyUdpProtocol protocol = new MyUdpProtocol();

    public static void main(String[] args) {

        String port = RequestExampleHandler.getInstance().getProperty("PORT");
        String hostName = RequestExampleHandler.getInstance().getProperty("HOST_NAME");
        int portNumber = Integer.parseInt(port);

        try {
            InetAddress host = InetAddress.getByName(hostName);
            put5Times(host, portNumber);
            get5Times(host, portNumber);
            delete5Times(host, portNumber);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void put5Times(InetAddress hostName, int portNumber) {
        // put keys and values into server's map
        String putData = RequestExampleHandler.getInstance().getProperty("UDP_PUT_REQUEST_DATA");
        logger.config("Putting\n" + putData + "\ninto map");
        System.out.println("Putting\n" + putData + "\ninto map");

        System.out.println("===========================================");
        System.out.println("Putting\n" + putData + "\ninto map...");

        // use " | " (white space + vertical bar + white space) to split array
        String[] items = putData.split("\\s*\\|\\s*");

        for (String tokens : items) {
            protocol.toServer(tokens, hostName, portNumber);
        }
    }

    private static void get5Times(InetAddress hostName, int portNumber) {
        // get all keys used to retrieve data from server
        String keySets = RequestExampleHandler.getInstance().getProperty("UDP_GET_REQUEST_DATA");
        logger.config("Getting\n" + keySets + "\nfrom map");
        System.out.println("Getting\n" + keySets + "\nfrom map...");

        // use ", " (common + white space) to split array
        String[] items = keySets.split("\\s*\\|\\s*");

        for (String token : items) {
            protocol.toServer(token, hostName, portNumber);
        }
    }

    private static void delete5Times(InetAddress hostName, int portNumber) {
        String keySets = RequestExampleHandler.getInstance().getProperty("UDP_DEL_REQUEST_DATA");

        logger.config("Deleting data from server: " + keySets);
        System.out.println("Deleting\n" + keySets + "\nfrom map");
        System.out.println("...");

        // use ", " (common + white space) to split array
        String[] items = keySets.split("\\s*\\|\\s*");

        for (String tokens : items) {
            protocol.toServer(tokens, hostName, portNumber);
        }
    }


}
