import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
    private static final int PORT = 12345;
    private static List<String> connectedClients = new ArrayList<>();
    private static List<PrintWriter> clientWriters = new ArrayList<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server listening on port " + PORT);

            while (true) {
                new ClientHandler(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private String clientName;
        private Socket clientSocket;
        private BufferedReader reader;
        private PrintWriter writer;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                writer = new PrintWriter(clientSocket.getOutputStream(), true);

                clientName = reader.readLine();
                System.out.println("New connection from " + clientName);

                synchronized (connectedClients) {
                    connectedClients.add(clientName);
                }

                clientWriters.add(writer);

                // Send list of connected users to the newly connected client
                sendConnectedUsersListToClient(writer);

                String message;
                while ((message = reader.readLine()) != null) {
                    broadcastMessage(message);
                }
            } catch (IOException e) {
                System.out.println("Client " + clientName + " has disconnected");
            } finally {
                if (clientName != null) {
                    synchronized (connectedClients) {
                        connectedClients.remove(clientName);
                    }
                    clientWriters.remove(writer);
                }
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void broadcastMessage(String message) {
            for (PrintWriter writer : clientWriters) {
                writer.println(message);
            }
        }

       private void sendConnectedUsersListToClient(PrintWriter clientWriter) {
    synchronized (connectedClients) {
        StringBuilder userList = new StringBuilder("[ConnectedUsers]");
        for (String user : connectedClients) {
		
            userList.append(user).append(",");
        }
        clientWriter.println(userList.toString());
    }
}
        }
    }

