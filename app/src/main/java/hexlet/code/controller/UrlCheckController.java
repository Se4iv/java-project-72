package hexlet.code.controller;

import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.sql.SQLException;

public class UrlCheckController {
    public static void create(Context ctx) throws SQLException {
        var urlId = ctx.pathParamAsClass("id", Long.class)
                .get();
        var url = UrlRepository.find(urlId).orElseThrow(NotFoundResponse::new);

        try {
            HttpResponse<String> response = Unirest.get(url.getName()).asString();
            Document doc = Jsoup.parse(response.getBody());

            int statusCode = response.getStatus();
            String title = doc.title();

            Element h1Element = doc.selectFirst("h1");
            var h1 = h1Element == null ? "" : h1Element.text();

            var descriptionElement = doc.selectFirst("meta[name=description]");
            var description = descriptionElement == null ? "" : descriptionElement.attr("content");

            var urlCheck = new UrlCheck(statusCode, title, h1, description, urlId);
            UrlCheckRepository.save(urlCheck);

            ctx.sessionAttribute("flash", "URL checked");
            ctx.sessionAttribute("flash-type", "success");
        } catch (Exception e) {
            ctx.sessionAttribute("flash", "Unsuccessful check");
            ctx.sessionAttribute("flash-type", "warning");
        }

        ctx.redirect(NamedRoutes.urlPath(urlId));
    }
}
