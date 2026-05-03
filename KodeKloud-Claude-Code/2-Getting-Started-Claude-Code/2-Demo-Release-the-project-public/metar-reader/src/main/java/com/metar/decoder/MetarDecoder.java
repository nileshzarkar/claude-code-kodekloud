package com.metar.decoder;

import com.metar.model.CloudLayer;
import com.metar.model.DecodedMetar;
import com.metar.model.WeatherPhenomenon;
import com.metar.model.WindInfo;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Core METAR parsing engine. Tokenizes a raw METAR string and decodes each field
 * (report type, station, time, wind, visibility, weather phenomena, clouds,
 * temperature/dew point, altimeter, remarks) into a structured {@link DecodedMetar} object.
 * All lookup tables (weather codes, cloud coverage, compass directions) are initialized
 * once at class load in static initializer blocks.
 */
@ApplicationScoped
public class MetarDecoder {

    private static final Map<String, String> DESCRIPTORS = new LinkedHashMap<>();
    private static final Map<String, String> PRECIPITATION = new LinkedHashMap<>();
    private static final Map<String, String> OBSCURATION = new LinkedHashMap<>();
    private static final Map<String, String> OTHER_PHENOMENA = new LinkedHashMap<>();
    private static final Map<String, String> CLOUD_COVERAGE = new LinkedHashMap<>();
    private static final Map<String, String> COMPASS_DIRECTIONS = new LinkedHashMap<>();

    static {
        DESCRIPTORS.put("MI", "Shallow");
        DESCRIPTORS.put("BC", "Patches");
        DESCRIPTORS.put("PR", "Partial");
        DESCRIPTORS.put("DR", "Low Drifting");
        DESCRIPTORS.put("BL", "Blowing");
        DESCRIPTORS.put("SH", "Showers");
        DESCRIPTORS.put("TS", "Thunderstorm");
        DESCRIPTORS.put("FZ", "Freezing");

        PRECIPITATION.put("DZ", "Drizzle");
        PRECIPITATION.put("RA", "Rain");
        PRECIPITATION.put("SN", "Snow");
        PRECIPITATION.put("SG", "Snow Grains");
        PRECIPITATION.put("IC", "Ice Crystals");
        PRECIPITATION.put("PL", "Ice Pellets");
        PRECIPITATION.put("GR", "Hail");
        PRECIPITATION.put("GS", "Small Hail/Snow Pellets");
        PRECIPITATION.put("UP", "Unknown Precipitation");

        OBSCURATION.put("BR", "Mist");
        OBSCURATION.put("FG", "Fog");
        OBSCURATION.put("FU", "Smoke");
        OBSCURATION.put("VA", "Volcanic Ash");
        OBSCURATION.put("DU", "Widespread Dust");
        OBSCURATION.put("SA", "Sand");
        OBSCURATION.put("HZ", "Haze");
        OBSCURATION.put("PY", "Spray");

        OTHER_PHENOMENA.put("PO", "Dust/Sand Whirls");
        OTHER_PHENOMENA.put("SQ", "Squalls");
        OTHER_PHENOMENA.put("FC", "Funnel Cloud/Tornado/Waterspout");
        OTHER_PHENOMENA.put("SS", "Sandstorm");
        OTHER_PHENOMENA.put("DS", "Duststorm");

        CLOUD_COVERAGE.put("FEW", "Few (1/8–2/8 sky covered)");
        CLOUD_COVERAGE.put("SCT", "Scattered (3/8–4/8 sky covered)");
        CLOUD_COVERAGE.put("BKN", "Broken (5/8–7/8 sky covered)");
        CLOUD_COVERAGE.put("OVC", "Overcast (8/8 sky covered — complete cloud cover)");
        CLOUD_COVERAGE.put("SKC", "Sky Clear (no clouds)");
        CLOUD_COVERAGE.put("CLR", "Clear (no clouds below 12,000 ft)");
        CLOUD_COVERAGE.put("NSC", "No Significant Clouds");
        CLOUD_COVERAGE.put("NCD", "No Cloud Detected");

        COMPASS_DIRECTIONS.put("N", "North"); COMPASS_DIRECTIONS.put("NNE", "North-Northeast");
        COMPASS_DIRECTIONS.put("NE", "Northeast"); COMPASS_DIRECTIONS.put("ENE", "East-Northeast");
        COMPASS_DIRECTIONS.put("E", "East"); COMPASS_DIRECTIONS.put("ESE", "East-Southeast");
        COMPASS_DIRECTIONS.put("SE", "Southeast"); COMPASS_DIRECTIONS.put("SSE", "South-Southeast");
        COMPASS_DIRECTIONS.put("S", "South"); COMPASS_DIRECTIONS.put("SSW", "South-Southwest");
        COMPASS_DIRECTIONS.put("SW", "Southwest"); COMPASS_DIRECTIONS.put("WSW", "West-Southwest");
        COMPASS_DIRECTIONS.put("W", "West"); COMPASS_DIRECTIONS.put("WNW", "West-Northwest");
        COMPASS_DIRECTIONS.put("NW", "Northwest"); COMPASS_DIRECTIONS.put("NNW", "North-Northwest");
    }

