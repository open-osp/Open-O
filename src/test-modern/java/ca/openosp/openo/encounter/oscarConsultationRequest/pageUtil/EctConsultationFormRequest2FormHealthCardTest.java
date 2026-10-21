package ca.openosp.openo.encounter.oscarConsultationRequest.pageUtil;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link EctConsultationFormRequest2Form#getFormattedHealthCard()}.
 *
 * <p>The health card is displayed as "number version (card type)". The version code and the
 * card type are both two characters drawn from the same alphabet, so a purely space-separated
 * rendering leaves a version code such as AB looking like a second province code; the
 * parentheses are what disambiguate them.</p>
 *
 * <p>The optional parts are frequently absent - {@code demographic.ver} and
 * {@code demographic.hc_type} are both nullable - so the cases below pin down that a missing
 * part is dropped entirely rather than leaving a stray gap or an empty pair of parentheses.</p>
 *
 * @since 2026-08-20
 */
@DisplayName("EctConsultationFormRequest2Form - Health Card Formatting")
@Tag("unit")
@Tag("fast")
@Tag("encounter")
public class EctConsultationFormRequest2FormHealthCardTest {

    /** Fails the Ontario health-number check digit, so it cannot match a real card. */
    private static final String NUMBER = "1234567890";
    private static final String VERSION_CODE = "AB";
    private static final String CARD_TYPE = "ON";

    private static EctConsultationFormRequest2Form formWith(String number, String versionCode, String cardType) {
        EctConsultationFormRequest2Form form = new EctConsultationFormRequest2Form();
        form.setPatientHealthNum(number);
        form.setPatientHealthCardVersionCode(versionCode);
        form.setPatientHealthCardType(cardType);
        return form;
    }

    @Nested
    @DisplayName("all parts present")
    class AllPartsPresent {

        @Test
        @DisplayName("should render number, version code and parenthesised card type")
        void shouldRenderAllParts_whenAllPresent() {
            assertThat(formWith(NUMBER, VERSION_CODE, CARD_TYPE).getFormattedHealthCard())
                    .isEqualTo("1234567890 AB (ON)");
        }

        @Test
        @DisplayName("should separate the version code from the card type")
        void shouldDisambiguateVersionCodeFromCardType_whenBothLookLikeProvinceCodes() {
            // A version code of ON on a BC card is the worst case: spacing alone would render
            // "1234567890 ON BC", which reads as two jurisdictions.
            assertThat(formWith(NUMBER, "ON", "BC").getFormattedHealthCard())
                    .isEqualTo("1234567890 ON (BC)");
        }
    }

    @Nested
    @DisplayName("optional parts absent")
    class OptionalPartsAbsent {

        @Test
        @DisplayName("should omit the version code when it is not on file")
        void shouldOmitVersionCode_whenNull() {
            assertThat(formWith(NUMBER, null, CARD_TYPE).getFormattedHealthCard())
                    .isEqualTo("1234567890 (ON)");
        }

        @Test
        @DisplayName("should omit the card type when it is not on file")
        void shouldOmitCardType_whenNull() {
            assertThat(formWith(NUMBER, VERSION_CODE, null).getFormattedHealthCard())
                    .isEqualTo("1234567890 AB");
        }

        @Test
        @DisplayName("should render the number alone when both optional parts are absent")
        void shouldRenderNumberOnly_whenBothOptionalPartsNull() {
            assertThat(formWith(NUMBER, null, null).getFormattedHealthCard())
                    .isEqualTo("1234567890");
        }

        @Test
        @DisplayName("should return an empty string when nothing is on file")
        void shouldReturnEmpty_whenNothingOnFile() {
            assertThat(formWith(null, null, null).getFormattedHealthCard()).isEmpty();
        }

        @Test
        @DisplayName("should render the version code and card type when the number is absent")
        void shouldRenderVersionAndType_whenNumberAbsent() {
            // Each part is rendered on its own merits, mirroring the demographic master file where
            // HC Type occupies its own row regardless of whether a health number is on file.
            assertThat(formWith(null, VERSION_CODE, CARD_TYPE).getFormattedHealthCard())
                    .isEqualTo("AB (ON)");
        }

        @Test
        @DisplayName("should render the card type alone when number and version code are absent")
        void shouldRenderTypeOnly_whenNumberAndVersionAbsent() {
            // Reachable in practice: the card type defaults to a province on new demographics,
            // so it is commonly set before any health number has been entered.
            assertThat(formWith(null, null, CARD_TYPE).getFormattedHealthCard())
                    .isEqualTo("(ON)");
        }

        @Test
        @DisplayName("should render the version code alone when number and card type are absent")
        void shouldRenderVersionOnly_whenNumberAndTypeAbsent() {
            assertThat(formWith("", VERSION_CODE, "").getFormattedHealthCard())
                    .isEqualTo("AB");
        }

        @Test
        @DisplayName("should treat blank and whitespace-only parts as absent")
        void shouldTreatBlankAsAbsent_whenPartsAreEmptyOrWhitespace() {
            assertThat(formWith(NUMBER, "", "").getFormattedHealthCard()).isEqualTo("1234567890");
            assertThat(formWith(NUMBER, "   ", "\t").getFormattedHealthCard()).isEqualTo("1234567890");
        }
    }

    @Nested
    @DisplayName("output hygiene")
    class OutputHygiene {

        @Test
        @DisplayName("should never emit empty parentheses")
        void shouldNeverEmitEmptyParentheses_whenCardTypeAbsent() {
            for (String cardType : new String[]{null, "", "  "}) {
                assertThat(formWith(NUMBER, VERSION_CODE, cardType).getFormattedHealthCard())
                        .doesNotContain("(")
                        .doesNotContain(")");
            }
        }

        @Test
        @DisplayName("should never emit a doubled or trailing space")
        void shouldNeverEmitStraySpacing_whenPartsAreAbsent() {
            String[][] cases = {
                    {NUMBER, null, CARD_TYPE},
                    {NUMBER, "", CARD_TYPE},
                    {NUMBER, VERSION_CODE, null},
                    {NUMBER, null, null},
                    {null, VERSION_CODE, CARD_TYPE},
            };
            for (String[] c : cases) {
                String formatted = formWith(c[0], c[1], c[2]).getFormattedHealthCard();
                assertThat(formatted).doesNotContain("  ");
                assertThat(formatted).isEqualTo(formatted.trim());
            }
        }
    }

    @Nested
    @DisplayName("encoding boundary")
    class EncodingBoundary {

        @Test
        @DisplayName("should return raw text so the caller can encode without double-encoding")
        void shouldReturnRawText_whenPartsContainHtmlSpecialCharacters() {
            // Encoding belongs at the JSP output boundary (Encode.forHtml). If this method were
            // to encode as well, the rendered value would be double-encoded.
            assertThat(formWith("<&'\"", VERSION_CODE, CARD_TYPE).getFormattedHealthCard())
                    .isEqualTo("<&'\" AB (ON)");
        }
    }
}
