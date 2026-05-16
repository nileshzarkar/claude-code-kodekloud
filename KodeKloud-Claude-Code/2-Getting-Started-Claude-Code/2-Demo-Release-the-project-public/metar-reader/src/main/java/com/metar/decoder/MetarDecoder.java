package com.metar.decoder;

import com.metar.model.CloudLayer;
import com.metar.model.DecodedMetar;
import com.metar.model.WindInfo;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ApplicationScoped
public class MetarDecoder {

    private static final Pattern WIND_PATTERN =
            Pattern.compile("^(VRB|\\d{3})(\\d{2,3})(G(\\d{2,3}))?(KT|MPS)$");
    private static final Pattern CLOUD_PATTERN =
            Pattern.compile("^(FEW|SCT|BKN|OVC|VV)(\\d{3})(CB|TCU)?$");
    private static final Pattern TEMP_PATTERN =
            Pattern.compile("^(M?\\d+)/(M?\\d*)$");
    private static final Pattern WEATHER_PATTERN =
            Pattern.compile("^[-+]?VC?(MI|PR|BC|DR|BL|SH|TS|FZ)?(DZ|RA|SN|SG|IC|PL|GR|GS|UP|BR|FG|FU|VA|DU|SA|HZ|PY|PO|SQ|FC|SS|DS)+$");

    private static final Map<String, String> WEATHER_CODES = Map.ofEntries(
            Map.entry("DZ", "drizzle"), Map.entry("RA", "rain"), Map.entry("SN", "snow"),
            Map.entry("SG", "snow grains"), Map.entry("IC", "ice crystals"),
            Map.entry("PL", "ice pellets"), Map.entry("GR", "hail"),
            Map.entry("GS", "small hail"), Map.entry("UP", "unknown precipitation"),
            Map.entry("BR", "mist"), Map.entry("FG", "fog"), Map.entry("FU", "smoke"),
            Map.entry("VA", "volcanic ash"), Map.entry("DU", "dust"), Map.entry("SA", "sand"),
            Map.entry("HZ", "haze"), Map.entry("PY", "spray"), Map.entry("PO", "dust whirls"),
            Map.entry("SQ", "squalls"), Map.entry("FC", "funnel cloud"),
            Map.entry("SS", "sandstorm"), Map.entry("DS", "duststorm"), Map.entry("TS", "thunderstorm")
    );

    private static final Map<String, String> WEATHER_DESCRIPTORS = Map.of(
            "MI", "shallow", "PR", "partial", "BC", "patchy", "DR", "low drifting",
            "BL", "blowing", "SH", "showers of", "TS", "thunderstorm with",
            "FZ", "freezing"
    );