    private static final Pattern WIND_PATTERN = Pattern.compile("^(VRB|\\d{3})(\\d{2,3})(G(\\d{2,3}))?(KT|MPS)$");
    private static final Pattern WIND_VAR_PATTERN = Pattern.compile("^(\\d{3})V(\\d{3})$");
    private static final Pattern VIS_METRIC_PATTERN = Pattern.compile("^(\\d{4})$");
    private static final Pattern VIS_SM_PATTERN = Pattern.compile("^(M)?(\\d+)(SM)$");
    private static final Pattern VIS_FRAC_SM_PATTERN = Pattern.compile("^(M)?(\\d+)/(\\d+)(SM)$");
    private static final Pattern VIS_WHOLE_FRAC_SM_PATTERN = Pattern.compile("^(\\d+)/(\\d+)(SM)$");
    private static final Pattern CLOUD_PATTERN = Pattern.compile("^(FEW|SCT|BKN|OVC)(\\d{3})(CB|TCU)?$");
    private static final Pattern SKY_CLEAR_PATTERN = Pattern.compile("^(SKC|CLR|NSC|NCD)$");
    private static final Pattern VERT_VIS_PATTERN = Pattern.compile("^VV(\\d{3}|///)$");
    private static final Pattern TEMP_PATTERN = Pattern.compile("^(M?\\d{1,2})/(M?\\d{1,2})$");
    private static final Pattern ALT_Q_PATTERN = Pattern.compile("^Q(\\d{4})$");
    private static final Pattern ALT_A_PATTERN = Pattern.compile("^A(\\d{4})$");
    private static final Pattern RVR_PATTERN = Pattern.compile("^R\\d{2}[LRC]?/.*$");
    private static final Pattern DATE_TIME_PATTERN = Pattern.compile("^(\\d{2})(\\d{2})(\\d{2})Z$");
    private static final Pattern WEATHER_PATTERN = Pattern.compile(
        "^([-+]|VC)?(MI|BC|PR|DR|BL|SH|TS|FZ)?(DZ|RA|SN|SG|IC|PL|GR|GS|UP|BR|FG|FU|VA|DU|SA|HZ|PO|SQ|FC|SS|DS)+$"
    );

