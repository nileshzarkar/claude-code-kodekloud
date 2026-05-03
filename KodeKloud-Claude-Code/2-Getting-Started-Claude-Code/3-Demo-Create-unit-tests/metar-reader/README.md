# metar-reader

A Quarkus microservice that fetches live aviation weather reports (METARs) by ICAO airport code and decodes them into structured, human-readable JSON.

## What is a METAR?

A **METAR** (Meteorological Aerodrome Report) is the standard format for aviation weather observations. It is issued every hour (METAR) or whenever significant weather changes occur (SPECI) and is used by pilots, air traffic control, and flight planners worldwide.

A typical raw METAR looks like:

```
METAR KLAX 011755Z 27015KT 10SM FEW040 SCT080 22/10 A2992 RMK AO2
```

This service decodes every field into plain English and provides derived values such as relative humidity and FAA flight category.

## Features

- Fetch the latest METAR for any ICAO airport code (e.g. `KLAX`, `EGLL`, `VABB`)
- Decode wind direction, speed, and gusts with compass direction names
- Parse visibility in both metric (meters) and statute miles
- Decode weather phenomena (rain, fog, thunderstorms, haze, etc.)
- Decode cloud layers with altitude and type (CB, TCU)
- Convert temperature and dew point to both °C and °F
- Calculate relative humidity from the Magnus formula
- Derive FAA flight category: **VFR / MVFR / IFR / LIFR**
- Swagger UI for interactive API exploration
- Lightweight HTML frontend served at `/`

## Tech Stack

