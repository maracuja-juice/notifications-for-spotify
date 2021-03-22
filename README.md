# Spotify Releases App

 <img src="https://user-images.githubusercontent.com/16801528/38161679-26d1106e-34d4-11e8-817d-71121e8e922a.png"  align="left"
width="200"
    >

[![Build Status](https://travis-ci.org/panmau/spotify-releases-app.svg?branch=master)](https://travis-ci.org/panmau/spotify-releases-app)
[![Releases](https://img.shields.io/badge/release-v1.1.1-blue.svg)](https://github.com/panmau/spotify-releases-app/releases/latest)

Ever thought that you want to see which new albums of your favourite artists got released? Now you can!

[<img src="https://user-images.githubusercontent.com/16801528/38161462-4e1f19b8-34cf-11e8-8082-49cf95d17be9.png"
      alt="Direct apk download"
      height="80">](https://github.com/panmau/notifications-for-spotify/releases/latest)

---

### Current Features

- A list of all albums of artists that you follow (sorted by release date, newest first). 
The List displays:
   - artist(s)
   - name of the album
   - release date
   - album art
- It only downloads new albums everyday or when you explicitly request it. The albums get merged with the current albums saved in the database
- When clicking on an item in the list it will open the album/single directly in the Spotify app

#### Screenshot

   <img src="https://user-images.githubusercontent.com/16801528/38161360-e11aeb9a-34cd-11e8-9345-8aa49faf0f28.png" width="250">

### Planned future improvements
- A setting that allows the app (when setting is turned on) to notify the user when there is a new album added. Or otherwise reminding the user that there (maybe?) were new albums released.
- Play Spotify track directly from the app
  
## Specs / Open-Source libraries:
- Minimum **SDK 15**
- [**Android Auth**](https://github.com/spotify/android-auth) for the Spotify Authentication
- [**Spotify Web API Android**](https://github.com/kaaes/spotify-web-api-android) for the Spotify Web API requests
- [**Joda Time**](https://github.com/dlew/joda-time-android) for easier handling of dates and time
- [**Lightweight Stream API**](https://github.com/aNNiMON/Lightweight-Stream-API) for backwards compatible streams
- [**Objectbox**](https://github.com/objectbox/objectbox-java) for super fast database handling
- [**Jackson**](https://github.com/FasterXML/jackson-databind) for super fast JSON parsing
- [**FastScroll**](https://github.com/L4Digital/FastScroll) to add FastScroll behaviour to a RecyclerView
- [**Smooth ProgressBar**](https://github.com/castorflex/SmoothProgressBar) for the Progress Bar
- [**Picasso**](https://github.com/square/picasso) for loading images
- [**Dagger**](https://github.com/google/dagger) for Dependency Injection
- **Android Support Libraries**, the almighty :)
  
## Setup of Development environment

To further develop the app you need the authentication with Spotify to work correctly.

1. Create a new app on the [Spotify Developer site](https://beta.developer.spotify.com/dashboard/) 
   Steps needed:
   - Login
   - Click on `Create a Client ID`
   - In the popup give the app a name, description and click that is a mobile app. It's not so important what name you give it.
   - Go to the next step
   - Click `No` and go to the next step
   - Read the text in the three boxes and check all of them (otherwise this won't work...)
   - Click on submit
2. Run the android app once
3. Get your dev key (SHA1)

    Run the Gradle Task `signingReport` (located under `android`)
    In the output window you should see an output similar to this:
    `SHA1: E7:47:B5:45:71:A9:B4:47:EA:AD:21:D7:7C:A2:8D:B4:89:1C:BF:75`

4. Add the following information to the app on the Spotify Developer website:
   - the key you just got
   - the package name of the app (`com.maracuja_juice.spotifynotifications`)
   - a redirect URI (for example: `spotify-notifications-android://callback`)
   
   You can add it by clicking on `Edit Settings` and then putting the information into the corresponding fields.
   
5. Change the content of the file `config.properties` in the `res/raw` folder
Replace it with the two following lines:
    ```
   redirectUri={insert your redirect uri that you set in your spotify app}
   clientId={insert the client id that you must copy from your spotify app dashboard}
    ```
   And then insert your redirectUri and clientId into the `{}` placeholders.
   Don't check in the updated version of this file, this is just for yourself.
6. Have fun!

If you have any problems with the setup: let me know.

## App Logo

Icon made by <a href="https://www.flaticon.com/authors/smashicons" title="Smashicons">Smashicons</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a>
