/**
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * <p>
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package ca.openosp.openo.integration.vigilance.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

/**
 * Response object for a Vigilance query analysis.
 * Contains the full analysis result including summary, profile data, side effects, and more.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record VigilanceQueryResponse(
        @JsonProperty("summary") Summary summary,
        @JsonProperty("profileGen") Map<String, ProfileGenEntry> profileGen,
        @JsonProperty("profileSideEffects") List<ProfileSideEffect> profileSideEffects,
        @JsonProperty("profileIntensity") ProfileIntensity profileIntensity,
        @JsonProperty("profileCytochromes") List<ProfileCytochrome> profileCytochromes,
        @JsonProperty("dictionary") Dictionary dictionary,
        @JsonProperty("query") QueryResponse query,
        @JsonProperty("profile") Profile profile,
        @JsonProperty("institution") Institution institution) implements VigilanceResponse {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Summary(
            @JsonProperty("dateTime") String dateTime,
            @JsonProperty("version") Version version,
            @JsonProperty("servicePerformed") Boolean servicePerformed,
            @JsonProperty("preprocessChanges") List<PreprocessChange> preprocessChanges,
            @JsonProperty("displayIcon") String displayIcon,
            @JsonProperty("analysisLimits") AnalysisLimits analysisLimits) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Version(
            @JsonProperty("engine") String engine,
            @JsonProperty("data") String data) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PreprocessChange(
            @JsonProperty("id") String id,
            @JsonProperty("target") String target,
            @JsonProperty("source") String source,
            @JsonProperty("fr") String fr,
            @JsonProperty("en") String en) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record AnalysisLimits(
            @JsonProperty("medications") MedicationLimit medications) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record MedicationLimit(
            @JsonProperty("limited") Integer limited) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Source(
            @JsonProperty("p") Integer p,
            @JsonProperty("c") Integer c,
            @JsonProperty("i") Integer i,
            @JsonProperty("impact") String impact) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ProfileGenEntry(
            @JsonProperty("source") List<Source> source) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ProfileSideEffect(
            @JsonProperty("id") String id,
            @JsonProperty("n") Integer n,
            @JsonProperty("fr") String fr,
            @JsonProperty("en") String en,
            @JsonProperty("source") List<Source> source) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ProfileIntensity(
            @JsonProperty("profile") Integer profile,
            @JsonProperty("detail") List<Map<String, Integer>> detail) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ProfileCytochrome(
            @JsonProperty("id") String id,
            @JsonProperty("source") List<Source> source) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Dictionary(
            @JsonProperty("text") TextDictionary text) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record TextDictionary(
            @JsonProperty("al_limited") BilingualText al_limited,
            @JsonProperty("limited") BilingualText limited) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record BilingualText(
            @JsonProperty("fr") String fr,
            @JsonProperty("en") String en) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record QueryResponse(
            @JsonProperty("config") QueryConfig config,
            @JsonProperty("service") ServiceInfo service) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record QueryConfig(
            @JsonProperty("zone") List<String> zone) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ServiceInfo(
            @JsonProperty("id") String id,
            @JsonProperty("userType") Integer userType,
            @JsonProperty("analysisMode") Integer analysisMode) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Profile(
            @JsonProperty("patient") Patient patient,
            @JsonProperty("medications") List<MedicationEntry> medications,
            @JsonProperty("diagnoses") List<Diagnosis> diagnoses) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Patient(
            @JsonProperty("firstName") String firstName,
            @JsonProperty("lastName") String lastName,
            @JsonProperty("age") Age age,
            @JsonProperty("weightKg") Integer weightKg,
            @JsonProperty("race") Integer race) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Age(
            @JsonProperty("years") Integer years) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record MedicationEntry(
            @JsonProperty("product") List<Product> product) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Product(
            @JsonProperty("code") String code,
            @JsonProperty("fmt") String fmt,
            @JsonProperty("displayNames") DisplayNames displayNames,
            @JsonProperty("analysisLimits") AnalysisLimitsEntry analysisLimits,
            @JsonProperty("detail") ProductDetail detail) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record DisplayNames(
            @JsonProperty("usual") BilingualText usual,
            @JsonProperty("usualAndForm") BilingualText usualAndForm,
            @JsonProperty("usualAndStrengthForm") BilingualText usualAndStrengthForm,
            @JsonProperty("usualAndCombi") BilingualText usualAndCombi,
            @JsonProperty("usualAndIngredient") List<BilingualText> usualAndIngredient,
            @JsonProperty("genName") BilingualText genName,
            @JsonProperty("genDetail") List<BilingualText> genDetail,
            @JsonProperty("form") BilingualText form,
            @JsonProperty("strength") BilingualText strength) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record AnalysisLimitsEntry(
            @JsonProperty("value") String value,
            @JsonProperty("note") String note) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ProductDetail(
            @JsonProperty("name") BilingualText name,
            @JsonProperty("strength") BilingualText strength,
            @JsonProperty("form") BilingualText form,
            @JsonProperty("manufacturer") String manufacturer,
            @JsonProperty("legalStatus") String legalStatus,
            @JsonProperty("legalStatusCanada") String legalStatusCanada,
            @JsonProperty("gen") String gen,
            @JsonProperty("ingredients") List<Ingredient> ingredients,
            @JsonProperty("coverage") Map<String, Coverage> coverage,
            @JsonProperty("isActive") Map<String, Integer> isActive,
            @JsonProperty("monograph") Monograph monograph,
            @JsonProperty("comparativeChart") ComparativeChart comparativeChart) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Ingredient(
            @JsonProperty("gen") String gen,
            @JsonProperty("genName") BilingualText genName,
            @JsonProperty("analysisLimits") AnalysisLimitsEntry analysisLimits) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Coverage(
            @JsonProperty("covered") String covered,
            @JsonProperty("supply") Integer supply) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Monograph(
            @JsonProperty("fr") String fr,
            @JsonProperty("en") String en,
            @JsonProperty("title") BilingualText title,
            @JsonProperty("a") String a) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ComparativeChart(
            @JsonProperty("fr") String fr,
            @JsonProperty("en") String en,
            @JsonProperty("title") BilingualText title,
            @JsonProperty("a") String a,
            @JsonProperty("q") BilingualText q) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Diagnosis(
            @JsonProperty("code") String code,
            @JsonProperty("active") Integer active,
            @JsonProperty("displayNames") BilingualText displayNames,
            @JsonProperty("detail") DiagnosisDetail detail) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record DiagnosisDetail(
            @JsonProperty("name") BilingualText name,
            @JsonProperty("link") DiagnosisLink link,
            @JsonProperty("documentsDiagnosis") List<DocumentDiagnosis> documentsDiagnosis) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record DiagnosisLink(
            @JsonProperty("fr") String fr,
            @JsonProperty("en") String en,
            @JsonProperty("title") BilingualText title,
            @JsonProperty("a") String a,
            @JsonProperty("q") BilingualText q) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record DocumentDiagnosis(
            @JsonProperty("link") DiagnosisLink link,
            @JsonProperty("category") String category) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Institution(
            @JsonProperty("type") Integer type) {}
}
