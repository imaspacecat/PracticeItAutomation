import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.IOException;

public class Main {
    // self check - https://www.buildingjavaprograms.com/self-check-solutions-4ed.html
    // exercies - https://github.com/ramakastriot/practiceit/tree/master/chapter2

    public static void main(String[] args) throws IOException {
        try (final WebClient webClient = new WebClient()) {
            final HtmlPage scSolutions = webClient.getPage(
                    "https://www.buildingjavaprograms.com/self-check-solutions-4ed.html");
            String html = scSolutions.asXml();
            String[] chapters = html.split("^.*<h2.*$", -1);
            for (int i = 0; i < chapters.length; i++) {
                System.out.println(i);
                System.out.println(chapters[i]);
            }
            System.out.println(chapters.length);

        }

    }

}
