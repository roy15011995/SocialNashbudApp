'use strict'


const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.sendNotification = functions.database.ref('Notifications/{receiver_id}/{notification_id}')
.onWrite((change,context) =>
{
	const receiver_id = context.params.receiver_id;
	const notification_id = context.params.notification_id;

	console.log('We have a notification to send to :',receiver_id);

	if(!change.after.val())
	{
		return console.log('A notification has been deleted from the database : ',notification_id);

	}


	const deviceToken = admin.database().ref(`/users/${receiver_id}/instanceId`).once('value');


	return deviceToken.then(result =>

	{
		const token_id = result.val();

		const payload =
		{
			notification:
			{
				title: "Friend Request",
				body: "you have received a new friend request",
				icon: "default"
			}
		};

		return admin.messaging().sendToDevice(token_id,payload)
		    .then(response =>
			{
			  console.log('This was the notification feture.');
			});
	});
});