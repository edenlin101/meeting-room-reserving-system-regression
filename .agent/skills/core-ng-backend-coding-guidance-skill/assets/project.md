Since we just have only one meeting room, there is always conflict in our workmates, so we need you to implement a system for reserving our meeting room based on core-ng.

Here are our situations.
a.	Website
1.	The user opens the register page, inputs the username and selects the company the user belongs to, then register an account and waits the account to be active.
2.	When the account is active, the user logins into the system, then selects the meeting room of the company which the user belongs to.
3.	Then the user can pick a date (the date range is seven days in the future), and our system will show reservation info in every time range (the time range is from 9 am, and every 30 minutes is a range) about the meeting room in the picked date.
4.	When there is any time range can be reserved, the user can reserve it. If the user reserves successfully, other user can not reserve the same time range in the same meeting room.
5.	After reserving, the user can cancel it.
6.	The system should have a schedule job to notify the user whose reservations will take effect in 10 minutes. (send a message using Kafka)
b.	Back-office
1.	Use admin/admin to login into the back-office.
2.	Open the company list page, click the create button, input the company info and save the company. If you click the remove button, the system will remove the specific company.
3.	Open the meeting room list page, click the create button, input the meeting room info and select related company, then save the meeting room. If you click the remove button, the system will remove the specific meeting room.
4.	Open the reservations list page, select the company and the meeting room and search the reservations.
5.	Open the user list page, click the active/inactive button to manage the user’s status.

Here are our claims:
1.	Divide the suitable services for the system.
2.	We should use MySQL, MongoDB, Cache, Kafka
