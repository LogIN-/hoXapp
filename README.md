## Introduction
`hoXapp` is simple security/tracking tool allowing you to track your cellphone. 
It is perfect app to locate your phone if lost or stolen. `hoXapp` also allows you to track other cellphones with the app installed.

After installing `hoXapp` it will not be visible in app menu. 

* To activate the app you will need to dial "6322" and press call button.
* After that you will be asked to set your own personal password. 
* Now go to the "DeviceAdmin" menu in upper left corner and click "Enable" to enable device administrator, and press Activate. That's it. By activating this option any phone user will not be able to uninstall or disable  `hoXapp` until you remove this option.

##  List of functions supported:
You need to send SMS from some other phone to phone where  `hoXapp` is installed.

Send SMS example: `hoXapp 6322 XYZ`

### Explanation:

* hoXapp (application Identifier)
* 6322 (default hoXapp password)
* XY (commands to execute)

`hoXapp 6322 GPS url*`

Send Longitude and Latitude to desired URL or SMS. 
If GPS is disabled and  `hoXapp` cannot enable it or if  `hoXapp` cannot calculate triangulation between mobile antennas,  `hoXapp` will send (for manual tracking):

* MCC (mobile country code(decimal))
* MNC (mobile network code(decimal))
* LAC (locale area code (in decimal))
* IMEI (International Mobile Equipment Identity)

### Optional:
url* is optional value, you can specific url that hoXapp will use to send httpPOST with following variables:

* "sms_message"
* "sms_number"
* "sms_number_sender"
* "latitude"
* "longitude"
* "timestamp"

`hoXapp 6322 ALARM`

MobilePhone starts to ring for desired time specified.  `hoXapp` uses user-defined ALARM sound. If ALARM sound is not set, than it uses Notification sound, if that is not set then it uses RingTone.

`hoXapp 6322 WIPE`

Automatic background service is started on mobile to Delete contents of SDCard.

`hoXapp 6322 WIPEALL`

Service is started to Wipe the device data (restore the device to its factory defaults).

`hoXapp 6322 LOCK`

Device is locked immediately using password that is set in  `hoXapp` → DeviceAdmin.

`hoXapp 6322 CALL`

Phone calls you to the number you've sent message from.

If `hoXapp` detects SIM CARD(phone-number) is changed if yes it sends you SMS message of location and phone number of new device!


## Support and Bugs
If you are having trouble, have found a bug, or want to contribute don't be shy.

I did a very little testing so if your JS code is valid but after minimization doesn't work please open a bug ticket so we can fix that!

[Open a ticket](https://github.com/LogIN-/hoXapp/issues) on GitHub.

## License
`hoxApp` source-code uses the The MIT License (MIT), see our `LICENSE` file.
```
The MIT License (MIT)

Copyright (c) LogIN- 2014 

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

