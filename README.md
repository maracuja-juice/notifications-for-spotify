# Notifications for Spotify

## The Problem

- You can follow artists on Spotify but when they add a new album you don't get notified and therefore don't know that they have a new album. You have to find out yourself i.e. trough a different channel (Twitter, Artist Newsletter, etc.)
- There's no list with recently added albums of all your albums.

## The Solution

This app.

The goal is to make a good looking Android Material Design app with at least the following features:

1. List of recently added albums of all albums (sorted by date added, desc). List should display details like artist, name, album art, etc.
2. A setting that allows the app (when setting is turned on) to notify the user when there is a new album added.
3. When clicking on an album in the list it should go to a second view. 
    * This view should show details of the album as in the list
    * It should also display a list of all the tracks of the album
    * It should allow the album to be opened in Spotify
    * (optional) Allow the album/individual tracks to be saved/removed from the library of the user
    

    
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

5. Add a raw folder to your res folder
6. Add the file `config.properties` to the `res/raw` folder
7. In the file add the following lines:
    ```
   redirectUri={insert your redirect uri that you set in your spotify app}
   clientId={insert client id that you must copy from your spotify app dashboard}
    ```

8. Your good to go.
9. Have fun.
