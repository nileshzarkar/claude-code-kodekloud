# METAR Reader

A Quarkus-based microservice that fetches and decodes METAR aviation weather reports into plain-English, human-readable weather data — served via a REST API and a lightweight browser frontend.

## What is METAR?

METAR (Meteorological Aerodrome Report) is a standardized aviation weather format used by pilots and air traffic control worldwide. A raw METAR looks like this:

```
METAR KJFK 011200Z 27015KT 10SM FEW045 BKN250 22/14 A2992
```

This application decodes that into structured fields: wind, visibility, cloud layers, temperature, dew point, pressure, and a plain-English summary.

## Features

- REST API endpoint: `GET /api/metar/{icao-code}`
- Plain-English weather summary with flight category badge (VFR / MVFR / IFR / LIFR)
- Lightweight HTML frontend — no build step required
- Live data from [aviationweather.gov](https://aviationweather.gov)
- OpenAPI / Swagger UI included
- CORS enabled for browser-based access

## Requirements

- Java 21
- Maven 3.8+

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/nileshzarkar/claude-code-kodekloud.git
cd claude-code-kodekloud/.../metar-reader
```

### 2. Run in development mode

```bash
mvn quarkus:dev
```

The app starts at `http://localhost:8080` with live reload enabled.

### 3. Build and run the packaged application

```bash
mvn clean package
java -jar target/quarkus-app/quarkus-run.jar
```

## API Usage

### Get decoded METAR

```http
GET http://localhost:8080/api/metar/{icao-code}
```

**Example:**

```http
GET http://localhost:8080/api/metar/KJFK
```

**Response:**

```json
{
  "station": "KJFK",
  "observationTime": "2024-01-01T12:00:00Z",
  "rawMetar": "KJFK 011200Z 27015KT 10SM FEW045 BKN250 22/14 A2992",
  "friendlyReport": "Clear skies with a few clouds at 4,500 ft. Wind from the west at 15 knots.",
  "flightCategory": "VFR",
  "wind": { "directionCardinal": "W", "speedKnots": 15, "speedMph": 17, "calm": false },
  "temperatureCelsius": 22,
  "temperatureFahrenheit": 72,
  "dewPointCelsius": 14,
  "dewPointFahrenheit": 57,
  "pressureHpa": 1013,
  "pressureInHg": 29.92
}
```

**HTTP status codes:**

| Status | Meaning |
|--------|---------|
| 200 | METAR decoded successfully |
| 400 | Invalid ICAO code format (must be 3–4 characters) |
| 404 | No METAR data found for the given code |
| 503 | Upstream aviation weather service unavailable |

## Frontend

Open `http://localhost:8080` in a browser. Enter any ICAO airport code (e.g. `KJFK`, `EGLL`, `VABB`, `YSSY`) and click **Get Weather**.

## API Documentation

| URL | Description |
|-----|-------------|
| `http://localhost:8080/swagger-ui` | Interactive Swagger UI |
| `http://localhost:8080/openapi` | Raw OpenAPI JSON spec |

## Project Structure

```
metar-reader/
├── src/main/java/com/metar/
│   ├── client/       AviationWeatherClient.java   # REST client for aviationweather.gov
│   ├── decoder/      MetarDecoder.java             # Raw METAR string parser
│   ├── model/        DecodedMetar.java + others    # Domain model
│   ├── resource/     MetarResource.java            # REST endpoint
│   └── service/      MetarService.java             # Orchestration layer
├── src/main/resources/
│   ├── application.properties                      # Quarkus config
│   └── META-INF/resources/index.html              # Browser frontend
└── pom.xml
```

## Configuration

Key settings in `src/main/resources/application.properties`:

| Property | Default | Description |
|----------|---------|-------------|
| `quarkus.http.port` | `8080` | HTTP port |
| `quarkus.rest-client.aviation-weather.url` | `https://aviationweather.gov` | Upstream API base URL |
| `quarkus.rest-client.aviation-weather.connect-timeout` | `5000` | Connection timeout (ms) |
| `quarkus.rest-client.aviation-weather.read-timeout` | `10000` | Read timeout (ms) |

## Tech Stack

- [Quarkus 3.8](https://quarkus.io) — Java microservice framework
- Java 21
- MicroProfile REST Client — typed HTTP client
- SmallRye OpenAPI — API documentation
- Vanilla HTML/CSS/JS — frontend (no framework)

## Data Source

Live METAR data is fetched from the [FAA Aviation Weather Center API](https://aviationweather.gov/api/data/metar).
