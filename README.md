# Weather

- A simple program to warn me if it is going to rain and any other weather things.

[![Build Status](https://travis-ci.org/ripperbone/weather.svg?branch=master)](https://travis-ci.org/ripperbone/weather)

Run locally with:
* First, create a file called api_key.txt in the same resources directory as your_api_key_here that contains your DarkSky.net API key.
* Install redis. For example: `apt install redis-server`
* Then do `./gradlew run`


Package it up with:
`./gradlew clean distZip`
