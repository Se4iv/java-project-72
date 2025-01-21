@import hexlet.code.util.NamedRoutes
@import hexlet.code.dto.BasePage
@param BasePage page

@template.layout.page(
page = page,
content = @`
    <form action="${NamedRoutes.urlsPath()}" method="post">
        <div>
            <div class="container">
                <div class="row justify-content-center">

                    <div class="col-10">
                        <h1 class="display-3">Анализатор страниц</h1>
                        <p class="lead">
                            Бесплатно проверяйте сайты на
                            <a href="https://ru.wikipedia.org/wiki/Поисковая_оптимизация">SEO</a>
                            пригодность
                        </p>

                        <form>
                            <div class="row">
                                <div class="col-md-10 form-floating">
                                    <input type="text"
                                           class="form-control form-control-lg text-start"
                                           required name="url"
                                           id="urlInput"
                                           placeholder="Ваша ссылка (до 100 символов)"
                                           maxlength="100">
                                    <label for="urlInput" style="margin-left: 12px">Ваша ссылка (до 100 символов)</label>
                                </div>
                                <div class="col-auto">
                                    <button type="submit" class="btn btn-primary btn-lg">Проверить</button>
                                </div>
                            </div>
                            <div class="row">
                                <p class="lead text-body-secondary">
                                    <small>
                                        Пример: https://www.example.com
                                    </small>
                                </p>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </form>
`
)