import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ChatClientAWT extends Frame implements ActionListener {
    private TextField messageField;
    private Button sendButton;
    private TextArea userListArea;
    private TextArea chatArea;
    private String clientName;

    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    private List<String> connectedUsers = new ArrayList<>();

    public ChatClientAWT(String clientName) {
        super("Chat Client: " + clientName);
        this.clientName = clientName;

        // Initialize GUI components
        messageField = new TextField(40);
        sendButton = new Button("Send");
        userListArea = new TextArea(10, 20);
        chatArea = new TextArea(20, 40);
        chatArea.setEditable(false);

        // Set fonts and colors
        Font font = new Font("Arial", Font.PLAIN, 14);
        Color backgroundColor = new Color(240, 240, 240);
        Color textColor = Color.BLACK;
        Color buttonColor = new Color(0, 0, 139);
        Color buttonTextColor = Color.WHITE;

        messageField.setFont(font);
        sendButton.setFont(font);
        userListArea.setFont(font);
        chatArea.setFont(font);

        messageField.setBackground(Color.WHITE);
        sendButton.setBackground(buttonColor);
        sendButton.setForeground(buttonTextColor);
        userListArea.setBackground(backgroundColor);
        chatArea.setBackground(backgroundColor);
        userListArea.setForeground(textColor);
        chatArea.setForeground(textColor);

        // Set layout
        setLayout(new BorderLayout());
        Panel bottomPanel = new Panel(new FlowLayout());
        bottomPanel.add(messageField);
        bottomPanel.add(sendButton);
        add(bottomPanel, BorderLayout.SOUTH);
        add(userListArea, BorderLayout.WEST);
        add(chatArea, BorderLayout.CENTER);

        // Event listeners
        sendButton.addActionListener(this);
        messageField.addActionListener(this); // Add ActionListener to messageField

        // Window listener
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                disconnect();
                System.exit(0);
            }
        });

        // Set up network connection
        try {
            socket = new Socket("localhost", 12345); // Connect to server
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer.println(clientName); // Send client name to server
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Start thread to listen for messages from server
        new Thread(new Runnable() {
            public void run() {
                try {
                    String message;
                    while ((message = reader.readLine()) != null) {
                        if (message.startsWith("[ConnectedUsers]")) {
                            updateConnectedUsers(message.substring(16));
                        } else {
                            displayMessage(message);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sendButton || e.getSource() == messageField) { // Check if action source is sendButton or messageField
            sendMessage();
        }
    }

    private void sendMessage() {
        String message = messageField.getText();
        if (!message.isEmpty()) {
            writer.println(clientName + ": " + message);
            messageField.setText("");
        }
    }

    private void updateConnectedUsers(String userListString) {
        connectedUsers.clear();
        String[] userList = userListString.split(",");
        for (String user : userList) {
            connectedUsers.add(user);
        }
        updateUserList();
    }

    private void updateUserList() {
        userListArea.setText("Connected Users:\n");
        for (String user : connectedUsers) {
            userListArea.append(user + "\n");
        }
    }

    private void displayMessage(String message) {
        chatArea.append(message + "\n");
    }

    private void disconnect() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java ChatClientAWT <clientName>");
            System.exit(1);
        }
        String clientName = args[0];
        ChatClientAWT client = new ChatClientAWT(clientName);
        client.setSize(600, 400);
        client.setVisible(true);
    }
}
