/**
 *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package ca.openosp.openo.managers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import ca.openosp.openo.commn.dao.UserPropertyDAO;
import ca.openosp.openo.commn.model.Provider;
import ca.openosp.openo.commn.model.UserProperty;
import ca.openosp.openo.utility.LoggedInInfo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link ConsultationManagerImpl#formatIssueNotes(List, boolean, String)},
 * which builds the text a section pastes into the consultation request, and for
 * {@link ConsultationManagerImpl#formatPasteNote(LoggedInInfo, List, String, boolean)}, which picks
 * the layout from the provider's preferences.
 *
 * <p>The contract: Single Line joins the notes with commas on one line, Multi Line keeps one
 * note per line; a heading is an inline "Social History:" prefix on Single Line and a
 * =====Social History===== banner and a blank line on Multi Line; an empty section pastes
 * nothing, not even a heading.</p>
 */
@DisplayName("ConsultationManager CPP paste formatting")
@Tag("unit")
@Tag("fast")
@Tag("consultation")
class ConsultationManagerCppPasteFormatUnitTest {

    private static final String PROVIDER_NO = "999998";

    private UserPropertyDAO userPropertyDAO;
    private ConsultationManagerImpl consultationManager;
    private LoggedInInfo loggedInInfo;

    @BeforeEach
    void setUp() {
        userPropertyDAO = mock(UserPropertyDAO.class);
        consultationManager = new ConsultationManagerImpl();
        consultationManager.userPropertyDAO = userPropertyDAO;

        loggedInInfo = new LoggedInInfo();
        loggedInInfo.setLoggedInProvider(new Provider(PROVIDER_NO));
    }

    private void givenPreference(String name, String value) {
        UserProperty property = new UserProperty();
        property.setName(name);
        property.setValue(value);
        when(userPropertyDAO.getProp(PROVIDER_NO, name)).thenReturn(property);
    }

    @Test
    @DisplayName("should join notes with commas when single line without heading")
    void shouldJoinNotesWithCommas_whenSingleLineWithoutHeading() {
        String note = ConsultationManagerImpl.formatIssueNotes(
                Arrays.asList("Smoker", "Lives alone"), true, null);

        assertThat(note).isEqualTo("Smoker, Lives alone\n");
    }

    @Test
    @DisplayName("should prefix the heading on the same line when single line with heading")
    void shouldPrefixHeadingOnSameLine_whenSingleLineWithHeading() {
        String note = ConsultationManagerImpl.formatIssueNotes(
                Arrays.asList("Smoker", "Lives alone"), true, "Social History");

        assertThat(note).isEqualTo("Social History: Smoker, Lives alone\n");
    }

    @Test
    @DisplayName("should collapse newlines inside a note when single line")
    void shouldCollapseNewlinesInsideNote_whenSingleLine() {
        String note = ConsultationManagerImpl.formatIssueNotes(
                Arrays.asList("Smoker \n 10 per day", "Lives alone"), true, null);

        assertThat(note).isEqualTo("Smoker 10 per day, Lives alone\n");
    }

    @Test
    @DisplayName("should keep one note per line when multi line without heading")
    void shouldKeepOneNotePerLine_whenMultiLineWithoutHeading() {
        String note = ConsultationManagerImpl.formatIssueNotes(
                Arrays.asList("Smoker", "Lives alone"), false, null);

        assertThat(note).isEqualTo("Smoker\nLives alone\n");
    }

    @Test
    @DisplayName("should start with a banner and a blank line, then one note per line, when multi line with heading")
    void shouldStartWithBannerThenOneNotePerLine_whenMultiLineWithHeading() {
        String note = ConsultationManagerImpl.formatIssueNotes(
                Arrays.asList("Smoker", "Lives alone"), false, "Social History");

        assertThat(note).isEqualTo("=====Social History=====\n\nSmoker\nLives alone\n");
    }

    @Test
    @DisplayName("should preserve each note's own whitespace when multi line without heading")
    void shouldPreserveNoteWhitespace_whenMultiLineWithoutHeading() {
        String note = ConsultationManagerImpl.formatIssueNotes(
                Arrays.asList("Smoker \n", " Lives alone"), false, null);

        assertThat(note).isEqualTo("Smoker \n\n Lives alone\n");
    }

