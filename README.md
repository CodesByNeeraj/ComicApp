# Comic Viewer App

## Overview
This is an Android app that fetches and displays comic images from an API based on user input. 
The app gets the comic number from the user, downloads the corresponding comic from the internet, and displays the image along with its title.

## Features
1) Takes user input for a comic number
2) Fetches comic details from an API
3) Displays the comic image and title
4) Handles network requests using multi-threading
5) Provides error handling with Toast messages

## How It Works

### User Input Handling

The app retrieves user input from an EditText field when a button is clicked.
If the network is active, it fetches the comic. Otherwise, it shows a Toast message.

### Fetching Comic Data
- Uses Utils.buildURL(userInput) to generate the API URL.
- Calls Utils.getJSON(url) to fetch the comic details in JSON format.
- Parses the JSON to extract:
    - Image URL (img key)
    - Title (safe_title key)
-Downloads the image using Utils.getBitmap(imageUrl).

### Multi-threading Concepts Used
- Executor & Runnable: Used to run network operations on a background thread.
- Looper & Handler: Passes the downloaded data back to the main thread to update the UI.
- Container Class (Utils): Helps in sharing objects between the background and main thread.

### BackgroundTask
- Implements the BackgroundTask abstract class to manage threading efficiently.
- Uses the Template Method Design Pattern, where a base class defines a general structure for a task, and subclasses provide specific implementations.
