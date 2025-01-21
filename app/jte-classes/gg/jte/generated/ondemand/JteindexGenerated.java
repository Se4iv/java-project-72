package gg.jte.generated.ondemand;
import hexlet.code.util.NamedRoutes;
import hexlet.code.dto.BasePage;
public final class JteindexGenerated {
	public static final String JTE_NAME = "index.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,2,2,4,4,7,7,8,8,8,8,8,8,8,8,8,49,49,49,49,49,2,2,2,2};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, BasePage page) {
		jteOutput.writeContent("\n");
		gg.jte.generated.ondemand.layout.JtepageGenerated.render(jteOutput, jteHtmlInterceptor, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\n    <form");
				var __jte_html_attribute_0 = NamedRoutes.urlsPath();
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
					jteOutput.writeContent(" action=\"");
					jteOutput.setContext("form", "action");
					jteOutput.writeUserContent(__jte_html_attribute_0);
					jteOutput.setContext("form", null);
					jteOutput.writeContent("\"");
				}
				jteOutput.writeContent(" method=\"post\">\n        <div>\n            <div class=\"container\">\n                <div class=\"row justify-content-center\">\n\n                    <div class=\"col-10\">\n                        <h1 class=\"display-3\">Анализатор страниц</h1>\n                        <p class=\"lead\">\n                            Бесплатно проверяйте сайты на\n                            <a href=\"https://ru.wikipedia.org/wiki/Поисковая_оптимизация\">SEO</a>\n                            пригодность\n                        </p>\n\n                        <form>\n                            <div class=\"row\">\n                                <div class=\"col-md-10 form-floating\">\n                                    <input type=\"text\"\n                                           class=\"form-control form-control-lg text-start\"\n                                           required name=\"url\"\n                                           id=\"urlInput\"\n                                           placeholder=\"Ваша ссылка (до 100 символов)\"\n                                           maxlength=\"100\">\n                                    <label for=\"urlInput\" style=\"margin-left: 12px\">Ваша ссылка (до 100 символов)</label>\n                                </div>\n                                <div class=\"col-auto\">\n                                    <button type=\"submit\" class=\"btn btn-primary btn-lg\">Проверить</button>\n                                </div>\n                            </div>\n                            <div class=\"row\">\n                                <p class=\"lead text-body-secondary\">\n                                    <small>\n                                        Пример: https://www.example.com\n                                    </small>\n                                </p>\n                            </div>\n                        </form>\n                    </div>\n                </div>\n            </div>\n        </div>\n    </form>\n");
			}
		}, page);
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		BasePage page = (BasePage)params.get("page");
		render(jteOutput, jteHtmlInterceptor, page);
	}
}
