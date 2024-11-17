import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Main {
    private static final int MAX_DEPTH = 3;

    public static void main(String[] args) {
        System.out.println("Inserisci il seed URL: ");
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        String seedURL = scanner.nextLine();
        scanner.close();

        crawlDFS(seedURL, MAX_DEPTH);
    }

    // ricerca DFS
    public static Set<String> crawlDFS(Set<String> links, String currentUrl, int maxDepth, int currentDepth) { // se maxDepth è negativo va avanti all'infinito

        // passaggi da fare:
        // trovare i collegamenti all'interno dell'url di root
        // per ogni collegamento:
        //      aggiungere il collegamento al set
        //      chiamare la crawlDFS per ogni url trovato

        // aggiungo l'url al set di link
        links.add(currentUrl);
        System.out.println("Depth: " + currentDepth + " | URL: " + currentUrl);
        if (maxDepth < 0 || maxDepth > currentDepth) {
            try {
                // ottengo gli url contenuti nella pagina del currentUrl
                Document doc = Jsoup.connect(currentUrl).get();
                Elements urls = doc.select("a[href]");

                for (Element element : urls) {
                    String url = element.absUrl("href");
                    if (!links.contains(url))
                        links = crawlDFS(links, url, maxDepth, currentDepth + 1);
                }
            } catch (IOException e) {
                System.out.println("Errore durante l'accesso a: " + currentUrl + " | " + e.getMessage());
            }
        } else {
            return links;
        }
        return links;
    }

    public static Set<String> crawlDFS(String rootUrl, int maxDepth) {
        return crawlDFS(new HashSet<>(), rootUrl, maxDepth, 0);
    }
}