import org.junit.Test;

import javax.sound.midi.Soundbank;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class HttpServerImplTest {

    @Test
    public void main() throws IOException {
        String method = "GET";
        String URL = "https://localhost:8080";
        String httpVersion = "HTTP/1.1";
        String host = "localhost";
        String accept = "image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, */*";
        String acceptLanguage = "ru";
        Socket clientSocket = new Socket("localhost", 8080);

        String getRequest = method + " " + URL + " " + httpVersion + "\n" + "Host: " +
                host + "\n" + "Accept: " + accept + "\n" + "Accept-Language: " + acceptLanguage + "\n\n";
        System.out.println(getRequest);
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        out.write(getRequest);
        out.flush();



//        final URL url = new URL("https://localhost:8080");
//        final HttpURLConnection con = (HttpURLConnection) url.openConnection();
//        con.setRequestMethod("GET");
//        con.setRequestProperty("Content-Type", "application/json");
//        con.setConnectTimeout(2000);
//        con.setReadTimeout(2000);
//
//        try (final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
//            String inputLine;
//            final StringBuilder content = new StringBuilder();
//            while ((inputLine = in.readLine()) != null) {
//                content.append(inputLine);
//            }
//            System.out.println(content.toString());
//        } catch (final Exception ex) {
//            ex.printStackTrace();
//        }

//        HttpClient client = HttpClient.newHttpClient();
//        HttpRequest request = HttpRequest.newBuilder()
//                .version(HttpClient.Version.HTTP_1_1)
//                .GET()
//                .uri(URI.create("https://localhost:8080"))
//                .timeout(Duration.ofSeconds(15))
//                .build();
//        System.out.println(request);
//
//        try {
//            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//            System.out.println(response);
//        }
//        catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}