    /**
     * Parses a raw METAR string into a structured {@link DecodedMetar} object.
     * <p>
     * The method tokenizes the input on whitespace and advances a cursor through
     * each recognized METAR field in order: report type, station identifier,
     * date/time, wind, CAVOK/visibility, RVR (skipped), weather phenomena,
     * cloud layers/sky condition, temperature/dew point, altimeter, and remarks.
     * After parsing, the flight category (VFR/MVFR/IFR/LIFR) is derived from
     * the ceiling and visibility values.
     *
     * @param rawMetar    the raw METAR observation string (e.g. "METAR KLAX 011755Z 27015KT 10SM FEW040 22/10 A2992")
     * @param stationName human-readable airport name returned by the API (used for display only)
     * @return a fully populated {@link DecodedMetar}; on empty input returns an object with an error field set
     */
    public DecodedMetar decode(String rawMetar, String stationName) {
        DecodedMetar result = new DecodedMetar();
        result.setRawMetar(rawMetar);
        result.setStationName(stationName != null ? stationName : "Unknown");

        if (rawMetar == null || rawMetar.trim().isEmpty()) {
            result.setError("Empty METAR string");
            return result;
        }

        List<String> tokens = new ArrayList<>(Arrays.asList(rawMetar.trim().split("\\s+")));
        int i = 0;

        // 1. Report type (optional)
        if (i < tokens.size() && (tokens.get(i).equals("METAR") || tokens.get(i).equals("SPECI"))) {
            String type = tokens.get(i++);
            result.setReportType(type);
            result.setReportTypeDescription(type.equals("METAR")
                    ? "Routine Meteorological Aerodrome Report (issued every hour)"
                    : "Special Meteorological Aerodrome Report (issued when weather changes significantly)");
        } else {
            result.setReportType("METAR");
            result.setReportTypeDescription("Routine Meteorological Aerodrome Report (issued every hour)");
        }

        // 2. Station identifier
        if (i < tokens.size() && tokens.get(i).matches("^[A-Z0-9]{3,4}$")) {
            result.setStationId(tokens.get(i++));
        }

        // 3. Date and time
        if (i < tokens.size()) {
            Matcher dtMatcher = DATE_TIME_PATTERN.matcher(tokens.get(i));
            if (dtMatcher.matches()) {
                String dtToken = tokens.get(i++);
                String day = dtMatcher.group(1);
                String hour = dtMatcher.group(2);
                String minute = dtMatcher.group(3);
                result.setDateTime(dtToken);
                result.setDateTimeDescription(String.format(
                        "Day %s of the month, %s:%s UTC (Coordinated Universal Time)", day, hour, minute));
            }
        }

        // 4. Wind
        if (i < tokens.size()) {
            Matcher windMatcher = WIND_PATTERN.matcher(tokens.get(i));
            if (windMatcher.matches()) {
                WindInfo wind = parseWind(tokens.get(i++), windMatcher);
                result.setWind(wind);
                // Variable wind direction range (e.g., 260V290)
                if (i < tokens.size()) {
                    Matcher varMatcher = WIND_VAR_PATTERN.matcher(tokens.get(i));
                    if (varMatcher.matches()) {
                        wind.setVariableFrom(Integer.parseInt(varMatcher.group(1)));
                        wind.setVariableTo(Integer.parseInt(varMatcher.group(2)));
                        wind.setDescription(wind.getDescription() + String.format(
                                ", direction varying between %s° and %s°",
                                varMatcher.group(1), varMatcher.group(2)));
                        i++;
                    }
                }
            }
        }

        // 5. Visibility
        if (i < tokens.size() && tokens.get(i).equals("CAVOK")) {
            result.setVisibility("CAVOK");
            result.setVisibilityDescription("Ceiling And Visibility OK: visibility ≥10 km, no CB/TCU below 5000 ft, no significant weather");
            result.setCavok(true);
            i++;
        } else if (i < tokens.size()) {
            i = parseVisibility(tokens, i, result);
        }

        // 6. Skip RVR (Runway Visual Range)
        while (i < tokens.size() && RVR_PATTERN.matcher(tokens.get(i)).matches()) {
            i++;
        }

        // 7. Weather phenomena
        List<WeatherPhenomenon> phenomena = new ArrayList<>();
        while (i < tokens.size() && isWeatherPhenomenon(tokens.get(i))) {
            phenomena.add(parseWeatherPhenomenon(tokens.get(i)));
            i++;
        }
        if (!phenomena.isEmpty()) {
            result.setWeatherPhenomena(phenomena);
        }

        // 8. Cloud layers / sky condition
        List<CloudLayer> clouds = new ArrayList<>();
        while (i < tokens.size()) {
            String token = tokens.get(i);
            Matcher cloudMatcher = CLOUD_PATTERN.matcher(token);
            Matcher clearMatcher = SKY_CLEAR_PATTERN.matcher(token);
            Matcher vvMatcher = VERT_VIS_PATTERN.matcher(token);

            if (cloudMatcher.matches()) {
                i++;
                String coverage = cloudMatcher.group(1);
                int base = Integer.parseInt(cloudMatcher.group(2)) * 100;
                String type = cloudMatcher.group(3);
                String typeDesc = type == null ? "" : (type.equals("CB") ? " (Cumulonimbus — severe thunderstorm)" : " (Towering Cumulus — strong convection)");
                String desc = CLOUD_COVERAGE.getOrDefault(coverage, coverage) + " at " + base + " ft" + typeDesc;
                clouds.add(new CloudLayer(coverage, CLOUD_COVERAGE.getOrDefault(coverage, coverage), base, type, desc));
            } else if (clearMatcher.matches()) {
                i++;
                result.setSkyClear(token);
                result.setSkyClearDescription(CLOUD_COVERAGE.getOrDefault(token, token));
                break;
            } else if (vvMatcher.matches()) {
                i++;
                String vvStr = vvMatcher.group(1);
                if (!vvStr.equals("///")) {
                    int vvFt = Integer.parseInt(vvStr) * 100;
                    result.setVerticalVisibility(vvFt);
                    result.setVerticalVisibilityDescription("Vertical visibility " + vvFt + " ft (sky obscured)");
                }
                break;
            } else {
                break;
            }
        }
        if (!clouds.isEmpty()) {
            result.setCloudLayers(clouds);
        }

        // 9. Temperature / Dewpoint
        if (i < tokens.size()) {
            Matcher tempMatcher = TEMP_PATTERN.matcher(tokens.get(i));
            if (tempMatcher.matches()) {
                String[] parts = tokens.get(i++).split("/");
                double temp = parseTemperature(parts[0]);
                double dew = parseTemperature(parts[1]);
                result.setTemperature(temp);
                result.setDewPoint(dew);
                result.setTemperatureDescription(String.format(
                        "Temperature: %.0f°C (%.0f°F) | Dew Point: %.0f°C (%.0f°F)",
                        temp, celsiusToFahrenheit(temp), dew, celsiusToFahrenheit(dew)));
                result.setRelativeHumidity(calculateHumidity(temp, dew));
            }
        }

        // 10. Altimeter setting
        if (i < tokens.size()) {
            Matcher qMatcher = ALT_Q_PATTERN.matcher(tokens.get(i));
            Matcher aMatcher = ALT_A_PATTERN.matcher(tokens.get(i));
            if (qMatcher.matches()) {
                String altToken = tokens.get(i++);
                int hpa = Integer.parseInt(qMatcher.group(1));
                result.setAltimeter(altToken);
                result.setAltimeterDescription(String.format(
                        "QNH %d hPa (%.2f inHg) — pressure adjusted to sea level", hpa, hpa / 33.8639));
            } else if (aMatcher.matches()) {
                String altToken = tokens.get(i++);
                double inHg = Integer.parseInt(aMatcher.group(1)) / 100.0;
                result.setAltimeter(altToken);
                result.setAltimeterDescription(String.format(
                        "%.2f inHg (%.0f hPa) — pressure adjusted to sea level", inHg, inHg * 33.8639));
            }
        }

        // 11. Remarks
        if (i < tokens.size() && tokens.get(i).equals("RMK")) {
            StringBuilder remarks = new StringBuilder();
            for (int j = i; j < tokens.size(); j++) {
                if (j > i) remarks.append(" ");
                remarks.append(tokens.get(j));
            }
            result.setRemarks(remarks.toString());
            result.setRemarksDescription("Additional observer remarks (not decoded in standard decoding)");
        }

        calculateFlightCategory(result);
        return result;
    }

