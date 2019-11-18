<p align="center">
  <img src='https://github.com/jaindiv26/Codeforces/blob/master/screenshots/codeforces_logo.png' width='400px'/>
</p>

<p align="center">
<b>Codeforces</b>
</p>

## About

Codeforces is an unofficial Android version of Codeforces web. This app is made to integrate all the available Codeforces API into this app. This way the users can have a mobile version of their handle and can get key information on the go.

## Getting Started

These instructions will get you a copy of the project up and be running on your local machine for development and testing purposes.

### Screenshots

<p align="center">
  <img src='https://raw.githubusercontent.com/jaindiv26/Codeforces/master/screenshots/Screenshot_2019-11-18-17-47-19-033_com.example.android.codeforces.png' width='400px'/>
</p>

<p align="center">
  <img src='https://raw.githubusercontent.com/jaindiv26/Codeforces/master/screenshots/Screenshot_2019-11-18-17-47-34-861_com.example.android.codeforces.png' width='400px'/>
</p>

<p align="center">
  <img src='https://raw.githubusercontent.com/jaindiv26/Codeforces/master/screenshots/Screenshot_2019-11-18-17-47-45-682_com.example.android.codeforces.png' width='400px'/>
</p>

<p align="center">
  <img src='https://raw.githubusercontent.com/jaindiv26/Codeforces/master/screenshots/Screenshot_2019-11-18-17-47-42-152_com.example.android.codeforces.png' width='400px'/>
</p>

### Prerequisites

[Android Studio](https://developer.android.com/studio), with a recent version of the Android SDK.

### Setting up your development environment

- Download and install Git.

- Fork the [Codeforces project](https://github.com/Chromicle/Codeforces)

- Clone your fork of the project locally. At the command line:
    ```
    $ git clone https://github.com/YOUR-GITHUB-USERNAME/Codeforces.git
    ```

If you prefer not to use the command line, you can use Android Studio to create a new project from version control using 
```
https://github.com/YOUR-GITHUB-USERNAME/Codeforces.git
```

Open the project in the folder of your clone from Android Studio and build the project. If there are any missing dependencies, install them first by clicking on the links provided by the Android studio. Once the project is built successfully, run the project by clicking on the green arrow at the top of the screen.

## How it Works?

On the [Codeforces](https://codeforces.com/) official web page, they provide various API which we access to get data in machine-readable JSON format. Then by using HttpURLConnection values of each column are parsed and set into the model to display the data. 

When the app is opened, the user is required to enter his preferred handle (Username). After submitting the handle, all the contest that the Handel is associated it is displayed. Upon opening, this screen user can perform the following operation -

* On clicking on the contest, it will redirect the user to the official contest page on the Codeforces.com
* User can filter the contest based on the positive and negative.
* User can view its profile.  
