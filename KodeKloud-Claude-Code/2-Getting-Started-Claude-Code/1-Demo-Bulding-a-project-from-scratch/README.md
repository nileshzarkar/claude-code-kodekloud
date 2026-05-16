Start a new project using Claude Code from scratch.

The project idea is to build a METAR reader.
https://en.wikipedia.org/wiki/METAR
A METAR is a standardized aviation weather report used by pilots and weather services. The raw METAR format looks cryptic and hard to understand for normal users.

Example:
Airport code: KJFK
Raw METAR contains information like: wind speed/direction / temperature / visibility / humidity / cloud conditions

The goal of the application:
* User enters an airport code like KJFK or KHIO
* The app fetches the METAR report
* The app converts it into plain English

Example output:
“Clear skies”
“70°F”
“Wind from the north at 11 knots”
“Visibility 10 miles”

We want a:
* A quarkus web application
* Built quickly using Claude Code
Claude helps generate:
project structure / templates / CSS / test setup / requirements / decoding logic

Claude automatically creates:
* To-do lists
* Java dependencies
* HTML templates
* Static files
* Weather decoding functions

The app decodes METAR values such as:
Visibility / Cloud types / Weather phenomena / Wind direction / Temperature / Pressure

Examples:
CLR → clear skies
SKC → sky clear
FEW → few clouds
SCT → scattered clouds
BKN → broken clouds
OVC → overcast

Weather codes:
rain / snow / drizzle / fog / mist / thunderstorms / showers

Claude also:
* creates java project structure
* build and download maven dependencies
* starts quarkus application
* builds UI automatically

The final app allows entering airport codes and viewing:
readable weather report / raw METAR data

Examples tested:
KHIO / KLAX / KJFK

Claude Code makes “vibe coding” fast
It can quickly build working applications from prompts
Future plans include:
adding agents / tests / documentation / more features around METAR/weather systems



Lets get started with this VIBE CODING
So we need to mention 
what the app is ?
what its going to do ?
what it will be built with ?
what we expect to be the criteria of success?
With just those things Claude can build us an application.

So here goes the prompt. 

When you build apps using LLM tools you have to make assumptions of what LLM knows or explain everything. 
I am going to assume it knows what a METAR is and so I will just give an example.

"
Create a quarkus web application. The application will be a "METAR Reader". The user can type in airport code, and then the application will fetch the METAR reading for that airport decode it. METAR is
a standardized weather report. Its is somewhat cryptic, so I would like to convert it into plain English that people can understand. For instance "Clear day, 70 degrees, wind 5mph to the south. 
This app will be successful if people can type in an airport code, and receive a friendly readable weather report from it.
Here is the service I will use. When you change the airport code, it outputs the corresponding METAR.
https://aviationweather.gov/api/data/metar?ids=VABB 

The UI should be simple html and java script and the backend should be Quarkus microservice. Try to have minimilistic and simple design  

Make the application in folder D:\AI\claude-code-kodekloud\KodeKloud-Claude-Code\2-Getting-Started-Claude-Code\1-Demo-Bulding-a-project-from-scratch
"

PS D:\AI\claude-code-kodekloud\KodeKloud-Claude-Code\2-Getting-Started-Claude-Code\1-Demo-Bulding-a-project-from-scratch> claude
 ▐▛███▜▌   Claude Code v2.1.143
▝▜█████▛▘  Sonnet 4.6 with high effort · Claude Pro
  ▘▘ ▝▝    D:\AI\claude-code-kodekloud\KodeKloud-Claude-Code\2-Getting-Started-Claude-Code\1-Demo-Bulding-a-project-from-scratch


❯ Create a quarkus web application. The application will be a "METAR Reader". The user can type in airport code, and then the application will fetch the METAR reading for that airport decode it. METAR is
a standardized weather report. Its is somewhat cryptic, so I would like to convert it into plain English that people can understand. For instance "Clear day, 70 degrees, wind 5mph to the south. 
This app will be successful if people can type in an airport code, and receive a friendly readable weather report from it.
Here is the service I will use. When you change the airport code, it outputs the corresponding METAR.
https://aviationweather.gov/api/data/metar?ids=VABB 

The UI should be simple html and java script and the backend should be Quarkus microservice. Try to have minimilistic and simple design  

Make the application in folder D:\AI\claude-code-kodekloud\KodeKloud-Claude-Code\2-Getting-Started-Claude-Code\1-Demo-Bulding-a-project-from-scratch



 ---
  METAR Reader — Complete

  Project structure

  metar-reader/
  ├── pom.xml
  └── src/main/
      ├── java/com/metar/
      │   ├── client/AviationWeatherClient.java   ← calls aviationweather.gov
      │   ├── decoder/MetarDecoder.java            ← parses raw METAR → friendly English
      │   ├── model/  (DecodedMetar, WindInfo, CloudLayer)
      │   ├── resource/MetarResource.java          ← GET /api/metar/{code}
      │   └── service/MetarService.java
      └── resources/
          ├── application.properties
          └── META-INF/resources/index.html       ← frontend

  To run

  cd metar-reader
  mvn quarkus:dev
  Then open http://localhost:8080

  What it does

  - User types an ICAO airport code (e.g. KJFK, EGLL, VABB)
  - Backend calls aviationweather.gov/api/data/metar?ids={code}&format=raw
  - MetarDecoder parses: wind direction/speed, visibility, cloud layers, temperature, dew point, pressure, weather phenomena
  - Returns a friendly English report like: "Partly cloudy at KJFK. Temperature is 18°C (64°F), feeling comfortable. Wind from the West at
  12 knots (13.8 mph). Visibility greater than 10 km — excellent…"
  - UI card shows: flight category badge (VFR/MVFR/IFR/LIFR), detail grid, and a collapsible raw METAR section
  - Swagger UI available at http://localhost:8080/swagger-ui


D:\AI\claude-code-kodekloud\KodeKloud-Claude-Code\2-Getting-Started-Claude-Code\1-Demo-Bulding-a-project-from-scratch\metar-reader> mvn clean compile

PS D:\AI\claude-code-kodekloud\KodeKloud-Claude-Code\2-Getting-Started-Claude-Code\1-Demo-Bulding-a-project-from-scratch\metar-reader> mvn quarkus:dev

http://localhost:8080/
OR
http://localhost:8080/api/metar/VABB

This is a vibe coding dream. As far as Claude Code, if you're doing anything in Python or JavaScript or Java, it pretty much builds things for you very, very fast.