    /**
     * Parses the visibility portion of the METAR token list starting at index {@code i}.
     * Handles metric visibility in meters (e.g. "9999", "0800"), whole statute-mile
     * values (e.g. "10SM"), fractional statute-mile values (e.g. "1/2SM"), and the
     * two-token "whole + fraction" format (e.g. "1 1/2SM").
     *
     * @param tokens the full list of METAR tokens
     * @param i      the current cursor position pointing at the first visibility token
     * @param result the {@link DecodedMetar} object to populate with visibility data
     * @return the updated cursor position after consuming all visibility token(s)
     */
    private int parseVisibility(List<String> tokens, int i, DecodedMetar result) {
        String token = tokens.get(i);
        Matcher metricMatcher = VIS_METRIC_PATTERN.matcher(token);
        Matcher smMatcher = VIS_SM_PATTERN.matcher(token);
        Matcher fracMatcher = VIS_FRAC_SM_PATTERN.matcher(token);

        if (metricMatcher.matches()) {
            i++;
            int visMet = Integer.parseInt(token);
            if (visMet == 9999) {
                result.setVisibility("9999");
                result.setVisibilityDescription("10 km or more (excellent visibility)");
                result.setVisibilityMeters(10000);
            } else {
                result.setVisibility(token);
                result.setVisibilityDescription(visMet + " meters (" + String.format("%.1f", visMet / 1000.0) + " km)");
                result.setVisibilityMeters(visMet);
            }
        } else if (smMatcher.matches()) {
            i++;
            String lessPrefix = smMatcher.group(1);
            double miles = Double.parseDouble(smMatcher.group(2));
            String prefix = "M".equals(lessPrefix) ? "Less than " : "";
            result.setVisibility(token);
            result.setVisibilityDescription(prefix + (int) miles + " statute mile" + (miles != 1 ? "s" : ""));
            result.setVisibilityMeters((int) (miles * 1609.34));
        } else if (fracMatcher.matches()) {
            i++;
            String lessPrefix = fracMatcher.group(1);
            int num = Integer.parseInt(fracMatcher.group(2));
            int den = Integer.parseInt(fracMatcher.group(3));
            double miles = (double) num / den;
            String prefix = "M".equals(lessPrefix) ? "Less than " : "";
            result.setVisibility(token);
            result.setVisibilityDescription(prefix + num + "/" + den + " statute mile");
            result.setVisibilityMeters((int) (miles * 1609.34));
        } else if (token.matches("\\d+") && i + 1 < tokens.size()) {
            // Handle "1 1/2SM" style (two tokens)
            Matcher nextFrac = VIS_WHOLE_FRAC_SM_PATTERN.matcher(tokens.get(i + 1));
            if (nextFrac.matches()) {
                int whole = Integer.parseInt(token);
                int num = Integer.parseInt(nextFrac.group(1));
                int den = Integer.parseInt(nextFrac.group(2));
                double miles = whole + (double) num / den;
                result.setVisibility(token + " " + tokens.get(i + 1));
                result.setVisibilityDescription(miles + " statute miles");
                result.setVisibilityMeters((int) (miles * 1609.34));
                i += 2;
            }
        }
        return i;
    }

