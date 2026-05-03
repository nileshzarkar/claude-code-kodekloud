Slide - 1 
=========
What is METAR ?
Prompt to generate a quarkus microservice to read METAR reading for an airport.

https://en.wikipedia.org/wiki/METAR

Prompt:
Create a Quarkus microservice. This microservice will be a "METAR Reader". The user can type an airport code and then the application will fetch the METAR reading from that airport, and decode it.
You are an aviation expert and meteorology trainer. Explain METAR (Meteorological Aerodrome Report) in a simple and beginner-friendly way.

Use the following structure and ensure clarity with short explanations:
1. **What is METAR (simple)**
* METAR stands for Meteorological Aerodrome Report
* It tells the current weather at an airport
* It is used for flight planning and safety
* It is issued every hour (or more frequently if weather changes)
2. **Structure of a METAR (order matters)**
   A METAR always follows this fixed pattern:
   [Station] [Date/Time] [Wind] [Visibility] [Weather] [Clouds] [Temp/Dew] [Pressure]
3. **Example METAR**
   METAR VABB 011200Z 27010KT 6000 FEW020 SCT100 30/24 Q1012
4. **Step-by-step decoding**
* METAR → Type of report (routine weather report)
* VABB → Airport code (Chhatrapati Shivaji Maharaj International Airport)
* 011200Z → Date & time (UTC)
  * 01 → 1st day of the month
  * 1200Z → 12:00 UTC
* 27010KT → Wind information
  * 270 → wind from west (270°)
  * 10KT → speed is 10 knots
* 6000 → Visibility = 6000 meters (6 km)
* Weather conditions → may include codes like RA (rain), FG (fog), etc.
* FEW020 SCT100 → Cloud layers
  * FEW020 → few clouds at 2000 ft
  * SCT100 → scattered clouds at 10,000 ft
* 30/24 → Temperature / Dew point
  * 30°C → air temperature
  * 24°C → dew point
* Q1012 → Pressure (QNH = 1012 hPa)

https://aviationweather.gov/api/data/metar?ids=VABB&format=raw

Slide - 2
=========
+---------------+-----------------------------+------------------------------------------+
| Layer         | File                        | Purpose                                  |
+---------------+-----------------------------+------------------------------------------+
| REST Endpoint | MetarResource.java          | GET /api/metar/{code}                    |
| Service       | MetarService.java           | Orchestrates fetch + decode              |
| HTTP Client   | AviationWeatherClient.java  | Calls aviationweather.gov                |
| Decoder       | MetarDecoder.java           | Parses all METAR sections                |
| Frontend      | index.html                  | Dark aviation-themed UI                  |
+---------------+-----------------------------+------------------------------------------+

Your vibe coding dream comes true 
