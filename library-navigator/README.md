# Library Navigator

The Book Query Service is a full-stack application designed to provide users with access to a database of books. It enables users to query the database based on an author's family name and a library's city. The system features a client-server architecture with a JavaFX-based graphical user interface on the client side for inputting queries and displaying results. On the server side, multi-threading is employed to handle multiple client requests concurrently, ensuring efficient service delivery. Secure interaction with the PostgreSQL database is ensured using JDBC, preventing SQL injection attacks. 

## Features

- **Graphical User Interface:** User-friendly interface for inputting author surnames and library cities.
- **Multi-threaded Server:** Efficiently handles multiple client requests concurrently.
- **Secure Database Interaction:** Utilizes JDBC for secure interaction with the PostgreSQL database, preventing SQL injection attacks.
- **Real-time Query Results:** Displays query results in real-time on the client-side interface.
- **Robust Error Handling:** Gracefully handles exceptions and errors, ensuring smooth operation.

## Technologies Used

- **JavaFX:** For building the graphical user interface on the client-side.
- **JDBC:** For database connectivity and interaction.
- **TCP/IP Networking:** Used for communication between the client and server components.
- **Multi-threading:** Implemented on the server side to handle concurrent client requests.
- **PostgreSQL:** Relational database management system for storing and querying book data.

## Usage

To use the Book Query Service, follow these steps:

1. Ensure you have Java installed on your system.
2. Download the provided .zip file containing the Java source files.
3. Extract the files and navigate to the extracted directory.
4. Compile the Java files using your preferred IDE or command-line tools.
5. Run the server application (`BooksDatabaseServer.java`) first.
6. Run the client application (`BooksDatabaseClient.java`) and input the author's surname and library city to query the database.
7. View the results displayed in the client interface.

## Sample Outputs

Sample outputs for both the server and client applications can be found in the `SampleOutputs` directory.

## Contributors

- Jainendra Kumar Jain (https://github.com/jaik97)

## License

This project is licensed under the [MIT License](LICENSE).