    /**
     * Builds a {@link WindInfo} object from a pre-matched wind token.
     * Converts MPS speeds to knots, determines whether the wind is calm or variable,
     * and constructs a human-readable description. Gust speed is included when present.
     *
     * @param token   the raw wind token string (e.g. "27015G25KT")
     * @param matcher a {@link Matcher} already matched against {@code WIND_PATTERN}
     * @return a populated {@link WindInfo} with direction, speed, gusts, and description
     */
    private WindInfo parseWind(String token, Matcher matcher) {
        WindInfo wind = new WindInfo();
        String dirStr = matcher.group(1);
        int speed = Integer.parseInt(matcher.group(2));
        String gustStr = matcher.group(4);
        String unit = matcher.group(5);

        wind.setUnit(unit);
        wind.setSpeedKt(unit.equals("MPS") ? (int) Math.round(speed * 1.944) : speed);
        wind.setGustKt(gustStr != null ? (unit.equals("MPS") ? (int) Math.round(Integer.parseInt(gustStr) * 1.944) : Integer.parseInt(gustStr)) : null);

        boolean isCalm = speed == 0 && (dirStr.equals("000") || dirStr.equals("VRB"));
        boolean isVariable = dirStr.equals("VRB");

        wind.setCalm(isCalm);
        wind.setVariable(isVariable);
        wind.setRawDirection(dirStr);

        if (isCalm) {
            wind.setDirectionDegrees(0);
            wind.setDirectionDescription("Calm (no wind)");
            wind.setDescription("Calm winds — no significant wind");
        } else if (isVariable) {
            wind.setDirectionDegrees(-1);
            wind.setDirectionDescription("Variable direction");
            wind.setDescription("Variable wind direction at " + wind.getSpeedKt() + " kt");
        } else {
            int degrees = Integer.parseInt(dirStr);
            wind.setDirectionDegrees(degrees);
            String compassDir = getCompassDirection(degrees);
            wind.setDirectionDescription(compassDir + " (" + degrees + "°)");
            String desc = "Wind from " + compassDir + " (" + degrees + "°) at " + wind.getSpeedKt() + " knots";
            if (wind.getGustKt() != null) {
                desc += ", gusting to " + wind.getGustKt() + " knots";
            }
            wind.setDescription(desc);
        }
        return wind;
    }

