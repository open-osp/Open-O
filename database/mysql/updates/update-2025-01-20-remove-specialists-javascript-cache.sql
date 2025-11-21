--
-- Remove specialistsJavascript table - legacy JavaScript generation cache
--
-- This table stored dynamically-generated JavaScript code for consultation lookups.
-- It has been replaced with a modern AJAX approach via ConsultationLookup2Action.
--
-- The actual data remains in the proper relational tables:
--   - consultationServices (service categories)
--   - professionalSpecialists (specialist contact info)
--   - serviceSpecialists (many-to-many join)
--
-- Migration Date: 2025-01-20
-- Related Changes:
--   - New: ConsultationLookup2Action.java (AJAX endpoints)
--   - Updated: ConsultationFormRequest.jsp (uses AJAX instead of embedded JavaScript)
--   - Removed: JavaScript generation from admin actions
--

DROP TABLE IF EXISTS specialistsJavascript;

-- Note: The following classes/DAOs can be removed in a future cleanup:
--   - ca.openosp.openo.commn.model.SpecialistsJavascript
--   - ca.openosp.openo.commn.dao.SpecialistsJavascriptDao
--   - ca.openosp.openo.commn.dao.SpecialistsJavascriptDaoImpl
--   - ca.openosp.openo.encounter.oscarConsultationRequest.config.data.EctConConfigurationJavascriptData
--   - ca.openosp.openo.encounter.oscarConsultationRequest.config.pageUtil.EctConConstructSpecialistsScriptsFile