    public DecodedMetar decode(String rawMetar) {
        DecodedMetar result = new DecodedMetar();
        result.rawMetar = rawMetar.trim();

        String[] tokens = rawMetar.trim().split("\\s+");
        int idx = 0;

        // Skip report type
        if (idx < tokens.length && (tokens[idx].equals("METAR") || tokens[idx].equals("SPECI"))) {
            idx++;
        }

        // Station identifier
        if (idx < tokens.length) {
            result.station = tokens[idx++];
        }

        // Date/time (DDHHMM Z)
        if (idx < tokens.length && tokens[idx].matches("\\d{6}Z")) {
            result.observationTime = parseDateTime(tokens[idx++]);
        }

        // Skip AUTO/COR modifiers
        if (idx < tokens.length && (tokens[idx].equals("AUTO") || tokens[idx].equals("COR"))) {
            idx++;
        }

        // Wind
        if (idx < tokens.length) {
            Matcher m = WIND_PATTERN.matcher(tokens[idx]);
            if (m.matches()) {
                result.wind = parseWind(m);
                idx++;
                // Variable wind direction range e.g. "090V180"
                if (idx < tokens.length && tokens[idx].matches("\\d{3}V\\d{3}")) {
                    idx++;
                }
            }
        }

        // Visibility
        if (idx < tokens.length) {
            String vis = tokens[idx];
            if (vis.equals("CAVOK")) {
                result.visibilityDescription = "Ceiling and Visibility OK — excellent conditions";
                result.cloudLayers = List.of();
                result.weatherPhenomena = List.of();
                idx++;
            } else if (vis.matches("\\d{4}")) {
                result.visibilityDescription = describeVisibilityMeters(Integer.parseInt(vis));
                idx++;
                // Directional minimum e.g. "0600NW"
                if (idx < tokens.length && tokens[idx].matches("\\d{4}[NSEW]{1,2}")) {
                    idx++;
                }
            } else if (vis.endsWith("SM")) {
                result.visibilityDescription = describeVisibilityStatuteMiles(vis);
                idx++;
            }
        }

        // Weather phenomena
        if (result.weatherPhenomena == null) {
            List<String> phenomena = new ArrayList<>();
            while (idx < tokens.length && WEATHER_PATTERN.matcher(tokens[idx]).matches()) {
                phenomena.add(decodePhenomenon(tokens[idx++]));
            }
            result.weatherPhenomena = phenomena;
        }

        // Cloud layers
        if (result.cloudLayers == null) {
            List<CloudLayer> clouds = new ArrayList<>();
            while (idx < tokens.length) {
                String tok = tokens[idx];
                if (tok.equals("SKC") || tok.equals("CLR") || tok.equals("NSC") || tok.equals("NCD")) {
                    CloudLayer layer = new CloudLayer();
                    layer.coverage = "SKC";
                    layer.coverageDescription = "Clear";
                    layer.description = "Sky clear";
                    clouds.add(layer);
                    idx++;
                } else {
                    Matcher m = CLOUD_PATTERN.matcher(tok);
                    if (m.matches()) {
                        clouds.add(parseCloudLayer(m));
                        idx++;
                    } else {
                        break;
                    }
                }
            }
            result.cloudLayers = clouds;
        }

        // Temperature / dew point
        if (idx < tokens.length) {
            Matcher m = TEMP_PATTERN.matcher(tokens[idx]);
            if (m.matches()) {
                result.temperatureCelsius = parseTemp(m.group(1));
                result.temperatureFahrenheit = celsiusToFahrenheit(result.temperatureCelsius);
                if (!m.group(2).isEmpty()) {
                    result.dewPointCelsius = parseTemp(m.group(2));
                    result.dewPointFahrenheit = celsiusToFahrenheit(result.dewPointCelsius);
                }
                idx++;
            }
        }

        // Pressure
        if (idx < tokens.length) {
            String tok = tokens[idx];
            if (tok.startsWith("Q") && tok.substring(1).matches("\\d{4}")) {
                result.pressureHpa = Double.parseDouble(tok.substring(1));
                result.pressureInHg = Math.round(result.pressureHpa * 0.02953 * 100.0) / 100.0;
                idx++;
            } else if (tok.startsWith("A") && tok.substring(1).matches("\\d{4}")) {
                result.pressureInHg = Double.parseDouble(tok.substring(1)) / 100.0;
                result.pressureHpa = Math.round(result.pressureInHg * 33.8639 * 10.0) / 10.0;
                idx++;
            }
        }

        result.flightCategory = calculateFlightCategory(result);
        result.friendlyReport = buildFriendlyReport(result);
        return result;
    }

    private String parseDateTime(String token) {
        // DDHHMM Z → "Day DD at HH:MMz"
        int day = Integer.parseInt(token.substring(0, 2));
        int hour = Integer.parseInt(token.substring(2, 4));
        int minute = Integer.parseInt(token.substring(4, 6));
        return String.format("Day %d at %02d:%02d UTC", day, hour, minute);
    }

    private WindInfo parseWind(Matcher m) {
        WindInfo wind = new WindInfo();
        String dir = m.group(1);
        int speed = Integer.parseInt(m.group(2));
        boolean isMps = "MPS".equals(m.group(5));

        wind.speedKnots = isMps ? (int) Math.round(speed * 1.94384) : speed;
        wind.speedMph = Math.round(wind.speedKnots * 1.15078 * 10.0) / 10.0;

        if (m.group(4) != null) {
            int gust = Integer.parseInt(m.group(4));
            wind.gustKnots = isMps ? (int) Math.round(gust * 1.94384) : gust;
            wind.gustMph = Math.round(wind.gustKnots * 1.15078 * 10.0) / 10.0;
        }

        if (wind.speedKnots == 0) {
            wind.calm = true;
            wind.description = "Calm (no wind)";
        } else if ("VRB".equals(dir)) {
            wind.variable = true;
            wind.description = buildWindDescription(wind, "variable direction");
        } else {
            wind.directionDegrees = Integer.parseInt(dir);
            wind.directionCardinal = degreesToCardinal(wind.directionDegrees);
            wind.description = buildWindDescription(wind, "from the " + wind.directionCardinal);
        }
        return wind;
    }

    private String buildWindDescription(WindInfo wind, String dirPhrase) {
        StringBuilder sb = new StringBuilder();
        sb.append("Wind ").append(dirPhrase)
                .append(" at ").append(wind.speedKnots).append(" knots")
                .append(" (").append(wind.speedMph).append(" mph)");
        if (wind.gustKnots > 0) {
            sb.append(", gusting to ").append(wind.gustKnots).append(" knots")
                    .append(" (").append(wind.gustMph).append(" mph)");
        }
        return sb.toString();
    }