    @Test
    @DisplayName("should paste nothing when the section has no notes")
    void shouldReturnEmpty_whenNoNotes() {
        String note = ConsultationManagerImpl.formatIssueNotes(
                Collections.emptyList(), false, "Social History");

        assertThat(note).isEmpty();
    }

    @Test
    @DisplayName("should paste nothing, not even the heading, when every note is blank")
    void shouldReturnEmpty_whenNotesAreBlank() {
        String note = ConsultationManagerImpl.formatIssueNotes(
                Arrays.asList(null, "   ", "\n"), true, "Social History");

        assertThat(note).isEmpty();
    }

    @Test
    @DisplayName("should skip blank notes when other notes exist")
    void shouldSkipBlankNotes_whenOtherNotesExist() {
        String note = ConsultationManagerImpl.formatIssueNotes(
                Arrays.asList("Smoker", "  ", null, "Lives alone"), true, null);

        assertThat(note).isEqualTo("Smoker, Lives alone\n");
    }

    @Test
    @DisplayName("should paste one entry per line without a heading when the provider set no preferences")
    void shouldPasteMultiLineWithoutHeading_whenNoPreferencesSet() {
        String note = consultationManager.formatPasteNote(loggedInInfo,
                Arrays.asList("Metformin 500mg", "Ramipril 5mg"), "Active Medications", true);

        assertThat(note).isEqualTo("Metformin 500mg\nRamipril 5mg\n");
    }

    @Test
    @DisplayName("should start with the heading banner when the provider turned headings on")
    void shouldPasteHeadingBanner_whenHeadingPreferenceOn() {
        givenPreference(UserProperty.CONSULT_PASTE_HEADING, "true");

        String note = consultationManager.formatPasteNote(loggedInInfo,
                Arrays.asList("Metformin 500mg", "Ramipril 5mg"), "Active Medications", true);

        assertThat(note).isEqualTo("=====Active Medications=====\n\nMetformin 500mg\nRamipril 5mg\n");
    }

    @Test
    @DisplayName("should prefix the heading on one line when the provider chose single line with headings")
    void shouldPrefixHeadingOnOneLine_whenSingleLineAndHeadingPreferencesOn() {
        givenPreference(UserProperty.CONSULT_PASTE_HEADING, "true");
        givenPreference(UserProperty.CONSULTATION_REQ_PASTE_FMT, "single");

        String note = consultationManager.formatPasteNote(loggedInInfo,
                Arrays.asList("Metformin 500mg", "Ramipril 5mg"), "Long Term Medications", true);

        assertThat(note).isEqualTo("Long Term Medications: Metformin 500mg, Ramipril 5mg\n");
    }

    @Test
    @DisplayName("should leave out the heading when the provider turned headings off")
    void shouldOmitHeading_whenHeadingPreferenceOff() {
        givenPreference(UserProperty.CONSULT_PASTE_HEADING, "false");
        givenPreference(UserProperty.CONSULTATION_REQ_PASTE_FMT, "single");

        String note = consultationManager.formatPasteNote(loggedInInfo,
                Arrays.asList("Metformin 500mg", "Ramipril 5mg"), "Active Medications", true);

        assertThat(note).isEqualTo("Metformin 500mg, Ramipril 5mg\n");
    }

    @Test
    @DisplayName("should ignore the provider's preferences when the field does not apply them")
    void shouldPastePlainOnePerLine_whenPastePreferencesNotApplied() {
        givenPreference(UserProperty.CONSULT_PASTE_HEADING, "true");
        givenPreference(UserProperty.CONSULTATION_REQ_PASTE_FMT, "single");

        String note = consultationManager.formatPasteNote(loggedInInfo,
                Arrays.asList("Metformin 500mg", "Ramipril 5mg"), "Active Medications", false);

        assertThat(note).isEqualTo("Metformin 500mg\nRamipril 5mg\n");
    }
}