| Layer | Technology |
|---|---|
| Runtime | [Quarkus 3.27](https://quarkus.io) |
| Language | Java 21 |
| REST server | Quarkus REST (RESTEasy Reactive) |
| REST client | MicroProfile REST Client |
| JSON | Jackson via `quarkus-rest-jackson` |
| API docs | SmallRye OpenAPI / Swagger UI |
| Build | Maven 3 |
| Data source | [aviationweather.gov](https://aviationweather.gov) |

## Architecture

```
HTTP Request
     │
     ▼
MetarResource          ← JAX-RS endpoint  GET /api/metar/{code}
     │
     ▼
MetarService           ← validates input, calls API, handles errors
     │
     ├──► AviationWeatherClient  ← MicroProfile REST Client → aviationweather.gov
     │
     └──► MetarDecoder           ← tokenizes and decodes the raw METAR string
               │
               ▼
         DecodedMetar            ← structured JSON response model
```

## Getting Started

### Prerequisites

- Java 21+
- Maven 3.8+

### Run in Dev Mode (with live reload)

```bash
cd metar-reader
mvn quarkus:dev
```

The application starts on **http://localhost:8080**.

### Build and Run the Packaged JAR

```bash
cd metar-reader
mvn clean package
java -jar target/quarkus-app/quarkus-run.jar
```

## API Reference

### Decode METAR for an airport

```
GET /api/metar/{airportCode}
```

| Parameter | Type | Description |
|---|---|---|
| `airportCode` | path | ICAO airport code, 3–4 characters (e.g. `KLAX`, `EGLL`, `VABB`) |

**Example request:**
```
GET http://localhost:8080/api/metar/KLAX
```

**Example response (abbreviated):**
```json
{
  "rawMetar": "KLAX 011755Z 27015KT 10SM FEW040 SCT080 22/10 A2992",
  "reportType": "METAR",
  "reportTypeDescription": "Routine Meteorological Aerodrome Report (issued every hour)",
  "stationId": "KLAX",
  "stationName": "Los Angeles, Los Angeles International Airport",
  "dateTime": "011755Z",
  "dateTimeDescription": "Day 01 of the month, 17:55 UTC (Coordinated Universal Time)",
  "wind": {
    "rawDirection": "270",
    "directionDegrees": 270,
    "directionDescription": "West (270°)",
    "speedKt": 15,
    "gustKt": null,
    "calm": false,
    "variable": false,
    "description": "Wind from West (270°) at 15 knots"
  },
  "visibility": "10SM",
  "visibilityDescription": "10 statute miles",
  "visibilityMeters": 16093,
  "cloudLayers": [
    {
      "coverage": "FEW",
      "coverageDescription": "Few (1/8–2/8 sky covered)",
      "altitudeFt": 4000,
      "cloudType": null,
      "description": "Few (1/8–2/8 sky covered) at 4000 ft"
    }
  ],
  "temperature": 22.0,
  "dewPoint": 10.0,
  "relativeHumidity": 46,
  "temperatureDescription": "Temperature: 22°C (72°F) | Dew Point: 10°C (50°F)",
  "altimeter": "A2992",
  "altimeterDescription": "29.92 inHg (1013 hPa) — pressure adjusted to sea level",
  "flightCategory": "VFR",
  "flightCategoryDescription": "Visual Flight Rules — good conditions, ceiling ≥3,000 ft and visibility ≥5 SM"
}
```

### Health check

```
GET /api/metar/health
```

Returns `{"status": "UP", "service": "METAR Reader"}` with HTTP 200.

### Error responses

| HTTP Status | Cause |
|---|---|
| `400` | Airport code is not 3–4 characters |
| `404` | No METAR data found for the requested station |
| `503` | Upstream Aviation Weather API is unreachable |

## Interactive API Docs

After starting the application:

- **Swagger UI:** http://localhost:8080/swagger-ui
- **OpenAPI JSON:** http://localhost:8080/openapi

## Configuration

Key settings in [src/main/resources/application.properties](src/main/resources/application.properties):

| Property | Default | Description |
|---|---|---|
| `quarkus.http.port` | `8080` | HTTP port |
| `quarkus.rest-client.aviation-weather.url` | aviationweather.gov | Base URL for the upstream API |
| `quarkus.http.cors` | `true` | CORS enabled for browser-based access |
| `quarkus.swagger-ui.always-include` | `true` | Swagger UI available in all profiles |

## Flight Category Reference

| Category | Ceiling | Visibility | Meaning |
|---|---|---|---|
| **VFR** | ≥ 3,000 ft | ≥ 5 SM | Visual Flight Rules — clear conditions |
| **MVFR** | 1,000–2,999 ft | 3–4.99 SM | Marginal VFR — reduced but flyable |
| **IFR** | 500–999 ft | 1–2.99 SM | Instrument Flight Rules — poor visibility |
| **LIFR** | < 500 ft | < 1 SM | Low IFR — very poor, dangerous conditions |

## Project Structure

```
metar-reader/
├── pom.xml
└── src/main/
    ├── java/com/metar/
    │   ├── client/
    │   │   └── AviationWeatherClient.java   # REST client for aviationweather.gov
    │   ├── decoder/
    │   │   └── MetarDecoder.java            # Core METAR parsing engine
    │   ├── model/
    │   │   ├── AviationWeatherResponse.java # API response DTO
    │   │   ├── CloudLayer.java              # Cloud layer model
    │   │   ├── DecodedMetar.java            # Full decoded METAR output model
    │   │   ├── WeatherPhenomenon.java        # Weather phenomenon model
    │   │   └── WindInfo.java                # Wind data model
    │   ├── resource/
    │   │   └── MetarResource.java           # JAX-RS REST endpoints
    │   └── service/
    │       └── MetarService.java            # Orchestration service
    └── resources/
        ├── application.properties
        └── META-INF/resources/
            └── index.html                   # Browser frontend
```

## References

- [METAR format — Wikipedia](https://en.wikipedia.org/wiki/METAR)
- [Aviation Weather API](https://aviationweather.gov/api/data/metar?ids=KLAX&format=json)
- [Quarkus documentation](https://quarkus.io/guides/)
- [FAA Flight Categories](https://www.aviationweather.gov/metar/help)
