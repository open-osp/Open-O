package ca.openosp.openo.eform.data;

import java.util.regex.Pattern;

import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Forces the DejaVu Sans fonts on eForm text, so the browser and wkhtmltopdf on the server draw
 * it the same way.
 *
 * This is the server half, applied on the way to the PDF. The browser half is applyDejaVuFont()
 * in eform_floating_toolbar.js.
 */
public final class EFormFieldFont {

    /**
     * The family name the fields are forced to. A made-up name, so a form's own font
     * declarations can never clash with it.
     */
    private static final String FIELD_FONT_FAMILY = "OpenO eForm Sans";

    /** Matches an @font-face rule and its block. */
    private static final Pattern FONT_FACE_RULE = Pattern.compile("(?i)@font-face\\s*\\{[^{}]*\\}");

    /**
     * Id of the style written below, so a form already carrying one has it replaced rather than
     * gaining a second. Not the toolbar's "eform-field-font": the toolbar skips its work when it
     * finds that id.
     */
    private static final String FIELD_FONT_STYLE_ID = "eform-field-font-saved";

    /**
     * Two rules that keep a line the same width on both sides. font-synthesis stops the browser
     * inventing a bold or italic it has no file for. text-rendering turns kerning on in
     * wkhtmltopdf, which has it off by default. !important, because some forms carry
     * text-rendering rules of their own.
     */
    private static final String TEXT_STYLE_CSS =
            "*{font-synthesis:none;}*{text-rendering:optimizeLegibility !important;}";

    /**
     * Form controls drawn with CSS. The PDF converter paints the native checkbox, radio, select
     * arrow and slider at a fixed device-pixel size, so at the eForm resolution they shrink to
     * dots. The problem is in wkhtmltopdf itself, reported and still open:
     * https://github.com/wkhtmltopdf/wkhtmltopdf/issues/4216. A CSS drawing scales with the
     * page. Only minimum sizes are set, the browser's defaults, so any width and height the
     * form gives its controls still apply.
     */
    private static final String FORM_CONTROL_CSS =
            "input[type=checkbox],input[type=radio]{-webkit-appearance:none;min-width:13px;min-height:13px;"
            + "border:1px solid #000;background:#fff;vertical-align:middle;}"
          + "input[type=radio]{border-radius:50%;}"
          + "input:checked{background:#000;box-shadow:inset 0 0 0 2px #fff;}"
          + "select:not([multiple]):not([size]),select[size='0']:not([multiple]),select[size='1']:not([multiple])"
            + "{-webkit-appearance:none;border:1px solid #000;border-radius:0;"
            + "padding:1px 20px 1px 3px;background-color:#fff;background-repeat:no-repeat;background-position:100% 50%;"
            + "background-size:7px 7px,14px 7px;background-image:"
            + "-webkit-gradient(linear,0 0,7 7,color-stop(0.5,#000),color-stop(0.5,#fff)),"
            + "-webkit-gradient(linear,0 7,7 0,color-stop(0.5,transparent),color-stop(0.5,#000));}"
          + "input[type=range]{-webkit-appearance:none;min-width:120px;min-height:10px;border:0;vertical-align:middle;"
            + "background:-webkit-gradient(linear,left top,left bottom,from(#000),to(#000)) no-repeat 0 50%;"
            + "background-size:100% 2px;}"
          + "input[type=range]::-webkit-slider-thumb{-webkit-appearance:none;width:10px;height:10px;border:0;"
            + "border-radius:50%;background:#000;}";

    /** The forced family, mapped to the DejaVu Sans fonts installed on the server. */
    private static final String INSTALLED_FONT_CSS =
            "@font-face{font-family:'" + FIELD_FONT_FAMILY + "';font-weight:normal;font-style:normal;src:local('DejaVu Sans');}"
          + "@font-face{font-family:'" + FIELD_FONT_FAMILY + "';font-weight:bold;font-style:normal;src:local('DejaVu Sans');}"
          + "@font-face{font-family:'" + FIELD_FONT_FAMILY + "';font-weight:normal;font-style:italic;src:local('DejaVu Sans');}"
          + "@font-face{font-family:'" + FIELD_FONT_FAMILY + "';font-weight:bold;font-style:italic;src:local('DejaVu Sans');}";

    /**
     * Every field a provider types into, plus the body so labels inherit the font. Elements
     * that name their own font, such as icons and the toolbar, are left alone. Keep identical
     * to the selector in eform_floating_toolbar.js.
     */
    private static final String FONT_SELECTOR =
            "html body,"
            + "input:not([type=button]):not([type=submit]):not([type=reset])"
            + ":not([type=image]):not([type=checkbox]):not([type=radio]):not([type=file])"
            + ":not([type=hidden]):not(#remote_eform_subject),"
            + "select,"
            + "textarea,"
            + "[contenteditable]:not([contenteditable=false])";

    private EFormFieldFont() {
    }

    /**
     * Removes the form's own @font-face rules, which the PDF converter cannot load, and writes one
     * style mapping the field font to the DejaVu Sans installed on the server and drawing
     * checkboxes and radios as CSS boxes.
     *
     * @param document org.jsoup.nodes.Document the parsed eForm
     */
    public static void apply(Document document) {
        if (document == null) {
            return;
        }

        document.select("style#" + FIELD_FONT_STYLE_ID).remove();
        for (Element style : document.getElementsByTag("style")) {
            String css = style.data();
            String stripped = FONT_FACE_RULE.matcher(css).replaceAll("");
            if (!stripped.equals(css)) {
                style.empty();
                style.appendChild(new DataNode(stripped));
            }
        }

        Element head = document.head();
        if (head == null) {
            return;
        }

        Element style = document.createElement("style");
        style.attr("id", FIELD_FONT_STYLE_ID);
        // a DataNode, not text: jsoup escapes a text node even inside <style>
        style.appendChild(new DataNode(TEXT_STYLE_CSS + FORM_CONTROL_CSS + INSTALLED_FONT_CSS
                + FONT_SELECTOR + "{font-family:'" + FIELD_FONT_FAMILY + "',sans-serif !important;}"));
        head.appendChild(style);
    }
}
