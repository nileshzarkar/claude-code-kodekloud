# METAR Reader

## Project Overview
`metar-reader` is a Quarkus-based microservice designed to fetch and decode METAR weather reports for a given airport ICAO code.

The application offers:
- A REST endpoint for METAR lookup
- A native Java HTTP client to call the FAA/Aviation Weather API
- A parser that decodes raw METAR strings into structured weather data
- A lightweight frontend interface served from `index.html`

## What is METAR?
METAR stands for "Meteorological Aerodrome Report." It is a standardized aviation weather report used by pilots, air traffic control, and flight planners.

A typical METAR contains:
- station identifier
- observation time
- wind
- visibility
- weather phenomena
- cloud layers
- temperature and dew point
- pressure

Example:
`METAR VABB 011200Z 27010KT 6000 FEW020 SCT100 30/24 Q1012`

## Architecture and Key Components

### 1. REST Endpoint
- `src/main/java/com/metar/resource/MetarResource.java`
- Exposes: `GET /api/metar/{code}`
- Accepts an ICAO airport code and returns decoded METAR data in JSON.

### 2. Service Layer
- `src/main/java/com/metar/service/MetarService.java`
- Orchestrates METAR retrieval and decoding.
- Calls the HTTP client and hands raw METAR text to the decoder.

### 3. Aviation Weather Client
- `src/main/java/com/metar/client/AviationWeatherClient.java`
- Uses Quarkus REST Client to request METAR data from `https://aviationweather.gov`.
- Configured with connection and read timeouts in `application.properties`.

### 4. Decoder
- `src/main/java/com/metar/decoder/MetarDecoder.java`
- Parses fields from raw METAR strings and converts them into a structured `DecodedMetar` object.

### 5. Domain Model
- `src/main/java/com/metar/model/DecodedMetar.java`
- `AviationWeatherResponse.java`
- `CloudLayer.java`
- `WeatherPhenomenon.java`
- `WindInfo.java`

## Configuration

### `src/main/resources/application.properties`
- `quarkus.application.name=metar-reader`
- `quarkus.http.port=8080`
- REST client config for `aviation-weather` service
- CORS enabled for all origins
- Swagger UI at `/swagger-ui`
- OpenAPI path at `/openapi`

## Build and Run

### Requirements
- Java 21
- Maven

### Build
```bash
cd metar-reader
mvn clean package
```

### Run in Dev Mode
```bash
cd metar-reader
mvn quarkus:dev
```

### Run the Packaged Application
```bash
cd metar-reader
java -jar target/quarkus-app/quarkus-run.jar
```

## API Usage

### Get METAR by ICAO Code
Request:
```http
GET http://localhost:8080/api/metar/{code}
```
Example:
```http
GET http://localhost:8080/api/metar/VABB
```

Response:
- JSON payload containing decoded weather fields
- Example fields likely include station, time, wind, visibility, clouds, temperature, dew point, and pressure.

## OpenAPI / Swagger
- Swagger UI: `http://localhost:8080/swagger-ui`
- OpenAPI JSON: `http://localhost:8080/openapi`

## Data Flow Summary
1. Client requests `/api/metar/{code}`
2. `MetarResource` forwards request to `MetarService`
3. `MetarService` calls `AviationWeatherClient`
4. Raw METAR text is retrieved from aviationweather.gov
5. `MetarDecoder` parses the raw METAR string
6. Decoded response is returned as JSON

## Notes
- The project is optimized for learning Quarkus REST API structure and METAR decoding logic.
- CORS is enabled to allow browser-based frontend access from any origin.
- The frontend `index.html` is served via Quarkus static resources under `META-INF/resources`.

## Helpful Links
- METAR reference: https://en.wikipedia.org/wiki/METAR
- Aviation Weather API example: `https://aviationweather.gov/api/data/metar?ids=VABB&format=raw`
