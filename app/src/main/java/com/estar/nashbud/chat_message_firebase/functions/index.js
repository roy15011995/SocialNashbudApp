'use strict'


var functions = require('firebase-functions');
var admin = require('firebase-admin');

admin.initializeApp(functions.config().firebase);

exports.sendMessageNotification = functions.database.ref('notifications/messages/{pushId}')
    .onWrite((change,context) =>
    {
        const pushId = context.params.pushId;
        const total_body = change.after.val();
        const sender_id = total_body.from;
        const receiver_id = total_body.to;
        const body = total_body.body;
        const currentUserName = total_body.currentuserName;
        const currentUserPhoto = total_body.currentuserPhoto;
        const promises = [];

        console.log(pushId);
        console.log("receiver_id :",receiver_id);
        console.log("sender_id :",sender_id);
        console.log("body :",body);
        console.log("total_body :",total_body);
        console.log("currentUserPhoto",currentUserPhoto);
        console.log("currentUserName",currentUserName);



        if (sender_id == receiver_id) {
            //if sender is receiver, don't send notification
            promises.push(event.data.current.ref.remove());
            return Promise.all(promises);
        }
   if(!change.after.val())
	{
		return console.log('A notification has been deleted from the database : ',pushId);
	}

        const getInstanceIdPromise = admin.database().ref(`/users/${receiver_id}/instanceId`).once('value');
        const getSenderUidPromise =  admin.auth().getUser(sender_id);

        return Promise.all([getInstanceIdPromise, getSenderUidPromise]).then(results => {
            const instanceId = results[0].val();
            const sender = results[1];
            console.log("Result",results);
            console.log('notifying ' + receiver_id + ' about ' + body + ' from ' + sender_id);

            /*console.log("senderNameFromUser",sender.displayName);
            console.log("senderPhotoFromUser",sender.photoUrl);*/

            const payload = {
                notification: {
                    title: currentUserName,
                    body:  body,
                    icon:  "default",
                    sound: "default"
                }
            };

            admin.messaging().sendToDevice(instanceId, payload)
                .then(function (response) {
                    console.log("Successfully sent message:", response);
                })
                .catch(function (error) {
                    console.log("Error sending message:", error);
                });

              });

          });

