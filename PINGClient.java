
/**************************************************************************************
  *		PINGClient.java
  *		Client File
  ***************************************************************************************
  * Function:
  * 		- Sents packets to the server as a client
  * 		- Prints the payloads of the sent packet and response recieved packet with their headers  
  * 		- At the end, summarizes the number of sent packets, the number of response packets, minimum RTT, maximum RTT, average RTT, and the lost packets' percentage, and the average payload size
  *----------------------------------------------------------------------------------------------------------------------------------------
  *    Input:
  *          Parameters - 	argv[0] || The IP or the host name of the server
  *          				argv[1] || The port number
  *          				argv[2] || The Client ID
  *          				argv[3] || The Number of Packets
  *          				argv[4] || The seconds the client can wait for the response 
  *    Output:
  *          Return – The payloads of the sent packet and the response received packet with their headers
**************************************************************************************/
import java.net.*;
import java.text.DecimalFormat;

class PINGClient {
    public static void main(String argv[]) throws Exception {
        // socket variables
        DatagramSocket clientSocket;
        DatagramPacket sendPacket;
        DatagramPacket receivePacket;
        byte[] receiveData = new byte[1024];
        byte[] sendData = new byte[1024];
        InetAddress serverIPAddress; // The server's IP address
        InetAddress clientIPAddress; // The client's IP address
        String clientHostName; // The client's host name

        // command-line arguments
        int port; // The input port number
        int clientID; // The input Client ID
        int nPackets; // The input number of packets
        int waitSec = 0; // The input amount of seconds to wait
        String server; // The input server

        // client variables
        String serverSentence; // Stores the received response
        int initialRestBytes; // The used bytes
        int restBytes; // The subtraction of the used bytes from the random payload size "randomPay"
        int min = 150; // The minimum number of payload size
        int max = 300; // The maximum number of payload size
        int randomPay; // The random number for payload size
        int nPingResponse = 0; // The number of ping responses
        int total_Pay_Load_size = 0; // The total payload size
        int ave_Pay_Load_Size = 0; // The average payload size
        int calcLoss; // The calculated loss
        float totalRTT = 0; // The total RTT
        float aveRTT = 0; // The average RTT
        double pRTT = 0; // Each time the packet is created, it stores its RTT
        double minRTT = Integer.MAX_VALUE; // Set the min RTT to max to compare with with min RTT numbers
        double maxRTT = Integer.MIN_VALUE; // Set the max RTT to min to compare with with max RTT numbers
        double iTimeStamp = 0; // Stores the current time the packet was constructed
        double rTimeStamp = 0; // Stores the current time the response was received

        String projectName = "My_UDP_Network_Project";
        String initialRest; // The string of packet payload without the rest
        String randomLetNum = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890"; // Random letters &
                                                                                                // numbers
        String rest = "";// The rest string made of random letters and numbers
        String message;// Combination of header and payload
        String timeStamp;// String type of the time stamp, converted from double
        String[] combinedArray;// The combination of the header and the payload parts
        String[] splitRandomLetNum;// Stores the "randomLetNum" string's elements splited each comma
        String[] splitReceived;// Stores the received response content
        // Splitting "randomLetNum" string's elements each comma and store in
        // splitRandomLetNum
        splitRandomLetNum = randomLetNum.split("(?!^)");//

        // Have the value with three decimal places
        DecimalFormat decimal = new DecimalFormat("#.###");

        // For missing any process command-line arguments, print
        if (argv.length < 5) {
            for (int i = argv.length + 1; i <= 5; i++) {
                System.out.print("ERR - arg " + i + "\n");
            }
            System.exit(-1);
        }

        // Get the client IP and host name
        clientIPAddress = InetAddress.getLocalHost();
        clientHostName = clientIPAddress.getHostName();

        // Set the arguments to variables
        server = argv[0];// Set the first argv to the IP or the hostname "server"
        port = Integer.parseInt(argv[1]);// Set the second argv to the port number
        clientID = Integer.parseInt(argv[2]);// Set the third argv to the client ID
        nPackets = Integer.parseInt(argv[3]);// Set the fourth argv to the number of packets
        waitSec = Integer.parseInt(argv[4]);// Set the fifth argv to the seconds the client can wait for the response

        // If the port number is not a positive integer or if it's bigger than 65536,
        // then give error and return the error given arg
        if (port < 0 || port >= 65536) {
            System.out.print("ERR - arg 1\r\n");
            System.exit(-1);
        }

        // To calculate the rest bytes, have a "initialRest" string
        // Set "initialRest"'s bytes' length to the "initialRestBytes"
        initialRest = "Host: " + clientHostName + "\nProject-name: " + projectName + "\nRest: ";

        initialRestBytes = initialRest.getBytes().length;

        // Create client socket to destination
        clientSocket = new DatagramSocket();

        // If the IP Address can be found from the hostName, then send and recieve
        // packets and print out the packets headers and payloads
        try {
            // Convert hostName to the IP Address
            String serverIP = InetAddress.getByName(server).getHostAddress();
            serverIPAddress = InetAddress.getByName(serverIP);

            // Print out the arguments: IP address, port number, client ID, number of
            // packets to send, and the seconds to wait
            System.out.println("\r\nPINGClient started with server IP: " + serverIP + ", port: " + port + ", client ID: "
                            + clientID + ", packets: " + nPackets + ", wait: " + waitSec + "\n");

            // Create packet and send to server for the number of packets to send
            for (int i = 1; i <= nPackets; i++) {

                // Have a random number for the payload size between 150 - 300
                randomPay = (min + (int) (Math.random() * ((max - min) + 1)));
                // Subtract the initial size from the random payload size
                restBytes = randomPay - initialRestBytes;
                // Add the pay size to the total payload size
                total_Pay_Load_size += randomPay;

                // Reset rest for each packet to avoid growing indefinitely
                rest = "";

                // Create a string of random letters & numbers as much as the unused bytes left
                // from the random payload size
                for (int j = 0; j < restBytes; j++) {
                    int index = ((int) (Math.random() * (splitRandomLetNum.length)));
                    // Add the randomly picked letter or number to the "rest" string
                    rest = rest + splitRandomLetNum[index];
                }

                // Set the "iTimeStamp" to the current time the packet was constructed
                iTimeStamp = System.currentTimeMillis();
                // Convert iTimeStamp to string
                timeStamp = Double.toString(iTimeStamp);

                // Have a string array to store the header information
                String[] header = { "1", Integer.toString(clientID), Integer.toString(i), timeStamp,
                        Integer.toString(randomPay) };

                // Have a string array to store the payload information
                String[] payload = { clientHostName, projectName, rest };

                // Combine header and payload string array in "combinedArray" string array
                combinedArray = new String[header.length + payload.length];
                System.arraycopy(header, 0, combinedArray, 0, header.length);
                System.arraycopy(payload, 0, combinedArray, header.length, payload.length);

                // Copy "combinedArray" string array to the "message" string and have ","
                // between elements
                message = String.join(",", combinedArray);

                // Place packet's header and the payload combined in the buffer
                sendData = message.getBytes();

                // Create packet and send to server
                sendPacket = new DatagramPacket(sendData, sendData.length, serverIPAddress, port);
                clientSocket.send(sendPacket);

                // Print out the request packet header and payload for the packet
                System.out.println("---------- Ping Request Packet Header ----------");
                System.out.println("Version: 1");// Print out the version of the system
                System.out.println("Client ID: " + clientID);// Print out the client ID
                System.out.println("Sequence No.: " + i);// Print out the sequence number of the sent packet
                System.out.println("Time: " + timeStamp);// Print out the current time the packet was constructed
                System.out.println("Payload Size: " + randomPay);// Print out the randomly picked payload size
                System.out.println("--------- Ping Request Packet Payload ------------");
                System.out.println("Host: " + clientHostName);// Print out the host name of the client
                System.out.println("Project-name: " + projectName);// Print out the project name
                System.out.println("Rest: " + rest);// Print out the unused bytes left from the random payload size
                System.out.println("---------------------------------------\n");

                // If the response is received within the wait seconds
                try {
                    if (waitSec == 0) {
                        waitSec = 1;
                    }
                    clientSocket.setSoTimeout(waitSec * 1000);
                    // Create receiving packet and receive from server
                    receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    clientSocket.receive(receivePacket);

                    // Set it to the time the packet was received
                    rTimeStamp = System.currentTimeMillis();

                    // Increase the number of received responses
                    nPingResponse += 1;

                    // Set the "serverSentence" to the received response
                    serverSentence = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    // Store the received response in a string array and split in each comma
                    splitReceived = serverSentence.split(",");

                    // Print out the received packet's header and payload
                    System.out.println("---------- Received Ping Response Packet Header ----------");
                    System.out.println("Version: " + splitReceived[0]);// Print out the version of the system
                    System.out.println("Client ID: " + splitReceived[1]);// Print out the client ID
                    System.out.println("Sequence No.: " + splitReceived[2]);// Print out the sequence number
                    System.out.println("Time: " + splitReceived[3]);// Print out the current time of construction
                    System.out.println("Payload Size: " + splitReceived[4]);// Print out payload size
                    System.out.println("--------- Ping Response Packet Payload ------------");
                    System.out.println("Host: " + splitReceived[5]);// Host name
                    System.out.println("Project-name: " + splitReceived[6]);// Project name
                    System.out.println("Rest: " + splitReceived[7]);// Rest string
                    System.out.println("---------------------------------------");

                    pRTT = (rTimeStamp - iTimeStamp) / 1000; // RTT in seconds
                    System.out.println("RTT: " + decimal.format(pRTT) + " seconds\n");// Print out the RTT
                    minRTT = Math.min(minRTT, pRTT);// Compare RTT values to find the min RTT
                    maxRTT = Math.max(maxRTT, pRTT);// Compare RTT values to find the max RTT
                    totalRTT += pRTT;// Add each RTT to the total RTT

                    // If the response is not received before the wait seconds
                } catch (SocketTimeoutException ex) {
                    System.out.println("--------------- Ping Response Packet Timed-Out ------------------\n");
                    continue;
                }
            }

            // Close the socket
            clientSocket.close();

            // If there's any ping response, then calculate the average RTT
            if (nPingResponse > 0) {
                aveRTT = (totalRTT / nPingResponse);
            }
            // If there's no ping response, then set the minimum, maximum, and average RTT
            // to 0.
            if (nPingResponse == 0) {
                minRTT = 0;
                maxRTT = 0;
                aveRTT = 0;
            }

            // The percent loss is equal to (100 * (the number of packets - the number of
            // ping responses)) / (number of packets)
            calcLoss = ((100) * (nPackets - nPingResponse)) / nPackets;

            // Calculate the average payload Size
            ave_Pay_Load_Size = total_Pay_Load_size / nPackets;

            // Print out the summary
            System.out.println("Summary: " + nPackets + " :: " + nPingResponse + " :: " + decimal.format(minRTT)
                    + " :: " + decimal.format(maxRTT) + " :: " + decimal.format(aveRTT) + " :: " + calcLoss + "% :: "
                    + ave_Pay_Load_Size + "\r\n");

            // If the given server host does not exist, then give an error
        } catch (UnknownHostException e) {
            // If the host does not exist, then give error
            System.out.println("\r\nUnable to lookup IP address for host\n");
            System.exit(-1);
        }
    }
}
