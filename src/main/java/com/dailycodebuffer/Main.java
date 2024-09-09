package com.dailycodebuffer;

import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlForm;
import org.htmlunit.html.HtmlInput;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlTable;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    record Player(String name, String id, Integer rating , Integer quickRating){}

    public static void main(String[] args) throws IOException {

        WebClient client = new WebClient();
        client.getOptions().setJavaScriptEnabled(false);
        client.getOptions().setCssEnabled(false);
        HtmlPage searchPage = client.getPage("https://new.uschess.org/civicrm/player-search");
        HtmlForm form = (HtmlForm) searchPage.getByXPath("//form").getFirst();
        HtmlInput firstName = form.getInputByName("first-name-2");
        HtmlInput displayNameField = form.getInputByName("last-name-3");
        HtmlInput submitButton = form.getInputByName("op");
        firstName.type("Alex");
        displayNameField.type("lee");
        HtmlPage resultsPage = submitButton.click();

        List<Player> players = parseResults(resultsPage);

        for (Player player : players){
            System.out.println(player);

        }
    }

    private static List<Player> parseResults(HtmlPage resultsPage) {
        HtmlTable table = (HtmlTable) resultsPage.getByXPath("//table").getFirst();
        List<Player> players = table.getBodies().getFirst().getRows().stream()
                .map(r ->{
                    String rating = r.getCell(2).getTextContent();
                    String quickRating = r.getCell(3).getTextContent();
                    return new Player(
                            r.getCell(0).getTextContent(),
                            r.getCell(1).getTextContent(),
                            rating.isEmpty() ? null : Integer.parseInt(rating),
                            quickRating.isEmpty() ? null : Integer.parseInt(quickRating)

                    );
                }).collect(Collectors.toList());

        return players;
    }
}