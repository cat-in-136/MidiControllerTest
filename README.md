# MidiControllerTest

A test app to make your android phone into an USB MIDI controller for controlling DAW softwares etc... on PC.

[![Android CI](https://github.com/cat-in-136/MidiControllerTest/actions/workflows/android.yml/badge.svg)](https://github.com/cat-in-136/MidiControllerTest/actions/workflows/android.yml)

## How to Build

    $ ./gradlew assembleDebug
    $ adb install app/build/outputs/apk/debug/app-debug.apk

## How to Use

1. (Prepare) Install this app to an android phone.
2. Connect PC and the phone using an USB cable.
3. Choose "USB" in "USB Preferences" on the phone.
4. Launch the app on the phone.
5. Launch a DAW app on the PC.
6. Configure the DAW app to use the phone as a MIDI controller.
7. Have fun.

* If the app failed to open MIDI device, you can see an emoji on the app.
  * 🤔 : Could not found MIDI environment on the phone.
    * Check if you really did as instruction above. e.g. Is "USB Preferences" really set to "USB"?
  * ⁉️ : Device was found but not failed load MIDI.
    * Something wrong occur. Please follow the instructions below:
       1. Quit the DAW app on the PC.
       2. Quit the app on the phone (choose "Quit App" on the menu)
       3. Choose other than "USB" in "USB Preferences" on the phone
       4. Follow how-to-use instruction step 3 and later again

