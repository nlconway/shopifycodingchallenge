Initially, to use the application changes must be made in the following code:

At Webpage line:13,14,15 the postgres details must be set 

url - the postgres database url, username - database username, password - database password

At MainDemo line:8,9

name - name for repository, filepath - the path for where the images will be stored locally

After these are set the application can be started.

AFTER STARTING APPLICATION: LOGIN PANEL

The application will open a JFRame with a login form. To login, enter the Username and Password, for the sake of demo the user already added is "TipTop" with password "a123a123";
Once the credentials are verified the selection panel will display. 

SELECTION PANEL: ADDING IMAGE 

To add an image to the repository the url of the image and a given name is used. The name must be unique to the user. If the text is reset to blank it means the adding was successful. If the text doesn't disappear there was an error (either the name wasn't unique or the image couldn't be downloaded). 

The image information is then added to the database and the image is saved to the folder given. 

SELECTION PANEL: GETTING IMAGE TO DISPLAY

An image can be searched by the name given when adding. A user can only get their own images. If the image exists it will be displayed and the text box will be reset (be set to blank again). 

SELECTION PANEL: GETTING IMAGES

This button will get all images added by the logged in user. The user can cycle through the images they've added to the repository.

SELECTION PANEL: LOGOUT OUT

Clicking the logout button will return to the login form

DELETING REPOSITORY

Upon exiting the JFrame the folder containing the images will be deleted as will the tables keeping track of users and images.



Methods have been created internally to register a user, and delete a user or image. These methods do not yet have graphic interfaces to interact with.




