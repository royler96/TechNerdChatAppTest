package com.basemp.chatapptechnerd

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage




class onMessageReceive : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.size > 0) {
            Log.d("debug ", "Message data payload: " + remoteMessage.data)
            if ( true) {
               // scheduleJob()
            } else {
                //handleNow()
            }
        }

        if (remoteMessage.notification != null) {
            Log.d("debug", "Message Notification Body: " + remoteMessage.notification!!.body)
        }
    }

    override fun onNewToken(token: String) {
        Log.d("debug", "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(token)
    }
}