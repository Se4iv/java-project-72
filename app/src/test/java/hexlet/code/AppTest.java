package hexlet.code;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.StringJoiner;

import hexlet.code.model.Url;
import hexlet.code.repository.BaseRepository;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import hexlet.code.util.NormalizedData;
import hexlet.code.util.Time;

import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.mockwebserver.MockResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class AppTest {
    private static Javalin app;
    private static MockWebServer mockServer;
    private static final String URL_SIMPLE = "https://example.com";
    private static final String URL_WITH_PORT = "https://example.com:8080";
    private static final String URL_COMPLEX = "https://example.com:8080/example/unnesessary/long/url/address";
    private static final String URL_INCORRECT = "123QWE";
    private static final String URL_WRAPPER = "url=[%s]";
    private static String urlName;
    private static final String HTML_PATH = "src/test/resources/index.html";

    @BeforeAll
    public static void beforeAllTests() throws IOException {
        mockServer = new MockWebServer();
        urlName = mockServer.url("/").toString();
        var mockResponse = new MockResponse().setBody(getContentFromHtml());
        mockServer.enqueue(mockResponse);
    }

    @BeforeEach
    public final void setUpTests() throws IOException, SQLException {
        app = App.getApp();
    }

    @AfterEach
    public final void closeConnection() {
        if (BaseRepository.dataSource != null) {
            BaseRepository.dataSource.close();
        }
    }

    @AfterAll
    public static void afterAllTests() throws IOException {
        mockServer.shutdown();
    }

    public static String getContentFromHtml() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(HTML_PATH));
        String lineOfFile = reader.readLine();
        var result = new StringJoiner("\n");

        while (lineOfFile != null) {
            result.add(lineOfFile);
            lineOfFile = reader.readLine();
        }
        return result.toString();
    }

    @Test
    public void testRootPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.rootPath());
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains(
                    "Анализатор страниц",
                    "Бесплатно проверяйте сайты на",
                    "<a href=\"https://ru.wikipedia.org/wiki/Поисковая_оптимизация\">SEO</a>",
                    "пригодность");
        });
    }

    @Test
    public void testUrlsPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlsPath());
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Сайты");
        });
    }

    @Test
    public void testCreateUrl() {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=" + URL_SIMPLE;
            var response = client.post(NamedRoutes.urlsPath(), requestBody);
            var urlsList = UrlRepository.getEntities();
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string().contains(URL_SIMPLE));
            assertThat(urlsList).hasSize(1);
            assertThat(urlsList.get(0).getName()).isEqualTo(URL_SIMPLE);
        });
    }

    @Test
    public void testCreateInaccurateUrl() {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = String.format(URL_WRAPPER, "   hTtPs://ExAmPle.COM...  ");
            var response = client.post(NamedRoutes.urlsPath(), requestBody);
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string().contains(URL_SIMPLE));
        });
    }

    @Test
    public void testCreateUrlOnlyDomainProtocolAndPort() throws SQLException {
        var url = new Url(URL_WITH_PORT);
        UrlRepository.save(url);
        JavalinTest.test(app, (server, client) -> {
            var requestBody = String.format(URL_WRAPPER, URL_COMPLEX);
            var response = client.post(NamedRoutes.urlsPath(), requestBody);
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string().contains(URL_WITH_PORT));
            assertThat(UrlRepository.getEntities()).hasSize(1);
        });
    }

    @Test
    public void testCreateIncorrectUrl() {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = String.format(URL_WRAPPER, URL_INCORRECT);
            var response = client.post(NamedRoutes.urlsPath(), requestBody);
            assertThat(response.code()).isEqualTo(200);
            assertThat(UrlRepository.getEntities()).hasSize(0);
        });
    }

    @Test
    public void testCreateExistingUrl() throws SQLException {
        var url = new Url(URL_SIMPLE);
        UrlRepository.save(url);
        JavalinTest.test(app, (server, client) -> {
            var requestBody = String.format(URL_WRAPPER, URL_SIMPLE);
            var response = client.post(NamedRoutes.urlsPath(), requestBody);
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string().contains(URL_SIMPLE));
            assertThat(UrlRepository.getEntities()).hasSize(1);

            var response2 = client.post(NamedRoutes.urlsPath(), requestBody);
            assertThat(response2.code()).isEqualTo(200);
            assertThat(UrlRepository.getEntities()).hasSize(1);
        });
    }

    @Test
    public void testUrlPage() throws SQLException {
        var url = new Url(URL_SIMPLE);
        UrlRepository.save(url);
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlPath(url.getId()));
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains(
                    "Сайт:", url.getName(), "ID", url.getId().toString(), "Имя", url.getName(), "Дата добавления",
                    "Проверки:"
            );
            assertThat(UrlRepository.getEntities()).hasSize(1);
        });
    }

    @Test
    public void testNotFoundUrl() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlPath(99999999999999999L));
            assertThat(response.code()).isEqualTo(404);
        });
    }

    @Test
    public void testCheckUrl() throws SQLException {
        var url = new Url(urlName);
        UrlRepository.save(url);

        JavalinTest.test(app, (server1, client) -> {
            var listOfLastChecks = UrlCheckRepository.getListOfLastChecks();
            assertThat(listOfLastChecks.size()).isEqualTo(0);
            assertThat(listOfLastChecks.get(0L)).isNull();
            var response = client.post(NamedRoutes.urlChecksPath(url.getId()));
            assertThat(response.code()).isEqualTo(200);
            var urlCheck = UrlCheckRepository.getListOfLastChecks().get(url.getId());
            var title = urlCheck.getTitle();
            var h1 = urlCheck.getH1();
            var description = urlCheck.getDescription();

            assertThat(title).isEqualTo("This is a Title =^_^=");
            assertThat(h1).isEqualTo("This is kinda header");
            assertThat(description).isEqualTo("some description text for tests");
            var listOfLastChecks1 = UrlCheckRepository.getListOfLastChecks();
            assertThat(listOfLastChecks1.size()).isEqualTo(1);
            var lastCheck = listOfLastChecks1.get(1L);
            assertThat(lastCheck).isNotNull();
        });
    }

    @Test
    public void testUrlNormalizer() throws URISyntaxException, MalformedURLException {
        var url = new URI(URL_COMPLEX).toURL();
        assertThat(NormalizedData.getNormalizedUrl(url)).isEqualTo(URL_WITH_PORT);
    }

    @Test
    public void testFormattedCreatedAt() {
        var time = Time.getTime();
        var simpleTime = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        assertThat(Time.getSimpleTime(time)).isEqualTo(simpleTime.format(time));
    }

    @Test
    public void testFindUrlByNameAndIsUrlExists() throws SQLException {
        var url = new Url(URL_SIMPLE);
        assertThat(UrlRepository.findUrlByName(URL_SIMPLE)).isNull();
        assertThat(UrlRepository.isUrlExists(URL_SIMPLE)).isFalse();
        UrlRepository.save(url);
        assertThat(UrlRepository.findUrlByName(URL_SIMPLE).getName()).isEqualTo(url.getName());
        assertThat(UrlRepository.isUrlExists(URL_SIMPLE)).isTrue();
    }
}