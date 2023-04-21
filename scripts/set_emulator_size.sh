#!/bin/bash

# Set the desired screen size
SCREEN_SIZE="1090x2340"
LCD_HEIGHT="2340"
LCD_WIDTH="1090"

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

# Check if the hw.lcd.height configuration exists in the file
if grep -q "hw.lcd.height" "$CONFIG_FILE"; then
  # Replace the entire line with the new LCD height
  sed -i "s/^hw.lcd.height.*/hw.lcd.height = $LCD_HEIGHT/" "$CONFIG_FILE"
else
  # Add the new LCD height to the end of the file
  echo "hw.lcd.height = $LCD_HEIGHT" >> "$CONFIG_FILE"
fi

# Check if the hw.lcd.width configuration exists in the file
if grep -q "hw.lcd.width" "$CONFIG_FILE"; then
  # Replace the entire line with the new LCD width
  sed -i "s/^hw.lcd.width.*/hw.lcd.width = $LCD_WIDTH/" "$CONFIG_FILE"
else
  # Add the new LCD width to the end of the file
  echo "hw.lcd.width = $LCD_WIDTH" >> "$CONFIG_FILE"
fi
