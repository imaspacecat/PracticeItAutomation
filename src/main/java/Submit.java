import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Queue;

public class Submit {
    // self check - https://www.buildingjavaprograms.com/self-check-solutions-4ed.html
    // exercies - https://github.com/ramakastriot/practiceit/tree/master/chapter2

    public static void main(String[] args) throws IOException {

        Submit submit = new Submit();

        String url = "src/main/problemInfo";

        Queue<String[]> problemInfo = submit.readProblemInfo(url);

        try(WebClient webClient = submit.login()){
            for(String[] problem : problemInfo){
                System.out.println(problem[4] + "." + problem[5]);
                if(problem[2].equals("multipleSelect")){
                    submit.multipleSelectBruteForce(problem, webClient);
                }else if(problem[2].equals("multipleChoice")){
                    submit.multipleChoiceBruteForce(problem, webClient);
                }else if(problem[1].equals("ex")){
                    submit.exerciseSubmit(webClient, problem);
                }
            }
        }


        /*
        String q = "random word £500 bank $";
        String url = "https://example.com?q=" + URLEncoder.encode(q, StandardCharsets.UTF_8);
         */

    }
    public WebClient login() throws IOException{
        try (final WebClient webClient = new WebClient()) {

            //webClient.getOptions().setCssEnabled(true);
            //webClient.getOptions().setJavaScriptEnabled(true);

            final String email = "s172343@scusd.net";
            final String password = "Qy20041221";

            final HtmlPage loginPage = webClient.getPage(
                    "https://practiceit.cs.washington.edu/login");

            final HtmlForm loginForm = loginPage.getHtmlElementById("loginform");

            HtmlTextInput userField = loginForm.getInputByName("usernameoremail");
            HtmlPasswordInput passField = loginForm.getInputByName("password");

            userField.type(email);
            passField.type(password);

            HtmlSubmitInput submitInput = loginForm.getInputByName("login");
            submitInput.click();
            return webClient;
        }
    }
    public Queue<String[]> readProblemInfo(String fileName) throws IOException {
        FileReader file = new FileReader(fileName);
        BufferedReader out = new BufferedReader(file);
        Queue<String[]> problemInfo = new LinkedList<>();
        String inputLine;
        while((inputLine = out.readLine()) != null){
            String[] inputValues = inputLine.split(",");
            problemInfo.add(inputValues);
        }
        return problemInfo;
    }
    public void submitAnswers(Queue<String[]> problems, Queue<Queue<String>> scAnswers, Queue<String> exAnswers) throws IOException {

        for(String[] problemInfo : problems){
            if(problemInfo[3].equals("multipleChoice")){
                //multipleChoiceBruteForce();
            }else if(problemInfo[3].equals("multipleSelect")){
                return;
            }else if(problemInfo[3].equals("multipleResponse")){
                continue;
            }else{
                continue;
            }
        }
    }
    public void multipleChoiceBruteForce(String[] problemInfo, WebClient webClient) throws IOException {
        String link = "https://practiceit.cs.washington.edu/test/enqueue-job?problemid=";
        link += problemInfo[0] + "&cheated=0&mechanical1=answer";

        int numberOfIterations = Integer.parseInt(problemInfo[3]);
        String submitURL;
        for(int i = 0; i < numberOfIterations; i++){
            submitURL = link+i;
            webClient.getPage(submitURL);
        }
    }
    public void multipleSelectBruteForce(String[] problemInfo, WebClient webClient) throws IOException{
        String url = "https://practiceit.cs.washington.edu/test/enqueue-job?problemid=";
        url += problemInfo[0] + "&cheated=0";

        multipleSelectRecursive(webClient, 0, Integer.parseInt(problemInfo[3]), url);
    }
    private void multipleSelectRecursive(WebClient webClient, int min, int max, String url) throws IOException{
        String newURL;
        while(min<max){
            newURL = url + "&mechanical1=answer" + min;
            min++;
            //System.out.println(newURL);
            webClient.getPage(newURL);
            multipleSelectRecursive(webClient, min, max, newURL);
        }
    }
    private void exerciseSubmit(WebClient webClient, String[] problem) throws IOException {
        String answerLocation = "src/main/java/exercises/chapter"+problem[4]+"/"+problem[6]+".txt";
        StringBuilder answer = new StringBuilder();
        BufferedReader out = new BufferedReader(new FileReader(answerLocation));
        String inputLine;
        while((inputLine = out.readLine()) != null){
            answer.append(inputLine).append("\n");
        }
        String url = "https://practiceit.cs.washington.edu/test/enqueue-job?problemid="+problem[0]+"&cheated=0&solution=";
        url += URLEncoder.encode(answer.toString(), StandardCharsets.UTF_8);
        webClient.getPage(url);
    }

    private void freeResponse(WebClient webClient, int problemID, String answer) throws IOException {
        String url = "https://practiceit.cs.washington.edu/test/enqueue-job?problemid="+problemID+"&cheated=0&mechanical1=";
        url += URLEncoder.encode(answer, StandardCharsets.UTF_8);
        webClient.getPage(url);
    }
    private void exceptions(WebClient webClient) throws IOException {
        webClient.getPage("https://practiceit.cs.washington.edu/test/enqueue-job?problemid=2640&cheated=0&mechanical1=makeAWindow");
    }
}


