package com.metar.service;

import com.metar.client.AviationWeatherClient;
import com.metar.model.AviationWeatherResponse;
import com.metar.model.DecodedMetar;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@QuarkusTest
class MetarServiceTest {

    @InjectMock
    @RestClient
    AviationWeatherClient aviationWeatherClient;

    @Inject
    MetarService metarService;

    // -------------------------------------------------------------------------
    // Input validation
    // -------------------------------------------------------------------------

    @Test
    void tooShortCodeThrows400() {
        WebApplicationException ex = assertThrows(WebApplicationException.class,
                () -> metarService.getMetar("AB"));
        assertEquals(400, ex.getResponse().getStatus());
    }

    @Test
    void tooLongCodeThrows400() {
        WebApplicationException ex = assertThrows(WebApplicationException.class,
                () -> metarService.getMetar("ABCDE"));
        assertEquals(400, ex.getResponse().getStatus());
    }

    @Test
    void lowercaseAndPaddedCodeIsNormalized() {
        setAviationWeatherClientResponse(List.of(mockResponse(
                "METAR KLAX 011755Z 27015KT 10SM FEW040 22/10 A2992",
                "Los Angeles Intl Airport")));

        DecodedMetar result = metarService.getMetar("  klax  ");
        assertEquals("KLAX", result.getStationId());
    }

    // -------------------------------------------------------------------------
    // Client / upstream failures
    // -------------------------------------------------------------------------

    @Test
    void clientExceptionThrows503() {
        setAviationWeatherClientException(new RuntimeException("Connection refused"));

        WebApplicationException ex = assertThrows(WebApplicationException.class,
                () -> metarService.getMetar("KLAX"));
        assertEquals(503, ex.getResponse().getStatus());
    }

    @Test
    void nullResponseListThrows404() {
        setAviationWeatherClientResponse(null);

        WebApplicationException ex = assertThrows(WebApplicationException.class,
                () -> metarService.getMetar("KLAX"));
        assertEquals(404, ex.getResponse().getStatus());
    }

    @Test
    void emptyResponseListThrows404() {
        setAviationWeatherClientResponse(Collections.emptyList());

        WebApplicationException ex = assertThrows(WebApplicationException.class,
                () -> metarService.getMetar("KLAX"));
        assertEquals(404, ex.getResponse().getStatus());
    }

    @Test
    void nullRawObThrows404() {
        setAviationWeatherClientResponse(List.of(mockResponse(null, "Some Airport")));

        WebApplicationException ex = assertThrows(WebApplicationException.class,
                () -> metarService.getMetar("KLAX"));
        assertEquals(404, ex.getResponse().getStatus());
    }

    @Test
    void blankRawObThrows404() {
        setAviationWeatherClientResponse(List.of(mockResponse("   ", "Some Airport")));

        WebApplicationException ex = assertThrows(WebApplicationException.class,
                () -> metarService.getMetar("KLAX"));
        assertEquals(404, ex.getResponse().getStatus());
    }

    // -------------------------------------------------------------------------
    // Successful decoding — mock METAR strings interpreted correctly
    // -------------------------------------------------------------------------

    @Test
    void standardUsMetarDecodesAllFields() {
        // KLAX: 270°/15kt, 10SM, FEW clouds at 4000ft, 22°C/10°C, A2992 → VFR
        setAviationWeatherClientResponse(List.of(mockResponse(
                "METAR KLAX 011755Z 27015KT 10SM FEW040 22/10 A2992",
                "Los Angeles Intl Airport")));

        DecodedMetar m = metarService.getMetar("KLAX");

        assertNull(m.getError());
        assertEquals("KLAX", m.getStationId());
        assertEquals("Los Angeles Intl Airport", m.getStationName());
        assertEquals("011755Z", m.getDateTime());
        // wind
        assertNotNull(m.getWind());
        assertEquals(270, m.getWind().getDirectionDegrees());
        assertEquals(15, m.getWind().getSpeedKt());
        assertNull(m.getWind().getGustKt());
        // visibility: 10SM = 16093 meters
        assertEquals(16093, m.getVisibilityMeters());
        // temperature / dew point
        assertEquals(22.0, m.getTemperature());
        assertEquals(10.0, m.getDewPoint());
        // altimeter
        assertEquals("A2992", m.getAltimeter());
        // flight category: ceiling=none(FEW), vis=10SM → VFR
        assertEquals("VFR", m.getFlightCategory());
    }

    @Test
    void cavokMetarSetsCavokFlagAndVfr() {
        // EGLL: CAVOK, 090°/10kt, 15°C/8°C, Q1020
        setAviationWeatherClientResponse(List.of(mockResponse(
                "METAR EGLL 011200Z 09010KT CAVOK 15/08 Q1020",
                "London Heathrow")));

        DecodedMetar m = metarService.getMetar("EGLL");

        assertTrue(m.isCavok());
        assertEquals("CAVOK", m.getVisibility());
        assertEquals("VFR", m.getFlightCategory());
        assertEquals(15.0, m.getTemperature());
        assertEquals(8.0, m.getDewPoint());
        assertEquals("Q1020", m.getAltimeter());
    }

