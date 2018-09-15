# HackZurich2018

```
  ________           ________         ___________.__             ___________                     __
 /  _____/  ____    /  _____/  ____   \__    ___/|__| _____   ___\__    ___/___________    ____ |  | __ ___________
/   \  ___ /  _ \  /   \  ___ /  _ \    |    |   |  |/     \_/ __ \|    |  \_  __ \__  \ _/ ___\|  |/ // __ \_  __ \
\    \_\  (  <_> ) \    \_\  (  <_> )   |    |   |  |  Y Y  \  ___/|    |   |  | \// __ \\  \___|    <\  ___/|  | \/
 \______  /\____/   \______  /\____/    |____|   |__|__|_|  /\___  >____|   |__|  (____  /\___  >__|_ \\___  >__|
        \/                 \/                             \/     \/                    \/     \/     \/    \/
```


---

Developers:

- ThmX
- BlackSquirrelz

Finally created at HackZurich 2018 after many years of utter disgust for logging time-sheets manually everyday.

---

## Description

The basic idea for the application came from our daily work life, as we need to accurately track every minute of
working time on a variety of projects and mandates.
Since manually creating time-sheets, is both inefficient and inaccurate, we were looking for a more automated solution.
As nearly everyone constantly has the smartphone with an integrated assistant at hand, we decided to do a
voice controlled application.

### Time Tracking Application

Initially the goal was to develop our own time tracking application from the start. However,
we quickly realised that there are already many sophisticated solutions available. Therefore we have used  clockify.me
which offers an extensive time tracking application.


With https://clockify.me/ we have found a time-tracking service, which offers great API's to third party developers.

---

## Architecture


### Google Actions API

#### Request to send start the time tracking

```json
{
  "id": "8e596309-6b19-4cd9-b592-e6c49adca94e",
  "name": "start.logging",
  "auto": true,
  "contexts": [],
  "responses": [
    {
      "resetContexts": false,
      "action": "start_time",
      "affectedContexts": [],
      "parameters": [
        {
          "id": "4536d128-61b2-4835-b3e5-59712aa5fcc6",
          "required": false,
          "dataType": "@sys.url",
          "name": "start_funct",
          "value": "https://europe-west1-thmx-ch.cloudfunctions.net/hackzurich18_test1-start_time",
          "isList": false
        }
      ],
      "messages": [
        {
          "type": "simple_response",
          "platform": "google",
          "lang": "en",
          "textToSpeech": "Ok, I have submitted you starting time."
        },
        {
          "type": "custom_payload",
          "platform": "google",
          "lang": "en",
          "payload": {
            "action": "start"
          }
        },
        {
          "type": 0,
          "lang": "en",
          "speech": "Ok, let me log the time for you. Don\u0027t forget to let me know once you are finished, or have a break."
        }
      ],
      "defaultResponsePlatforms": {
        "google": false
      },
      "speech": []
    }
  ],
  "priority": 500000,
  "webhookUsed": true,
  "webhookForSlotFilling": true,
  "lastUpdate": 1537048713,
  "fallbackIntent": false,
  "events": []
}

```


#### Request to send end the time tracking

```json

{
  "id": "64ce796e-13c8-4ec7-b60d-3d0762e1e585",
  "name": "stop.logging",
  "auto": true,
  "contexts": [],
  "responses": [
    {
      "resetContexts": false,
      "action": "end_time",
      "affectedContexts": [],
      "parameters": [
        {
          "id": "7bdfbfa7-277d-4921-8ff4-2a56054864fa",
          "required": false,
          "dataType": "@sys.url",
          "name": "end_time",
          "value": "https://europe-west1-thmx-ch.cloudfunctions.net/hackzurich18_test1-end_time",
          "isList": false
        }
      ],
      "messages": [
        {
          "type": "simple_response",
          "platform": "google",
          "lang": "en",
          "items": [
            {
              "textToSpeech": "Will stop to track the time now"
            },
            {
              "textToSpeech": "Ok, will stop tracking, let me know when you want to resume"
            }
          ]
        },
        {
          "type": "link_out_chip",
          "platform": "google",
          "lang": "en",
          "destinationName": "Stopping time",
          "url": "https://europe-west1-thmx-ch.cloudfunctions.net/hackzurich18_test1-end_time"
        },
        {
          "type": "custom_payload",
          "platform": "google",
          "lang": "en",
          "payload": {
            "action": "stop"
          }
        },
        {
          "type": 0,
          "lang": "en",
          "speech": "Glad you finished, I will stop tracking the time for now."
        }
      ],
      "defaultResponsePlatforms": {},
      "speech": []
    }
  ],
  "priority": 500000,
  "webhookUsed": true,
  "webhookForSlotFilling": false,
  "lastUpdate": 1537051239,
  "fallbackIntent": false,
  "events": []
}

```