    /**
     * Converts a wind direction in degrees to a human-readable compass direction name
     * (e.g. 270 → "West", 45 → "Northeast"). Uses 16-point compass rose with 22.5° sectors.
     *
     * @param degrees wind direction in degrees true (0–360)
     * @return the full compass direction name (e.g. "North-Northwest")
     */
    private String getCompassDirection(int degrees) {
        String[] dirs = {"N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE",
                "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW"};
        int idx = (int) Math.round(((double) degrees % 360) / 22.5) % 16;
        String dir = dirs[idx];
        return COMPASS_DIRECTIONS.getOrDefault(dir, dir);
    }

    /**
     * Determines whether a METAR token represents a weather phenomenon code.
     * Excludes known non-phenomenon keywords (RMK, NOSIG, TEMPO, BECMG) before
     * testing the token against the weather phenomenon regex pattern.
     *
     * @param token the METAR token to evaluate
     * @return {@code true} if the token matches a weather phenomenon pattern; {@code false} otherwise
     */
    private boolean isWeatherPhenomenon(String token) {
        if (token.equals("RMK") || token.equals("NOSIG") || token.equals("TEMPO") || token.equals("BECMG")) return false;
        return WEATHER_PATTERN.matcher(token).matches();
    }

    /**
     * Decodes a single weather phenomenon token into a {@link WeatherPhenomenon} with
     * a human-readable description. Handles intensity prefixes ({@code +} heavy, {@code -} light,
     * {@code VC} in vicinity), an optional descriptor (e.g. SH showers, TS thunderstorm),
     * and one or more two-letter phenomenon codes from precipitation, obscuration, or other categories.
     *
     * @param token the raw weather phenomenon token (e.g. "-RASN", "+TSRA", "VCFG")
     * @return a {@link WeatherPhenomenon} containing the original code and its expanded description
     */
    private WeatherPhenomenon parseWeatherPhenomenon(String token) {
        StringBuilder description = new StringBuilder();
        String remaining = token;

        if (remaining.startsWith("+")) {
            description.append("Heavy ");
            remaining = remaining.substring(1);
        } else if (remaining.startsWith("-")) {
            description.append("Light ");
            remaining = remaining.substring(1);
        } else if (remaining.startsWith("VC")) {
            description.append("In Vicinity: ");
            remaining = remaining.substring(2);
        } else if (!remaining.isEmpty()) {
            description.append("Moderate ");
        }

        for (Map.Entry<String, String> entry : DESCRIPTORS.entrySet()) {
            if (remaining.startsWith(entry.getKey())) {
                description.append(entry.getValue()).append(" ");
                remaining = remaining.substring(entry.getKey().length());
                break;
            }
        }

        List<String> phenomenaList = new ArrayList<>();
        while (remaining.length() >= 2) {
            String code = remaining.substring(0, 2);
            if (PRECIPITATION.containsKey(code)) {
                phenomenaList.add(PRECIPITATION.get(code));
                remaining = remaining.substring(2);
            } else if (OBSCURATION.containsKey(code)) {
                phenomenaList.add(OBSCURATION.get(code));
                remaining = remaining.substring(2);
            } else if (OTHER_PHENOMENA.containsKey(code)) {
                phenomenaList.add(OTHER_PHENOMENA.get(code));
                remaining = remaining.substring(2);
            } else {
                break;
            }
        }

        if (!phenomenaList.isEmpty()) {
            description.append(String.join(" with ", phenomenaList));
        }

        return new WeatherPhenomenon(token, description.toString().trim());
    }

