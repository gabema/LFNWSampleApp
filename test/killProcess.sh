adb -e shell ps | grep "gabema.lfnwsampleapp" | head -1 | cut -d " " -f 5 | xargs adb -e shell kill
