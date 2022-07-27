# webmonitor [![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=ennioVisco_webmonitor&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=ennioVisco_webmonitor)

## Quick Overview (on YouTube 🎥)
[![WebMonitor Demo - With Live Coding 💻](https://i.ytimg.com/vi/hqVw0JU3k9c/hqdefault.jpg)](https://youtu.be/hqVw0JU3k9c "WebMonitor Demo - With Live Coding 💻")

## Prerequisites

Please remember that to execute this tool, the following condition must be met:

- JVM 11+ installed (IntelliJ should have one out of the box)
- The selected browser engines (possibly, Google Chrome 99+ and Mozilla Firefox 99+)
- A desktop environment must be available, with a resolution greater or equal than the ones addressed by the
  experiments (i.e. a screen bigger than 800x600, ideally bigger than 1920x1200 )

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

### Running on Windows
Since Windows 11, the default terminal is PowerShell, so the previous command should run out of the box. If it doesn't, and you are using the good old fashion CLI (Why are you doing it? You should riconsider your priorities...), you can substitute `./gradlew` with `gradlew.bat` and the rest should work the same way.

## Source file example
The spec file contains the key information about the browsing session that will be run. An example follows:
```kts
// source.XXX.kts
import at.ac.tuwien.trustcps.WebSource
import at.ac.tuwien.trustcps.tracking.Browser

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

## Spec file example
The spec file contains the key information about the browsing session that will be run. An example follows:
```kts
// spec.YYY.kts
import at.ac.tuwien.trustcps.*

Spec.atoms(
    select { "h1" } read "visibility" equals "visible",
)

Spec.record(
    after { "click" }
)

val screen = Spec.screen
val isVisible = Spec.atoms[0]

val f1 = isVisible and screen
val f2 = f1 and (eventually(isVisible) implies not(isVisible)) // some random complex formula to show operators.

Spec.formula = f2


```

## Thanks
Special thanks to [Moonlight](https://github.com/MoonLightSuite/MoonLight) and [Selenium Webdriver](https://github.com/SeleniumHQ/selenium), and the supporting developers.
