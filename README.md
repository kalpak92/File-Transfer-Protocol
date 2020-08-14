# Implementation of FTP client and server
This repository hosts a project that implements the **FTP Client and Server** as part of the coursework for Computer Networks (CNT5106C) under the department of Computer Science of the University of Florida.

## Problem Statement
Implement a simple version of FTP client/server software. It consists of two programs: ftpclient and ftpserver. First, the ftpserver is started on a computer. It listens on a certain TCP port.
Then, the ftpclient is executed on the same or a different computer. <br/>
The server’s address and port number are supplied in the command line, for example, `ftpserver sand.cise.ufl.edu 5106`. <br/>
The client will prompt for `username` and `password`. <br/>

After logging in, the user can issue three commands at the client side: 
- `dir` is to retrieve the list of file names available at the server, 
- `get <filename>` is to retrieve a file from the server, 
- `upload < filename>` is to upload a file to the server.


The implementation does not have to conform to the FTP standard. Data and command may use the same TCP connection or different connections. 
The server should support multiple concurrent clients.

## Execution
Folder location : `cd < ProjectPath >/src`

Compilation: <br/> 
- Server ->  `javac com/UFL/FTPServer/FtpServer.java` <br/>
- Client1 -> `javac com/UFL/FTPClient/Client1/FtpClient.java` <br/>
- Client2 -> `javac com/UFL/FTPClient/Client2/FtpClient.java` <br/>



Run: <br/>
- Server ->  `java com.UFL.FTPServer.FtpServer` <br/>
- Client1 -> `java com.UFL.FTPClient.Client1.FtpClient` <br/>
- Client2 -> `java com.UFL.FTPClient.Client2.FtpClient` <br/>

The **`username`** and **`password`** can be found at `users.txt` file under /src folder. 
Each pair of username and password are mentioned in each line of the `users.txt` file.


## Test Cases
<details>
    <summary> Click to Expand </summary>
    
    - Start the server
    - Start client 1
    - Start client 2
    - Try an invalid command on one of the clients (other than `ftpclient <IP port>`, `dir`, `get <filename>` and `upload <filename>`)
    - Try one of the valid commands `dir`, `get <filename>` and `upload <filename>`.
    - Try command `ftpclient <IP port>` with wrong IP or port number
    - Command `ftpclient <IP port>` with correct IP and port number
    - Try one of the commands `ftpclient <IP port>`, `dir`, `get <filename>` and `upload <filename>`
    - Try logging in with the wrong username or password
    -  Login with correct username and password
    - Try an invalid command on the client (other than `ftpclient <IP port>`, `dir`, `get <filename>` and `upload <filename>`)
    - Try command `ftpclient <IP port>`
    - Try uploading a file that doesn’t exist
    - Command `upload` for a valid file from client 1 to server
    - Command `dir` from client 2
    - Try `get` wrong file name
    - Command “get” on client 2 for the file that client 1 uploaded to the server
</details>

# Warning
Before connecting the client with the server, set the correct path to the variables `sharedPath` and `userList` in FTPServer.java file, else clients won't be able to connect to the server. <br/>

`userList` is the variable which contains the path of the file that maintains list of username and password.

`sharedPath` is the variable which contains the path of the folder that is shared by the server to the client and where client can upload the files. <br/>

