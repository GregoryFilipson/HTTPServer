import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Server implements Runnable {
    private Socket socket;
    private BufferedReader in;
    private BufferedOutputStream out;

    public Server(Socket socket) {
        this.socket = socket;
        try {
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new BufferedOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        final var validPaths = List.of("/index.xtml", "/spring.svg", "/spring.png");
        String[] parts;
        while (true) {
            String requestLine = null;
            try {
                requestLine = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            parts = requestLine.split(" ");
            if (parts.length != 3) {
                break;
            }
        }

        final var path = parts[1];
        if (!validPaths.contains(path)) {
            try {
                out.write((
                        "HTTP/1.1 404 Not Found\r\n" +
                                "Content-Length: 0\r\n" +
                                "Connection: close\r\n" +
                                "\r\n"
                ).getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        final var filePath = Path.of(".", "public", path);
        String mimeType;
        long length;
        try {
            mimeType = Files.probeContentType(filePath);
            length = Files.size(filePath);
            out.write((
                    "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: " + mimeType + "\r\n" +
                            "Content-Length" + length + "\r\n" +
                            "Connection: close\r\n" +
                            "\r\n"
            ).getBytes());
            Files.copy(filePath, out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}


