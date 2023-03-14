# GoNature
GoNature is a software system for managing visits to parks and nature reservesץ
The system is decentralized, and enables local entry control (for example limiting the number of visitors) at the various sites (parks), as well as central management of activity control and sales promotion.

<img src="./main_page.png" alt="Main Page" width="400"/>

## Sign In
The visitor can sign in with his ID number; if there is no order for him, he will be navigated to the make order page. (Note: each visitor can make one order.)

<span><img src="./signin_visitor.png" alt="Visitor signIn page" width="400"/>
<img src="./authorized_signin.png" alt="Authorized signIn page" width="400"/></span>


## Visitation Order
There are different types of orders:
Individual, Family, Small Group, or Organized Group
(Note: all the inputs will be checked on clicking "submit," and if there are errors, a pop-up will appear with the error details.)

<span><img src="./make_order.png" alt="Visitor make order page" width="400"/>
<img src="./popup.png" alt="alert popup" width="400"/></span>

* Making a successful order

<span><img src="./make_order_success.png" alt="successful order" width="400"/>
<img src="./visitor_order.png" alt="visitor_order" width="400"/></span>

* No available places:

  If there aren't enough available places at the chosen time, the visitor can select another time from the table with the available time for his order or wait in the waitlist.

<img src="./not_enough.png" alt="there are not enough places choose another" width="400"/>

After choosing to add to waitlist             |  Choose another available time for this order for the next 7 days
:--------------------------------------------:|:----------------------------------------------------------------:
![add to waitlist](./add_to_waitlist.png)  |  ![table with the available times](./table_available.png)

#### Cancel Order
When a visitor cancels his order, a message is sent to the first order that has enough places on the waiting list at the same time. The visitor must confirm the massage within one hour; if he doesn't, the place is handed over to the next visitor.
This is the message sent to the visitor on the waitlist to confirm his order:

<img src="./confirm.png" alt="confirm" width="400"/>

## Authorized users:
### * Park manager:
He can change the maximum number of visitors, casual visitors, and the estimated visitation time; any change in these needs approval from the department manager; once approved the parameters will be updated.

<img src="./park_manager.png" alt="park manager page" width="400"/>

In addition, he can make discounts, also needs the department manager's approval. As well as preparing monthly reports to the department manager.

<span><img src="./manager_update_visitors.png" alt="manager_update_visitors" width="400"/>
<img src="./prepare_reports.png" alt="prepare_reports" width="400"/></span>
----
### * Department manager:
On top of the approvalation of the park managers’s requests, the department manager can view the prepared reports from the park manager.

The home page:

<img src="./department_home.png" alt="department_home" width="400"/>

The generated reports from the the park manager |  The park manager's requests
:----------------------------------------------:|:-----------------------------:
<img src="./department_reports.png" alt="department_reports" width="400"/>  | <img src="./department_confirm.png" alt="department_confirm" width="400"/>
#
### * Service representative:
He is responsible for making a Guid register & Family / Individual Subscription

<img src="./service_representative.png" alt="service_representative" width="400"/>

Guide Registration |  Family / Individual Subscription 
:----------------------------------------------:|:-----------------------------:
<img src="./guide_register.png" alt="guide_register" width="400"/>  | <img src="./subscription.png" alt="subscription" width="400"/>
