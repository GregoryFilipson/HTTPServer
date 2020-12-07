import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class Main {
    public static void main(String[] args) throws IOException {
        final var serverSocket = new ServerSocket(9999);
        ExecutorService executorService = Executors.newFixedThreadPool(64);
        while (true) {
            Socket s = serverSocket.accept();
            System.out.println("Client accepted");
            executorService.submit(new Server(s));
            executorService.shutdown();
            try {
                executorService.awaitTermination(1, TimeUnit.DAYS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}







//
//public class Main {
//    public static void main(String[] args) {
//        final var validPaths = List.of("/index.xtml", "/spring.svg", "/spring.png");
//        try (final var serverSocket = new ServerSocket(9999)) {
//            while (true) {
//                try (
//                        final var socket = serverSocket.accept();
//                        final var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                        final var out = new BufferedOutputStream(socket.getOutputStream());
//                ) {
//                    final var requestLine = in.readLine();
//                    final var parts = requestLine.split(" ");
//                    if (parts.length != 3) {
//                        continue;
//                    }
//
//                    final var path = parts[1];
//                    if (!validPaths.contains(path)) {
//                        out.write((
//                                "HTTP/1.1 404 Not Found\r\n" +
//                                        "Content-Length: 0\r\n" +
//                                        "Connection: close\r\n" +
//                                        "\r\n"
//                        ).getBytes());
//                        out.flush();
//                        continue;
//                    }
//                    final var filePath = Path.of(".", "public", path);
//                    final var mimeType = Files.probeContentType(filePath);
//                    final var length = Files.size(filePath);
//                    out.write((
//                            "HTTP/1.1 200 OK\r\n" +
//                                    "Content-Type: " + mimeType + "\r\n" +
//                                    "Content-Length" + length + "\r\n" +
//                                    "Connection: close\r\n" +
//                                    "\r\n"
//                    ).getBytes());
//                    Files.copy(filePath, out);
//                    out.flush();
//                } catch (IOException e)
//                {
//                    e.printStackTrace();
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
