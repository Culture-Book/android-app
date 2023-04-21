#!/bin/bash

# Set the desired screen size
SCREEN_SIZE="1090x2340"

AVD_NAME="$1"

# Get the path to the config.ini file
CONFIG_FILE="$HOME/.android/avd/$AVD_NAME.avd/config.ini"

# Check if the skin.name configuration exists in the file
if grep -q "skin.name" "$CONFIG_FILE"; then
  # Replace the entire line with the new screen size
  sed -i "s/^skin.name.*/skin.name = $SCREEN_SIZE/" "$CONFIG_FILE"
else
  # Add the new screen size to the end of the file
  echo "skin.name = $SCREEN_SIZE" >> "$CONFIG_FILE"
fi
