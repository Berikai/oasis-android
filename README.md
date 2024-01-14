# Oasis Android App
An android application for OASIS, the student information system of Izmir University of Economics. 

## Behind the Scene
It's basically a WebView attached to the ContentView of the application which loads [OASIS](https://oasis.izmirekonomi.edu.tr) URL. So, it's actually a wrapper of the original web application. 

The main trick this app does is storing the credentials of the user account on the installed device. It is being done by injecting Javascript into the WebView. 
The injected script calls Kotlin functions back from the app with Android Javascript Interface method. 
By doing so, it gathers user credentials from the HTML input elements and it simulates the log in process under the hood by sending HTTP requests and gets the session cookie.
It automatically logs in with these gathered credentials when session expires and creates a seemless experience of unexpiring session.

## Motivation
When there is a message from the lecturers, an SMS is being sent by YBS about there is a message to be seen on OASIS without the content of the message. 
Since OASIS sessions expires after 30 mins, it is not a good experience to log in everytime to the website. 
On the other hand, a native application usually serves a better experience for the end user.

## Setup Development Environment
You can use Android Studio to open the project. 

## Screenshot
![Oasis App Screenshot](https://github.com/Berikai/oasis-android/assets/18515671/13ea1df1-206f-4039-89b2-5e4ff455a931)
