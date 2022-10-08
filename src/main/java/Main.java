import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.html.parser.HTMLParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    // self check - https://www.buildingjavaprograms.com/self-check-solutions-4ed.html
    // exercies - https://github.com/ramakastriot/practiceit/tree/master/chapter2
    // alternate self check - http://what-when-how.com/Tutorial/topic-995khifo/Building-Java-Programs-A-Back-to-Basics-Approach-1066.html

    public static void main(String[] args) throws IOException {
//        try (final WebClient webClient = new WebClient()) {
//            final String email = "imaspacecatperson@gmail.com";
//            final String password = "thisisthepasswordbecareful";
//
//            final HtmlPage loginPage = webClient.getPage(
//                    "https://practiceit.cs.washington.edu/login");
//
//            final HtmlForm loginForm = loginPage.getHtmlElementById("loginform");
//
//            HtmlTextInput userField = loginForm.getInputByName("usernameoremail");
//            HtmlPasswordInput passField = loginForm.getInputByName("password");
//
//            userField.type(email);
//            passField.type(password);
//
//            HtmlSubmitInput submitInput = loginForm.getInputByName("login");
//            HtmlPage inboxPage = submitInput.click();
//            HtmlPage problems = webClient.getPage("https://practiceit.cs.washington.edu/problem/list");
//            inboxPage.save(new File("inbox.html"));
//
//
//        }
        Document scSolutions = Jsoup.connect(
                "https://www.buildingjavaprograms.com/self-check-solutions-4ed.html").get();

        String html = scSolutions.html();
        String[] chapters = html.split("<h2");

        Document[] chapterDocs = new Document[chapters.length];
        List<Elements> chapterElements = new ArrayList<>();
        for (int i = 0; i < chapters.length; i++) {
            System.out.println("---------------------");
//            System.out.println(chapters[i]);
            chapterDocs[i] = Jsoup.parse(chapters[i]);
            chapterElements.add(chapterDocs[i].select("pre, li"));
            System.out.println(chapterElements.get(i).text());
        }
        System.out.println("+++++++++++++++++");
        System.out.println(chapterDocs[1].html());








    }



}