    private String degreesToCardinal(int deg) {
        String[] dirs = {"North", "NNE", "NE", "ENE", "East", "ESE", "SE", "SSE",
                "South", "SSW", "SW", "WSW", "West", "WNW", "NW", "NNW"};
        return dirs[(int) Math.round(deg / 22.5) % 16];
    }

    private String describeVisibilityMeters(int meters) {
        if (meters >= 9999) return "Visibility greater than 10 km — excellent";
        if (meters >= 5000) return String.format("Visibility %d km — good", meters / 1000);
        if (meters >= 1000) return String.format("Visibility %.1f km — reduced", meters / 1000.0);
        return String.format("Visibility %d meters — very low", meters);
    }

    private String describeVisibilityStatuteMiles(String token) {
        // e.g. "10SM", "3/4SM"
        String num = token.replace("SM", "");
        try {
            if (num.contains("/")) {
                String[] parts = num.split("/");
                double val = Double.parseDouble(parts[0]) / Double.parseDouble(parts[1]);
                return String.format("Visibility %.2f statute miles — %s", val, val >= 3 ? "adequate" : "low");
            }
            double val = Double.parseDouble(num);
            if (val >= 10) return "Visibility greater than 10 miles — excellent";
            return String.format("Visibility %.0f miles — %s", val, val >= 3 ? "good" : "reduced");
        } catch (NumberFormatException e) {
            return "Visibility: " + token;
        }
    }

    private String decodePhenomenon(String token) {
        StringBuilder sb = new StringBuilder();
        String t = token;

        if (t.startsWith("+")) { sb.append("Heavy "); t = t.substring(1); }
        else if (t.startsWith("-")) { sb.append("Light "); t = t.substring(1); }

        if (t.startsWith("VC")) { sb.append("in vicinity: "); t = t.substring(2); }

        for (Map.Entry<String, String> entry : WEATHER_DESCRIPTORS.entrySet()) {
            if (t.startsWith(entry.getKey())) {
                sb.append(entry.getValue()).append(" ");
                t = t.substring(entry.getKey().length());
                break;
            }
        }

        List<String> parts = new ArrayList<>();
        while (!t.isEmpty()) {
            boolean matched = false;
            for (String code : WEATHER_CODES.keySet()) {
                if (t.startsWith(code)) {
                    parts.add(WEATHER_CODES.get(code));
                    t = t.substring(code.length());
                    matched = true;
                    break;
                }
            }
            if (!matched) { t = t.substring(1); }
        }
        sb.append(String.join(" and ", parts));
        return capitalize(sb.toString().trim());
    }

    private CloudLayer parseCloudLayer(Matcher m) {
        CloudLayer layer = new CloudLayer();
        layer.coverage = m.group(1);
        layer.altitudeFeet = Integer.parseInt(m.group(2)) * 100;
        layer.type = m.group(3);

        layer.coverageDescription = switch (layer.coverage) {
            case "FEW" -> "Few clouds";
            case "SCT" -> "Scattered clouds";
            case "BKN" -> "Broken clouds";
            case "OVC" -> "Overcast";
            case "VV"  -> "Vertical visibility";
            default    -> layer.coverage;
        };

        String extra = layer.type != null ? (" (" + ("CB".equals(layer.type) ? "cumulonimbus" : "towering cumulus") + ")") : "";
        layer.description = layer.coverageDescription + extra + " at " + formatAltitude(layer.altitudeFeet);
        return layer;
    }

    private String formatAltitude(int feet) {
        if (feet >= 1000) return String.format("%,d feet", feet);
        return feet + " feet";
    }

    private int parseTemp(String s) {
        if (s.startsWith("M")) return -Integer.parseInt(s.substring(1));
        return Integer.parseInt(s);
    }

    private int celsiusToFahrenheit(int c) {
        return (int) Math.round(c * 9.0 / 5.0 + 32);
    }

