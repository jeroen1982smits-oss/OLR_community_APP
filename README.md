# OLR Community App

Een native Android-communityapp voor OLR-racers, gebouwd met Kotlin en Jetpack Compose.

## Schermen

- Home met de volgende race, gridstatus en communityactiviteit
- Races & Events met evenementdetails en inschrijving
- Championships met seizoensstand en punten
- Community met berichten plaatsen en liken
- Driver Profile met bewerkbare rijdersgegevens en statistieken

## APK bouwen

GitHub Actions bouwt bij elke push naar `main` automatisch een installeerbare debug-APK. Open op je telefoon de repository op GitHub, ga naar **Actions**, open de nieuwste geslaagde **Android APK**-run en download het artifact `olr-community-debug-apk`.

Lokaal bouwen met JDK 17 en Android SDK Platform 35:

```sh
./gradlew assembleDebug
```

De APK staat na een geslaagde build in `app/build/outputs/apk/debug/app-debug.apk`.
