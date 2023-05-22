# webmonitor ![CI workflow](https://github.com/ennioVisco/webmonitor/actions/workflows/build.yml/badge.svg) [![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=ennioVisco_webmonitor&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=ennioVisco_webmonitor)

A formal approach to monitoring web pages as spatio-temporal traces.

## Quick Overview (on YouTube 🎥)

[![WebMonitor Demo - With Live Coding 💻](https://i.ytimg.com/vi/hqVw0JU3k9c/hqdefault.jpg)](https://youtu.be/hqVw0JU3k9c "WebMonitor Demo - With Live Coding 💻")

## Prerequisites

Please remember that to run the tool:

- an internet connection is required
- when not in headless mode, a desktop environment must be available on the running machine.
  Alternatively, to run in headless mode, change the browser to `CHROME_HEADLESS` in `source.*.kts` files.

## Create Monitoring files

To launch WebMonitor, two files must be provided in `src/main/resources`: `source.XXX.kts` and `spec.YYY.kts`.

## Executing WebMonitor

Once the required `source.XXX.kts` and `spec.YYY.kts` files are created, run the following commands.

```sh
./gradlew run --args="XXX YYY" 
```

Or just

```sh
./gradlew run --args="XXX" 
```

if XXX = YYY

Note that the first time you run the tool, it will download the required dependencies, so it might take a while.

### Usage as Maven package

To use it as a maven package, just include the following line:

```kts
implementation("com.enniovisco:webmonitor:1.0.0")
```

### Running on Windows

Since Windows 11, the default terminal is PowerShell, so the previous command should run out of the box. If it doesn't,
and you are using the good old fashion CLI (Why are you doing it? You should reconsider your priorities...), you can
substitute `./gradlew` with `gradlew.bat` and the rest should work the same way.

## Examples

Some examples are available in `src/main/resources/examples`.

In general though, few lines are needed to run the tool on a new website.

### Source file example

The spec file contains the key information about the browsing session that will be run. An example follows:

```kts
// source.XXX.kts
import com.enniovisco.*

// Browsing window size:
WebSource.screenWidth = 600 // px 
WebSource.screenHeight = 600 // px

// OPTIONAL: Change the browser used for the evaluation. 
WebSource.browser = Browser.CHROME // - default - only allowed values currently are CHROME and FIREFOX

// Browsing session duration, after which the browser is closed
WebSource.maxSessionDuration = 5_000 // ms

// OPTIONAL: sets a waiting time before starting recording the browser (so that initial loading errors can be skipped by the analysis)
WebSource.wait = 1_000 // ms 

// Browser's address of the analyzed page
WebSource.targetUrl = "https://enniovisco.github.io/webmonitor/"

```

### Spec file example

The spec file contains the key information about the browsing session that will be run. An example follows:

```kts
// spec.YYY.kts
import com.enniovisco.*

Spec.atoms(
    select { "h1" } read "visibility" equals "visible",
)

Spec.record(
    after { "click" }
)

// Helper formulae
val screen = Spec.screen
val isVisible = Spec.atoms[0]

val f1 = isVisible and screen
val f2 = f1 and (eventually(isVisible) implies not(isVisible)) // some random complex formula to show operators.

// Final monitored formula
Spec.formula = f2
```

## Thanks

Special thanks to [Moonlight](https://github.com/MoonLightSuite/MoonLight)
and [Selenium Webdriver](https://github.com/SeleniumHQ/selenium), and the supporting developers.