    private String calculateFlightCategory(DecodedMetar d) {
        int ceiling = Integer.MAX_VALUE;
        if (d.cloudLayers != null) {
            for (CloudLayer layer : d.cloudLayers) {
                if ("BKN".equals(layer.coverage) || "OVC".equals(layer.coverage) || "VV".equals(layer.coverage)) {
                    ceiling = Math.min(ceiling, layer.altitudeFeet);
                }
            }
        }

        // Estimate visibility in miles from description
        double visMiles = 10.0;
        String vis = d.visibilityDescription != null ? d.visibilityDescription.toLowerCase() : "";
        if (vis.contains("greater than 10")) visMiles = 10.0;
        else if (vis.contains("km")) {
            try {
                visMiles = Double.parseDouble(vis.replaceAll("[^0-9.]", "").trim()) * 0.621371;
            } catch (NumberFormatException ignored) {}
        } else if (vis.contains("meters")) {
            try {
                visMiles = Double.parseDouble(vis.replaceAll("[^0-9.]", "").trim()) * 0.000621371;
            } catch (NumberFormatException ignored) {}
        }

        if (ceiling < 500 || visMiles < 1) return "LIFR";
        if (ceiling < 1000 || visMiles < 3) return "IFR";
        if (ceiling < 3000 || visMiles < 5) return "MVFR";
        return "VFR";
    }

    private String buildFriendlyReport(DecodedMetar d) {
        StringBuilder sb = new StringBuilder();

        // Opening sky/conditions summary
        sb.append(skySummary(d)).append(" ");

        // Temperature
        sb.append(String.format("Temperature is %d°C (%d°F)", d.temperatureCelsius, d.temperatureFahrenheit));
        if (d.dewPointCelsius != 0) {
            int spread = d.temperatureCelsius - d.dewPointCelsius;
            String humidity = spread <= 3 ? "very humid" : spread <= 7 ? "humid" : spread <= 14 ? "comfortable" : "dry";
            sb.append(String.format(" with a dew point of %d°C — feeling %s.", d.dewPointCelsius, humidity));
        } else {
            sb.append(".");
        }

        // Wind
        if (d.wind != null) {
            sb.append(" ").append(d.wind.description).append(".");
        }

        // Visibility
        if (d.visibilityDescription != null) {
            sb.append(" ").append(d.visibilityDescription).append(".");
        }

        // Weather phenomena
        if (d.weatherPhenomena != null && !d.weatherPhenomena.isEmpty()) {
            sb.append(" Weather: ").append(String.join(", ", d.weatherPhenomena)).append(".");
        }

        // Clouds
        if (d.cloudLayers != null && !d.cloudLayers.isEmpty()) {
            List<String> cloudDescs = new ArrayList<>();
            boolean allClear = d.cloudLayers.stream().allMatch(c -> "SKC".equals(c.coverage));
            if (!allClear) {
                for (CloudLayer layer : d.cloudLayers) {
                    if (!"SKC".equals(layer.coverage)) cloudDescs.add(layer.description);
                }
                if (!cloudDescs.isEmpty()) {
                    sb.append(" ").append(String.join("; ", cloudDescs)).append(".");
                }
            }
        }

        // Pressure
        if (d.pressureHpa > 0) {
            sb.append(String.format(" Barometric pressure: %.0f hPa (%.2f inHg).", d.pressureHpa, d.pressureInHg));
        }

        // Flight category
        if (d.flightCategory != null) {
            sb.append(" Flying conditions: ").append(d.flightCategory)
                    .append(" — ").append(flightCategoryDescription(d.flightCategory)).append(".");
        }

        return sb.toString().trim();
    }

    private String skySummary(DecodedMetar d) {
        boolean hasPrecip = d.weatherPhenomena != null && !d.weatherPhenomena.isEmpty();
        boolean hasStorm = d.weatherPhenomena != null &&
                d.weatherPhenomena.stream().anyMatch(p -> p.toLowerCase().contains("thunder"));

        if (hasStorm) return "Thunderstorm conditions at " + d.station + ".";
        if (hasPrecip) return "Precipitation reported at " + d.station + ".";

        if (d.cloudLayers == null || d.cloudLayers.isEmpty()) {
            return "Clear skies at " + d.station + ".";
        }

        boolean clear = d.cloudLayers.stream().allMatch(c -> "SKC".equals(c.coverage));
        if (clear) return "Clear skies at " + d.station + ".";

        long overcastCount = d.cloudLayers.stream()
                .filter(c -> "OVC".equals(c.coverage) || "BKN".equals(c.coverage)).count();
        if (overcastCount > 0) return "Overcast or cloudy skies at " + d.station + ".";

        return "Partly cloudy at " + d.station + ".";
    }

    private String flightCategoryDescription(String category) {
        return switch (category) {
            case "VFR"  -> "Visual Flight Rules — great flying weather";
            case "MVFR" -> "Marginal VFR — flyable but with caution";
            case "IFR"  -> "Instrument Flight Rules — poor visibility or low ceiling";
            case "LIFR" -> "Low IFR — very poor conditions";
            default     -> category;
        };
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
