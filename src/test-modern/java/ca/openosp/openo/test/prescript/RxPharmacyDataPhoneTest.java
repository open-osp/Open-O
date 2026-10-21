package ca.openosp.openo.test.prescript;

import ca.openosp.openo.commn.model.PharmacyInfo;
import ca.openosp.openo.prescript.data.RxPharmacyData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link RxPharmacyData#composePharmacyPhone(PharmacyInfo)}, the helper
 * shared by the printed Rx pharmacy address block and the "Rx faxed to" chart note.
 *
 * <p>The helper must join phone1 and phone2 with a single space while skipping whichever is
 * absent, so that neither output ever shows a stray separator, a literal "null", or a dangling
 * "Tel:" label. Stored phone formats are mixed free text and must be returned verbatim.</p>
 *
 * <p>The helper is static and {@link RxPharmacyData}'s Spring lookups are instance fields, so no
 * container is needed and the class is never instantiated here.</p>
 *
 * @since 2026-08-13
 */
@Tag("unit")
@Tag("rx")
@DisplayName("RxPharmacyData.composePharmacyPhone")
class RxPharmacyDataPhoneTest {

    /**
     * Builds a pharmacy carrying only the two phone fields under test.
     *
     * @param phone1 String the primary phone number, may be null
     * @param phone2 String the secondary phone number, may be null
     * @return PharmacyInfo a pharmacy with the given phone numbers
     */
    private PharmacyInfo pharmacyWithPhones(String phone1, String phone2) {
        PharmacyInfo pharmacy = new PharmacyInfo();
        pharmacy.setPhone1(phone1);
        pharmacy.setPhone2(phone2);
        return pharmacy;
    }

    @Test
    @DisplayName("should join both numbers with a single space when both are present")
    void shouldJoinBothNumbers_whenBothPresent() {
        String phone = RxPharmacyData.composePharmacyPhone(
                pharmacyWithPhones("(416) 269-4820", "416-555-0000"));

        assertThat(phone).isEqualTo("(416) 269-4820 416-555-0000");
    }

    @Test
    @DisplayName("should trim both numbers and still join them with a single space")
    void shouldTrimBothNumbers_whenBothArePadded() {
        String phone = RxPharmacyData.composePharmacyPhone(
                pharmacyWithPhones("  (416) 269-4820  ", "  416-555-0000  "));

        assertThat(phone).isEqualTo("(416) 269-4820 416-555-0000");
    }

    @Test
    @DisplayName("should return phone1 alone when phone2 is absent")
    void shouldReturnPhone1Alone_whenPhone2Absent() {
        assertThat(RxPharmacyData.composePharmacyPhone(pharmacyWithPhones("(416) 269-4820", null)))
                .isEqualTo("(416) 269-4820");
        assertThat(RxPharmacyData.composePharmacyPhone(pharmacyWithPhones("(416) 269-4820", "")))
                .isEqualTo("(416) 269-4820");
    }

    @Test
    @DisplayName("should return phone2 alone without a leading separator when phone1 is absent")
    void shouldReturnPhone2Alone_whenPhone1Absent() {
        String phone = RxPharmacyData.composePharmacyPhone(pharmacyWithPhones(null, "416-555-0000"));

        assertThat(phone).isEqualTo("416-555-0000");
        assertThat(phone).doesNotStartWith(" ").doesNotContain("null");
    }

    @Test
    @DisplayName("should return empty when no phone numbers are on file")
    void shouldReturnEmpty_whenNoPhoneNumbersOnFile() {
        assertThat(RxPharmacyData.composePharmacyPhone(pharmacyWithPhones(null, null))).isEmpty();
        assertThat(RxPharmacyData.composePharmacyPhone(pharmacyWithPhones("", ""))).isEmpty();
        assertThat(RxPharmacyData.composePharmacyPhone(pharmacyWithPhones("   ", "  "))).isEmpty();
    }

    @Test
    @DisplayName("should return empty when the pharmacy is null")
    void shouldReturnEmpty_whenPharmacyIsNull() {
        assertThat(RxPharmacyData.composePharmacyPhone(null)).isEmpty();
    }

    @Test
    @DisplayName("should preserve stored formatting verbatim")
    void shouldPreserveStoredFormatting_verbatim() {
        assertThat(RxPharmacyData.composePharmacyPhone(pharmacyWithPhones("(604) 707-5989", null)))
                .isEqualTo("(604) 707-5989");
        assertThat(RxPharmacyData.composePharmacyPhone(pharmacyWithPhones("14162694819", null)))
                .isEqualTo("14162694819");
    }

    @Test
    @DisplayName("should collapse embedded newlines so the note stays on one line")
    void shouldCollapseEmbeddedNewlines_soNoteStaysOnOneLine() {
        String phone = RxPharmacyData.composePharmacyPhone(
                pharmacyWithPhones("416-555-0000\r\next 123", null));

        assertThat(phone).isEqualTo("416-555-0000 ext 123");
        assertThat(phone).doesNotContain("\n").doesNotContain("\r");
    }
}
