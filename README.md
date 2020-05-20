# Mini-Instagram Backend Features


#### Diagram for Components
https://app.lucidchart.com/invitations/accept/cc243a85-5493-4c11-9105-d95a1b4f80b5


APIs
----------
####-User <br>
POST /login: return nothing<br>
POST /register: returns nothing <br>
POST /user/{username}/follow: returns nothing <br>
POST /user/{username}/unfollow: returns nothing <br>
GET /user/{user}: returns clientUser including username, intro, profile photo, posts, following count, follower count, and posts count <br> 
GET /feed: returns all photos of the following people and user-self's photos<br> 


####-Photo <br>
POST /photo/{photoId}/like: returns nothing<br> 
POST /photo/{photoId}/unlike: return nothing<br> 
POST /photo/upload: returns the photo<br> 
GET /explore: return photo url, photoId, number of likes, and number of comments made<br>

####-Comment <br>
POST /photo/{photoId}/comment returns comment <br>
POST /comment/{commentId}/reply returns comment <br>

####-Message <br>
/message returns message only
