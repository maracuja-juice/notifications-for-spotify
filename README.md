
[![contributions welcome](https://img.shields.io/badge/contributions-welcome-brightgreen.svg?style=flat)](https://github.com/maracuja-juice/notifications-for-spotify/issues)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/71c07fd378814700bf4321f12d9eb390)](https://app.codacy.com/app/mpanchaud/notifications-for-spotify?utm_source=github.com&utm_medium=referral&utm_content=maracuja-juice/notifications-for-spotify&utm_campaign=badger)
[![Known Vulnerabilities](https://snyk.io/test/github/maracuja-juice/notifications-for-spotify/badge.svg?targetFile=app%2Fbuild.gradle)](https://snyk.io/test/github/maracuja-juice/notifications-for-spotify?targetFile=app%2Fbuild.gradle)

# Notifications for Spotify

Ever thought that you want to see which new albums of your favourite artists got released? Now you can!

[<img src="https://user-images.githubusercontent.com/16801528/38161462-4e1f19b8-34cf-11e8-8082-49cf95d17be9.png"
      alt="Direct apk download"
      height="80">](https://github.com/maracuja-juice/notifications-for-spotify/releases/latest)


### Current Features

- A list of all albums of artists that you follow (sorted by release date, newest first). 
The List displays:
   - artist(s)
   - name of the album
   - release date
   - album art
- It only downloads new albums everyday or when you explicitly request it. The albums get merged with the current albums saved in the database
#### Screenshot from the List

   <img src="https://user-images.githubusercontent.com/16801528/38161360-e11aeb9a-34cd-11e8-9345-8aa49faf0f28.png" width="250">

### Planned future improvements
- A setting that allows the app (when setting is turned on) to notify the user when there is a new album added. (-> regularly downloading all albums, merging them with the database and send notification)
- When clicking on an album in the list it should go to a second view. 
    * This view should show details of the album as in the list
    * It should also display a list of all the tracks of the album
    * It should allow the album to be opened in Spotify
    * (maybe) Allow the album/individual tracks to be saved/removed from the library of the user
- Allow to filter the Album List by date
    
## Setup of Development environment

1. Create a new app on the Spotify Developer site. 
2. Generate your dev key trough the console:

    macOs:

    `keytool -exportcert -alias androiddebugkey -keystore ~/.android/debug.keystore -list -v | grep SHA1`

    Windows:

    `keytool -exportcert -alias androiddebugkey -keystore %HOMEPATH%\.android\debug.keystore -list -v | grep SHA1`

    The result of the command should look similar to this:

    `SHA1: E7:47:B5:45:71:A9:B4:47:EA:AD:21:D7:7C:A2:8D:B4:89:1C:BF:75`

3. Add the dev key to your Spotify online app along with the name of the package of the app.
In my case the package name is: `com.maracuja_juice.spotifynotifications`

4. Add a redirect URI
(for example: `spotify-notifications-android://callback`)

5. Change the file `config.properties` in the `res/raw` folder
6. In the file replace the following lines:
    ```
   redirectUri={insert your redirect uri that you set in your spotify app}
   clientId={insert client id that you must copy from your spotify app dashboard}
    ```

7. Have fun.

## Credits

<div>Icon made by <a href="https://www.flaticon.com/authors/smashicons" title="Smashicons">Smashicons</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>
