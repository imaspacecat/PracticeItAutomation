import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GetProblemInfo {
    public static void main(String[] args) throws IOException {

        GetProblemInfo get = new GetProblemInfo();

        List<String[]> problemInfo = get.getProblemInfo();

        FileWriter file = new FileWriter("src/main/problemInfo");
        BufferedWriter out = new BufferedWriter(file);

        for(String[] problem : problemInfo){
            switch(problem[1]){
                default:
                    out.write(problem[1]+","+problem[2]+","+problem[3]+","+problem[4]+","+problem[5]+","+problem[6]+"\n");
            }
        }
        out.close();
    }
    public List<String[]> getProblemInfo() throws IOException {
        List<String[]> problems = new ArrayList<>();
        getLinks(problems);
        getAnswerTypes(problems);
        getAnswerCount(problems);
        getProblemNumber(problems);
        return problems;
    }
    private void getLinks(List<String[]> problems) throws IOException{
        Document doc = Jsoup.connect(
                "https://practiceit.cs.washington.edu/problem/list").get();

        Elements data = doc.getElementsByClass("problemLink");

        int IDCounter = 2537;

        for (Element problemInfo : data){
            String[] linkInfo = new String[7];
            String problemLink = problemInfo.attr("abs:href");
            if(problemLink.contains("bjp4")){

                linkInfo[0] = problemLink;
                linkInfo[1] = String.valueOf(IDCounter);

                if(problemLink.contains("/s")) linkInfo[2] = "sc";
                else linkInfo[2] = "ex";

                IDCounter++;
                problems.add(linkInfo);
            }
        }
    }
    private void getAnswerTypes(List<String[]> problems) throws IOException {
        for (String[] problem : problems) {
            if(problem[2].equals("ex")) {
                problem[3] = "codeSubmit";
                continue;
            }
            Document doc = Jsoup.connect(problem[0]).get();
            String problemType = answerType(doc);
            problem[3] = problemType;
        }
    }
    private String answerType(Document doc) {
        if (doc.select("input.multiplechoiceanswer[name=mechanical1[]]").size() != 0) {
            return "multipleSelect";
        } else if (doc.select("td.multiplechoice").size() != 0) {
            return "multipleChoice";
        } else if (doc.select("input.expressionanswer[name=mechanical2]").size() != 0) {
            return "multipleResponse";
        } else if (doc.select("input.expressionanswer[name=mechanical1]").size() != 0){
            return "freeResponse";
        }else{
            return "codeSubmit";
        }
    }
    private void getAnswerCount(List<String[]> problems) throws IOException {
        for (String[] problem : problems) {
            if(Objects.equals(problem[3], "freeResponse") || Objects.equals(problem[3], "codeSubmit")){
                problem[4] = "1";
                continue;
            }
            Document doc = Jsoup.connect(problem[0]).get();
            if(Objects.equals(problem[3], "multipleResponse")){
                problem[4] = responseCount(doc);
            }else{
                problem[4] = multipleChoiceCount(doc);
            }
        }
    }
    private String responseCount(Document doc){
        int checkCounter = 1;
        while(true){
            if(doc.select("input.expressionanswer[name=mechanical" + checkCounter + "]").size() != 0){
                checkCounter++;
            }else{
                checkCounter--;
                break;
            }
        }
        return Integer.toString(checkCounter);
    }
    private String multipleChoiceCount(Document doc){
        int checkCounter = 1;
        while(true){
            if(doc.select("input.multiplechoiceanswer[value=answer" + checkCounter + "]").size() != 0){
                checkCounter++;
            }else break;
        }
        return Integer.toString(checkCounter);
    }
    private void getProblemNumber(List<String[]> problems){
        String[] splitted;
        for(String[] problem : problems){
            String reduced = problem[0].substring(62,68);
            if(problem[2].equals("sc"))
                splitted = reduced.split("/s");
            else
                splitted = reduced.split("/e");
            problem[5] = splitted[0];
            problem[6] = splitted[1].split("%")[0];
        }
    }
}
