import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class GetSolutionLinks {
    public static void main(String[] args) throws IOException {

        FileWriter file = new FileWriter("src/main/solutionLinks");
        BufferedWriter out = new BufferedWriter(file);

        Document doc = Jsoup.connect(
                "https://practiceit.cs.washington.edu/problem/list").get();
        Elements data = doc.getElementsByClass("problemLink");
        Queue<String> problemLinks = new LinkedList<>();

        for (Element problemInfo : data){
            String problemLink = problemInfo.attr("abs:href");
            if(problemLink.contains("bjp4")){
                problemLinks.add(problemLink);
            }
        }
        System.out.println(problemLinks);

        int counter = 1;
        Submit submit = new Submit();
        try (WebClient webClient = submit.login()) {
            for(String problemLink : problemLinks){
                System.out.println(counter);
                counter++;
                HtmlPage problem = webClient.getPage(problemLink);
                List<HtmlForm> forms = problem.getDocumentElement()
                        .getElementsByAttribute(
                                "form", "id", "solutionform");
                HtmlForm form = forms.get(0);

                List<HtmlAnchor> buttons = form.getElementsByAttribute(
                        "a", "id", "solutionsubmit");
                HtmlAnchor submitButton = buttons.get(0);

                String solutionPage = submitButton.getHrefAttribute();
                out.write(solutionPage + "\n");
            }
        }
        out.close();

    }
}