    /**
     * Parses a METAR temperature string into a double value in degrees Celsius.
     * The METAR convention uses the prefix "M" to denote negative (minus) temperatures
     * (e.g. "M05" → -5.0°C).
     *
     * @param s the temperature string from a METAR (e.g. "22", "M05")
     * @return the temperature as a signed double in degrees Celsius
     */
    private double parseTemperature(String s) {
        if (s.startsWith("M")) return -Double.parseDouble(s.substring(1));
        return Double.parseDouble(s);
    }

    /**
     * Converts a temperature from Celsius to Fahrenheit.
     *
     * @param c temperature in degrees Celsius
     * @return equivalent temperature in degrees Fahrenheit
     */
    private double celsiusToFahrenheit(double c) {
        return c * 9.0 / 5.0 + 32;
    }

    /**
     * Estimates relative humidity using the Magnus formula applied to temperature and dew point.
     * Returns the result rounded to the nearest whole percentage.
     *
     * @param temp temperature in degrees Celsius
     * @param dew  dew point in degrees Celsius
     * @return relative humidity as a percentage (0–100), rounded to the nearest integer
     */
    private long calculateHumidity(double temp, double dew) {
        double rh = 100.0 * Math.exp((17.625 * dew) / (243.04 + dew))
                / Math.exp((17.625 * temp) / (243.04 + temp));
        return Math.round(rh);
    }

    /**
     * Derives the FAA flight category (VFR, MVFR, IFR, or LIFR) from the decoded METAR's
     * ceiling and visibility values and writes the result back into the {@link DecodedMetar}.
     * <p>
     * CAVOK automatically yields VFR. Otherwise, the lowest BKN/OVC cloud layer is used as
     * the ceiling, and visibility in statute miles is converted from stored meters.
     * Category thresholds follow standard FAA definitions:
     * <ul>
     *   <li>LIFR — ceiling &lt; 500 ft OR visibility &lt; 1 SM</li>
     *   <li>IFR  — ceiling 500–999 ft OR visibility 1–2.99 SM</li>
     *   <li>MVFR — ceiling 1,000–2,999 ft OR visibility 3–4.99 SM</li>
     *   <li>VFR  — ceiling ≥ 3,000 ft AND visibility ≥ 5 SM</li>
     * </ul>
     *
     * @param metar the partially decoded METAR object; its flightCategory and
     *              flightCategoryDescription fields are set by this method
     */
    private void calculateFlightCategory(DecodedMetar metar) {
        if (metar.isCavok()) {
            metar.setFlightCategory("VFR");
            metar.setFlightCategoryDescription("Visual Flight Rules — excellent conditions, CAVOK");
            return;
        }

        Integer lowestCeiling = null;
        if (metar.getCloudLayers() != null) {
            for (CloudLayer layer : metar.getCloudLayers()) {
                if ("BKN".equals(layer.getCoverage()) || "OVC".equals(layer.getCoverage())) {
                    if (lowestCeiling == null || layer.getAltitudeFt() < lowestCeiling) {
                        lowestCeiling = layer.getAltitudeFt();
                    }
                }
            }
        }

        double visSM = metar.getVisibilityMeters() != null ? metar.getVisibilityMeters() / 1609.34 : 999.0;
        int ceiling = lowestCeiling != null ? lowestCeiling : 99999;

        String category, description;
        if (ceiling < 500 || visSM < 1.0) {
            category = "LIFR";
            description = "Low Instrument Flight Rules — very poor conditions, ceiling <500 ft or visibility <1 SM";
        } else if (ceiling < 1000 || visSM < 3.0) {
            category = "IFR";
            description = "Instrument Flight Rules — poor conditions, ceiling 500–999 ft or visibility 1–3 SM";
        } else if (ceiling < 3000 || visSM < 5.0) {
            category = "MVFR";
            description = "Marginal Visual Flight Rules — ceiling 1,000–2,999 ft or visibility 3–5 SM";
        } else {
            category = "VFR";
            description = "Visual Flight Rules — good conditions, ceiling ≥3,000 ft and visibility ≥5 SM";
        }

        metar.setFlightCategory(category);
        metar.setFlightCategoryDescription(description);
    }
}
