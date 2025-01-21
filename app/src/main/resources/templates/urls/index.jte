@import hexlet.code.util.NamedRoutes
@import hexlet.code.dto.UrlsPage
@import java.time.format.DateTimeFormatter
@param UrlsPage page

!{var formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");}

@template.layout.page(
page = page,
content = @`
    <div class="container-lg mt-3">
        <div class="row justify-content-center">
            <h3 class="display-5">Сайты</h3>
            @if (page.getUrls().isEmpty())
                <p class="lead mt-3">Пока не было добавлено ни одного URL</p>
            @else
                <table class="table table-bordered table-hover mt-3">
                    <thead>
                    <tr>
                        <th scope="col">ID</th>
                        <th scope="col">Имя</th>
                        <th scope="col">Последняя проверка</th>
                        <th scope="col">Код ответа</th>
                    </tr>
                    </thead>
                    <tbody>
                    @for(var url: page.getUrls())
                        <tr>
                            <th scope="row">${url.getId()}</th>
                            <td>
                                <a href="${NamedRoutes.urlPath(url.getId().toString())}">${url.getName()}</a>
                            </td>
                            <td>
                                ${page.getChecks().isEmpty() || page.getChecks().get(url.getId()) == null ? "" : page.getChecks().get(url.getId()).getCreatedAt().toLocalDateTime().format(formatter)}
                            </td>
                            <td>
                                ${page.getChecks().isEmpty() || page.getChecks().get(url.getId()) == null ? "" : page.getChecks().get(url.getId()).getStatusCode().toString()}
                            </td>
                        </tr>
                    @endfor
                    </tbody>
                </table>
            @endif
        </div>
    </div>
`
)