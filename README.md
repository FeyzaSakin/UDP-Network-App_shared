# UDP-Network-App_shared

## Overview

This project implements a simple Ping client and server using UDP sockets in Java. The client sends packets to the server, which either responds or causes packet loss based on an input percentage. The client measures round-trip times (RTT), packet loss, and average payload sizes, providing a detailed summary of network performance.

This project was designed to demonstrate proficiency in socket programming, UDP packet handling, and network statistics computation.

---

## Technologies Used

* **Language:** Java
* **Networking:** UDP sockets 

---

## Key Features

**1. Ping Client (PINGClient.java)**
* Sends user-set number of packets to the server using UDP.
* Generates random payloads of 150–300 bytes for each packet.
* Prints sent packet headers and payloads.
* Waits for server responses for timeout (seconds) set by user.
* Prints received packet headers and payloads or a timeout message if no response is received.
* Calculates and summarizes:
  * Number of sent packets
  * Number of received responses
  * Minimum, maximum, and average RTT
  * Packet loss percentage
  * Average payload size

**2. Ping Server (PINGServer.java)**
* Receives packets from clients via UDP.
* Prints received packet headers and payloads.
* Randomly drops packets based on a user-set loss percentage.
* Responds with capitalized payloads for packets not dropped.
* Logs client IP, port, client ID, sequence number, and packet status (`RECEIVED` or `DROPPED`).

---

## File Structure

* **PINGClient.java** – Sends packets, receives responses, computes RTT and packet statistics.
* **PINGServer.java** – Receives packets, causes packet loss, and responds to client requests.

---

## Running the Project

### PINGServer

**Inputs:**
* `argv[0]` – Port number (10000–11000) because they are open for use
* `argv[1]` – Packet loss percentage (0–100)

**Compile & Run:**

```bash
javac PINGServer.java
java PINGServer <port> <loss_percentage>
```

**Example:**

```bash
java PINGServer 10500 30
```

---

### PINGClient

**Inputs:**
* `argv[0]` – Server IP or hostname
* `argv[1]` – Server port (must match server)
* `argv[2]` – Client ID (it can be any number)
* `argv[3]` – Number of packets to send
* `argv[4]` – Timeout in seconds for each response

**Compile & Run:**

```bash
javac PINGClient.java
java PINGClient <server_ip> <port> <client_id> <num_packets> <timeout_seconds>
```

**Example:**

```bash
java PINGClient 10.0.0.2 10500 3333 100 2
```

---

## Output Example
**Note:** *IP addresses and hostnames in the screenshots are hidden for security.*

**Server:**
<a href="images/PINGServer Output Screenshot.png">
  <img src="images/PINGServer Output Screenshot.png" alt="PINGServer Output Example" width="700">
</a>

**Client:**
<a href="images/PINGClient Output Screenshot.png">
  <img src="images/PINGClient Output Screenshot.png" alt="PINGClient Output Example" width="700">
</a>