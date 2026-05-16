Start a new project using Claude Code from scratch.

The project idea is to build a METAR reader.

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

