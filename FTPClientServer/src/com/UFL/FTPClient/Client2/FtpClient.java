package com.UFL.FTPClient.Client1;

import java.net.*;
import java.io.*;

/**
 * Ftp client class that will connect to the server.
 */
public class FtpClient {
    public static void main(String args[]) throws Exception {
        while (true) {
            try {
                System.out.println("Enter the command as : ftpclient <IP port>");
                BufferedReader bufferedReader =
                        new BufferedReader(new InputStreamReader(System.in));
                String command = bufferedReader.readLine();
                String[] choice = command.split(" ");
                if (choice[0].compareTo("ftpclient") == 0) {
                    //Socket serverSocket = new Socket("localhost", 4000);
                    String ip = choice[1];
                    int port = Integer.parseInt(choice[2]);
                    if (ip.compareTo("localhost") == 0 &&
                            port == 4000) {
                        Socket serverSocket = new Socket(ip, port);
                        CNFTPClientRun client = new CNFTPClientRun(serverSocket);
                        client.initiateClient();
                    } else {
                        System.out.println("Wrong port or ip");
                    }
                } else {
                    System.out.println("Type correct command");
                }
//        Socket serverSocket = new Socket("localhost", 4000);
//        CNFTPClientRun client = new CNFTPClientRun(serverSocket);
//        client.initiateClient();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}

/**
 * Client socket class that will get connected to the server.
 */
class CNFTPClientRun {
    Socket clientSocket;
    DataInputStream inputStream;
    DataOutputStream outputStream;
    BufferedReader bufferedReader;
    String dir = "/Users/kalpak/Desktop/File-Transfer-Protocol-master/FTPClientServer/src/Test Folder/Client/Client2";
    String clientName;

    CNFTPClientRun(Socket soc) {
        try {
            clientSocket = soc;
            inputStream =
                    new DataInputStream(clientSocket.getInputStream());
            outputStream =
                    new DataOutputStream(clientSocket.getOutputStream());
            bufferedReader =
                    new BufferedReader(new InputStreamReader(System.in));
        } catch (Exception ex) {

        }
    }

    /**
     * Initiates the client
     *
     * @throws Exception
     */
    public void initiateClient() throws Exception {
        authenticateClient();
    }

    /**
     * Authenticates new client by validating correct id and password.
     *
     * @throws Exception
     */
    private void authenticateClient() throws Exception {
        boolean isLoginSuccess = false;
        while (true) {
            System.out.println("Client is starting...");
            System.out.println("Enter username:");
            String userName = bufferedReader.readLine();
            outputStream.writeUTF(userName);
            System.out.println("Enter password:");
            String password = bufferedReader.readLine();
            outputStream.writeUTF(password);
            if (inputStream.readUTF().equalsIgnoreCase("Success")) {
                isLoginSuccess = true;
                clientName = userName;
                System.out.println(clientName +
                        " logged in successfully");
                break;
            } else {
                System.out.println("Login failed");
            }
        }
        if (isLoginSuccess)
            displayMenu();
    }

    /**
     * Displays the list of operations that can be performed by each of the client
     *
     * @throws Exception
     */
    private void displayMenu() throws Exception {
        while (true) {
            System.out.println("Client Name: " + clientName);
            System.out.println(">>>>Valid Commands <<<<");
            System.out.println("1. dir");
            System.out.println("2. get filename");
            System.out.println("3. upload filename");
            System.out.println("4. exit");
            System.out.print("\nEnter command :");
            String command = bufferedReader.readLine();
            String[] choice = command.split(" ");
            if ((choice[0].compareTo("upload") == 0 ||
                    choice[0].compareTo("get") == 0) &&
                    choice.length == 1) {
                System.out.println("Please enter the filename along with command");
                continue;
            }
            switch (choice[0]) {
                case "upload":
                    outputStream.writeUTF("upload");
                    uploadFileToServer(choice[1]);
                    continue;
                case "get":
                    outputStream.writeUTF("get");
                    getFileFromServer(choice[1]);
                    continue;
                case "dir":
                    outputStream.writeUTF("dir");
                    browserDir();
                    continue;
                case "exit":
                    outputStream.writeUTF("exit");
                    System.exit(1);
                    break;
                default:
                    System.out.println("Invalid choice. Try again");
                    continue;
            }
        }
    }

    /**
     * Get the file from the server which will be downloaded by the client
     *
     * @param fileToReceive downloaded file
     * @throws Exception
     */
    void getFileFromServer(String fileToReceive) throws Exception {
        outputStream.writeUTF(fileToReceive);
        String msgFromServer = inputStream.readUTF();

        if (msgFromServer.compareTo("File Not Found") == 0) {
            System.out.println("File not found on Server ...");
            return;
        } else if (msgFromServer.compareTo("READY") == 0) {
            System.out.println("File found on server");
            System.out.println("Receiving File ...");
            File fileAtClient = new File(dir + "/" + fileToReceive);
            if (fileAtClient.exists()) {
                String option;
                System.out.println("File already exists at the client." +
                        " Want to OverWrite (Y/N) ?");
                option = bufferedReader.readLine();
                if (option == "N") {
                    outputStream.flush();
                    return;
                }
            }
            writeFile(fileAtClient);
            System.out.println(inputStream.readUTF());

        }
    }

    /**
     * Writes the file that is downloaded by the client in the disk of the client.
     *
     * @param file name of the downloaded file
     * @throws Exception
     */
    void writeFile(File file) throws Exception {
        FileOutputStream fout = new FileOutputStream(file);
        int ch;
        String temp;
        do {
            temp = inputStream.readUTF();
            ch = Integer.parseInt(temp);
            if (ch != -1) {
                fout.write(ch);
            }
        } while (ch != -1);
        fout.close();
    }

    /**
     * Uploads the file to the server.
     *
     * @param clientFilename filename that will be uploaded
     * @throws Exception
     */
    void uploadFileToServer(String clientFilename) throws Exception {
        String serverFileName;

        File fileToSend = new File(dir + "/" + clientFilename);
        if (!fileToSend.exists()) {
            System.out.println("File does not Exist in client...");
            outputStream.writeUTF("File not found");
            return;
        }
        serverFileName = clientFilename;
        outputStream.writeUTF(serverFileName);

        String msgFromServer = inputStream.readUTF();
        if (msgFromServer.compareTo("File already exists") == 0) {
            String option;
            System.out.println("File already exists at the server. Want to OverWrite (Y/N) ?");
            option = bufferedReader.readLine();
            if (option.equalsIgnoreCase("Y")) {
                outputStream.writeUTF("Y");
            } else {
                outputStream.writeUTF("N");
                return;
            }
        }

        System.out.println("Uploading File ...");
        readFile(fileToSend);
        System.out.println(inputStream.readUTF());
    }

    /**
     * Reads the file to upload it to the server by the client
     *
     * @param fileToSend filename that will be uploaded
     * @throws Exception
     */
    private void readFile(File fileToSend) throws Exception {
        FileInputStream fin = new FileInputStream(fileToSend);
        int ch;
        do {
            ch = fin.read();
            outputStream.writeUTF(String.valueOf(ch));
        }
        while (ch != -1);
        fin.close();
    }

    /**
     * Browses the shared directory of the server.
     *
     * @throws Exception
     */
    void browserDir() throws Exception {
        String msgFromServer = inputStream.readUTF();

        if (msgFromServer.compareTo("File Not Found") == 0) {
            System.out.println("File not found on Server ...");
            return;
        } else {
            System.out.println(msgFromServer);
        }
    }
}
