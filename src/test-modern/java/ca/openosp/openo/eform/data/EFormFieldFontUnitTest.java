package ca.openosp.openo.eform.data;

import ca.openosp.openo.documentManager.ConvertToEdoc;
import ca.openosp.openo.managers.NioFileManager;
import ca.openosp.openo.test.unit.OpenOUnitTestBase;

import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link EFormFieldFont#apply(Document)}, which runs on the way to the PDF.
 *
 * <p>The method forces the DejaVu Sans fonts onto the form's fields so the browser and the PDF
 * draw the same text: it removes the form's own @font-face rules, which the PDF converter cannot
 * load, and writes one style that maps the field font to the DejaVu Sans installed on the server.
 * These tests pin down that contract: the form's font declarations go, the rest of its CSS stays,
 * the written style carries no file paths, and applying it twice replaces the style instead of
 * stacking copies.</p>
 */
@DisplayName("EFormFieldFont.apply")
@Tag("unit")
@Tag("fast")
@Tag("eform")
class EFormFieldFontUnitTest extends OpenOUnitTestBase {

    private static final String STYLE_ID = "eform-field-font-saved";
    private static final String FORCED_FAMILY = "OpenO eForm Sans";

    @BeforeEach
    void registerStaticDependencies() {
        // ConvertToEdoc resolves this once at class load
        registerMock(NioFileManager.class, mock(NioFileManager.class));
    }

    /** Parses and serialises the way the PDF path does, so the test sees production output. */
    private String applyTo(String html) {
        Document document = ConvertToEdoc.getDocument(html);
        EFormFieldFont.apply(document);
        return ConvertToEdoc.documentToString(document);
    }

    @Test
    @DisplayName("should remove the form's @font-face rules when the eForm is rendered")
    void shouldRemoveFontFaceRules_whenEFormIsRendered() {
        String html = "<!doctype html><html><head><style>"
                + "@font-face { font-family: 'DejaVu Sans'; src: url('../library/fonts/DejaVuSans.ttf'); }"
                + "</style></head><body><div style=\"font-family:'DejaVu Sans';\">text</div></body></html>";

        String rendered = applyTo(html);

        assertThat(rendered).doesNotContain("url('../library/fonts/DejaVuSans.ttf')");
    }

    @Test
    @DisplayName("should remove every @font-face rule regardless of case")
    void shouldRemoveAllFontFaceRules_whenFormHasSeveral() {
        String html = "<!doctype html><html><head><style>"
                + "@font-face { font-family: A; src: url('a.ttf'); }"
                + "@FONT-FACE { font-family: B; src: url('b.woff'); }"
                + "</style><style>"
                + "@font-face{font-family:C;src:url('c.otf')}"
                + "</style></head><body>text</body></html>";

        String rendered = applyTo(html);

        assertThat(rendered).doesNotContain("a.ttf").doesNotContain("b.woff").doesNotContain("c.otf");
    }

    @Test
    @DisplayName("should keep the form's other CSS rules when removing @font-face")
    void shouldKeepOtherCssRules_whenEFormIsRendered() {
        String html = "<!doctype html><html><head><style>"
                + ".noborder { border: 0; overflow: hidden; }"
                + "@font-face { font-family: X; src: url('x.ttf'); }"
                + "body { background: #fff; }"
                + "</style></head><body>text</body></html>";

        String rendered = applyTo(html);

        assertThat(rendered).contains(".noborder { border: 0; overflow: hidden; }");
        assertThat(rendered).contains("body { background: #fff; }");
        assertThat(rendered).doesNotContain("x.ttf");
    }

    @Test
    @DisplayName("should leave @font-face alone when it is visible text, not a stylesheet")
    void shouldKeepFontFaceText_whenItIsVisibleContent() {
        String html = "<!doctype html><html><head></head><body>"
                + "<div>@font-face { font-family: shown; src: url('shown.ttf'); }</div>"
                + "</body></html>";

        String rendered = applyTo(html);

        assertThat(rendered).contains("shown.ttf");
    }

    @Test
    @DisplayName("should write the field font style when the eForm is rendered")
    void shouldWriteFieldFontStyle_whenEFormIsRendered() {
        String rendered = applyTo("<!doctype html><html><head></head><body>text</body></html>");

        assertThat(rendered).contains("id=\"" + STYLE_ID + "\"");
        assertThat(rendered).contains("font-family:'" + FORCED_FAMILY + "',sans-serif !important");
        assertThat(rendered).contains("src:local('DejaVu Sans')");
        assertThat(rendered).contains("text-rendering:optimizeLegibility !important");
        assertThat(rendered).contains("font-synthesis:none");
    }

    @Test
    @DisplayName("should write no file paths, so the form renders on any server")
    void shouldWriteNoFilePaths_whenEFormIsRendered() {
        String rendered = applyTo("<!doctype html><html><head></head><body>text</body></html>");

        int styleStart = rendered.indexOf(STYLE_ID);
        int styleEnd = rendered.indexOf("</style>", styleStart);
        assertThat(rendered.substring(styleStart, styleEnd)).doesNotContain("url(");
    }

    @Test
    @DisplayName("should write the style unescaped, so a CSS combinator still selects")
    void shouldWriteStyleUnescaped_whenSelectorHasCombinator() {
        String rendered = applyTo("<!doctype html><html><head></head><body>text</body></html>");

        int styleStart = rendered.indexOf(STYLE_ID);
        int styleEnd = rendered.indexOf("</style>", styleStart);
        assertThat(rendered.substring(styleStart, styleEnd))
                .doesNotContain("&gt;").doesNotContain("&lt;").doesNotContain("&amp;");
    }

    @Test
    @DisplayName("should replace its style when the eForm is rendered again")
    void shouldReplaceOwnStyle_whenEFormIsRenderedAgain() {
        String once = applyTo("<!doctype html><html><head><style>"
                + "@font-face { font-family: X; src: url('x.ttf'); }"
                + "</style></head><body>text</body></html>");

        String twice = applyTo(once);

        assertThat(countOf(twice, STYLE_ID)).isEqualTo(countOf(once, STYLE_ID)).isEqualTo(1);
        assertThat(twice).isEqualTo(once);
    }

    private static int countOf(String html, String token) {
        int count = 0;
        for (int at = html.indexOf(token); at > -1; at = html.indexOf(token, at + 1)) {
            count++;
        }
        return count;
    }

    @Test
    @DisplayName("should draw checkboxes and radios as CSS boxes when the eForm is rendered")
    void shouldDrawFormControlsAsCssBoxes_whenEFormIsRendered() {
        String html = "<!doctype html><html><head></head><body><input type=\"checkbox\" checked></body></html>";

        String rendered = applyTo(html);

        assertThat(rendered).contains("input[type=checkbox],input[type=radio]{-webkit-appearance:none;min-width:13px;");
        assertThat(rendered).contains("input:checked{background:#000;");
        assertThat(rendered).contains("select:not([multiple]):not([size]),select[size='0']:not([multiple]),select[size='1']:not([multiple]){-webkit-appearance:none;");
        assertThat(rendered).contains("input[type=range]::-webkit-slider-thumb{-webkit-appearance:none;");
    }
}