    @Test
    void ifrConditionsDecodeWeatherAndCeiling() {
        // KSFO: 1SM vis, -RA, OVC at 500ft → IFR
        setAviationWeatherClientResponse(List.of(mockResponse(
                "METAR KSFO 011755Z 18005KT 1SM -RA OVC005 10/09 A2985",
                "San Francisco Intl Airport")));

        DecodedMetar m = metarService.getMetar("KSFO");

        assertEquals("IFR", m.getFlightCategory());
        // weather phenomena
        assertNotNull(m.getWeatherPhenomena());
        assertEquals(1, m.getWeatherPhenomena().size());
        assertEquals("-RA", m.getWeatherPhenomena().get(0).getCode());
        assertTrue(m.getWeatherPhenomena().get(0).getDescription().contains("Rain"));
        // cloud layers
        assertNotNull(m.getCloudLayers());
        assertEquals(1, m.getCloudLayers().size());
        assertEquals("OVC", m.getCloudLayers().get(0).getCoverage());
        assertEquals(500, m.getCloudLayers().get(0).getAltitudeFt());
    }

    @Test
    void metricMetarDecodesVisibilityAndCloudLayers() {
        // VABB (Mumbai): 6000m vis, FEW020 + SCT100, 30°C/24°C, Q1012
        setAviationWeatherClientResponse(List.of(mockResponse(
                "METAR VABB 011200Z 27010KT 6000 FEW020 SCT100 30/24 Q1012",
                "Chhatrapati Shivaji Intl Airport")));

        DecodedMetar m = metarService.getMetar("VABB");

        assertEquals(6000, m.getVisibilityMeters());
        assertEquals("Q1012", m.getAltimeter());
        assertEquals(30.0, m.getTemperature());
        assertEquals(24.0, m.getDewPoint());
        assertNotNull(m.getCloudLayers());
        assertEquals(2, m.getCloudLayers().size());
        assertEquals("FEW", m.getCloudLayers().get(0).getCoverage());
        assertEquals(2000, m.getCloudLayers().get(0).getAltitudeFt());
        assertEquals("SCT", m.getCloudLayers().get(1).getCoverage());
        assertEquals(10000, m.getCloudLayers().get(1).getAltitudeFt());
    }

    @Test
    void lifrFogWithVerticalVisibilityDecodesCorrectly() {
        // KORD: M1/4SM, FG, VV002 → LIFR
        setAviationWeatherClientResponse(List.of(mockResponse(
                "METAR KORD 011800Z 36003KT M1/4SM FG VV002 01/01 A2990",
                "Chicago O'Hare Intl Airport")));

        DecodedMetar m = metarService.getMetar("KORD");

        assertEquals("LIFR", m.getFlightCategory());
        assertNotNull(m.getWeatherPhenomena());
        assertTrue(m.getWeatherPhenomena().stream().anyMatch(p -> p.getCode().equals("FG")));
        assertEquals(200, m.getVerticalVisibility());
    }

    @Test
    void gustyWindDecodesGustSpeed() {
        // KDFW: 180°/15kt gusting 25kt, 7SM, BKN020 → MVFR (2000ft ceiling)
        setAviationWeatherClientResponse(List.of(mockResponse(
                "METAR KDFW 011800Z 18015G25KT 7SM BKN020 25/18 A2995",
                "Dallas Fort Worth Intl Airport")));

        DecodedMetar m = metarService.getMetar("KDFW");

        assertEquals(180, m.getWind().getDirectionDegrees());
        assertEquals(15, m.getWind().getSpeedKt());
        assertEquals(25, m.getWind().getGustKt());
        assertEquals("MVFR", m.getFlightCategory());
    }

    @Test
    void speciReportTypeDecodesCorrectly() {
        // KJFK: SPECI, 1SM vis, thunderstorm + rain, BKN015 → IFR
        setAviationWeatherClientResponse(List.of(mockResponse(
                "SPECI KJFK 011830Z 05012KT 1SM -TSRA BKN015 OVC025 18/15 A2980",
                "John F Kennedy Intl Airport")));

        DecodedMetar m = metarService.getMetar("KJFK");

        assertEquals("SPECI", m.getReportType());
        assertEquals("IFR", m.getFlightCategory());
    }

    @Test
    void negativeTemperaturesDecodeCorrectly() {
        // CYYZ (Toronto winter): M05/M12, OVC100, 15SM → VFR
        setAviationWeatherClientResponse(List.of(mockResponse(
                "METAR CYYZ 011800Z 31010KT 15SM FEW060 OVC100 M05/M12 A2998",
                "Toronto Pearson Intl Airport")));

        DecodedMetar m = metarService.getMetar("CYYZ");

        assertEquals(-5.0, m.getTemperature());
        assertEquals(-12.0, m.getDewPoint());
        // OVC100 = 10000ft ceiling → VFR
        assertEquals("VFR", m.getFlightCategory());
    }

    // -------------------------------------------------------------------------
    // Helper
    // -------------------------------------------------------------------------

    private void setAviationWeatherClientResponse(List<AviationWeatherResponse> response) {
        when(aviationWeatherClient.getMetar(anyString(), anyString(), anyBoolean(), anyInt()))
                .thenReturn(response);
    }

    private void setAviationWeatherClientException(RuntimeException ex) {
        when(aviationWeatherClient.getMetar(anyString(), anyString(), anyBoolean(), anyInt()))
                .thenThrow(ex);
    }

    private AviationWeatherResponse mockResponse(String rawOb, String name) {
        AviationWeatherResponse r = new AviationWeatherResponse();
        r.setRawOb(rawOb);
        r.setName(name);
        return r;
    }
}
