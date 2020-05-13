# Mini-Instagram Backend Features


#### Diagram for Components
https://app.lucidchart.com/invitations/accept/cc243a85-5493-4c11-9105-d95a1b4f80b5


APIs
----------
#####-User
GET /login: return login page <br>
GET /register: returns sign-up page <br>
POST /register: returns nothing <br>

POST /user/{username}/follow: returns nothing <br>
POST /user/{username}/unfollow: returns nothing <br>

GET /user/{user}: returns clientUser including username, intro, profile photo, posts, following count, follower count, and posts count <br> 
GET /feed: returns all photos of the following people and user-self's photos<br> 


#####-Photo
POST /photo/{photoId}/like: returns nothing <br> 
POST /photo/{photoId}/unlike: return nothing <br> 
POST /photo/upload: returns the photo <br> 
GET /explore: return photo url, photoId, number of likes, and number of comments made <br>

####-Comment 
POST /photo/{photoId}/comment returns the photo <br>
POST /comment/{commentId}/reply returns the photo <br>


####-Message
/message returns message only <br>
