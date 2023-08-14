## Getting started

### Initialize
First of all, you need to read the privacy policy of this module carefully.
Then remind the user that you need to access his personal information clearly.
After he agrees, the following operations can be carried out'
```
    AfAnalytics.setPrivacyAgree(true);
```
Then initialize the SDK like this:
```
    AfAnalytics.init();
```

### AutoTrack
We provide three track data: 'app_heartbeat', 'app_start' and 'app_end'.
#### app_heart_beat
This data will be triggered once every minute, mainly used for statistics of user usage time.
In order to more accurately count the usage time of the foreground and background, 
we also report this data when switching between the front and back of the app.
#### app_start
Application cold start or warm start will trigger the reporting of this data.
#### app_end
Once the application is invisible, we will trigger the reporting of the data.

### UserTrack
We also support user-defined data structure reporting.
First you must define a class and specify the field variable name.
```
    class TestData(
        @SerializedName("md5")
        val md5: String?
    )
```
Report this data in your business logic:
```
    AfAnalytics.report(
        ServiceInfoData(
            "test",
        ), "your track id", your EnentType
    )
```

### Test
You can check the data details according to the data ID on the data platform if you have permission.
QA environment: https://etm.athh.cc/logQuery/realTimeQuery
Production environment: https://etm.athh.cc/logQuery/realTimeQuery